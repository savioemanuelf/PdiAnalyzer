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
    return colaboradorRepositoryJpa.findByEmail(email);
  }

  @Override
  public Colaborador save(Candidato candidato) {
    return colaboradorRepositoryJpa.save((Colaborador) candidato);
  }
}

