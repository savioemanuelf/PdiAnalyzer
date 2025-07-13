package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.CargoDto;
import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.service.CargoServicePDI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController {

  private final CargoServicePDI cargoService;

  @PostMapping
  public ResponseEntity<CargoDto> criarCargo(@RequestBody @Valid CargoDto dto) {
    CargoDto cargo = cargoService.criarCargo(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CargoDto> buscarCargoPorId(@PathVariable UUID id) {
    CargoDto cargo = cargoService.buscarPorId(id);
    return ResponseEntity.ok(cargo);
  }

  @GetMapping
  public ResponseEntity<List<CargoDto>> listarCargosPorEmpresa() {
    List<CargoDto> cargos = cargoService.listarCargosPorEmpresa();
    return ResponseEntity.ok(cargos);
  }

  @DeleteMapping("/{cargoId}")
  public ResponseEntity<Map<String, String>> apagarCargo(@PathVariable UUID cargoId) {
    cargoService.excluirCargo(cargoId);
    Map<String, String> response = Map.of("mensagem", "Cargo com ID " + cargoId + " foi excluído com sucesso.");
    return ResponseEntity.ok(response);
  }
}
