package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.ColaboradorDto;
import com.pdianalyzer.domain.DTO.ColaboradorRequestDto;
import com.pdianalyzer.domain.DTO.EmailDto;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

  private final ColaboradorService colaboradorService;

  @PostMapping
  public ResponseEntity<Colaborador> salvar(@Valid @RequestBody Colaborador colaborador) {
    Colaborador novoColaborador = colaboradorService.salvar(colaborador);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoColaborador);
  }

  @GetMapping("/me")
  public ResponseEntity<List<ColaboradorDto>> listarColaboradoresDaEmpresaLogada() {
    List<ColaboradorDto> colaboradores = colaboradorService.listarTodosPorEmpresaId();
    return ResponseEntity.ok(colaboradores);
  }

  @GetMapping("/buscar")
  public ResponseEntity<List<ColaboradorDto>> buscarPorNome(@RequestParam("nome") String nome) {
    List<ColaboradorDto> colaboradores = colaboradorService.buscarCandidatoPorNome(nome);
    return ResponseEntity.ok(colaboradores);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> atualizarColaborador(@PathVariable UUID id, @Valid @RequestBody ColaboradorRequestDto data) {
    colaboradorService.atualizarColaboradorPorId(id, data);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/email")
  public ResponseEntity<Colaborador> atualizarEmailDoColaborador(@PathVariable UUID id, @Valid @RequestBody EmailDto email) {
    Colaborador colaboradorAtualizado = colaboradorService.atualizarEmailPorId(id, email);
    return ResponseEntity.ok(colaboradorAtualizado);
  }

  @PostMapping("/{idColaborador}/associar-cargo/{idCargo}")
  public ResponseEntity<Void> associarColaboradorAoCargo(@PathVariable UUID idColaborador, @PathVariable UUID idCargo) {
    colaboradorService.associarAoCargo(idColaborador, idCargo);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarColaborador(@PathVariable UUID id) {
    colaboradorService.deletarColaboradorPorId(id);
    return ResponseEntity.noContent().build();
  }
}
