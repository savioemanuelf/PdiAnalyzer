package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Colaborador;
import com.smarthirepro.domain.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboradorRepositoryJpa
        extends JpaRepository<Colaborador, UUID>{

    Optional<Candidato> findByEmail(String email);

    @Query("SELECT c FROM Colaborador c JOIN c.cargo cargo " +
            "JOIN cargo.nivel nivel " +
            "JOIN nivel.trilhaDeCarreira trilha " +
            "WHERE trilha.empresa.id = :empresaId")
    List<Colaborador> findByEmpresaId(UUID empresaId);

    List<Colaborador> findByNomeContainingIgnoreCase(String nomeColaborador);

    Optional<Colaborador> findByCargo_Id(UUID cargoId);

    @Query("SELECT c FROM Colaborador c WHERE c.cargo.id = :cargoId")
    Optional<Colaborador> findByCargoId(@Param("cargoId") UUID cargoId);
}