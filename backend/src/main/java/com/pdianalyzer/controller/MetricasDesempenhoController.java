package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.MetricasDesempenhoDto;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.service.MetricasDesempenhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/metricas-desempenho")
@RequiredArgsConstructor
public class MetricasDesempenhoController {

  private final MetricasDesempenhoService metricasService;

  @PostMapping
  public ResponseEntity<MetricasDesempenhoDto> criarOuAtualizar(@Valid @RequestBody MetricasDesempenhoDto dto) {
    MetricasDesempenhoDto metricasSalvas = metricasService.criarMetricas(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(metricasSalvas);
  }

  @GetMapping("/colaborador/{colaboradorId}")
  public ResponseEntity<MetricasDesempenhoDto> buscarPorColaborador(@PathVariable UUID colaboradorId) {
    MetricasDesempenhoDto metricas = metricasService.buscarPorColaboradorId(colaboradorId);
    return ResponseEntity.ok(metricas);
  }

  @DeleteMapping("/colaborador/{colaboradorId}")
  public ResponseEntity<Map<String, String>> deletarPorColaborador(@PathVariable UUID colaboradorId) {
    metricasService.deletarPorColaboradorId(colaboradorId);
    Map<String, String> response = Map.of("message", "Métricas de desempenho do colaborador " + colaboradorId + " foram excluídas com sucesso.");
    return ResponseEntity.ok(response);
  }
}