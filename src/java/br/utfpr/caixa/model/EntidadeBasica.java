/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Marcos
 */
@MappedSuperclass 
public abstract class EntidadeBasica {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private int codigo;
    private String descricao; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        return getCodigo(); 
    }

    @Override
    public boolean equals(Object obj) {
        EntidadeBasica obj2 = (EntidadeBasica)obj;
        if (obj2 == null)
            return false;
        return getCodigo() == obj2.getCodigo();
    }
    
}
