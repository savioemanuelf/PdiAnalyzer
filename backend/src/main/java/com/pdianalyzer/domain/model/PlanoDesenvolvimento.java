package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.CargoGenerico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@DiscriminatorValue("PDI")
public class PlanoDesenvolvimento extends CargoGenerico {

    @ManyToOne
    @JoinColumn(name = "cargo_alvo_id")
    private Cargo cargoAlvo;

}