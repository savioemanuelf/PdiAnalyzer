package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.CargoCompetencias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CargoCompetenciasRepositoryJpa extends JpaRepository<CargoCompetencias, UUID> {
    CargoCompetencias findByCargoId(UUID cargoId);
}
