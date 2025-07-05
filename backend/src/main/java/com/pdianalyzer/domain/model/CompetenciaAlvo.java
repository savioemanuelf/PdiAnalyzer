package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.CargoCompetenciasGenerico;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("PDI")
public class CompetenciaAlvo extends CargoCompetenciasGenerico {

}