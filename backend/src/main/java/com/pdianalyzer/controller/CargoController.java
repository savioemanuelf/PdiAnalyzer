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
import java.util.UUID;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController {

  private final CargoServicePDI cargoService;

  @PostMapping
  public ResponseEntity<?> criarCargo(@RequestBody @Valid CargoDto dto) {
    cargoService.criarCargo(dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cargo> buscarCargoPorId(@PathVariable UUID id) {
    Cargo cargo = cargoService.buscarPorId(id);
    return ResponseEntity.ok(cargo);
  }

  @GetMapping
  public ResponseEntity<List<Cargo>> listarCargosPorEmpresa() {
    List<Cargo> cargos = cargoService.listarCargosPorEmpresa();
    return ResponseEntity.ok(cargos);
  }
}
