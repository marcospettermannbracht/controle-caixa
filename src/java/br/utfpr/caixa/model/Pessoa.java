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
public class Pessoa extends EntidadeBasica {
    private String documento;
    private TipoPessoa tipo;
    
    public Pessoa() {
        
    }

    public Pessoa(int codigo, String documento, String nome, TipoPessoa tipo) {
        this.setCodigo(codigo);
        this.documento = documento;
        this.setDescricao(nome);
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }
    
    public String getDocumentoFormatado() {
        if (documento == null)
            return null;
        if (documento.length() == 14)
            return String.format("%1$s.%2$s.%3$s/%4$s-%5$s", documento.substring(0, 2), documento.substring(2, 5),
                    documento.substring(5, 8), documento.substring(8, 12), documento.substring(12));
        if (documento.length() == 11)
            return String.format("%1$s.%2$s.%3$s-%4$s", documento.substring(0, 3), documento.substring(3, 6),
                    documento.substring(6, 9), documento.substring(9));
        return null;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoPessoa getTipo() {
        return tipo;
    }

    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }
    
    public enum TipoPessoa {
        Cliente, Doador, Fornecedor, Mantenedor, Outros
    };
}
