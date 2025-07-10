package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ColaboradorRequestDto (
  @NotBlank String nome,
  @NotBlank @Email String email,
  @NotBlank String telefone,
  @NotNull UUID metricasDesempenhoId,
  @NotNull UUID cargoId,
  @NotBlank String nivel
) {}

