package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.NivelDto;
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
  public Nivel criarNivel(NivelDto dto) {
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

    // Lógica para definir o próximo nível (sucessão)
    // Busca o nível anterior na mesma trilha para linkar
    trilha.getNiveis().stream()
      .filter(n -> n.getOrdem() == dto.ordem() - 1)
      .findFirst()
      .ifPresent(nivelAnterior -> nivelAnterior.setProximoNivel(nivel));

    return nivelRepository.save(nivel);
  }

  public List<Nivel> listarNiveisPorTrilha(UUID trilhaId) {
    if (!trilhaDeCarreiraRepository.existsById(trilhaId)) {
      throw new ItemNotFoundException("Trilha de Carreira", trilhaId);
    }
    return nivelRepository.findByTrilhaDeCarreiraIdOrderByOrdemAsc(trilhaId);
  }
}