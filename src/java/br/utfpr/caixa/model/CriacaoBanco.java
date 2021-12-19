/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.model;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Startup
@Singleton
public class CriacaoBanco {
    
    private EntityManagerFactory factory; 
    
    public CriacaoBanco() {
        
    }
    
    @PostConstruct
    public void init() {
        factory = Persistence.createEntityManagerFactory("tcc_final");
        
        // caso não exista usuário cadastrado, cria um Administrador com senha admin
        EntityManager manager = getEntityManager();
        Query query = manager.createQuery("SELECT COUNT(c) from Usuario c");
        long num = (long)query.getSingleResult(); 
        if (num == 0) {
            manager.getTransaction().begin();
            Usuario admin = new Usuario();
            admin.setCodigo(1);
            admin.setAdministrador(true);
            admin.setDescricao("Administrador");
            admin.setSenha("admin");
            manager.merge(admin);
            manager.getTransaction().commit();
        }
        manager.close();
    }
    
    @PreDestroy
    public void destroy() {
        factory.close();
    }
    
    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
    
}
