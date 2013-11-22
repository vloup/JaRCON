package jarcon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Represents a typical Quake3 protocol based server
 * 
 * @author Barto
 */
public final class Server implements Cloneable {
	private final String name;
	private final String ip;
	private final int port;
	private final String rconPassword;

	/**
	 * Create a server
	 * 
	 * @param name							server name
	 * @param ip							IP of the server
	 * @param port							port of the server (integer)
	 * @param rconPassword					admin password of the server
	 * @throws IllegalArgumentException		for some invalid inputs
	 * @throws UnknownHostException			for an invalid IP
	 */
	public Server(String name, String ip, int port, String rconPassword) throws UnknownHostException {
		//checking args
		if (name == null || ip == null || rconPassword == null) {
			throw new IllegalArgumentException("Invalid arguments");
		} else if (name == "" || ip == "" || rconPassword == "") {
			throw new IllegalArgumentException("Invalid arguments");
		} else if (port < 0 || port > 65535) { //java sucks, I cannot have an unsigned short (16 bit integer) :(
			throw new IllegalArgumentException("Invalid port number"); 
		}

		//checking the IP
		this.ip = InetAddress.getAllByName(ip)[0].getHostAddress();
		
		//we're fine
		this.name = name;
		this.port = port;
		this.rconPassword = rconPassword;
	}

	/**
	 * Return the encrypted password of the server
	 * 
	 * Method:
	 * 1-	We first do a xor between a mask and the password
	 * 		the mask is defined as "name + ip + port"
	 * 2-	We then encrypt the whole in Base64
	 * Remarks:
	 * 		It is not a really strong password storage,
	 * 		but it it enough for that kind of usage.
	 * 		I really want to avoid any master-password,
	 * 		so by definition, it is unsecure.
	 * 
	 * @return		weakly encryption of the password
	 */
	public String cryptPassword() {
		String mask = name + ip + port;
		return base64Encode(xorWithKey(rconPassword.getBytes(), mask.getBytes()));
	}

	/**
	 * Create an instance of Server with the given infos required to decrypt the password and create an instance of Server
	 * 
	 * Method:
	 * 1-	We firstly need to base64 decrypt our given crypted password
	 * 2-	Then we apply the xor with a mask
	 * 		The mask is defined same as encryption: "name + ip + port"
	 * Remarks:
	 * 		You clearly see that I put all eggs in the same basket.
	 *		This encryption method is bad, but it's better than anything.
	 *
	 * @param encryptedPassword		a weakly encrypted password
	 * @param name					name of the server
	 * @param ip					IP of the server
	 * @param port					port of the server
	 * @return						instance of Server with a unencrypted password
	 * @throws IOException			if something goes wrong during decryption
	 */
	public static Server decryptPassword(String encryptedPassword, String name, String ip, int port) throws IOException {
		String mask = name + ip + port;
		return new Server(name, ip, port, new String(xorWithKey(base64Decode(encryptedPassword), mask.getBytes())));
	}

	/**
	 * Queries the server with the given command
	 * 
	 * @param rconCommand			command to execute on the server
	 * @return						the answer from the server
	 * @throws IOException			if the rconPassword is wrong or server is unreachable
	 */
	public String query(String rconCommand) throws IOException {
		byte[] query = ("XXXXrcon " + rconPassword + " " + rconCommand).getBytes();
		for (int i=0; i<4; i++) {
			query[i] = (byte)0xFF;
		}

		//sending process
		InetAddress serverAddress = InetAddress.getByName(ip);
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(3000); //3 seconds timeout

		DatagramPacket packetSend = new DatagramPacket(query, query.length, serverAddress, port);
		socket.send(packetSend);

		//receiving process
		byte[] receiveBuffer = new byte[65507];
		DatagramPacket packetReceive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		socket.receive(packetReceive);

		//stopping socket
		socket.close();

		//output
		String output = new String(receiveBuffer);

		//checking the output
		byte[] wrongRconPattern = "XXXXdisconnect".getBytes();
		for (int i=0; i<4; i++) {
			wrongRconPattern[i] = (byte)0xFF;
		}
		String badRcon = new String(wrongRconPattern);
		if (output.split("\n")[0].equals(badRcon)) {
			throw new IOException("Bad rconPassword");
		}

		//removing the first line
		return output;
	}

	/**
	 * Getter for the server's name
	 * 
	 * @return		the name of the server
	 */
	public String name() {
		return name;
	}

	/**
	 * Getter for the IP of the server
	 * 
	 * @return		IP of the server on human readable representation
	 */
	public String ip() {
		return ip;
	}

	/**
	 * Getter of the port of the server
	 * 
	 * @return		port number of the server
	 */
	public int port() {
		return port;
	}

	/**
	 * Create a properties instance of the current server
	 * 
	 * @return	properties instance of the current server
	 */
	public Properties toProperties() {
		Properties prop = new Properties();
		prop.setProperty("name", name);
		prop.setProperty("ip", ip);
		prop.setProperty("port", port + ""); //quick and dirty trick
		prop.setProperty("rconPassword", cryptPassword()); //I can force my weak encryption that way ;-)
		return prop;
	}

	@Override
	public int hashCode() {
		int hashName = name.hashCode() << 8;
		int hashIP = ip.hashCode() << 8;
		int hashPort = port << 8;
		int hashPassword = cryptPassword().hashCode() << 8; //better use the encrypted password here
		return hashName + hashIP + hashPort  + hashPassword;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o.getClass() == getClass()) {
			Server s = (Server)o;
			return name.equals(s.name) && ip.equals(s.ip) && port == s.port && rconPassword.equals(s.rconPassword);
		}
		return false;
	}

	@Override
	public Server clone() throws CloneNotSupportedException {
		//these exceptions shouldn't get triggered, but java forces me to do this ugly code
		try {
			return new Server(name, ip, port, rconPassword);
		} catch (UnknownHostException e) {
			throw new CloneNotSupportedException(e.getMessage());
		}
	}

	@Override
	public String toString() {
		return name + "@" + ip + ":" + port;
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

	/**
	 * Makes a XOR with 2 arrays of bytes.
	 * 
	 * @param a		first array
	 * @param b		second array, as the key
	 * @return		bitwise a xor b
	 */
	private static byte[] xorWithKey(byte[] a, byte[] b) {
		byte[] output = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			output[i] = (byte) (a[i] ^ b[i % b.length]);
		}
		return output;
	}
}