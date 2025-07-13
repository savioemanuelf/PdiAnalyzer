package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.TrilhaDeCarreiraDto;
import com.pdianalyzer.domain.model.TrilhaDeCarreira;
import com.pdianalyzer.domain.repository.TrilhaDeCarreiraRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;
import com.smarthirepro.domain.repositories.EmpresaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrilhaDeCarreiraService {

  private final TrilhaDeCarreiraRepositoryJpa trilhaDeCarreiraRepository;
  private final EmpresaRepository empresaRepository;

  @Transactional
  public TrilhaDeCarreira criarTrilha(TrilhaDeCarreiraDto dto) {
    UUID empresaId = AuthUtils.getEmpresaId();
    Empresa empresa = empresaRepository.findById(empresaId)
      .orElseThrow(() -> new ItemNotFoundException("Empresa", empresaId));

    TrilhaDeCarreira trilha = new TrilhaDeCarreira();
    trilha.setNome(dto.nome());
    trilha.setEmpresa(empresa);

    return trilhaDeCarreiraRepository.save(trilha);
  }

  public TrilhaDeCarreira listarPorId(UUID id) {
    return trilhaDeCarreiraRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Trilha de Carreira", id));
  }

  public List<TrilhaDeCarreira> listarTrilhasPorEmpresa() {
    UUID empresaId = AuthUtils.getEmpresaId();
    return trilhaDeCarreiraRepository.findByEmpresaId(empresaId);
  }
}