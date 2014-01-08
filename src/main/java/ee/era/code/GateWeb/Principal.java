package ee.era.code.GateWeb;

import java.util.Set;

public interface Principal {

    boolean checkPassword(String password);

    String getName();

    String getUsername();

    Set<String> getRoles();
}