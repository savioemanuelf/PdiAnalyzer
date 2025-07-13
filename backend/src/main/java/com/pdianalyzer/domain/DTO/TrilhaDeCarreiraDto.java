package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TrilhaDeCarreiraDto(
  @NotBlank String nome,
  UUID nivelId
){}
