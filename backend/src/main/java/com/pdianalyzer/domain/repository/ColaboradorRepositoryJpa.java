package com.pdianalyzer.domain.repository;

import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.repositories.CandidatoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboradorRepositoryJpa
        extends JpaRepository<Candidato, UUID>, CandidatoRepository {

    @Override
    Candidato save(Candidato candidato);

    @Override
    Optional<Candidato> findByEmail(String email);
}