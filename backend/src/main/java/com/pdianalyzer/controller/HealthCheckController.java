package com.pdianalyzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  public HealthCheckController() {
    System.out.println("HealthCheckController inicializado");
  }

  @GetMapping("/health")
  public String health() {
    return "OK";
  }
}