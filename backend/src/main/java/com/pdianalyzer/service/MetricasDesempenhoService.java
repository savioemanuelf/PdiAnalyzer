package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.MetricasDesempenhoDto;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.domain.repository.MetricasDesempenhoRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetricasDesempenhoService {

  private final MetricasDesempenhoRepositoryJpa metricasRepository;
  private final ColaboradorRepositoryJpa colaboradorRepository;
  private final ColaboradorService colaboradorService;

  @Transactional
  public MetricasDesempenhoDto criarMetricas(MetricasDesempenhoDto dto) {
    Colaborador colaborador = colaboradorRepository.findById(dto.colaboradorId())
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", dto.colaboradorId()));

    MetricasDesempenho metricas = metricasRepository.findByColaborador_Id(colaborador.getId())
      .orElse(new MetricasDesempenho());

    metricas.setColaborador(colaborador);
    metricas.setCompetenciasTecnicas(dto.competenciasTecnicas());
    metricas.setCompetenciasPessoais(dto.competenciasPessoais());
    metricasRepository.save(metricas);

    colaborador.setMetricasDesempenho(metricas);
    colaboradorRepository.save(colaborador);

    return dto;
  }

  public MetricasDesempenhoDto buscarPorColaboradorId(UUID colaboradorId) {
    MetricasDesempenho metricas = metricasRepository.findByColaborador_Id(colaboradorId)
      .orElseThrow(() -> new ItemNotFoundException("Métricas de Desempenho para o colaborador", colaboradorId));

    return new MetricasDesempenhoDto(
        metricas.getColaborador().getId(),
        metricas.getCompetenciasTecnicas(),
        metricas.getCompetenciasPessoais()
    );
  }

  @Transactional
  public void deletarPorColaboradorId(UUID colaboradorId) {
    MetricasDesempenho metricas = metricasRepository.findByColaborador_Id(colaboradorId)
      .orElseThrow(() -> new ItemNotFoundException("Métricas de Desempenho para o colaborador", colaboradorId));

    metricasRepository.delete(metricas);
  }
}