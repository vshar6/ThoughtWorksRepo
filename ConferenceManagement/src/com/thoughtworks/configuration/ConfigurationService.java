package com.thoughtworks.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ConfigurationService {
	private static final Logger LOG = Logger
			.getLogger(ConfigurationService.class);

	public static Properties loadProperties() {

		Properties prop = new Properties();
		FileReader input = null;

		try {
			String filePath = new File(StringUtils.EMPTY).getAbsolutePath()
					+ "/resources/config.properties";
			input = new FileReader(filePath);
			prop.load(input);
		} catch (IOException ex) {
			LOG.error("Exception in reading config properties", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error(
							"Exception in closing stream for config properties",
							e);
				}
			}
		}
		return prop;
	}
}
