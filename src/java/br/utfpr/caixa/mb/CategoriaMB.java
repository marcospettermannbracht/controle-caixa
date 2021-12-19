/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.Categoria;
import br.utfpr.caixa.model.DatabaseHelper;
import br.utfpr.caixa.model.EntidadeBasica;
import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Marcos
 */
@Named(value = "categoriaMB")
@ViewScoped
public class CategoriaMB extends DatabaseHelper implements Serializable {

    private List<Categoria> categorias;
    
    private Categoria novaCategoria; 
    
    public List<Categoria> getCategorias() {
        if (categorias == null)
            categorias = (List<Categoria>)(List<?>)consultar(Categoria.class, false);
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Categoria getNovaCategoria() {
        if (novaCategoria == null) {
            novaCategoria = new Categoria();
            novaCategoria.setCodigo(getProximoCodigo((List<EntidadeBasica>)(List<?>)categorias)); 
        }
        return novaCategoria;
    }

    public void setNovaCategoria(Categoria novaCategoria) {
        this.novaCategoria = novaCategoria;
    }
    
    public void salvarCategoria() {
        if (!validarRepeticao(novaCategoria)) {
            encerrarEntityManager();
            criarEntityManager();
            setCategorias(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", String.format("Já existe uma categoria com código %1$d.", novaCategoria.getCodigo())));
            return;            
        }
        
        salvar(getNovaCategoria());
        setCategorias(null);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Categoria \"%1$s\" criada.", novaCategoria.getDescricao()) ));
        RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide();");     
    }
    
    public void deletarCategoria() {
        if (deletar(getNovaCategoria())) {
            setCategorias(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Categoria \"%1$s\" excluído.", novaCategoria.getDescricao())));
        } else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", String.format("Categoria \"%1$s\" já utilizado em lançamentos, não é possível removê-lo.", novaCategoria.getDescricao())));
    }
    
    public Categoria.TipoLancamento[] getTipos() {
        return Categoria.TipoLancamento.values();
    }
}
