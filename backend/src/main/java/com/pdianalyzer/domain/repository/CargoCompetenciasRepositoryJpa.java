package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.CargoCompetencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CargoCompetenciasRepositoryJpa extends JpaRepository<CargoCompetencias, String> {
    CargoCompetencias findByCargoId(UUID cargoId);

    @Query("""
          SELECT c.nivel.ordem
          FROM CargoCompetencias cc
          JOIN Cargo c ON cc = c.nivel.cargoCompetencias
          WHERE cc.id = :id
      """)
    int findNivelOrdemByCargoCompetenciasId(@Param("id") UUID id);
}
