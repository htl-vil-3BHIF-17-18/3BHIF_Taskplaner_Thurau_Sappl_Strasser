package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	private static PropertyManager instance = null;
	private String configFile = "config.properties";
	private Properties props = new Properties();

	private PropertyManager() {
	}

	public static PropertyManager getInstance() throws FileNotFoundException, IOException {
		if (PropertyManager.instance == null) {
			instance = new PropertyManager();
			instance.fillProperties();
		}
		return instance;
	}

	private void fillProperties() throws FileNotFoundException, IOException {
		try (FileReader reader = new FileReader(this.configFile)) {
			this.props.load(reader);
		}
	}

	public String readProperty(String name) {
		return this.props.getProperty(name);

	}

	public void writeProperty(String name, String value) throws IOException {
		this.props.setProperty(name, value);
		this.writeToFile();
	}

	private void writeToFile() throws IOException {
		try (FileWriter writer = new FileWriter(this.configFile)) {
			props.store(writer, "My fancy Properties");
		}
	}
}
