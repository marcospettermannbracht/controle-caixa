/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.DatabaseHelper;
import br.utfpr.caixa.model.Usuario;
import javax.inject.Named;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Marcos
 */
@Named(value = "loginMB")
@ViewScoped
public class LoginMB extends DatabaseHelper implements Serializable {
    
    private Usuario usuarioView;
    private Usuario usuarioValidacao;
    private String senha;
    private Integer codigoObj;
    
    @Inject
    private DadosSessao dadosSessao;
    
    public Usuario getUsuarioValidacao() {
        if (usuarioValidacao == null)
            usuarioValidacao = new Usuario();
        return usuarioValidacao;
    }
    
    public Usuario getUsuarioView() {
        if (usuarioView == null)
            usuarioView = new Usuario();
        return usuarioView;
    }

    public void setUsuarioView(Usuario usuarioView) {
        this.usuarioView = usuarioView;
        if (usuarioView == null)
            codigoObj = null;
        else
            codigoObj = usuarioView.getCodigo() > 0 ? usuarioView.getCodigo() : null;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getCodigoObj() {
        return codigoObj;
    }

    public void setCodigoObj(Integer codigoObj) {
        this.codigoObj = codigoObj;
    }
    
    // campoAlterado: 0 código, 1 nome
    public void consultaUsuario(boolean campoAlterado) {
        if ((codigoObj == null || codigoObj == 0) && (getUsuarioView().getDescricao() == null || getUsuarioView().getDescricao().isEmpty()) ||
            (campoAlterado && (codigoObj == null || codigoObj == 0)) ||
            (!campoAlterado && (getUsuarioView().getDescricao() == null || getUsuarioView().getDescricao().isEmpty()))) {
            usuarioView = new Usuario();
            usuarioValidacao = new Usuario();
            codigoObj = null;
            return;
        }
        if (codigoObj != null && codigoObj == getUsuarioValidacao().getCodigo() && 
            getUsuarioView().getDescricao().equalsIgnoreCase(getUsuarioValidacao().getDescricao())) {
            return;
        }
        
        String msgValidacao = "";
        if (codigoObj != null && codigoObj > 0) {
            usuarioValidacao = (Usuario)getRegistro(Usuario.class, codigoObj);
            msgValidacao = String.format("Não foi encontrado usuário com código %1$d.", codigoObj);
        } else if (getUsuarioView().getDescricao() != null && !getUsuarioView().getDescricao().isEmpty()) {
            usuarioValidacao = (Usuario)getRegistro(Usuario.class, getUsuarioView().getDescricao());
            msgValidacao = String.format("Não foi encontrado usuário com nome \"%1$s\".", getUsuarioView().getDescricao());
        }
        
        if (usuarioValidacao != null)
            setUsuarioView(getUsuarioValidacao().clone());
        else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro", msgValidacao));
    }
    
    public String entrar() {
        if (getUsuarioValidacao().getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro", "Usuário não encontrado."));
            return null;
        }
        String senhaBanco = usuarioView.getSenha();
        usuarioView.setSenha(senha);
        if (!senhaBanco.equals(usuarioView.getSenha())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro", "Senha incorreta."));
            return null;
        }
        dadosSessao.setUsuario(usuarioView);
        return "controlecaixa/resumo?faces-redirect=true";
    }
    
    
}
