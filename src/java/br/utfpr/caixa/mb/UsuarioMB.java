/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.DatabaseHelper;
import br.utfpr.caixa.model.EntidadeBasica;
import br.utfpr.caixa.model.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Marcos
 */
@Named(value = "usuarioMB")
@ViewScoped
public class UsuarioMB extends DatabaseHelper implements Serializable {
    
    private List<Usuario> usuarios;
    
    private String senhaDescripto;
    
    private Usuario novoUsuario;
    
    private String confirmacaoSenha;

    public List<Usuario> getUsuarios() {
        if (usuarios == null)
            usuarios = (List<Usuario>)(List<?>)consultar(Usuario.class, false);
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getNovoUsuario() {
        if (novoUsuario == null) {
            novoUsuario = new Usuario();
            novoUsuario.setCodigo(getProximoCodigo((List<EntidadeBasica>)(List<?>)usuarios));
        }
        return novoUsuario;
    }

    public void setNovoUsuario(Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }

    public String getSenhaDescripto() {
        return senhaDescripto;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
    
    public void salvarUsuario() {
        if (!validarRepeticao(novoUsuario)) {
            encerrarEntityManager();
            criarEntityManager();
            setUsuarios(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Valida????o", String.format("J?? existe um usu??rio com c??digo %1$d.", novoUsuario.getCodigo())));
            return;            
        }
        
        boolean geradaSenha = false;
        if (novoUsuario.getId() == null) {
            senhaDescripto = getPrimeiraSenha();
            novoUsuario.setSenha(senhaDescripto);
            geradaSenha = true;
        }
        
        salvar(getNovoUsuario());
        setUsuarios(null);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Usu??rio \"%1$s\" criado.", novoUsuario.getDescricao())));
        RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide();" + (geradaSenha ? "PF('dlgNovaSenha').show()" : ""));     
    }
    
    public void salvarSenha() {
        Usuario usuarioValidacao = new Usuario();
        usuarioValidacao.setSenha(confirmacaoSenha);
        if (novoUsuario.getSenha() == null || usuarioValidacao.getSenha() == null || 
            !novoUsuario.getSenha().equals(usuarioValidacao.getSenha())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Valida????o", "As senhas informadas n??o coincidem."));
            return;                        
        }
        
        usuarioValidacao = (Usuario)getRegistro(Usuario.class, novoUsuario.getCodigo());
        usuarioValidacao.setSenha(confirmacaoSenha);
        salvar(usuarioValidacao);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", "Senha alterada."));
        RequestContext.getCurrentInstance().execute("PF('dlgSenha').hide();");     
    }
    
    public void deletarUsuario() {
        if (deletar(getNovoUsuario())) {
            setUsuarios(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Usu??rio \"%1$s\" exclu??da.", novoUsuario.getDescricao())));
        } else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Valida????o", String.format("Usu??rio \"%1$s\" j?? utilizado em lan??amentos, n??o ?? poss??vel remov??-lo.", novoUsuario.getDescricao())));
    }
    
    public String getPrimeiraSenha() {
        String senha = "";
        // 3 letras
        for (int i = 0; i < 3; i ++)
            senha += (char)(97 + (int)(Math.random() * 25));
        // 3 algarismos
        for (int i = 0; i < 3; i ++)
            senha += (char)(48 + (int)(Math.random() * 9));
        return senha;
    }
    
    
    
}
