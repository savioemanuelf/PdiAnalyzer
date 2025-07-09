package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.CargoCompetenciasGenerico;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cargo_habilidades")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("PDI")
public class CargoCompetencias extends CargoCompetenciasGenerico {

}
