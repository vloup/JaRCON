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

package jarcon;

import java.io.File;
import java.io.IOException;

import jarcon.config.RootConfig;
import jarcon.config.ServerConfig;
import jarcon.net.Server;
import jarcon.util.Q3ColorsToBash;

/**
 * Main class of the JaRCON program
 * 
 * @author Barto
 */
public final class JaRCON {
	private final static String VERSION = "1.0";

	/**
	 * Main function of the JaRCON program
	 * 
	 * @param args		arguments in cli
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			help();
		} else {
			if (args[0].equals("--help") || args[0].equals("-h")) {
				help();
			} else if(args[0].equals("--firstRun")) {
				try {
					new File(System.getProperty("user.home") + "/.jarcon/servers").mkdirs();
					new File(System.getProperty("user.home") + "/.jarcon/config.properties").createNewFile();
					System.out.println("JaRCON is nearly ready to do your will.");
					System.out.println("Just use --new, --server and --colors to edit your config.");
				} catch (IOException e) {
					displayException(e);
				}
			} else if(args[0].equals("--new") && 4 < args.length) {
				//if we have --new and the 4 next arguments exist
				//use: "--new <name> <ip> <port> <rconPassword>"
				try {
					ServerConfig.addServer(new Server(args[1], args[2], Integer.parseInt(args[3]), args[4]));
					System.out.println("Succefully added " + args[1] + " in the server's list");
				} catch (IOException e) {
					displayException(e);
				}
			} else if (args[0].equals("--delete") && 1 < args.length) {
				//if we have --delete and the next argument exists
				//use: "--delete <name>"
				try {
					RootConfig rootConfig = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
					rootConfig.load();
					ServerConfig.deleteServer(args[1]);
					System.out.println("Succefully deleted " + args[1] + " from the server's list");
				} catch (IOException e) {
					displayException(e);
				}
			} else if (args[0].equals("--status")) {
				//if we have --status
				try {
					RootConfig rootConfig = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
					rootConfig.load();
					String colorStatus = rootConfig.areBashColorsEnabled() ? "enabled" : "disabled";
					System.out.println("Currently using " + rootConfig.getCurrentServer().toString() + " with colors " + colorStatus);
				} catch (IOException e) {
					displayException(e);
				}
			} else if (args[0].equals("--colors") && 1 < args.length) {
				//set color support
				try {
					RootConfig rootConfig = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
					rootConfig.load();
					rootConfig.useColors(args[1].equals("true"));
					String colorStatus = rootConfig.areBashColorsEnabled() ? "enabled" : "disabled";
					System.out.println("Colors are now " + colorStatus);
				} catch (IOException e) {
					displayException(e);
				}
			} else if (args[0].equals("--server") && 1 < args.length) {
				//set the current server to use
				//use: "--server servername"
				try {
					RootConfig rootConfig = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
					rootConfig.load();
					ServerConfig sc = new ServerConfig(args[1]);
					sc.load();
					rootConfig.setCurrentServer(sc.getServer());
					System.out.println("Current server is now set as " + args[1]);
				} catch (IOException e) {
					displayException(e);
				}
			} else {
				//query the server
				try {
					RootConfig rootConfig = new RootConfig(new File(System.getProperty("user.home") + "/.jarcon/config.properties"));
					rootConfig.load();
					Server server = rootConfig.getCurrentServer();

					//array of args to a single string
					String command = "";
					for (int i = 0; i < args.length; i++) {
						command += args[i] + " ";
					}
					command.trim();

					//query
					String answer = server.rcon(command);

					//color support
					if (rootConfig.areBashColorsEnabled()) {
						answer = Q3ColorsToBash.convertToBash(answer);
					} else {
						answer = Q3ColorsToBash.removeColors(answer);
					}

					//do not display when nothing was received
					if (!answer.equals("")) {
						System.out.println(answer);
					}
				} catch (IOException e) {
					displayException(e);
				}
			}
		}
	}

	/**
	 * display the help of the program
	 */
	private static void help() {
		System.out.println("JaRCON " + VERSION + " (c) Barto");
		System.out.println("Options:");
		System.out.println("COMMAND [VALUE]\t\t\t\tQuery the server with the given command and value");
		System.out.println("nothing\t\t\t\t\tSame as --help");
		System.out.println("--help or -h\t\t\t\tShow this help");
		System.out.println("--firstRun\t\t\t\tSetup the JaRCON working environment");
		System.out.println("--server SERVERNAME\t\t\tSet the current server as SERVERNAME");
		System.out.println("--new SERVERNAME IP PORT PASSWORD\tCreate a new server config file");
		System.out.println("--delete SERVERNAME\t\t\tRemove a server");
		System.out.println("--colors BOOLEAN\t\t\tSet the coloring display");
		System.out.println("--status\t\t\t\tShow the current used server");
		System.out.println();
		System.out.println("JaRCON  Copyright (C) 2013  Barto");
		System.out.println("This program comes with ABSOLUTELY NO WARRANTY; for details type `--help'.");
		System.out.println("This is free software, and you are welcome to redistribute it");
		System.out.println("under certain conditions; type `--help' for details.");
	}

	/**
	 * Display the exception to the user
	 * 
	 * @param e		Exception to display
	 */
	private static void displayException(Exception e) {
		System.out.println("[Error] " + e.getMessage());
		System.out.println("Have you used --firstRun to complete your installation?");
		System.out.println("Try to use --help for more details");
	}
}
