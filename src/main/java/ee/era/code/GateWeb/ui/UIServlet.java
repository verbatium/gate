package ee.era.code.GateWeb.ui;

import com.google.inject.Injector;
import ee.era.code.GateWeb.Launcher;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.lang.StringUtils.capitalize;

public class UIServlet extends HttpServlet {
    private final static Logger LOG = Logger.getLogger(UIServlet.class);
    Injector injector;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        injector = Launcher.injector;
        try {
            Labels.init(config.getServletContext().getResource("/WEB-INF/labels.yaml"));
        }
        catch (Exception e) {
            throw new ServletException("Failed to load labels: " + e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long t = -System.currentTimeMillis();
        t += System.currentTimeMillis();

        String requestURI = request.getRequestURI();
        Class<? extends RequestHandler> requestHandlerClass = getRequestHandlerClass(requestURI);
        if (requestHandlerClass == null) {
            response.sendError(SC_NOT_FOUND);
            return;
        }

        RequestHandler requestHandler = injector.getInstance(requestHandlerClass);
        requestHandler.request = request;
        requestHandler.response = response;
        //requestHandler.setLanguage();
        requestHandler.doExecute();

        LOG.info("[:" + request.getLocalPort() + "] " + request.getMethod() + " " + requestURI + " - " + t + "ms");

    }
    Class<? extends RequestHandler> getRequestHandlerClass(String requestURI) {
        try {
            String uri = requestURI.replace('/', '.');
            String pkg = View.class.getPackage().getName() + uri.substring(0, uri.lastIndexOf('.'));
            String className = "";
            for (String part : uri.substring(uri.lastIndexOf('.') + 1).split("-")) className += capitalize(part);
            Class<?> clazz = Class.forName(pkg + '.' + className);
            //noinspection unchecked
            return (Class<? extends RequestHandler>) clazz;
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
}
