/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.Entity;

/**
 *
 * @author Marcos
 */
@Entity
public class Usuario extends EntidadeBasica {
    
    private String senha;
    private boolean administrador;
    
    public Usuario() {
        
    }
    
    public Usuario(int codigo, String nome) {
        this.setCodigo(codigo);
        this.setDescricao(nome);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(senha.getBytes(), 0, senha.length());
            this.senha = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            this.senha = senha;            
        }
    }
    
    public void setSenhaCriptografada(String senha) {
        this.senha = senha;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
    
    @Override
    public Usuario clone() {
        Usuario retorno = new Usuario();
        retorno.setCodigo(getCodigo());
        retorno.setDescricao(getDescricao());
        retorno.setSenhaCriptografada(senha);
        retorno.setAdministrador(administrador);
        return retorno;
    }
    
    
}
