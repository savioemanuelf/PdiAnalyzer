package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Colaborador;
import com.smarthirepro.domain.model.Candidato;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CandidatoRepositoryAdapter implements com.smarthirepro.domain.repositories.CandidatoRepository {

  private ColaboradorRepositoryJpa colaboradorRepositoryJpa;

  public CandidatoRepositoryAdapter(ColaboradorRepositoryJpa candidatoRepositoryJpa) {
    this.colaboradorRepositoryJpa = candidatoRepositoryJpa;
  }

  @Override
  public Optional<Candidato> findByEmail(String email) {
    return (Optional) colaboradorRepositoryJpa.findByEmail(email);
  }

  @Override
  public Colaborador save(Candidato candidato) {
    if (candidato instanceof Colaborador) {
      return colaboradorRepositoryJpa.save((Colaborador) candidato);
    }
    Colaborador colaborador = new Colaborador();
    colaborador.setNome(candidato.getNome());
    colaborador.setEmail(candidato.getEmail());
    colaborador.setTelefone(candidato.getTelefone());
    colaborador.setCurriculo(candidato.getCurriculo());
    colaborador.setCargo(candidato.getCargo());

    return colaboradorRepositoryJpa.save(colaborador);
  }
}

