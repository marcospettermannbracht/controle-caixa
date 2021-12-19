/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.converter;

import br.utfpr.caixa.model.Caixa;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Caixa.class)
public class CaixaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new Caixa(Integer.valueOf(value), null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Caixa caixa = (Caixa)value;
        if (caixa != null)
            return String.valueOf(caixa.getCodigo());
        return null;
    }
    
}
