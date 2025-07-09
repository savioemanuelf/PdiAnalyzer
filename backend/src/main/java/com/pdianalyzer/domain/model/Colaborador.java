package com.pdianalyzer.domain.model;

import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.model.Curriculo;
import com.smarthirepro.domain.model.Empresa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Colaborador extends Candidato {

    @OneToOne
    public Cargo getCargoEspecifico() {
        if (super.getCargo() instanceof Cargo) {
            return (Cargo) super.getCargo();
        }
        return null;
    }

    @OneToOne
    public Cargo getCargo() {
        var cargoGenerico = super.getCargo();

        if (cargoGenerico == null) {
            return null;
        }
        if (!(cargoGenerico instanceof Cargo)) {
            throw new IllegalStateException("O cargo associado a este colaborador não é um Cargo de PDI. Tipo encontrado: " + cargoGenerico.getClass().getName());
        }

        return (Cargo) cargoGenerico;
    }

    public void setCargoEspecifico(Cargo cargo) {
        super.setCargo(cargo);
    }
}
