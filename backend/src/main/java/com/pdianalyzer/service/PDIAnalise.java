package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.CargoCompetencias;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.domain.model.Nivel;
import com.pdianalyzer.domain.repository.CargoCompetenciasRepositoryJpa;
import com.pdianalyzer.domain.repository.CargoRepositoryJpa;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.domain.repository.NivelRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import com.smarthirepro.core.exception.FlaskConnectionException;
import com.smarthirepro.core.service.impl.AnaliseTemplate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PDIAnalise extends AnaliseTemplate<Cargo> {

  private final CargoCompetenciasRepositoryJpa cargoCompetenciasRepository;
  private final ColaboradorRepositoryJpa colaboradorRepositoryJpa;
  private final CargoRepositoryJpa cargoRepositoryJpa;
  private final NivelRepositoryJpa nivelRepository;

  private final RestTemplate restTemplate = new RestTemplate();
  private final PromptService promptService;
  private final ColaboradorService colaboradorService;

  @Override
  public List<String> definirCriterios(UUID proximoNivelId) {
    List<String> competenciasNecessarias = new ArrayList<>();

    Nivel nivel = nivelRepository.findById(proximoNivelId)
      .orElseThrow(() -> new ItemNotFoundException("Próximo nível", proximoNivelId));
    int nivelOrdem = nivel.getOrdem();
    UUID cargoCompetenciasId = UUID.fromString(nivel.getCargoCompetencias().getId());
    CargoCompetencias cargoCompetencias = cargoCompetenciasRepository.findById(cargoCompetenciasId)
      .orElseThrow(() -> new ItemNotFoundException("Cargo não encontrado com ID: ", cargoCompetenciasId));


    if (nivelOrdem == 1) {
      competenciasNecessarias.add("Competências Técnicas: " + cargoCompetencias.getCompetenciasTecnicas());
      competenciasNecessarias.add("Competências Comportamentais: " + cargoCompetencias.getCompetenciasPessoais());
    } else if (nivelOrdem >= 2 && nivelOrdem < 4) {
      competenciasNecessarias.add("Competências Técnicas: " + cargoCompetencias.getCompetenciasTecnicas());
      competenciasNecessarias.add("Competências Comportamentais: " + cargoCompetencias.getCompetenciasPessoais());
      competenciasNecessarias.add("Competências de Idiomas: " + cargoCompetencias.getIdiomas());
    } else {
      competenciasNecessarias.add("Competências Técnicas: " + cargoCompetencias.getCompetenciasTecnicas());
      competenciasNecessarias.add("Competências Comportamentais: " + cargoCompetencias.getCompetenciasPessoais());
      competenciasNecessarias.add("Competências de Idiomas: " + cargoCompetencias.getIdiomas());
      competenciasNecessarias.add("Certificações: " + cargoCompetencias.getCertificacoes());
    }
    return competenciasNecessarias;
  }


  @Override
  public String executarAnalise(UUID colaboradorId, List<String> competenciasNecessarias) {
    String flaskUrl = "http://localhost:5000/compare_resumes"; // criar endpoint de análise de métricas
    Colaborador colaborador = colaboradorRepositoryJpa.findById(colaboradorId)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador não encontrado com ID: ", colaboradorId));
    Nivel proximoNivel = colaboradorService.getProximoNivel(colaboradorId);
    if (proximoNivel == null) {
      throw new PDIBusinessRuleException("Não há próximo nível definido para o colaborador.");
    }
    MetricasDesempenho desempenho = colaborador.getMetricasDesempenho();


    List<String> analiseRequest = promptService.gerarPromptDeAnaliseDeGaps(colaborador, proximoNivel, desempenho, competenciasNecessarias);
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<List<String>> requestEntity = new HttpEntity<>(analiseRequest, headers);

      ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);
      return response.getBody();
    } catch (RestClientException exception) {
      throw new FlaskConnectionException("Erro ao se conectar com o serviço externo");
    } catch (Exception e) {
      throw new FlaskConnectionException("Erro inesperado ao buscar pontuação do colaborador");
    }
  }

  @Override
  public String criarRelatorio(String resultado) {
    // Implementar a lógica de criação do relatório

    return null;
  }
}
