package com.pdianalyzer.config;

import com.pdianalyzer.domain.DTO.PlanoDesenvolvimentoDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smarthirepro.core.service.IAvaliacaoLlm;
import com.smarthirepro.core.service.impl.AvaliacaoLlmImpl;

@Configuration
public class FrameworkBeansConfig {

  @Bean
  public AvaliacaoLlmImpl<PlanoDesenvolvimentoDto> avaliacaoLlmImpl(IAvaliacaoLlm avaliacaoLlm) {
    return new AvaliacaoLlmImpl<>(avaliacaoLlm, PlanoDesenvolvimentoDto.class);
  }
}
