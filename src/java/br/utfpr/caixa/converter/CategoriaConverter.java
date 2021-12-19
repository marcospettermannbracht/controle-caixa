/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.converter;

import br.utfpr.caixa.model.Categoria;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter  implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new Categoria(Integer.valueOf(value), null, Categoria.TipoLancamento.Entrada);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Categoria categ = (Categoria)value;
        if (categ != null)
            return String.valueOf(categ.getCodigo());
        return null;
    }
    
}
