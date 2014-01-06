package ee.era.code.GateWeb;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher {
    private static final Logger LOG = Logger.getLogger(Launcher.class);
    public static final long startTimestamp = System.currentTimeMillis();

    public static void main(String args[]) throws Exception {
        LOG.error("Starting Gate");
        Launcher launcher = new Launcher();
        launcher.run();
    }

    void run() throws Exception {
        Server server = new Server();
        ServerConnector http = getHttpConnector(server);
        server.setConnectors(new Connector[]{http});
        server.setHandler(createHandler());
        server.start();
        LOG.info("Gate started in " + (System.currentTimeMillis() - startTimestamp) + " ms");
        server.join();
    }

    private Handler createHandler() {
        WebAppContext webAppContext = new WebAppContext("webapp", "/");
        //webAppContext.setErrorHandler(new PlainTextErrorHandler());
        webAppContext.setMaxFormContentSize(20 * 1024 * 1024);
        webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
        return webAppContext;
    }

    private ServerConnector getHttpConnector(Server server) {
        ServerConnector http= new ServerConnector(server, new HttpConnectionFactory(new HttpConfiguration()));
        http.setPort(8080);
        http.setIdleTimeout(30000);
        return http;
    }
}
