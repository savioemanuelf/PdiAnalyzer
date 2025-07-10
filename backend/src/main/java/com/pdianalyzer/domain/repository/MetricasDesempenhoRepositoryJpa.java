package com.pdianalyzer.domain.repository;

import com.pdianalyzer.domain.model.MetricasDesempenho;
import com.smarthirepro.domain.model.Curriculo;
import com.smarthirepro.domain.repositories.CurriculoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MetricasDesempenhoRepositoryJpa
  extends JpaRepository<MetricasDesempenho, UUID>, CurriculoRepository {

}