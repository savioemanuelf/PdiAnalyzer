package com.pdianalyzer.service;

import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.Colaborador;
import com.pdianalyzer.domain.repository.ColaboradorRepositoryJpa;
import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.domain.model.Curriculo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

  private final ColaboradorRepositoryJpa colaboradorRepository;
  private final CargoServicePDI cargoServicePDI;

  public Colaborador criarColaboradorComCurriculo(Curriculo curriculo, UUID idCargo) {
    if (curriculo == null) {
      throw new BusinessRuleException("Currículo não pode ser nulo.");
    }

    verificarEmailEmUso(curriculo.getEmail());

    Colaborador colaborador = new Colaborador();
    colaborador.setCurriculo(curriculo);
    colaborador.setNome(curriculo.getNome());
    colaborador.setEmail(curriculo.getEmail());
    colaborador.setTelefone(curriculo.getTelefone());

    Cargo cargo = cargoServicePDI.listarPorId(idCargo);
    colaborador.setCargo(cargo);

    return (Colaborador) colaboradorRepository.save(colaborador);
  }

  private void verificarEmailEmUso(String email) {
    boolean emailEmUso = this.colaboradorRepository.findByEmail(email).isPresent();
    if (emailEmUso) {
      throw new BusinessRuleException("E-mail já cadastrado no sistema.");
    }
  }
}