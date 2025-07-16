package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.Cargo;
import com.pdianalyzer.domain.model.Nivel;
import com.smarthirepro.domain.model.Empresa;
import com.smarthirepro.domain.repositories.CargoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface CargoRepositoryJpa extends JpaRepository<Cargo, UUID>, CargoRepository<Cargo> {

  Optional<Cargo> findById(UUID var1);

  List<Cargo> findByEmpresaId(UUID empresaId);

  List<Cargo> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, UUID empresaId);

  void deleteAllByEmpresaId(UUID id);

  boolean existsByNivelId(UUID nivelId);

  List<Cargo> findByTrilhaDeCarreira_Id(UUID trilhaId);

  Optional<Nivel> findNivelById(UUID cargoId);
}