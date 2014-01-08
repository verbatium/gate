package ee.era.code.GateWeb.ui;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Map;
import java.util.Set;


import static com.google.common.collect.Sets.newTreeSet;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Labels {
    protected static URL labelsYaml;

    private static ThreadLocal<String> language = new ThreadLocal<>();
    private static ThreadLocal<String> context = new ThreadLocal<>();
    private static final Logger LOG = Logger.getLogger(Labels.class);
    protected static WatchService watchService;
    private static Set<String> allowedLanguages = newTreeSet();
    private static Map<String, Map<String, String>> labels;

    static void load() throws IOException {
        InputStream stream = labelsYaml.openStream();
        setLabels(stream);
    }
    public static String getLanguage() {
        if (language.get()==null) setLanguage("ru");
        return language.get();
    }
    public static void setContext(String context) {
        Labels.context.set(context);
    }
    static void setLabels(InputStream yamlStream) throws IOException {

        labels = (Map<String, Map<String, String>>) new Yaml().load(yamlStream);
        yamlStream.close();
        LOG.info("Loaded " + labels.size() + " labels");
        for (Map<String, String> label : labels.values()) {
            for (String language : label.keySet()) allowedLanguages.add(language);
        }
        LOG.info("Available languages: " + allowedLanguages);
    }
    public static void init(URL labelsYaml) throws IOException, URISyntaxException {
        Labels.labelsYaml = labelsYaml;
        load();
        Path path = Paths.get(Labels.labelsYaml.toURI()).getParent();
        watchService = path.getFileSystem().newWatchService();
        path.register(watchService, ENTRY_MODIFY, ENTRY_CREATE);
    }
    public static String get(String key) {
        reloadIfChanged();
        String text = null;
        if (context.get() != null)
            text = _get(context.get() + '.' + key);
        if (text == null)
            text = _get(key);
        if (text == null) LOG.warn("Missing label: " + key);
        return text == null ? key : text;
    }
    static void reloadIfChanged() {
        try {
            WatchKey key = watchService.poll();
            if (key == null) return;
            for (WatchEvent<?> event : key.pollEvents()) {
                if (!event.context().toString().equals("labels.yaml"))  continue;
                load();
                break;
            }
            key.reset();
        }
        catch (Exception e) {
            LOG.error("Failed to reload labels: " + e);
        }
    }
    private static String _get(String key) {
        Map<String, String> label = labels.get(key);
        return label != null ? label.get(getLanguage()) : null;
    }
    public static void setLanguage(String language) {
        Labels.language.set(language);
    }
}
