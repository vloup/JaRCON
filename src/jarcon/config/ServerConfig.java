/**
 * Copyright (c) 2013 Barto
 * 
 * This file is part of JaRCON.
 * 
 * JaRCON is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JaRCON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JaRCON.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	 * @return				Server from config file
	 * @throws IOException	if password invalid
	 */
	public Server getServer() throws IOException {
		String name = getValue("name");
		String ip = getValue("ip");
		int port = Integer.parseInt(getValue("port"));
		String obfuscatedPassword = getValue("rconPassword");

		return Server.getServerFromObfuscatedPassword(name, ip, port, obfuscatedPassword);
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
		sc.setKey("rconPassword", s.getObfuscatedPassword());
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