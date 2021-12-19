/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.mb;

import br.utfpr.caixa.model.*;
import java.util.*;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Marcos
 */
@ApplicationScoped
public class ControleCaixaService {

    public final static List<Categoria> categorias;
    public final static List<Pessoa> pessoas;
    private static int novoSequencial;
    
    static {
        categorias = new ArrayList<>();
        categorias.add(new Categoria(1, "Depósito Bancário", Categoria.TipoLancamento.Saida));
        categorias.add(new Categoria(2, "Pagamento", Categoria.TipoLancamento.Saida));
        categorias.add(new Categoria(3, "Doação", Categoria.TipoLancamento.Entrada));
        categorias.add(new Categoria(4, "Venda", Categoria.TipoLancamento.Entrada));
        categorias.add(new Categoria(5, "Evento", Categoria.TipoLancamento.Entrada));
        categorias.sort((a, b) -> 
        {
            return ((Categoria)a).getDescricao().compareTo(((Categoria)b).getDescricao());
        });
        
        pessoas = new ArrayList<>();
        pessoas.add(new Pessoa(1, "07245633932", "Marcos Bracht", Pessoa.TipoPessoa.Doador));
        pessoas.add(new Pessoa(1, "23975187085", "João Silva", Pessoa.TipoPessoa.Cliente));
        pessoas.add(new Pessoa(1, "22381872000", "Carlos Sousa", Pessoa.TipoPessoa.Mantenedor));
        pessoas.add(new Pessoa(1, "51628173092", "Roberta Antunes", Pessoa.TipoPessoa.Mantenedor));
        pessoas.add(new Pessoa(1, "01879667029", "Ricardo Rocha", Pessoa.TipoPessoa.Cliente));
        pessoas.add(new Pessoa(1, "33469911000111", "Peças Panambi", Pessoa.TipoPessoa.Doador));
        pessoas.add(new Pessoa(1, "83175050000137", "Lojas Americanas", Pessoa.TipoPessoa.Mantenedor));
        pessoas.add(new Pessoa(1, "49762279000130", "Casas Bahia", Pessoa.TipoPessoa.Doador));
        pessoas.add(new Pessoa(1, "88020191000102", "Mecânica 4 Rodas", Pessoa.TipoPessoa.Mantenedor));
        pessoas.add(new Pessoa(1, "85279721000135", "Revista Gazeta", Pessoa.TipoPessoa.Doador));
        pessoas.sort((a, b) -> 
        {
            return ((Pessoa)a).getDescricao().compareTo(((Pessoa)b).getDescricao());
        });
    }
    
    public Categoria getCategoria(Categoria.TipoLancamento tipo) {        
        int indice;
        do {
            indice = (int)(Math.random() * 4);
        }
        while (categorias.get(indice).getTipo() != tipo);
        return categorias.get(indice);
    }
    
    public Categoria getCategoria() {
        return categorias.get((int)(Math.random() * 4));
    }
    
    public Pessoa getPessoa() {
        return pessoas.get((int)(Math.random() * 9));
    }
    
    public void geraLancamentos(Caixa caixa) {
        /*Calendar data = Calendar.getInstance();
        for (int i = 1; i <= 10; i++) {
            data.set(Calendar.DAY_OF_MONTH, (int)(Math.random() * 27) + 1);
            data.set(Calendar.HOUR_OF_DAY, (int)(Math.random() * 10) + 8);
            data.set(Calendar.MINUTE, (int)(Math.random() * 60));
            Lancamento lcto = new Lancamento(getNovoSequencial(), data.getTime(), caixa, getCategoria(), getPessoa(), (float) (Math.random() * 150), new Usuario(1, "Enio"));
            caixa.getLancamentos().add(lcto);
        }        
        caixa.getLancamentos().sort((a, b) -> a.compareTo(b));
        while (caixa.getSaldo() < 0) {
            Lancamento lcto = new Lancamento(getNovoSequencial(), data.getTime(), caixa, getCategoria(Categoria.TipoLancamento.Entrada), getPessoa(), (float) (Math.random() * 150), new Usuario(1, "Enio"));
            caixa.getLancamentos().add(lcto);            
        }*/
    }
    
    public static int getNovoSequencial() {
        return ++novoSequencial;
    }
}
