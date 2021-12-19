/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.DatabaseHelper;
import br.utfpr.caixa.model.EntidadeBasica;
import br.utfpr.caixa.model.Pessoa;
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
@Named(value = "pessoaMB")
@ViewScoped
public class PessoaMB  extends DatabaseHelper implements Serializable {

    private List<Pessoa> pessoas;
    
    private Pessoa novaPessoa; 

    public List<Pessoa> getPessoas() {
        if (pessoas == null)
            pessoas = (List<Pessoa>)(List<?>)consultar(Pessoa.class, true);
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public Pessoa getNovaPessoa() {
        if (novaPessoa == null) {
            novaPessoa = new Pessoa();
            novaPessoa.setCodigo(getProximoCodigo((List<EntidadeBasica>)(List<?>)pessoas));
        }
        return novaPessoa;
    }

    public void setNovaPessoa(Pessoa novaPessoa) {
        this.novaPessoa = novaPessoa;
    }
    
    public void salvarPessoa() {
        if (!validarRepeticaoPessoa(novaPessoa)) {
            encerrarEntityManager();
            criarEntityManager();
            setPessoas(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", "Já existe uma pessoa/entidade com o código/documento/nome informado."));
            return;            
        }
        
        salvar(getNovaPessoa());
        setPessoas(null);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Pessoa/entidade \"%1$s\" criada.", novaPessoa.getDescricao())));
        RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide();");     
    }
    
    public void deletarPessoa() {
        if (deletar(getNovaPessoa())) {
            setPessoas(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Pessoa/entidade \"%1$s\" excluída.", novaPessoa.getDescricao())));
        } else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", String.format("Pessoa/entidade \"%1$s\" já utilizada em lançamentos, não é possível removê-la.", novaPessoa.getDescricao())));
    }
    
    public Pessoa.TipoPessoa[] getTipos() {
        return Pessoa.TipoPessoa.values();
    }
}
