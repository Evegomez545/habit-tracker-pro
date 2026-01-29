package com.evelyn.habit_tracker;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Isso cria o construtor vazio que o Hibernate exige
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Isso faz o ID ser 1, 2, 3... automático
    private Long id;
    
    private String nome;
    private boolean concluido;

    // Construtor manual para facilitar o Controller
    public Habito(String nome) {
        this.nome = nome;
        this.concluido = false;
    }
}