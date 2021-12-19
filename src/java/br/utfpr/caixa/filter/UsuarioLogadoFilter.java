/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.filter;

import br.utfpr.caixa.mb.DadosSessao;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import org.jboss.weld.context.SerializableContextualInstanceImpl;

@WebFilter(filterName = "UsuarioLogadoFilter", urlPatterns = {"/controlecaixa/*"})
public class UsuarioLogadoFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        //Captura o ManagedBean chamado “usuarioMB”
        //DadosSessao dadosSessao = (DadosSessao)((HttpServletRequest) request).getSession().getAttribute("dadosSessao");
        
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        Enumeration<String> attribs = httpSession.getAttributeNames();
        DadosSessao dadosSessao = null;
        while (attribs.hasMoreElements()) {         
            String attrib = attribs.nextElement();
            Object obj = httpSession.getAttribute(attrib);
            if(obj instanceof SerializableContextualInstanceImpl){
                SerializableContextualInstanceImpl impl = (SerializableContextualInstanceImpl)obj;
                dadosSessao = (DadosSessao)impl.getInstance();
                break;
            }           
        }
        if (dadosSessao == null || dadosSessao.getUsuario() == null) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            ((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
        } 
        else
            chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void destroy() {
        
    }
}
