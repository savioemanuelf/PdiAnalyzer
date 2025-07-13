package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.TrilhaDeCarreira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrilhaDeCarreiraRepositoryJpa extends JpaRepository<TrilhaDeCarreira, UUID> {
  List<TrilhaDeCarreira> findByEmpresaId(UUID empresaId);
}
