package com.backend.agendacrista.demo.controller.dto;

import com.backend.agendacrista.demo.model.Estado;

import java.util.List;
import java.util.stream.Collectors;

public class EstadoDto {

    private Integer id;
    private String nome;
    private String uf;

    public EstadoDto(Estado estado) {
        this.id = estado.getId();
        this.nome = estado.getNome();
        this.uf = estado.getUf();
    }

    public static List<EstadoDto> converte(List<Estado> estados) {
        return estados.stream().map(EstadoDto::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUf() {
        return uf;
    }
}
