/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Named(value = "databaseHelper")
@ViewScoped
public class DatabaseHelper implements Serializable {
    
    @Inject
    private CriacaoBanco criacaoBanco;
    
    private EntityManager manager; 
    
    @PostConstruct
    public void init() {
        criarEntityManager();
    }
    
    @PreDestroy
    public void destroy() {
        encerrarEntityManager();
    }
    
    //@PostConstruct
    public void criarEntityManager() {
        manager = criacaoBanco.getEntityManager();
    }
    
    //@PreDestroy
    public void encerrarEntityManager() {
        manager.close();
    }
    
    public EntidadeBasica salvar(EntidadeBasica obj) {
        EntidadeBasica objSalvo = null;
        try {
            manager.getTransaction().begin();
            objSalvo = manager.merge(obj);
            manager.getTransaction().commit();
        } catch(Exception ex) {
            
        }
        return objSalvo;
    }
    
    public boolean deletar(EntidadeBasica obj) {
        try {
            manager.getTransaction().begin();
            manager.remove(obj);
            manager.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            return false;
        } 
    }
    
    public boolean validarRepeticao(EntidadeBasica obj) {
        
        Query query = manager.createQuery(String.format("SELECT c from %1$s c WHERE c.codigo = :cod and c.id != :id", obj.getClass().getName()))
                             .setParameter("cod", obj.getCodigo())
                             .setParameter("id", obj.getId() == null ? 0 : obj.getId());
        try {
            EntidadeBasica objCadastrado = (EntidadeBasica)query.getSingleResult();
            return objCadastrado == null;        
        } catch (NoResultException ex) {
            return true;
        } 
    }
    
    public List<EntidadeBasica> consultar(Class tipo, boolean ordemAlfabetica) {
        
        Query query = manager.createQuery(String.format("SELECT c from %1$s c", tipo.getSimpleName()));
        List<EntidadeBasica> lista = query.getResultList();
        if (ordemAlfabetica)
            lista.sort((a, b) -> 
            {
                return ((EntidadeBasica)a).getDescricao().compareTo(((EntidadeBasica)b).getDescricao());
            });    
        else
            lista.sort((a, b) -> 
            {
                return ((EntidadeBasica)a).getCodigo() > ((EntidadeBasica)b).getCodigo() ? 1 : -1;
            });            
        return lista;
    }
    
    public boolean validarRepeticaoPessoa(Pessoa obj) {        
        Query query = manager.createQuery("SELECT c from Pessoa c WHERE (c.codigo = :cod or c.documento = :doc or c.descricao = :desc) and c.id != :id")
                             .setParameter("cod", obj.getCodigo())
                             .setParameter("doc", obj.getDocumento() == null || obj.getDocumento().isEmpty() ? "SEM_DOCUMENTO" : obj.getDocumento())
                             .setParameter("desc", obj.getDescricao())
                             .setParameter("id", obj.getId() == null ? 0 : obj.getId());
        try {
            Pessoa objCadastrado = (Pessoa)query.getSingleResult();
            return objCadastrado == null;        
        } catch (NoResultException ex) {
            return true;
        }
    }
    
    public int getProximoCodigo(Class tipo) {
        Query query = manager.createQuery(String.format("SELECT max(c.codigo) from %1$s c", tipo.getSimpleName()));
        int maxCodigo = 0;
        try {
            maxCodigo = (int)query.getSingleResult();
        } catch (Exception ex) {
            
        }
        return maxCodigo + 1;
    }
    
    public int getProximoCodigo(List<EntidadeBasica> lista) {
        if (lista == null || lista.isEmpty())
            return 1;
        return lista.get(lista.size() - 1).getCodigo() + 1;
    }
    
    public EntidadeBasica getRegistro(Class tipo, int codigo) {
        try {
            Query query = manager.createQuery(String.format("SELECT c from %1$s c WHERE c.codigo = :cod", tipo.getSimpleName()))
                    .setParameter("cod", codigo);
            return (EntidadeBasica)query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }        
    }
    
    public EntidadeBasica getRegistro(Class tipo, String descricao) {
        try {
            Query query = manager.createQuery(String.format("SELECT c from %1$s c WHERE c.descricao = :desc", tipo.getSimpleName()))
                    .setParameter("desc", descricao);
            return (EntidadeBasica)query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }        
    }
    
    public List<Lancamento> consultaLancamentos(Caixa caixa, Date dataIni, Date dataFin) { 
        Query query = manager.createQuery("SELECT l from Lancamento l WHERE l.caixa.id = :idCaixa and l.dataHora >= :dataIni and l.dataHora <= :dataFin ORDER BY l.dataHora DESC")
                .setParameter("idCaixa", caixa.getId())
                .setParameter("dataIni", dataIni).setParameter("dataFin", dataFin);
        List<Lancamento> lista = query.getResultList();
        return lista;        
    }   
    
}
