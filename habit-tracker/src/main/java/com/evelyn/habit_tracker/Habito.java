package com.evelyn.habit_tracker;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor 
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String nome;
    private boolean concluido;

    
    public Habito(String nome) {
        this.nome = nome;
        this.concluido = false;
    }
}