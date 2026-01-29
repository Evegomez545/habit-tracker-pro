package com.evelyn.habit_tracker;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HistoricoRepository extends JpaRepository<HistoricoDiario, Long> {
    Optional<HistoricoDiario> findByDiaSemana(String diaSemana);
}
