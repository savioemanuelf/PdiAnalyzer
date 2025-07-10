package com.pdianalyzer.domain.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(
  @Email(message = "Email inválido")
  @NotBlank(message = "Email não pode ser vazio")
  String email
) {}
