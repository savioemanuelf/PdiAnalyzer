package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.CargoDto;
import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.CargoCompetencias;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoServicePDI {

  private final CargoRepositoryJpa cargoRepository;


  private final TrilhaDeCarreiraRepositoryJpa trilhaDeCarreiraRepository;
  private final CargoRepositoryJpa cargoRepositoryJpa;
  private final NivelRepositoryJpa nivelRepositoryJpa;
  private final EmpresaRepository empresaRepository;

  public CargoDto buscarPorId(UUID id) {
    Cargo cargo = cargoRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Cargo", id));
    return new CargoDto(
      cargo.getNome(),
      cargo.isActive(),
      cargo.getRequisitos().getHabilidades(),
      cargo.getNivel().getId()
    );
  }

  @Transactional
  public CargoDto criarCargo(CargoDto dto) {
    UUID empresaId = AuthUtils.getEmpresaId();
    Empresa empresa = empresaRepository.findById(empresaId)
      .orElseThrow(() -> new ItemNotFoundException("Empresa", empresaId));

    Nivel nivel = nivelRepositoryJpa.findById(dto.nivelId())
      .orElseThrow(() -> new PDIBusinessRuleException("Um cargo deve estar vinculado a um nível na trilha de carreira. Nível não encontrado com ID: " + dto.nivelId()));

    TrilhaDeCarreira trilha = trilhaDeCarreiraRepository.findById(nivel.getTrilhaDeCarreira().getId())
      .orElseThrow(() -> new PDIBusinessRuleException("Nível não pertence a uma trilha de carreira válida. Trilha não encontrada com ID: " + nivel.getTrilhaDeCarreira().getId()));

    if (!nivel.getTrilhaDeCarreira().getId().equals(trilha.getId())) {
      throw new PDIBusinessRuleException("O Nível com ID " + nivel.getId() + " não pertence à Trilha de Carreira " + trilha.getNome());
    }

    boolean nivelEmUso = cargoRepositoryJpa.existsByNivelId(nivel.getId());
    if (nivelEmUso) {
      throw new PDIBusinessRuleException("O Nível com ID " + nivel.getId() + " já está vinculado a outro cargo.");
    }

    Cargo cargo = new Cargo();
    cargo.setNome(dto.nome());
    cargo.setEmpresa(empresa);
    cargo.setActive(dto.isActive());

    CargoCompetencias requisitos = new CargoCompetencias();
    requisitos.setHabilidades(dto.habilidades());
    requisitos.setCargo(cargo);

    cargo.setRequisitos(requisitos);
    cargo.setNivel(nivel);
    cargo.setTrilhaDeCarreira(trilha);
    cargoRepository.save(cargo);
    return dto;
  }

  public List<CargoDto> listarCargosPorEmpresa() {
    UUID empresaId = AuthUtils.getEmpresaId();
    List<Cargo> cargos = cargoRepository.findByEmpresaId(empresaId);
    if (cargos.isEmpty()) {
      throw new ItemNotFoundException("Cargo", empresaId);
    }
    return cargos
      .stream()
      .map(
        cargo -> new CargoDto(
          cargo.getNome(),
          cargo.isActive(),
          cargo.getRequisitos().getHabilidades(),
          cargo.getNivel().getId()
        )
      )
      .toList();
  }

  public List<CargoDto> listarCargosPorTrilha(UUID trilhaId) {
    if (!trilhaDeCarreiraRepository.existsById(trilhaId)) {
      throw new ItemNotFoundException("Trilha de Carreira", trilhaId);
    }

    List<Cargo> cargos = cargoRepository.findByTrilhaDeCarreira_Id(trilhaId);
    if(cargos.isEmpty()) {
      throw new PDIBusinessRuleException("Nenhum cargo foi criado para a trilha de carreira com ID: " + trilhaId);
    }
    return cargos.stream()
      .map(cargo -> new CargoDto(
        cargo.getNome(),
        cargo.isActive(),
        cargo.getRequisitos().getHabilidades(),
        cargo.getNivel().getId()
      ))
      .collect(Collectors.toList());
  }

  public void excluirCargo(UUID id) {
    Cargo cargo = cargoRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Cargo", id));
    cargoRepository.delete(cargo);
  }
}