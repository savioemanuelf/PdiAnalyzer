package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NivelRepositoryJpa extends JpaRepository<Nivel, UUID> {

  List<Nivel> findAllByTrilhaDeCarreiraIdOrderByOrdemAsc(UUID trilhaId);
}
