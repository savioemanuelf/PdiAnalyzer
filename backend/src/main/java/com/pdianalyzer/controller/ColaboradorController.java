package com.pdianalyzer.controller;

import com.pdianalyzer.domain.DTO.ColaboradorDto;
import com.pdianalyzer.domain.DTO.EmailDto;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.service.ColaboradorService;
import com.smarthirepro.core.service.impl.CandidatoService;
import com.smarthirepro.core.service.impl.CurriculoService;
import com.smarthirepro.domain.model.Curriculo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

  private final ColaboradorService colaboradorService;
  private final CandidatoService candidatoService;
  private final CurriculoService curriculoService;

  @PostMapping("/criarComCurriculo/{idCargo}")
  public ResponseEntity<?> analisarCurriculos(@PathVariable("idCargo") UUID idVaga,
                                              @RequestParam("file") MultipartFile file) {

    String path = curriculoService.pegarCaminhoDoCurriculo(file, idVaga);

    List<Curriculo> result = curriculoService.salvarCurriculo(path, idVaga);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/me")
  public ResponseEntity<List<ColaboradorDto>> listarColaboradoresDaEmpresaLogada() {
    List<ColaboradorDto> colaboradores = colaboradorService.listarTodosPorEmpresaId();
    return ResponseEntity.ok(colaboradores);
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
