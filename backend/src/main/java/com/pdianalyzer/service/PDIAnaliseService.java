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
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PDIAnaliseService extends AnaliseTemplate<Cargo> {

  private final CargoCompetenciasRepositoryJpa cargoCompetenciasRepository;
  private final ColaboradorRepositoryJpa colaboradorRepositoryJpa;
  private final CargoRepositoryJpa cargoRepositoryJpa;
  private final NivelRepositoryJpa nivelRepository;

  private final RestTemplate restTemplate = new RestTemplate();
  private final PromptService promptService;
  private final ColaboradorService colaboradorService;

  @Override
  public List<String> definirCriterios(UUID cargoId) {
    List<String> competenciasNecessarias = new ArrayList<>();

    Colaborador colaborador = colaboradorRepositoryJpa.findByCargoId(cargoId)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador ", cargoId));

    Nivel nivelAtual = colaborador.getCargo().getNivel();

    Nivel nivel = nivelAtual.getProximoNivel();
    if (nivel == null) {
      throw new PDIBusinessRuleException("Colaborador está no último nível da carreira.");
    }
    int nivelOrdem = nivel.getOrdem();
    String cargoCompetenciasId = nivel.getCargoCompetencias().getId();
    CargoCompetencias cargoCompetencias = cargoCompetenciasRepository.findById(cargoCompetenciasId)
      .orElseThrow(() -> new PDIBusinessRuleException("Item não encontrado: Cargo Competências com ID: " + cargoCompetenciasId));


    if (nivelOrdem >= 2) {
      competenciasNecessarias.add("Competências Técnicas: " + cargoCompetencias.getCompetenciasTecnicas());
      competenciasNecessarias.add("Competências Comportamentais: " + cargoCompetencias.getCompetenciasPessoais());
    } else if (nivelOrdem >= 3 && nivelOrdem <= 4) {
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
  public String executarAnalise(UUID cargoId, List<String> competenciasNecessarias) {
    String flaskUrl = "http://localhost:5000/avaliar"; // criar endpoint de análise de métricas
    Colaborador colaborador = colaboradorRepositoryJpa.findByCargoId(cargoId)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador (cargoId) ", cargoId));
    Nivel proximoNivel = colaboradorService.getProximoNivel(colaborador.getId());
    if (proximoNivel == null) {
      throw new PDIBusinessRuleException("O colaborador já está no último nível da carreira.");
    }
    MetricasDesempenho desempenho = colaborador.getMetricasDesempenho();


    List<String> analiseRequest = promptService.gerarPromptDeAnaliseDeGaps(colaborador, proximoNivel, desempenho, competenciasNecessarias);
    String promptTexto = analiseRequest.get(0);

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      Map<String, String> payload = Map.of("prompt", promptTexto);

      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);

      ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);
      return response.getBody();
    } catch (RestClientException exception) {
      throw new FlaskConnectionException("Erro ao se conectar com o serviço externo");
    } catch (Exception e) {
      throw new FlaskConnectionException("Erro inesperado ao buscar pontuação do colaborador");
    }
  }

}
