package pl.portfolio.estimateplussb.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(urlPatterns = {"/user/*","/admin/*"}, filterName = "AppAccessFilter")
public class AuthFilterFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        final HttpServletRequest req = (HttpServletRequest) request;

        if (req.getSession().getAttribute("loggedUser") == null)
        {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect(req.getContextPath() + "/");
            return;
        }
        chain.doFilter(request, response);
    }
}
