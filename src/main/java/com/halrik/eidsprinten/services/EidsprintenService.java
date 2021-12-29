package com.halrik.eidsprinten.services;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.repository.ParticipantRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EidsprintenService {

    private static final Logger log = LoggerFactory.getLogger(EidsprintenService.class);

    private ParticipantRepository participantRepository;

    public EidsprintenService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void saveParticipantsAndTeams(List<Participant> participantList) {
        log.info("Saving participants and teams");

        participantList.forEach(participant -> participantRepository.save(participant));
    }

    public List<Participant> getParticipantsByClub(String clubName) {
        return participantRepository.findByClub(clubName);
    }
}
