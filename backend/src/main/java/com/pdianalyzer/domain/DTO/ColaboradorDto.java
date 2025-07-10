package com.pdianalyzer.domain.DTO;

import com.pdianalyzer.domain.model.MetricasDesempenho;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record ColaboradorDto(
    @NotNull UUID id,
    @NotBlank String nome,
    @Email @NotBlank  String email,
    @Pattern(regexp = "\\+?\\d{8,15}") String telefone,
    MetricasDesempenho metricasDesempenho,
    CargoDto cargo
) {}
