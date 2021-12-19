/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author Marcos
 */
@Entity
public class Caixa extends EntidadeBasica implements Serializable {
    
    private double saldo;
    
    public Caixa() {
        
    }
    
    public Caixa(int codigo, String descricao) {
        this.setCodigo(codigo);
        this.setDescricao(descricao);
    }

    public double getSaldo() {
        return saldo;
    }   

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public void adicionaLancamento(Lancamento l){
        this.saldo += l.getCategoria().getTipo() == Categoria.TipoLancamento.Entrada ? 
                      l.getValor() : 
                      -l.getValor();
    }
    
    public void removeLancamento(Lancamento l){
        this.saldo -= l.getCategoria().getTipo() == Categoria.TipoLancamento.Entrada ? 
                      l.getValor() : 
                      -l.getValor();
    }
}
