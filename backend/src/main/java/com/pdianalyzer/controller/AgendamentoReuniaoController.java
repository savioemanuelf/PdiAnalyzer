package com.pdianalyzer.controller;

import com.pdianalyzer.service.AgendamentoReuniaoFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/agendamento-pdi")
@RequiredArgsConstructor
public class AgendamentoReuniaoController {

  private final AgendamentoReuniaoFeedbackService agendamentoService;

  @GetMapping("/reuniao/{cargoId}/{candidatoId}")
  public ResponseEntity<String> agendarReuniaoFeedback(@PathVariable UUID cargoId,
                                                       @PathVariable UUID candidatoId) {
    String linkMeet = agendamentoService.runAgendamento(cargoId, candidatoId);
    return ResponseEntity.ok("Reunião agendada! Link: " + linkMeet);
  }
}
