package in.indigenous.finance.util;

import java.io.IOException;
import java.util.Properties;

public final class Config {
	
	private static final String CONFIG_FILES = "finance.properties";

	private Config() {
		// Do not instantiate this class.
	}
	
	public static String get(String property) {
		String [] configFilePath = CONFIG_FILES.split(",");
		String propertyValue = null;
		for(String filePath: configFilePath) {
			Properties properties = new Properties();
			try {
				properties.load(Config.class.getClassLoader().getResourceAsStream(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			propertyValue = properties.getProperty(property);
			if(propertyValue != null) {
				return propertyValue;
			}
		}
		return null;
	}
}
