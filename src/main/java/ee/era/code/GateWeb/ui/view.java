package ee.era.code.GateWeb.ui;

import ee.era.code.GateWeb.DateTimeHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class View extends RequestHandler {
    public abstract void execute() throws IOException;
    @Inject  VelocityEngine velocity;
    private VelocityContext velocityContext = new VelocityContext();
    protected String contentType = "text/html";

    public void doExecute() throws IOException {
        String requestURI = request.getRequestURI();
        Labels.setContext(requestURI.substring(1).replace('/', '.'));
        execute();
        put("DateTimeHelper", DateTimeHelper.class);
        put("Labels", Labels.class);
        Template template = velocity.getTemplate("/templates" + requestURI + ".vm", "UTF-8");
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
        template.merge(velocityContext, out);
        out.close();
    }
    protected void put(String name, Object value) {
        velocityContext.put(name, value);
    }
}
