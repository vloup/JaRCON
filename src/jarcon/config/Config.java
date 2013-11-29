package jarcon.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jarcon.Server;

/**
 * Dealing with configs
 * 
 * @author Barto
 */
public final class Config {
	private final String configFile = "config.properties";
	private Server currentServer;
	private boolean useColors;

	/**
	 * Constructor of the config (get current one)
	 * 
	 * @throws FileNotFoundException		if no config found
	 * @throws IOException					if read error on config file
	 */
	public Config() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(System.getProperty("user.home") + "/" + ".jarcon/" + configFile));

		//get current server
		String currentServerName = prop.getProperty("server");
		if (currentServerName == null || currentServerName == "") {
			throw new IllegalArgumentException("Missing server name entry");
		}
		currentServer = getServer(currentServerName);

		//color or not?
		useColors = prop.getProperty("colors", "false").equals("true"); //better than Boolean.parseBoolean(String s)
	}
	/**
	 * Get the current server
	 * 
	 * @return									new instance of Server
	 * @throws CloneNotSupportedException		if clone goes wrong
	 */
	public Server getCurrentServer() throws CloneNotSupportedException {
		return currentServer.clone();
	}

	/**
	 * Get the server from config file according to its name
	 * 
	 * @param serverName				name of the server to load
	 * @return							an instance of Server
	 * @throws FileNotFoundException	if no config file found
	 * @throws IOException				if some troubles with file's rights
	 */
	public Server getServer(String serverName) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(System.getProperty("user.home") + "/" + ".jarcon/servers/" + serverName + ".properties"));

		//getting the datas
		String name = prop.getProperty("name");
		String ip = prop.getProperty("ip");
		String portString = prop.getProperty("port", "27960");
		String cryptedPassword = prop.getProperty("rconPassword");

		//data checking
		if (name == null || ip == null || portString == null || cryptedPassword == null) {
			throw new IllegalArgumentException("Invalid server config file");
		} else if (name == "" || ip == "" || portString == "" || cryptedPassword == "") {
			throw new IllegalArgumentException("Ivalid server config file");
		}

		int port = Integer.parseInt(portString);
		return Server.decryptPassword(cryptedPassword, name, ip, port);
	}

	/**
	 * Set the default server in the master config
	 * 
	 * @param name						name of the server config file
	 * @throws FileNotFoundException	if server config no file
	 * @throws IOException				if the server config has some rights problem
	 */
	public void setServer(String name) throws FileNotFoundException, IOException {
		currentServer = getServer(name);
		saveProperties();
	}

	/**
	 * Add the current server to the query-able servers
	 * 
	 * @param server			server to add
	 * @throws IOException		if something goes wrong in file creation
	 */
	public void addServer(Server server) throws IOException {
		Properties prop = server.toProperties();
		prop.store(new FileOutputStream(System.getProperty("user.home") + "/" + ".jarcon/servers/" + server.name() + ".properties"), null);
	}

	/**
	 * Delete the given serverName
	 * 
	 * @param string						server to delete, according to its name
	 * @throws IOException					if something goes wrong in file deletion
	 * @throws IllegalArgumentException		if the server is set as the default one
	 */
	public void deleteServer(String string) throws IOException {
		if (getServer(string).equals(currentServer)) {
			throw new IllegalArgumentException("Server currently set as the default one");
		}
		File file = new File(System.getProperty("user.home") + "/" + ".jarcon/servers/" + string + ".properties");
		file.delete();
	}

	/**
	 * Set the colors
	 * 
	 * @throws IOException 				if we cannot save
	 * @throws FileNotFoundException 	if no save file
	 * 
	 */
	public void setColors(boolean enableColors) throws FileNotFoundException, IOException {
		useColors = enableColors;
		saveProperties();
	}
	/**
	 * Return the status of the current server
	 * 
	 * @return							the currently set server
	 * @throws IOException				if getting the current server's config fails	 
	 * @throws FileNotFoundException 	if there are no config for the given server
	 */
	public String status() {
		return currentServer.toString();
	}

	/**
	 * Return if the color support is enabled
	 * 
	 * @return			if colors are enabled
	 */
	public boolean hasColorSupportEnabled() {
		return useColors;
	}
	/**
	 * Send the query to the currently set server and format it
	 * 
	 * @param query						server's query
	 * @return							the answer from the server
	 * @throws FileNotFoundException	if there are no server file (master or server)
	 * @throws IOException				if there are some IO errors with config files
	 */
	public String currentServerQuery(String query) throws IOException {
		String queryOutout = currentServer.query(query);
		queryOutout = queryOutout.substring(queryOutout.indexOf('\n') + 1); //remove first line that is useless for us
		queryOutout = queryOutout.trim(); //may remove some extra \n at the end of the query String
		return useColors ? replaceFewColors(queryOutout) : removeColors(queryOutout);
	}

	/**
	 * Replace few colors by their Bash correspondent
	 * 
	 * @param colored		the string we want to convert the colors
	 * @return				a pseudo bash-colored string
	 */
	private String replaceFewColors(String colored) {
		colored = colored.replaceAll("\\^7", "\033[0m"); //normal color
		colored = colored.replaceAll("\\^[9|0|p|P|w|W|y|Y|z|Z|;|\\[]", "\033[0m"); //grey, but then changed to normal (since xterm likes bold)
		colored = colored.replaceAll("\\^[1|i|I|j|J|k|K|q|Q|\\*]", "\033[31m"); //red
		colored = colored.replaceAll("\\^[2|b|B|g|G|h|H|m|M|r|R|\\-|=|\\\\|\\]|']", "\033[32m"); //green
		colored = colored.replaceAll("\\^[3|8|a|A|l|L|n|N|o|O|s|S|x|X|,|.|/]", "\033[33m"); //yellow
		colored = colored.replaceAll("\\^[4|d|D|f|F|t|T]", "\033[34m"); //blue
		colored = colored.replaceAll("\\^[6|c|C|e|E|v|V]", "\033[35m"); //pink
		colored = colored.replaceAll("\\^[5|u|U]", "\033[36m"); // light blue
		//colored = colored.replaceAll(regex, "\033[37m"); //full white

		colored = colored.replaceAll("\n", "\033[0m\n"); //normal colors for \n

		return removeColors(colored);
	}

	/**
	 * Remove the Quake 3 coloring to the given String
	 * 
	 * Remarks:
	 * Quake 3 colors are composed by 2 characters: a '^' followed by a char
	 * The char could be an upper case letter, lower case or even a number.
	 * It thus couldn't be another '^' or some already made work with the bash colors
	 * 
	 * @param colors		colored text where we need to remove the colors
	 * @return				the same text without any colors
	 */
	private String removeColors(String colored) {
		return colored.replaceAll("\\^[^[^|\\033]]", ""); //beware when there is a match like ^\\033[0m because of some premade-work
	}

	/**
	 * Save the current config into a properties file
	 * 
	 * @throws FileNotFoundException			if no config found
	 * @throws IOException						if a writing problem
	 */
	private void saveProperties() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.setProperty("server", currentServer.name());
		prop.setProperty("colors", useColors + "");
		prop.store(new FileOutputStream(System.getProperty("user.home") + "/" + ".jarcon/" + configFile), null);
	}
}
