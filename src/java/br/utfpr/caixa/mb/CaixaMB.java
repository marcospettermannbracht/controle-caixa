/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.Caixa;
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
@Named(value = "caixaMB")
@ViewScoped
public class CaixaMB extends DatabaseHelper implements Serializable {

    private List<Caixa> caixas;
    
    private Caixa novoCaixa; 
    
    public List<Caixa> getCaixas() {
        if (caixas == null)
            caixas = (List<Caixa>)(List<?>)consultar(Caixa.class, false);
        return caixas;
    }
    
    public void setCaixas(List<Caixa> caixas) {
        this.caixas = caixas;
    }

    public Caixa getNovoCaixa() {
        if (novoCaixa == null) {
            novoCaixa = new Caixa();
            novoCaixa.setCodigo(getProximoCodigo((List<EntidadeBasica>)(List<?>)caixas)); 
        }
        return novoCaixa;
    }

    public void setNovoCaixa(Caixa novoCaixa) {
        this.novoCaixa = novoCaixa;
    }
    
    public void salvarCaixa() {
        if (!validarRepeticao(novoCaixa)) {
            encerrarEntityManager();
            criarEntityManager();
            setCaixas(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", String.format("Já existe um caixa com código %1$d.", novoCaixa.getCodigo())));
            return;            
        }
        
        salvar(getNovoCaixa());
        setCaixas(null);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Caixa \"%1$s\" criado.", novoCaixa.getDescricao())));
        RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide();");     
    }
    
    public void deletarCaixa() {
        if (deletar(getNovoCaixa())) {
            setCaixas(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Caixa \"%1$s\" excluído.", novoCaixa.getDescricao())));
        } else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", String.format("Caixa \"%1$s\" já utilizado em lançamentos, não é possível removê-lo.", novoCaixa.getDescricao())));
    }
}
