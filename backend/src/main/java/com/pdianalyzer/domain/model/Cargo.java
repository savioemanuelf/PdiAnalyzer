package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.CargoGenerico;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cargo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cargo extends CargoGenerico {

  @OneToOne
  @JoinColumn(name = "nivel_id")
  private Nivel nivel;

  @ManyToOne
  @JoinColumn(name = "trilha_carreira_id")
  private TrilhaDeCarreira trilhaDeCarreira;
}