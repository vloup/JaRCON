package jarcon;

import jarcon.config.Config;

/**
 * Main class for JaRcon
 * @author Barto
 */
public class Main {
	/**
	 * main function
	 * @param args		cli arguments
	 */
	public static void main(String[] args) {
		//parsing args
		if (args.length == 0) {
			help();
		} else {
			try {
				Config config = new Config();
				if (args[0].equals("--help") || args[0].equals("-h")) {
					//if we have --help or -h
					help();
				} else if(args[0].equals("--server") && 1 < args.length) {
					//if we have --server and the next argument exists
					//use:  "--server <name>"
					config.setServer(args[1]);
					System.out.println("Current server is now set as " + config.getCurrentServer().name());
				} else if(args[0].equals("--new") && 4 < args.length) {
					//if we have --new and the 4 next arguments exist
					//use: "--new <name> <ip> <port> <rconPassword>"
					config.addServer(new Server(args[1], args[2], Integer.parseInt(args[3]), args[4]));
					System.out.println("Succefully added " + args[1] + " in the server's list");
				} else if (args[0].equals("--delete") && 1 < args.length) {
					//if we have --delete and the next argument exists
					//use: "--delete <name>"
					config.deleteServer(args[1]);
					System.out.println("Succefully deleted " + args[1] + " from the server list");
				} else if (args[0].equals("--status")) {
					//if we have --status
					String status = config.hasColorSupportEnabled() ? "enabled" : "disabled";
					System.out.println("Currently using: " + config.status() + " with colors " + status);
				} else if (args[0].equals("--colors") && 1 < args.length) {
					//set color support
					//use: "--colors true/false"
					config.setColors(Boolean.parseBoolean(args[1]));
					String status = config.hasColorSupportEnabled() ? "enabled" : "disabled";
					System.out.println("Color support is now " + status);
				} else {
					//getting the query in a string
					String query = "";
					for (int i=0; i<args.length; i++) {
						query += args[i];
						if (i != args.length - 1) {
							query += " ";
						}
					}

					//querying
					System.out.print(config.currentServerQuery(query));	
				}
			} catch(Exception e) {
				displayError(e.getMessage());
			}
		}
	}

	private static void help() {
		System.out.println("JaRCON 1.0dev (c) Barto");
		System.out.println("Options:");
		System.out.println("COMMAND [VALUE]\t\t\t\tQuery the server with the given command and value");
		System.out.println("nothing\t\t\t\t\tSame as --help");
		System.out.println("--help or -h\t\t\t\tShow this help");
		System.out.println("--server SERVERNAME\t\t\tSet the current server as SERVERNAME");
		System.out.println("--new SERVERNAME IP PORT PASSWORD\tCreate a new server config file");
		System.out.println("--delete SERVERNAME\t\t\tRemove a server");
		System.out.println("--colors BOOLEAN\t\t\tSet the coloring display");
		System.out.println("--status\t\t\t\tShow the current used server");
	}

	private static void displayError(String message) {
		System.out.println("[Error] " + message);
		System.out.println("Try --help for more details");
	}
}