package jarcon.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Represent a Q3-like server
 * 
 * @author Barto
 */
public final class Server {
	private final String name;
	private final String ip;
	private final int port;
	private final String password;

	/**
	 * Constructor of the server
	 * @param name			name of the server
	 * @param ip			ip of the server
	 * @param port			port of the server
	 * @param password		rcon password of the server
	 */
	public Server(String name, String ip, int port, String password) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.password = password;
	}

	/**
	 * Alternative constructor with a default port (27960)
	 * 
	 * @param name			name of the server
	 * @param ip			ip of the server
	 * @param password		password of the server
	 */
	public Server(String name, String ip, String password) {
		this(name, ip, 27960, password);
	}

	/**
	 * Send a rcon query to the server
	 * 
	 * @param command			command to send to the server
	 * @return					server's response on this query
	 * @throws IOException		if server unreachable
	 */
	public String rcon(String command) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(500); //timeout of 0.5 second (I'm a hurry guy ;-) )
		InetAddress server = InetAddress.getByName(ip);

		//create packet to send
		byte oob = (byte)0xFF;
		byte[] request = ("XXXXrcon " + password + " " + command).getBytes();
		request[0] = oob;
		request[1] = oob;
		request[2] = oob;
		request[3] = oob;
		DatagramPacket packetSend = new DatagramPacket(request, request.length, server, port);

		socket.send(packetSend);

		//create packet response
		DatagramPacket packetResponse;
		byte[] output = new byte[65507];
		String response = "";
		//the response could be in more than one packet
		while(true) {
			try {
				//add to output
				packetResponse = new DatagramPacket(output, output.length);
				socket.receive(packetResponse);
				String packetOutput = new String(packetResponse.getData(), 0, packetResponse.getLength());
				//remove first line of each packet (which is XXXXprint\n)
				packetOutput = packetOutput.substring(packetOutput.indexOf('\n') + 1);
				response += packetOutput;
			} catch (IOException e) {
				socket.close();
				//remove useless line
				return response.trim();
			}
		}
	}

	/**
	 * Return the current name of the server
	 * 
	 * @return		name of the server
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return current ip of the server
	 * 
	 * @return		ip of the server
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Return current port of the server
	 * 
	 * @return		port of the server
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Return current password of the server
	 * Depreciated due to its lack of security
	 * 
	 * @return		current password
	 */
	@Deprecated
	public String getPassword() {
		return password;
	}

	/**
	 * Test if the current server equals the given server
	 * 
	 * @param that		second server
	 */
	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		} else if (that.getClass().equals(getClass())) {
			Server thatServer = (Server)that;
			return thatServer.name.equals(name) && thatServer.ip.equals(ip) && thatServer.port == port && thatServer.password.equals(password);
		} 
		return false;
	}

	/**
	 * Return the String representation of the server
	 * 
	 * @return		name@ip:port
	 */
	@Override
	public String toString() {
		return name + "@" + ip + ":" + port;
	}
}