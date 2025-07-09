package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.repository.CargoRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CargoServicePDI {

  private final CargoRepositoryJpa cargoRepository;

  public Cargo listarPorId(UUID id) {
    return cargoRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Cargo", id));
  }

  // salvar(Cargo cargo)
  // listarTodosPorTrilha(UUID trilhaId)
  // etc
}