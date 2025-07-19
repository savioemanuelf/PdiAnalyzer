package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.PlanoDesenvolvimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanoDesenvolvimentoRepository extends JpaRepository<PlanoDesenvolvimento, UUID> {

}
