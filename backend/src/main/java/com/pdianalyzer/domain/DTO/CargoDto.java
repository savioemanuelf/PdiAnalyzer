package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CargoDto(
  @NotNull UUID id,
  @NotBlank(message="O nome da vaga é obrigatório") String nome,
  @NotBlank UUID empresaId,
  boolean isActive,
  @NotBlank(message= "As habilidades necessárias são obrigatórias") String habilidades,
//  String competenciasTecnicas,
//  String competenciasPessoais,
//  String idiomas,
//  String formacao,
//  String experiencia,
  @NotNull(message = "Por favor, informe o id da trilha de carreira do cargo") UUID trilhaDeCarreiraId,
  @NotNull(message = "Informe o id do nível do cargo dentro da trilha de carreira") UUID nivelId
) {}
