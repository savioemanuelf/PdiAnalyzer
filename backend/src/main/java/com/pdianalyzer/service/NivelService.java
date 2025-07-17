package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.NivelDto;
import com.pdianalyzer.domain.DTO.NivelResponseDto;
import com.pdianalyzer.domain.model.CargoCompetencias;
import com.pdianalyzer.domain.model.Nivel;
import com.pdianalyzer.domain.model.TrilhaDeCarreira;
import com.pdianalyzer.domain.repository.NivelRepositoryJpa;
import com.pdianalyzer.domain.repository.TrilhaDeCarreiraRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NivelService {

  private final NivelRepositoryJpa nivelRepository;
  private final TrilhaDeCarreiraRepositoryJpa trilhaDeCarreiraRepository;

  @Transactional
  public NivelDto criarNivel(NivelDto dto) {
    TrilhaDeCarreira trilha = trilhaDeCarreiraRepository.findById(dto.trilhaDeCarreiraId())
      .orElseThrow(() -> new ItemNotFoundException("Trilha de Carreira", dto.trilhaDeCarreiraId()));

    trilha.getNiveis().stream()
      .filter(n -> n.getOrdem() == dto.ordem())
      .findAny()
      .ifPresent(n -> {
        throw new PDIBusinessRuleException("Já existe um nível com a ordem " + dto.ordem() + " nesta trilha.");
      });

    CargoCompetencias competencias = new CargoCompetencias();
    competencias.setCompetenciasTecnicas(dto.competenciasTecnicas());
    competencias.setCompetenciasPessoais(dto.competenciasPessoais());

    Nivel nivel = new Nivel();
    nivel.setSenioridade(dto.senioridade());
    nivel.setOrdem(dto.ordem());
    nivel.setTrilhaDeCarreira(trilha);
    nivel.setCargoCompetencias(competencias);

    // Busca o nível anterior na mesma trilha para linkar
    trilha.getNiveis().stream()
      .filter(n -> n.getOrdem() == dto.ordem() - 1)
      .findFirst()
      .ifPresent(nivelAnterior -> nivelAnterior.setProximoNivel(nivel));

    nivelRepository.save(nivel);
    return dto;
  }

  public List<NivelResponseDto> listarNiveisPorTrilha(UUID trilhaId) {
    if (!trilhaDeCarreiraRepository.existsById(trilhaId)) {
      throw new ItemNotFoundException("Trilha de Carreira", trilhaId);
    }
    return nivelRepository.findAllByTrilhaDeCarreiraIdOrderByOrdemAsc(trilhaId)
      .stream()
      .map(nivel -> new NivelResponseDto(
        nivel.getId(),
        nivel.getSenioridade(),
        nivel.getOrdem(),
        nivel.getTrilhaDeCarreira().getId(),
        nivel.getCargoCompetencias().getCompetenciasTecnicas(),
        nivel.getCargoCompetencias().getCompetenciasPessoais()
      ))
      .toList();
  }
}