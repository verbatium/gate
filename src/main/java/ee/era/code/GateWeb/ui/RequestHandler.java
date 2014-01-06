package ee.era.code.GateWeb.ui;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RequestHandler {
    protected Logger logger = Logger.getLogger(this.getClass());
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected abstract void doExecute() throws IOException;
}
