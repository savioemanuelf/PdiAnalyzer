package com.pdianalyzer.exception;

import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.core.exception.FrameworkBaseException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ItemNotFoundException extends FrameworkBaseException {
  public ItemNotFoundException(String item, UUID id) {
    super(String.format("%s com identificador %s não foi encontrado.", item, id), HttpStatus.NOT_FOUND);
  }
}
