package com.halrik.eidsprinten.repository;

import com.halrik.eidsprinten.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByAge(Integer age);

}
