package com.pdianalyzer.controller;

import com.smarthirepro.core.service.impl.CurriculoService;
import com.smarthirepro.domain.model.Curriculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/curriculos")
public class CurriculoController {

  @Autowired
  private CurriculoService curriculoService;

  @PostMapping("/analisar-curriculos/{idCargo}")
  public ResponseEntity<?> analisarCurriculos(@PathVariable("idCargo") UUID idCargo,
                                              @RequestParam("file") MultipartFile file) {

    String path = curriculoService.pegarCaminhoDoCurriculo(file, idCargo);

    List<Curriculo> result = curriculoService.salvarCurriculo(path, idCargo);
    return ResponseEntity.ok(result);
  }
}