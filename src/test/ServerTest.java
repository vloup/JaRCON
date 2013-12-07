package test;

import static org.junit.Assert.*;
import jarcon.net.Server;

import org.junit.Test;

/**
 * Tests for the Server class
 * 
 * @author Barto
 */
public class ServerTest {
	@Test
	public void testServerEquals() {
		Server a = new Server("testA", "192.168.1.100", "randomText");
		Server b = new Server("testA", "192.168.1.100", "randomText");
		assertEquals(true, a.equals(b));
		assertEquals(true, a.equals(a));
	}

	@Test
	public void testServerNotEquals() {
		Server a = new Server("testA", "192.168.1.100", "randomText");

		Server b = new Server("testB", "192.168.1.100", "randomText");
		assertEquals(false, a.equals(b));

		Server c = new Server("testA", "127.0.0.1", "randomText");
		assertEquals(false, a.equals(c));

		Server d = new Server("testA", "192.168.1.100", 8080, "randomText");
		assertEquals(false, a.equals(d));

		Server e = new Server("testA", "192.168.1.100", "wrongPassword");
		assertEquals(false, a.equals(e));
		
		assertEquals(false, a.equals(new Object()));
		assertEquals(false, a.equals(null));
	}
}