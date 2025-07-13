package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.Empresa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "trilha_carreira")
public class TrilhaDeCarreira {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String nome;

  @ManyToOne
  private Empresa empresa;

  @OneToMany(mappedBy = "trilhaDeCarreira", cascade = CascadeType.ALL)
  @OrderBy("ordem ASC")
  private List<Nivel> niveis;

}
