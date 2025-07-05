package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.PlanoDesenvolvimento;
import com.smarthirepro.domain.repositories.CargoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanoDesenvolvimentoRepository
        extends JpaRepository<PlanoDesenvolvimento, UUID>, CargoRepository<PlanoDesenvolvimento> {


}