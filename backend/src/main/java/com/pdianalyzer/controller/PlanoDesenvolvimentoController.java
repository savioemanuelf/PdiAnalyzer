package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.PlanoDesenvolvimentoDto;
import com.pdianalyzer.service.PlanoDesenvolvimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plano-desenvolvimento")
public class PlanoDesenvolvimentoController {
  private final PlanoDesenvolvimentoService planoDesenvolvimentoService;

  @PostMapping("/gerar/{cargoId}/{colaboradorId}")
  public ResponseEntity<PlanoDesenvolvimentoDto> gerarPlanoDesenvolvimentoIndividual(@PathVariable("cargoId") UUID cargoId,
                                                                                     @PathVariable("colaboradorId") UUID colaboradorId) {
    PlanoDesenvolvimentoDto planoDesenvolvimento = planoDesenvolvimentoService.realizarPlanoDesenvolvimento(cargoId, colaboradorId);
    planoDesenvolvimentoService.salvarPlanoDesenvolvimento(planoDesenvolvimento, colaboradorId);
    return ResponseEntity.ok(planoDesenvolvimento);
  }
}
