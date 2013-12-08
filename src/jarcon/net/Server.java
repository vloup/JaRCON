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

package jarcon.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
		socket.setSoTimeout(1000); //timeout of 1 second
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
	 * Return an obfuscated version of the password
	 * It is aimed to take out anyone who wants to read into the config file
	 * 
	 * @return		the obfuscated rcon password
	 */
	public String getObfuscatedPassword() {
		Random rand = createRandom(name, ip, port);
		byte[] mask = new byte[password.getBytes().length];
		rand.nextBytes(mask);

		return base64Encode(xor(password.getBytes(), mask));
	}

	/**
	 * Return the server from its obfuscated password
	 * 
	 * @param name					name of the server
	 * @param ip					ip of the server
	 * @param port					port of the server
	 * @param obfuscatedPassword	obfuscated password
	 * @return						the server with a now decoded password
	 * @throws IOException			if the obfuscated password isn't valid (not a base64 string in this case)
	 */
	public static Server getServerFromObfuscatedPassword(String name, String ip, int port, String obfuscatedPassword) throws IOException {
		Random rand = createRandom(name, ip, port);
		byte[] xoredPassword = base64Decode(obfuscatedPassword);
		byte[] mask = new byte[xoredPassword.length];
		rand.nextBytes(mask);

		String password = new String(xor(xoredPassword, mask));
		return new Server(name, ip, port, password);
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

	/**
	 * Create a random from server
	 * 
	 * @return		random
	 */
	private static Random createRandom(String name, String ip, int port) {
		//create seed
		String seedString = name + ip + port;
		long seed = 0;
		for (int i=0; i<seedString.length(); i++) {
			char c = seedString.charAt(i);
			seed += (int)c;
			seed = seed << 2;
		}

		//init rand
		return new Random(seed);
	}

	/**
	 * Make a xor byte per byte on the 2 given array
	 * 
	 * @param a			array to xor
	 * @param mask		mask for xor-ing
	 * @return			a xor mask
	 */
	private static byte[] xor(byte[] a, byte[] mask) {
		//check size
		if (a.length != mask.length) {
			throw new IllegalArgumentException("Arrays' length mismatch");
		}

		//make the xor
		byte[] result = new byte[a.length];
		for (int i=0; i<result.length; i++) {
			result[i] = (byte)(a[i] ^ mask[i]);
		}

		return result;
	}

	/**
	 * Encode the given sequence of bytes in base64
	 * 
	 * @param bytes		bytes to encode
	 * @return			encoded String of the array of bytes
	 */
	private String base64Encode(byte[] bytes) {
		BASE64Encoder enc = new BASE64Encoder();
		return enc.encode(bytes).replaceAll("\\s", "");
	}

	/**
	 * Decode the given String in base64
	 *
	 * @param s					String to decode
	 * @return					base64 decoding of s
	 * @throws IOException		if decoding goes wrong
	 */
	private static byte[] base64Decode(String s) throws IOException {
		BASE64Decoder d = new BASE64Decoder();
		return d.decodeBuffer(s);
	}
}