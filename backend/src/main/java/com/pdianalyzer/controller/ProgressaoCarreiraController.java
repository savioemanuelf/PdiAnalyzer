package com.pdianalyzer.controller;

import com.pdianalyzer.service.ProgressaoCarreiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/progressao-carreira")
@RequiredArgsConstructor
public class ProgressaoCarreiraController {

  private final ProgressaoCarreiraService progressaoCarreiraService;

  @GetMapping("/analise/{cargoId}")
  public ResponseEntity<String> obterAnaliseParaPromocao(@PathVariable UUID cargoId) {
    String respostaAnalise = progressaoCarreiraService.analiseDeProgressaoCarreira(cargoId);
    return ResponseEntity.ok(respostaAnalise);
  }
}
