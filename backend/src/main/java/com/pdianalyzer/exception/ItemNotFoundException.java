package com.pdianalyzer.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {
  private final HttpStatus status;

  public ItemNotFoundException(String item, UUID id) {
    super(String.format("%s com identificador %s não foi encontrado.", item, id));
    this.status = HttpStatus.NOT_FOUND;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
