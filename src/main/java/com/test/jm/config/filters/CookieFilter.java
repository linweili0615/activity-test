package com.test.jm.config.filters;

import com.test.jm.util.CookieUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "CookieFilter",urlPatterns = {"/*"})
public class CookieFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CookieUtils.getCookie(request,"jm");
    }

    @Override
    public void destroy() {

    }
}
