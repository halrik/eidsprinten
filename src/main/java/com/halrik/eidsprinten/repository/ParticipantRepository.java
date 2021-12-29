package com.halrik.eidsprinten.repository;

import com.halrik.eidsprinten.domain.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByClubName(String clubName);

}
