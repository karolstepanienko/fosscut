package com.fosscut.util;

import picocli.CommandLine.IVersionProvider;

import java.net.URL;
import java.util.Properties;

/**
 * {@link IVersionProvider} implementation that returns version information from a {@code /version.txt} file in the classpath.
 */
public class PropertiesVersionProvider implements IVersionProvider {

    public String[] getVersion() throws Exception {
        URL url = getClass().getResource("/version.txt");
        if (url == null) {
            return new String[] {"No version.txt file found in the classpath."};
        }

        Properties properties = new Properties();
        properties.load(url.openStream());
        return new String[] {
            properties.getProperty("Application-name")
            + " version \"" + properties.getProperty("Version") + "\"",
            "Built: " + properties.getProperty("Buildtime"),
        };
    }

}
