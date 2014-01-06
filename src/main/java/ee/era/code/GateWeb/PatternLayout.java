package ee.era.code.GateWeb;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public class PatternLayout extends org.apache.log4j.PatternLayout {

    @Override protected PatternParser createPatternParser(String pattern) {
        return new PatternParser(pattern);
    }

    private static class PatternParser extends org.apache.log4j.helpers.PatternParser {

        private PatternParser(String pattern) {
            super(pattern);
        }

        @Override protected void finalizeConverter(char c) {
            if (c == 'h') addConverter(new HeapSizePatternConverter());
            else super.finalizeConverter(c);
        }
    }

    private static class HeapSizePatternConverter extends PatternConverter {
        @Override protected String convert(LoggingEvent event) {
            Runtime runtime = Runtime.getRuntime();
            long used = runtime.totalMemory() - runtime.freeMemory();
            return (used / 1024 / 1024 + 1) + "MB";
        }
    }
}