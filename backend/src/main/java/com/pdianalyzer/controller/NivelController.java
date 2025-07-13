package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.NivelDto;
import com.pdianalyzer.domain.model.Nivel;
import com.pdianalyzer.service.NivelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/niveis")
@RequiredArgsConstructor
public class NivelController {

  private final NivelService nivelService;

  @PostMapping
  public ResponseEntity<Nivel> criarNovoNivel(@Valid @RequestBody NivelDto nivelDto) {
    Nivel novoNivel = nivelService.criarNivel(nivelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoNivel);
  }

  @GetMapping("/trilha/{trilhaId}")
  public ResponseEntity<List<Nivel>> listarNiveisPorTrilha(@PathVariable UUID trilhaId) {
    List<Nivel> niveis = nivelService.listarNiveisPorTrilha(trilhaId);
    return ResponseEntity.ok(niveis);
  }
}