package com.pdianalyzer.service;

import com.smarthirepro.core.service.impl.AnaliseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressaoCarreiraService {

  @Qualifier("PDIAnaliseService")
  private final AnaliseTemplate pdiAnaliseTemplate;

  public String analiseDeProgressaoCarreira(UUID cargoId) {
    return pdiAnaliseTemplate.runAnalise(cargoId);
  }
}
