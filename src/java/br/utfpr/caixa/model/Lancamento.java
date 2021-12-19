/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Marcos
 */
@Entity
public class Lancamento extends EntidadeBasica implements Serializable  {
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataHora;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="caixa_id")
    private Caixa caixa;
    @ManyToOne(fetch=FetchType.EAGER)
    private Categoria categoria;
    @ManyToOne(fetch=FetchType.EAGER)
    private Pessoa pessoa;
    @ManyToOne(fetch=FetchType.EAGER)
    private Usuario usuario;
    private float valor;
    
    public Lancamento() {
        
    }

    public Lancamento(int codigo, Date dataHora, Caixa caixa, Categoria catergoria, Pessoa pessoa, float valor, Usuario usuario) {
        this.setCodigo(codigo);
        this.dataHora = dataHora;
        this.caixa = caixa;
        this.categoria = catergoria;
        this.pessoa = pessoa;
        this.valor = valor;
        this.usuario = usuario;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public int compareTo(Lancamento x) {
        return this.dataHora.compareTo(x.getDataHora());
    }

    public String getValorStr() {
        return (categoria == null || categoria.getTipo() == Categoria.TipoLancamento.Entrada) ? String.format("+%1$.2f", valor)
                : String.format("-%1$.2f", valor);
    }
}
