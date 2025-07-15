package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.repository.CargoCompetenciasRepositoryJpa;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.smarthirepro.core.service.impl.AnaliseTemplate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PDIAnalise extends AnaliseTemplate<Cargo> {

  private final CargoCompetenciasRepositoryJpa cargoCompetenciasRepository;
  private final ColaboradorRepositoryJpa colaboradorRepositoryJpa;

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public List<String> definirCriterios(UUID id) {
    return null;
  }

  @Override
  public String executarAnalise(UUID id, List<String> criterios) {
    // Implementar a lógica de análise
    return null;
  }

  @Override
  public String criarRelatorio(String resultado) {
    // Implementar a lógica de criação do relatório
    return null;
  }
}
