package ee.era.code.GateWeb.core;

import ee.era.code.GateWeb.Principal;

import java.util.Set;

public class User implements Principal {
    @Override
    public boolean checkPassword(String password) {
        return true;
    }

    @Override
    public String getName() {
        return "Admin";
    }

    @Override
    public String getUsername() {
        return "admin";
    }

    @Override
    public Set<String> getRoles() {
        return null;
    }
}
