package ee.era.code.GateWeb;

import com.google.common.collect.ImmutableSet;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class AccessFilter implements Filter {
    @Inject
    LoginService loginService;

    private static final Logger LOG = Logger.getLogger(AccessFilter.class);
    public static final String PRINCIPAL = "ee.era.code.principal";
    public static final String REPRESENTEES = "ee.era.code.representees";

    public static final String HOME = "/home/login";
    public static final String LOGOUT = "/home/logout";
    private static final Set<String> PUBLIC_URLS = ImmutableSet.of(LOGOUT, HOME);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        if (uri.equals("/")) {
            response.sendRedirect(HOME);
            return;
        }
        Principal principal = session != null ? (Principal) session.getAttribute("principal") : null;

        if (principal == null) {
            principal = authenticate(request);


        }
        if (principal == null) {
            if (PUBLIC_URLS.contains(uri)) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        } else

        {
            request.setAttribute(PRINCIPAL, principal);
            chain.doFilter(servletRequest, servletResponse);
            response.sendRedirect("/admin/dashboard");
        }
        request.setAttribute(PRINCIPAL, principal);
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private Principal authenticate(HttpServletRequest request) {
        LOG.info("");

        String un = request.getParameter("Email");
        String pwd = request.getParameter("Password");
        LOG.info(un + ":" + pwd);
        if (un == null || pwd == null)
            return null;
        if (loginService == null)
            loginService = new LoginService();
        loginService.loadUsers();
        return loginService.loginWithPassword(un, pwd);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
