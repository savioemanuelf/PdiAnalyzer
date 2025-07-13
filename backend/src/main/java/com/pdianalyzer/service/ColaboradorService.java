package com.pdianalyzer.service;

import com.pdianalyzer.domain.DTO.CargoDto;
import com.pdianalyzer.domain.DTO.ColaboradorDto;
import com.pdianalyzer.domain.DTO.ColaboradorRequestDto;
import com.pdianalyzer.domain.DTO.EmailDto;
import com.pdianalyzer.domain.DTO.MetricasDesempenhoDTO;
import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.pdianalyzer.domain.repository.CargoRepositoryJpa;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.pdianalyzer.domain.repository.EmpresaRepositoryJpa;
import com.pdianalyzer.domain.repository.MetricasDesempenhoRepositoryJpa;
import com.pdianalyzer.exception.ItemNotFoundException;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.core.service.impl.CandidatoService;
import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.model.Curriculo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

  private final ColaboradorRepositoryJpa colaboradorRepository;
  private final CandidatoService candidatoService;
  private final CargoRepositoryJpa cargoRepositoryJpa;
  private final EmpresaRepositoryJpa empresaRepositoryJpa;
  private final CargoServicePDI cargoServicePDI;
  private final MetricasDesempenhoRepositoryJpa metricasDesempenhoRepositoryJpa;

  @Transactional
  public Colaborador salvar(Colaborador colaborador) {
    verificarEmailEmUso(colaborador.getEmail(), colaborador);
    return colaboradorRepository.save(colaborador);
  }

  public ColaboradorDto listarColaboradores(Colaborador colaborador) {
    Cargo cargo = colaborador.getCargo();
    MetricasDesempenho metricasDesempenho = colaborador.getMetricasDesempenho();
    verificarNulidadeMetricasDesempenho(colaborador.getCurriculo());

    CargoDto cargoDto = null;
    if (cargo != null) {
      cargoDto = new CargoDto(
        cargo.getId(),
        cargo.getNome(),
        cargo.getEmpresa().getId(),
        cargo.isActive(),
        cargo.getRequisitos().getHabilidades(),
        cargo.getTrilhaDeCarreira().getId(),
        cargo.getNivel().getId()
      );
    }

    return new ColaboradorDto(
      colaborador.getId(),
      colaborador.getNome(),
      colaborador.getEmail(),
      colaborador.getTelefone(),
      colaborador.getMetricasDesempenho(),
      cargoDto);
  }

  public List<ColaboradorDto> listarTodos() {
    List<Colaborador> colaboradores = colaboradorRepository.findAll();
    return colaboradores.stream()
      .map(c -> {
        try {
          return listarColaboradores(c);
        } catch (BusinessRuleException e) {
          throw new PDIBusinessRuleException("Erro ao listar colaboradores: " + e.getMessage());
        }
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  public List<ColaboradorDto> listarTodosPorEmpresaId() {
    UUID empresaId = AuthUtils.getEmpresaId();
    List<Colaborador> colaboradores = colaboradorRepository.findByCargo_Empresa_Id(empresaId);
    return colaboradores.stream()
      .map(c -> {
        try {
          return listarColaboradores(c);
        } catch (PDIBusinessRuleException e) {
          throw new BusinessRuleException("Erro ao listar colaboradores da empresa: " + e.getMessage());
        }
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
//
//  public List<ColaboradorDto> buscarCandidatoPorNome(String nomeColaborador) {
//    List<Colaborador> colaboradores = colaboradorRepository.findByNomeContainingIgnoreCase(nomeColaborador);
//    List<ColaboradorDto> colaboradoresDto = colaboradores.stream()
//      .map(this::listarColaboradores)
//      .collect(Collectors.toList());
//    if (colaboradoresDto.isEmpty()) {
//      throw new PDIBusinessRuleException("Colaborador não encontrado.");
//    }
//    return colaboradoresDto;
//  }
//
//  public Candidato atualizarColaboradorPorId(UUID id, ColaboradorRequestDto data) {
//    Colaborador colaborador = colaboradorRepository.findById(id)
//      .orElseThrow(() -> new ItemNotFoundException("Colaborador", id));
//
//    MetricasDesempenho metricasDesempenho = metricasDesempenhoRepositoryJpa.findById(data.metricasDesempenhoId())
//      .orElseThrow(() -> new ItemNotFoundException("MétricasDesempenho", data.metricasDesempenhoId()));
//
//    Cargo cargo = cargoRepositoryJpa.findById(data.cargoId())
//      .orElseThrow(() -> new ItemNotFoundException("Cargo", data.cargoId()));
//
//    verificarNulidadeMetricasDesempenho(metricasDesempenho);
//    verificarCargoAtivo(cargo);
//    colaborador.atualizarCom(data, metricasDesempenho, cargo);
//
//    return colaboradorRepository.save(colaborador);
//  }

  public Colaborador atualizarEmailPorId(UUID id, EmailDto email) {
    Colaborador colaborador = colaboradorRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", id));
    colaborador.updateEmail(email.email());
    return colaboradorRepository.save(colaborador);
  }

  @Transactional
  public void deletarColaboradorPorId(UUID id) {
    if (!colaboradorRepository.existsById(id)) {
      throw new ItemNotFoundException("Colaborador", id);
    }
    Colaborador colaborador = colaboradorRepository.findById(id)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", id));

    if (colaborador.getCargo() != null) {
      throw new PDIBusinessRuleException("Colaborador não pode ser deletado, pois é empregado da empresa.");
    }
    colaboradorRepository.delete(colaborador);
  }


  public void associarAoCargo(UUID idColaborador, UUID idCargo) {

    Colaborador colaborador = colaboradorRepository.findById(idColaborador)
      .orElseThrow(() -> new ItemNotFoundException("Colaborador", idColaborador));

    Cargo cargo = cargoRepositoryJpa.findById(idCargo)
      .orElseThrow(() -> new ItemNotFoundException("Cargo", idCargo));


    if (colaborador.getCargo() != null) {
      throw new PDIBusinessRuleException("Colaborador já está vinculado à empresa.");
    }
    verificarCargoAtivo(cargo);
    colaborador.setCargo(cargo);
    colaboradorRepository.save(colaborador);
  }

  private void verificarNulidadeMetricasDesempenho(Curriculo curriculo) {
    if (curriculo == null) {
      throw new PDIBusinessRuleException("Currículo não pode ser nulo.");
    }
  }

  private void verificarEmailEmUso(String email, Candidato candidato) {
    boolean emailEmUso = colaboradorRepository.findByEmail(candidato.getEmail())
      .filter(e -> !e.equals(candidato))
      .isPresent();
    if (emailEmUso) {
      throw new PDIBusinessRuleException("E-mail já cadastrado no sistema.");
    }
  }

  private void verificarCargoAtivo(Cargo cargo) {
    if (!cargo.isActive()) {
      throw new PDIBusinessRuleException("Esse cargo não está ativo.");
    }
  }
}