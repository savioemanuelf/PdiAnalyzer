package com.pdianalyzer.exception;

import com.smarthirepro.core.exception.BusinessRuleException;

public class PDIAnalysisException extends BusinessRuleException {
  public PDIAnalysisException(String message) {
    super(message);
  }
}

//throw new AnalisePdiException("Não é possível gerar o PDI, pois o colaborador não possui um currículo associado.");
//throw new AnalisePdiException("O nível alvo não possui competências definidas para a análise de gaps.");