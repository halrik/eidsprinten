package com.halrik.eidsprinten.services;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.repository.ParticipantRepository;
import com.halrik.eidsprinten.repository.TeamRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class EidsprintenService {

    private static final Logger log = LoggerFactory.getLogger(EidsprintenService.class);
    private static final List<Integer> INVALID_BIBS = List.of();
    private ParticipantRepository participantRepository;
    private TeamRepository teamRepository;

    public EidsprintenService(ParticipantRepository participantRepository,
        TeamRepository teamRepository) {
        this.participantRepository = participantRepository;
        this.teamRepository = teamRepository;
    }

    public void saveParticipantsAndTeams(List<Participant> participantList) {
        log.info("Saving participants and teams");

        validateTeams(participantList);

        List<Participant> savedParticipants = new ArrayList<>();

        participantList.forEach(participant -> saveParticipant(savedParticipants, participant));

        savedParticipants.forEach(savedParticipant -> addToTeam(savedParticipant));

        allocateBibs();
    }

    public void allocateBibs() {
        List<Team> teams = teamRepository.findAll();

        Map<Integer, Map<String, List<Team>>> ageGenderClassTeamMap = teams.stream()
            .collect(Collectors.groupingBy(Team::getAge, Collectors.groupingBy(Team::getGenderClass)));

        AtomicInteger bib = new AtomicInteger(0);
        ageGenderClassTeamMap.forEach((age, genderTeamMap) ->
            genderTeamMap.forEach((gender, teamMap) -> {
                    teamMap.forEach(team -> {
                        incrementBib(bib);
                        log.info("Set bib {} for team {}", bib.get(), team);
                        team.setBib(bib.get());
                        saveTeam(team);
                    });
                    // make room for some late arrivals
                    for (int i = 0; i < 2; i++) {
                        incrementBib(bib);
                    }
                }
            ));
    }

    private void incrementBib(AtomicInteger bib) {
        while (INVALID_BIBS.contains(bib.incrementAndGet())) {
            log.info("Skipped bib {}", bib.get());
        }
    }

    public int validateTeams() {
        return validateTeams(participantRepository.findAll());
    }

    private int validateTeams(List<Participant> participantList) {
        log.info("Validating teams");

        // groupBy age, gender class and team and check that all teams have 2 participants
        Map<Integer, Map<String, Map<String, List<Participant>>>> ageGenderClassTeamMap = participantList.stream()
            .collect(
                Collectors.groupingBy(participant -> participant.getAge(),
                    Collectors.groupingBy(participant -> participant.getGenderClass(),
                        Collectors.groupingBy(participant -> participant.getTeamName())
                    )));

        AtomicInteger numberOfTeams = new AtomicInteger();
        List<String> ageGenderTeamsWithMissingParticipants = new ArrayList<>();
        ageGenderClassTeamMap.forEach((age, genderTeamMap) ->
            genderTeamMap.forEach((gender, teamMap) ->
                teamMap.forEach((teamName, teamParticipants) -> {
                    numberOfTeams.getAndIncrement();
                    if (teamParticipants.size() != 2) {
                        ageGenderTeamsWithMissingParticipants.add(age + " " + gender + " " + teamName);
                    }
                })));

        if (ageGenderTeamsWithMissingParticipants.size() > 0) {
            throw new IllegalStateException(
                ageGenderTeamsWithMissingParticipants.size() + " teams are missing participants "
                    + ageGenderTeamsWithMissingParticipants);
        }

        return numberOfTeams.get();
    }

    private void addToTeam(Participant participant) {
        Team team = getTeam(participant);

        log.info("Add participant {} to team {}", participant, team);

        if (participant.getLeg() == 1) {
            team.setParticipantLeg1Id(participant.getId());
            team.setParticipantLeg1Name(participant.getFirstName() + " " + participant.getLastName());
        } else if (participant.getLeg() == 2) {
            team.setParticipantLeg2Id(participant.getId());
            team.setParticipantLeg2Name(participant.getFirstName() + " " + participant.getLastName());
        }

        saveTeam(team);
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    private Team getTeam(Participant participant) {
        Team matchingTeam = new Team();
        matchingTeam.setTeamName(participant.getTeamName());
        matchingTeam.setGroupName(participant.getGroupName());

        Team team = teamRepository.findOne(Example.of(matchingTeam, ExampleMatcher.matching()
            .withIgnorePaths("id")
            .withMatcher("teamName", ignoreCase())
            .withMatcher("groupName", ignoreCase()))).orElse(matchingTeam);

        team.setClubName(participant.getClubName());
        team.setTeamLeaderName(participant.getTeamLeaderName());
        team.setTeamLeaderPhone(participant.getTeamLeaderPhone());
        team.setTeamLeaderEmail(participant.getTeamLeaderEmail());
        team.setAge(participant.getAge());
        team.setGenderClass(participant.getGenderClass());

        return team;
    }

    private void saveParticipant(List<Participant> savedParticipants, Participant participant) {
        if (participantRepository.exists(Example.of(participant, ExampleMatcher.matching()
            .withIgnorePaths("id")
            .withMatcher("firstName", ignoreCase())
            .withMatcher("lastName", ignoreCase())))) {
            return;
        }

        log.info("Save participant {}", participant);
        savedParticipants.add(saveParticipant(participant));
    }

    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public List<Participant> getParticipantsByClub(String clubName) {
        return participantRepository.findByClubName(clubName);
    }

    public List<Team> getTeamsByAge(Integer age) {
        return teamRepository.findByAge(age);
    }

    public Map<String, List<Team>> getTeamsGroupedByClub() {
        Map<String, List<Team>> teamsGroupedByClub = new TreeMap<>();
        List<Team> allTeams = teamRepository.findAll();

        sortTeamsByGenderAndAge(allTeams);

        allTeams.forEach(team -> {
            String key = team.getClubName() + (team.getAge() > 10 ? " - Rangerte" : " - Urangerte");
            List<Team> teamsForClub =
                teamsGroupedByClub.get(key) != null ? teamsGroupedByClub.get(key) : new ArrayList<>();
            teamsForClub.add(team);
            teamsGroupedByClub.put(key, teamsForClub);
        });

        return teamsGroupedByClub;
    }

    private void sortTeamsByGenderAndAge(List<Team> teams) {
        Collections.sort(teams, (Comparator) (o1, o2) -> {
            String genderClass1 = ((Team) o1).getGenderClass();
            String genderClass2 = ((Team) o2).getGenderClass();
            int sComp = genderClass1.compareTo(genderClass2);

            if (sComp != 0) {
                return sComp;
            }

            Integer age1 = ((Team) o1).getAge();
            Integer age2 = ((Team) o2).getAge();
            return age1.compareTo(age2);
        });
    }

    @Transactional
    public long deleteTeam(Integer bib) {
        teamRepository.findByBib(bib).stream().findFirst().ifPresent(team -> {
            team.getHeats().forEach(heat -> heat.removeTeam(team));
            participantRepository.deleteById(team.getParticipantLeg1Id());
            participantRepository.deleteById(team.getParticipantLeg2Id());
        });
        return teamRepository.deleteByBib(bib);
    }

}
