package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CargoDto(
  @NotBlank(message="O nome da vaga é obrigatório") String nome,
  boolean isActive,
  @NotBlank(message= "As habilidades necessárias são obrigatórias") String habilidades,
//  String competenciasTecnicas,
//  String competenciasPessoais,
//  String idiomas,
//  String formacao,
//  String experiencia,
  @NotNull(message = "Informe o id do nível do cargo dentro da trilha de carreira") UUID nivelId
) {}
