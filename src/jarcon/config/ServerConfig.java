package jarcon.config;

import jarcon.net.Server;

import java.io.File;
import java.io.IOException;

/**
 * Server config file
 * 
 * @author Barto
 */
public final class ServerConfig extends Config {
	/**
	 * Constructor of the server's config file
	 * 
	 * @param serverName		name of the server to read
	 */
	public ServerConfig(String serverName) {
		super(new File(System.getProperty("user.home") + "/.jarcon/servers/" + serverName + ".properties"));
	}

	/**
	 * Return the server in the config file
	 * 
	 * @return		Server from config file
	 */
	public Server getServer() {
		String name = getValue("name");
		String ip = getValue("ip");
		int port = Integer.parseInt(getValue("port"));
		String password = getValue("rconPassword");
		return new Server(name, ip, port, password);
	}

	/**
	 * Add a server into the config
	 * 
	 * @param s					Server to add
	 * @throws IOException		if no file rights
	 */
	public static void addServer(Server s) throws IOException {
		ServerConfig sc = new ServerConfig(s.getName());
		sc.setKey("name", s.getName());
		sc.setKey("ip", s.getIP());
		sc.setKey("port", s.getPort() + "");
		sc.setKey("rconPassword", s.getPassword());
	}

	/**
	 * Delete the given server
	 * 
	 * @param serverName			name of the server to delete
	 * @throws IOException			if no file to delete, no rights or the server is the default one
	 */
	public static void deleteServer(String serverName) throws IOException {
		RootConfig rc = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
		rc.load();
		ServerConfig sc = new ServerConfig(serverName);
		sc.load();

		// if server is currently in use
		if (rc.getCurrentServer().equals(sc.getServer())) {
			throw new IOException("Server currently set as the default one");
		}

		File serverFile = new File(System.getProperty("user.home") + "/.jarcon/servers/" + serverName + ".properties");

		// if the server doesn't exists
		if (!serverFile.exists()) {
			throw new IOException("Server file not found");
		}

		serverFile.delete();
	}

	/**
	 * Check if the given server is saved
	 * 
	 * @param s			Server to check
	 * @return			if it exists
	 */
	public static boolean exists(Server s) {
		return new File(System.getProperty("user.home") + "/.jarcon/servers/" + s.getName() + ".properties").exists();
	}
}