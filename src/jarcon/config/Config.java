package jarcon.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Abstract class to represent a config file
 * 
 * @author Barto
 */
abstract public class Config {
	private final Properties properties;
	private final File file;

	/**
	 * Constructor of the config file
	 * 
	 * @param file		the config file to read/write
	 */
	public Config(File file) {
		this.properties = new Properties();
		this.file = file;
	}

	/**
	 * Load the config
	 * 
	 * @throws IOException		if no rights or file not found
	 */
	public void load() throws IOException {
		properties.load(new FileInputStream(file));
	}

	/**
	 * Set the given key as the given value in the config
	 * 
	 * @param key				identifier of the option
	 * @param value				value of the option
	 * @throws IOException		if no rights to save file
	 */
	public void setKey(String key, String value) throws IOException {
		properties.setProperty(key, value);
		save();
	}

	/**
	 * Return the value of the given key in the config file
	 * 
	 * @param key			identifier of the option
	 * @return				the value of the option
	 */
	public String getValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Save the current config file
	 * 
	 * @throws IOException		if no rights to edit this file
	 */
	public void save() throws IOException {
		properties.store(new FileOutputStream(file), "");
	}
}