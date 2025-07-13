package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.TrilhaDeCarreiraDto;
import com.pdianalyzer.domain.model.TrilhaDeCarreira;
import com.pdianalyzer.service.TrilhaDeCarreiraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trilhas-carreira")
@RequiredArgsConstructor
public class TrilhaDeCarreiraController {

  private final TrilhaDeCarreiraService trilhaDeCarreiraService;

  @PostMapping
  public ResponseEntity<TrilhaDeCarreira> criarTrilha(@Valid @RequestBody TrilhaDeCarreiraDto trilhaDto) {
    TrilhaDeCarreira novaTrilha = trilhaDeCarreiraService.criarTrilha(trilhaDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novaTrilha);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TrilhaDeCarreira> buscarTrilhaPorId(@PathVariable UUID id) {
    TrilhaDeCarreira trilha = trilhaDeCarreiraService.listarPorId(id);
    return ResponseEntity.ok(trilha);
  }

  @GetMapping("/me")
  public ResponseEntity<List<TrilhaDeCarreira>> listarTrilhasDaEmpresa() {
    List<TrilhaDeCarreira> trilhas = trilhaDeCarreiraService.listarTrilhasPorEmpresa();
    return ResponseEntity.ok(trilhas);
  }
}