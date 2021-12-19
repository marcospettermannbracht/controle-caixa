/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Marcos
 */
@Named(value = "dadosSessao")
@SessionScoped
public class DadosSessao implements Serializable {
    
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String sair() {
        setUsuario(null);
        return "/login.xhtml";
    }
}
