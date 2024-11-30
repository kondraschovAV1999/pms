package org.prison.repositories;

import org.prison.model.prisoners.Communication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunicationRepository extends JpaRepository<Communication, Integer> {

    Optional<Communication> findByPrisonerId(int prisonerId);
}
