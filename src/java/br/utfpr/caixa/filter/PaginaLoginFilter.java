/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.utfpr.caixa.filter;

import br.utfpr.caixa.mb.DadosSessao;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jboss.weld.context.SerializableContextualInstanceImpl;

@WebFilter(filterName = "PaginaLoginFilter", urlPatterns = {"/login.xhtml"})
public class PaginaLoginFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        Enumeration<String> attribs = httpSession.getAttributeNames();
        while (attribs.hasMoreElements()) {         
            String attrib = attribs.nextElement();
            Object obj = httpSession.getAttribute(attrib);
            if(obj instanceof SerializableContextualInstanceImpl){
                SerializableContextualInstanceImpl impl = (SerializableContextualInstanceImpl)obj;
                DadosSessao dadosSessao = (DadosSessao)impl.getInstance();
                if (dadosSessao != null && dadosSessao.getUsuario() != null) {
                    String contextPath = ((HttpServletRequest) request).getContextPath();
                      //Redirecionamos o usuário imediatamente 
                      //para a página de login.xhtml
                    ((HttpServletResponse) response).sendRedirect(contextPath + "/controlecaixa/resumo.xhtml");
                    return;
                } 
            }           
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void destroy() {
        
    }
    
}
