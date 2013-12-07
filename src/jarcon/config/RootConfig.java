package jarcon.config;

import jarcon.net.Server;

import java.io.File;
import java.io.IOException;

/**
 * Class to use for the main config file
 * 
 * @author Barto
 */
public final class RootConfig extends Config {
	/**
	 * Constructor of the main config file
	 * 
	 * @param file		path of the config
	 */
	public RootConfig(File file) {
		super(file);
	}

	/**
	 * Return the currently set server
	 * 
	 * @return					Server currently set in the main config file
	 * @throws IOException		if no file or no rights for the config file
	 */
	public Server getCurrentServer() throws IOException {
		String serverName = getValue("server");
		ServerConfig sc = new ServerConfig(serverName);
		sc.load();
		return sc.getServer();
	}

	/**
	 * Return if the bash color converter is enabled
	 * 
	 * @return				if we should use bash colors output
	 */
	public boolean areBashColorsEnabled() {
		return getValue("colors").equals("true");
	}

	/**
	 * Set if we need to use colored output
	 * 
	 * @param b					if we need to use colors
	 * @throws IOException		if no file rights
	 */
	public void useColors(boolean b) throws IOException {
		setKey("colors", b + "");
	}

	/**
	 * Set the current server as the given one
	 * 
	 * @param s					Server to use now
	 * @throws IOException		if no rights or no file
	 */
	public void setCurrentServer(Server s) throws IOException {
		if (!ServerConfig.exists(s)) {
			throw new IllegalArgumentException("No config file found for this server");
		}

		setKey("server", s.getName());
	}
}