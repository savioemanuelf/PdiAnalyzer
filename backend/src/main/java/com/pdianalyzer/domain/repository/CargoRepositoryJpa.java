package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Cargo;
import com.smarthirepro.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CargoRepositoryJpa extends JpaRepository<Cargo, UUID> {


    List<Cargo> findByEmpresaId(UUID empresaId);

    List<Cargo> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, UUID empresaId);

    void deleteAllByEmpresaId(UUID id);
}