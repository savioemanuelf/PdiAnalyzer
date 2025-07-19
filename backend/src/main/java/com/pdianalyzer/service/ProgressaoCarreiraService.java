package com.pdianalyzer.service;

import com.smarthirepro.core.service.impl.AnaliseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressaoCarreiraService {

  private final PDIAnaliseService analiseTemplate;

  public String analiseDeProgressaoCarreira(UUID cargoId) {
    return analiseTemplate.runAnalise(cargoId);
  }
}
