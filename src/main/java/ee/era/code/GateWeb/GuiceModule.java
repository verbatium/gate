package ee.era.code.GateWeb;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;

import javax.inject.Singleton;

import static org.apache.velocity.runtime.RuntimeConstants.*;
import static org.apache.velocity.runtime.log.Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER;

public abstract class GuiceModule extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(GuiceModule.class);

    protected String getEnvironmentName() {
        return this.getClass().getSimpleName().replace("GuiceModule", "");
    }
    @Provides
    @Singleton
    VelocityEngine createVelocityEngine() {
        VelocityEngine velocity = new VelocityEngine();
        velocity.setProperty(RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        velocity.setProperty(RUNTIME_LOG_LOG4J_LOGGER, "Velocity");
        velocity.setProperty(RESOURCE_LOADER, "file");
        velocity.setProperty(FILE_RESOURCE_LOADER_PATH, "webapp/WEB-INF");
        //velocity.setProperty(FILE_RESOURCE_LOADER_CACHE, isDevelopment() ? "false" : "true");
        velocity.setProperty(VM_LIBRARY, "/templates/macros.vm");
        velocity.setProperty(VM_LIBRARY_AUTORELOAD, "true");
        velocity.init();
        return velocity;
    }
}
