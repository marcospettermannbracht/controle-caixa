/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.converter;

import br.utfpr.caixa.model.Pessoa;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Pessoa.class)
public class PessoaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new Pessoa(Integer.valueOf(value), null, null, Pessoa.TipoPessoa.Cliente);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Pessoa caixa = (Pessoa)value;
        if (caixa != null)
            return String.valueOf(caixa.getCodigo());
        return null;
    }
    
}
