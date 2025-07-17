package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.NivelDto;
import com.pdianalyzer.domain.DTO.NivelResponseDto;
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
  public ResponseEntity<?> criarNovoNivel(@Valid @RequestBody NivelDto nivelDto) {
    NivelDto nivel = nivelService.criarNivel(nivelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(nivel);
  }

  @GetMapping("/trilha/{trilhaId}")
  public ResponseEntity<List<NivelResponseDto>> listarNiveisPorTrilha(@PathVariable UUID trilhaId) {
    List<NivelResponseDto> niveis = nivelService.listarNiveisPorTrilha(trilhaId);
    return ResponseEntity.ok(niveis);
  }
}