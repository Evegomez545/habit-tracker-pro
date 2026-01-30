package com.evelyn.habit_tracker;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historico_diario")
public class HistoricoDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diaSemana;
    private LocalDate data;
    private double porcentagem;

    
    public double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }

    public HistoricoDiario() {
    }

    public HistoricoDiario(String diaSemana, LocalDate data) {
        this.diaSemana = diaSemana;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}