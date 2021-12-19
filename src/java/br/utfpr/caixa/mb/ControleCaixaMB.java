/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Marcos
 */
@Named("controleCaixaMB")
@ViewScoped
public class ControleCaixaMB extends DatabaseHelper implements Serializable {

    private List<Lancamento> lancamentos;
    private Caixa caixaSelecionado; 
    private Lancamento novoLcto;
    private Date filtroDataIni = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    private Date filtroDataFin = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1).toInstant());    
    private String novaPessoa;
    
    @Inject
    private DadosSessao dadosSessao;
    
    @Inject
    private PessoaMB pessoaMB;

    public List<Lancamento> getLancamentos() {
        if (caixaSelecionado == null)
            lancamentos = null;
        else if (lancamentos == null)
            lancamentos = consultaLancamentos(caixaSelecionado, filtroDataIni, filtroDataFin);
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public Caixa getCaixaSelecionado() {
        return caixaSelecionado;
    }

    public void setCaixaSelecionado(Caixa caixaSelecionado) {
        if (caixaSelecionado != null && caixaSelecionado.getCodigo() > 0 && caixaSelecionado.getId() == null)
            caixaSelecionado = (Caixa)getRegistro(Caixa.class, caixaSelecionado.getCodigo());
        this.caixaSelecionado = caixaSelecionado;
    }

    public Lancamento getNovoLcto() {
        if (novoLcto == null) {
            novoLcto = new Lancamento();
            novoLcto.setDataHora(new Date());
            if (caixaSelecionado != null)
                novoLcto.setCaixa(caixaSelecionado);
        }
        return novoLcto;
    }

    public void setNovoLcto(Lancamento novoLcto) {
        this.novoLcto = novoLcto;
        if (novoLcto == null)
            novaPessoa = null;
    }

    public String getNovaPessoa() {
        if (getNovoLcto() != null && getNovoLcto().getPessoa() != null)
            novaPessoa = getNovoLcto().getPessoa().getDescricao();
        return novaPessoa;
    }

    public void setNovaPessoa(String novaPessoa) {
        this.novaPessoa = novaPessoa;
    }
    
    public List<Categoria> getCategorias() {
        return ControleCaixaService.categorias;
    }
    
    public List<Pessoa> getPessoas() {
        return ControleCaixaService.pessoas;
    }

    public Date getFiltroDataIni() {
        return filtroDataIni;
    }

    public void setFiltroDataIni(Date filtroDataIni) {
        this.filtroDataIni = filtroDataIni;
    }

    public Date getFiltroDataFin() {
        return filtroDataFin;
    }

    public void setFiltroDataFin(Date filtroDataFin) {
        this.filtroDataFin = filtroDataFin;
    }
    
    public void salvarLancamento() {
        
        String msgValidacao = null;
        
        getNovoLcto().setCaixa((Caixa)getRegistro(Caixa.class, novoLcto.getCaixa().getCodigo()));
        novoLcto.setCategoria((Categoria)getRegistro(Categoria.class, novoLcto.getCategoria().getCodigo()));
        
        if (novoLcto.getPessoa() == null && (novaPessoa == null || novaPessoa.isEmpty()))
            msgValidacao = "Informe a pessoa/entidade.";
        else if (getNovoLcto().getPessoa() == null) {
            Pessoa novaPessoaObj = (Pessoa)getRegistro(Pessoa.class, novaPessoa);
            if (novaPessoaObj != null)
                novoLcto.setPessoa(novaPessoaObj);
            else {
                novaPessoaObj = new Pessoa();
                novaPessoaObj.setCodigo(getProximoCodigo(Pessoa.class));
                novaPessoaObj.setDescricao(novaPessoa);
                novaPessoaObj.setTipo(novoLcto.getCategoria().getTipo() == Categoria.TipoLancamento.Entrada ? Pessoa.TipoPessoa.Doador : Pessoa.TipoPessoa.Fornecedor);
                novoLcto.setPessoa((Pessoa)salvar(novaPessoaObj));
                pessoaMB.setPessoas(null); //limpando a lista para que seja carregada de novo do banco, com o novo cadastro
            }
        } else    
            novoLcto.setPessoa((Pessoa)getRegistro(Pessoa.class, novoLcto.getPessoa().getCodigo()));
        if (msgValidacao != null) {
            encerrarEntityManager();
            criarEntityManager();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Validação", msgValidacao));
            return;
        }
        
        novoLcto.setCodigo(getProximoCodigo(Lancamento.class));
        novoLcto.setUsuario((Usuario)getRegistro(Usuario.class, dadosSessao.getUsuario().getCodigo()));
        novoLcto.setCodigo(getProximoCodigo(Lancamento.class));
        novoLcto.getCaixa().adicionaLancamento(novoLcto);
        
        salvar(novoLcto);
        setLancamentos(null);
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", String.format("Lançamento efetuado no caixa \"%1$s\"", novoLcto.getCaixa().getDescricao())));
        RequestContext.getCurrentInstance().execute("PF('dlgCadastro').hide();");        
    }
    
    public void deletaLancamento() {
        novoLcto.getCaixa().removeLancamento(novoLcto);
        deletar(novoLcto);
        setLancamentos(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", "Lançamento excluído."));
    }
    
    public double getSaldoPeriodo() {
        double saldo = 0;
        if (lancamentos != null) {
            for (Lancamento l : lancamentos) {
                saldo += l.getCategoria().getTipo() == Categoria.TipoLancamento.Entrada ? l.getValor()
                        : -l.getValor();
            }
        }
        return saldo;
    }
    
    public void validaRemocao() {
        Usuario usuarioLogado = (Usuario)getRegistro(Usuario.class, dadosSessao.getUsuario().getCodigo());
        if (novoLcto.getUsuario() == usuarioLogado || usuarioLogado.isAdministrador()) {
            RequestContext.getCurrentInstance().execute("PF('confirmDelete').show();"); 
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro", "Você não tem permissão para remover este lançamento."));
    }    
}
