package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.PlanoDesenvolvimentoDto;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.PlanoDesenvolvimento;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.domain.repository.PlanoDesenvolvimentoRepository;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import com.smarthirepro.core.service.impl.AgendamentoTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoReuniaoFeedbackService extends AgendamentoTemplate<PlanoDesenvolvimentoDto> {

  private final ColaboradorRepositoryJpa colaboradorRepository;
  private final PlanoDesenvolvimentoRepository planoDesenvolvimentoRepository;
  private final GoogleCalendarService googleCalendarService;

  @Override
  protected PlanoDesenvolvimentoDto buscarDadosRelatorio(UUID cargoId) {
    Colaborador colaborador = colaboradorRepository.findByCargoId(cargoId)
        .orElseThrow(() -> new ItemNotFoundException("Colaborador", cargoId));

    UUID pdiId = colaborador.getPdi().getId();
    PlanoDesenvolvimento pdi = planoDesenvolvimentoRepository.findById(pdiId)
        .orElseThrow(() -> new ItemNotFoundException("Plano de Desenvolvimento Individual", pdiId));


    return new PlanoDesenvolvimentoDto(
      pdi.getObjetivo(),
      pdi.getAcao(),
      pdi.getPrazoConclusao().toString(),
      pdi.getIndicadorSucesso(),
      pdi.getRecursoResponsavel()
    );
  }

  @Override
  protected String agendarReuniao(PlanoDesenvolvimentoDto dto, UUID colaboradorId) {
    LocalDate data = LocalDate.parse(dto.getPrazoConclusao());
    ZonedDateTime dataReuniao = data //ISO-8601
      .atStartOfDay(ZoneId.of("America/Sao_Paulo"))
      .minusWeeks(2);
    ZonedDateTime inicio = dataReuniao
      .withHour(10);
    ZonedDateTime fim = inicio.withHour(12);

    Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", colaboradorId));

    String titulo = "Reunião de Feedback PDI - " + colaborador.getNome();
    String descricao = "Reunião para discutir o PDI do colaborador " + colaborador.getNome() + ".\n" +
      "com prazo de Conclusão: " + dto.getPrazoConclusao();
    List<String> emailsConvidados = colaborador.getEmail().lines().toList();

    String linkMeet = null;
    try {
      linkMeet = googleCalendarService.agendarReuniaoComGoogleMeet(
        titulo,
        descricao,
        inicio,
        fim,
        emailsConvidados
      );
    } catch (IOException e) {
      throw new PDIBusinessRuleException("Não foi possível obter o link do Google Meet pois sua conta não possui esta permissão do Google Workspace. " + e.getMessage() + e);
    }

    return "Reunião agendada e link gerado com sucesso. Link: " + linkMeet;
  }

}
