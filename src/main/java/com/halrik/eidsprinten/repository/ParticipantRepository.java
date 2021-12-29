package com.halrik.eidsprinten.repository;

import com.halrik.eidsprinten.domain.Participant;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    List<Participant> findByClub(String club);

}
