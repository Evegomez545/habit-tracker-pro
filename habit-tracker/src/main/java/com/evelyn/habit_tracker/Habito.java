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
    @ManyToOne
    @jakarta.persistence.JoinColumn(name = "usuario_id")
    private Usuario usuario;

    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}