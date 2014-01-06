package ee.era.code.GateWeb.ui.admin;

import ee.era.code.GateWeb.DateTimeHelper;
import ee.era.code.GateWeb.ui.View;

import java.io.IOException;
import java.util.Date;

public class Dashboard extends View {
    @Override
    public void execute() throws IOException {
        put("now", DateTimeHelper.formatTimestamp(new Date()));
    }
}
