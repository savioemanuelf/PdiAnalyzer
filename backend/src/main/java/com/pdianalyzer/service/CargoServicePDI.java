package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.CargoDto;
import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.Nivel;
import com.pdianalyzer.domain.model.TrilhaDeCarreira;
import com.pdianalyzer.domain.repository.CargoRepositoryJpa;
import com.pdianalyzer.domain.repository.NivelRepositoryJpa;
import com.pdianalyzer.domain.repository.TrilhaDeCarreiraRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;
import com.smarthirepro.domain.repositories.EmpresaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CargoServicePDI {

  private final CargoRepositoryJpa cargoRepository;


  private final TrilhaDeCarreiraRepositoryJpa trilhaDeCarreiraRepository;
  private final NivelRepositoryJpa nivelRepositoryJpa;
  private final EmpresaRepository empresaRepository;

  public Cargo buscarPorId(UUID id) {
    return cargoRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Cargo", id));
  }

  @Transactional
  public Cargo criarCargo(CargoDto dto) {
    UUID empresaId = AuthUtils.getEmpresaId();
    Empresa empresa = empresaRepository.findById(empresaId)
      .orElseThrow(() -> new ItemNotFoundException("Empresa", empresaId));

    TrilhaDeCarreira trilha = trilhaDeCarreiraRepository.findById(dto.trilhaDeCarreiraId())
      .orElseThrow(() -> new PDIBusinessRuleException("Um cargo deve estar vinculado a uma trilha de carreira. Trilha não encontrada com ID: " + dto.trilhaDeCarreiraId()));

    Nivel nivel = nivelRepositoryJpa.findById(dto.nivelId())
      .orElseThrow(() -> new PDIBusinessRuleException("Um cargo deve estar vinculado a um nível na trilha de carreira. Nível não encontrado com ID: " + dto.trilhaDeCarreiraId()));

    if (!nivel.getTrilhaDeCarreira().getId().equals(trilha.getId())) {
      throw new PDIBusinessRuleException("O Nível com ID " + nivel.getId() + " não pertence à Trilha de Carreira " + trilha.getNome());
    }

    Cargo cargo = new Cargo();
    cargo.setNome(dto.nome());
    cargo.setEmpresa(empresa);
    cargo.setActive(dto.isActive());
    cargo.getRequisitos().setHabilidades(dto.habilidades());

    cargo.setNivel(nivel);
    cargo.setTrilhaDeCarreira(trilha);
    return cargoRepository.save(cargo);
  }

  public List<Cargo> listarCargosPorEmpresa() {
    UUID empresaId = AuthUtils.getEmpresaId();
    return cargoRepository.findByEmpresaId(empresaId);
  }


  // salvar(Cargo cargo)
  // listarTodosPorTrilha(UUID trilhaId)
  // etc
}