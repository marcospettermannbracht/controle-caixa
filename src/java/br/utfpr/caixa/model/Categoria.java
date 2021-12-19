/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import javax.persistence.Entity;

/**
 *
 * @author Marcos
 */
@Entity
public class Categoria extends EntidadeBasica {
    private TipoLancamento tipo;
    
    public Categoria() {
        
    }
    
    public Categoria(int codigo, String descricao, TipoLancamento tipo) {
        this.setCodigo(codigo);
        this.setDescricao(descricao);
        this.tipo = tipo;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoLancamento tipo) {
        this.tipo = tipo;
    }
    
    public enum TipoLancamento {
        Entrada, Saida
    };
}
