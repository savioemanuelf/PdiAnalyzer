package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Colaborador;
import com.smarthirepro.domain.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboradorRepositoryJpa
        extends JpaRepository<Colaborador, UUID>{

    Optional<Candidato> findByEmail(String email);

    List<Colaborador> findByCargo_Empresa_Id(UUID empresaId);

    List<Colaborador> findByNomeContainingIgnoreCase(String nomeColaborador);
}