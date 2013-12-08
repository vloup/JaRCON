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
			throw new IOException("No config file found for this server");
		}

		setKey("server", s.getName());
	}
}