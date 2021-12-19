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

@WebFilter(filterName = "UsuarioAdministradorFilter", urlPatterns = {"/controlecaixa/cadastro/*"})
public class UsuarioAdministradorFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
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
        } else if (dadosSessao.getUsuario() != null && !dadosSessao.getUsuario().isAdministrador()) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            ((HttpServletResponse) response).sendRedirect(contextPath + "/controlecaixa/resumo.xhtml");            
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
