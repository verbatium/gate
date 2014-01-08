package ee.era.code.GateWeb;

import ee.era.code.GateWeb.core.User;
import org.apache.log4j.Logger;

import javax.inject.Singleton;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Singleton
public class LoginService {
    private final static Logger LOG = Logger.getLogger(LoginService.class);
    Map<String, Principal> principals = newHashMap();
    public Principal loginWithPassword(String username, String password) {
        Principal principal = principals.get(username);
        return principal == null ? null : principal.checkPassword(password) ? principal : null;


    }
    public void loadUsers() {
        principals.put("Admin",new User());
        LOG.info("User loaded");
    }
}
