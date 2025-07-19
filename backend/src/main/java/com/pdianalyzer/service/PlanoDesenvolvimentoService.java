package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.PlanoDesenvolvimentoDto;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.PlanoDesenvolvimento;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.domain.repository.PlanoDesenvolvimentoRepository;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.smarthirepro.core.service.impl.AvaliacaoLlmImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanoDesenvolvimentoService {
  private final AvaliacaoLlmImpl<PlanoDesenvolvimentoDto> avaliacaoLlm;
  private final PlanoDesenvolvimentoRepository planoDesenvolvimentoRepository;
  private final ColaboradorRepositoryJpa colaboradorRepository;

  public PlanoDesenvolvimentoDto realizarPlanoDesenvolvimento(UUID cargoId, UUID colaboradorId) {
    return avaliacaoLlm.realizarAvaliacao(cargoId, colaboradorId);
  }

  public PlanoDesenvolvimento convertDtoToEntity(PlanoDesenvolvimentoDto dto) {
    PlanoDesenvolvimento entity = new PlanoDesenvolvimento();

    if(dto.getPrazoConclusao() != null && !dto.getPrazoConclusao().isEmpty()) {
      entity.setPrazoConclusao(LocalDate.parse(dto.getPrazoConclusao()));
    }

    entity.setAcao(dto.getAcao());
    entity.setIndicadorSucesso(dto.getIndicadorSucesso());
    entity.setObjetivo(dto.getObjetivo());
    entity.setRecursoResponsavel(dto.getRecursoResponsavel());

    return entity;
  }

  public PlanoDesenvolvimentoDto salvarPlanoDesenvolvimento(PlanoDesenvolvimentoDto planoDesenvolvimentoDto, UUID colaboradorId) {
    PlanoDesenvolvimento planoDesenvolvimento = convertDtoToEntity(planoDesenvolvimentoDto);
    planoDesenvolvimentoRepository.save(planoDesenvolvimento);
    Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
        .orElseThrow(() -> new ItemNotFoundException("Colaborador", colaboradorId));
    colaborador.setPdi(planoDesenvolvimento);
    colaboradorRepository.save(colaborador);
    return planoDesenvolvimentoDto;
  }

}
