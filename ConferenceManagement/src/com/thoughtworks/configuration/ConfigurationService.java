package com.thoughtworks.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationService {
	 public static Properties loadProperties() {
		 
			Properties prop = new Properties();
			FileReader input = null;
			
			try {input=new FileReader("config.properties");
		 		prop.load(input);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return prop;
		 
		  }
	 
	 public static void main(String[] args) {
		 loadProperties();
	}

}
