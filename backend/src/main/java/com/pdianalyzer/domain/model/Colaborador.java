package com.pdianalyzer.domain.model;

import com.pdianalyzer.domain.DTO.ColaboradorRequestDto;
import com.pdianalyzer.exception.PDIBusinessRuleException;
import com.smarthirepro.domain.model.Candidato;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

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
            throw new PDIBusinessRuleException("O cargo associado a este colaborador não é um Cargo de PDI. Tipo encontrado: " + cargoGenerico.getClass().getName());
        }

        return (Cargo) cargoGenerico;
    }

    public void setCargoEspecifico(Cargo cargo) {
        super.setCargo(cargo);
    }

    @ManyToOne
    @JoinColumn(name = "metricas_desempenho_id")
    public MetricasDesempenho metricasDesempenho;

    public void atualizarColaborador(ColaboradorRequestDto data, MetricasDesempenho metricasDesempenho, Cargo cargo) {
        this.setNome(data.nome());
        this.setEmail(data.email());
        this.setTelefone(data.telefone());
        setMetricasDesempenho(metricasDesempenho);
        this.setCargo(cargo);
    }
}
