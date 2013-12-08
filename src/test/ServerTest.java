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

package test;

import static org.junit.Assert.*;

import java.io.IOException;

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

	@Test
	public void testPasswordObfuscation() throws IOException {
		Server a = new Server("testA", "127.0.0.1", 27960, "randomText");
		String password = a.getObfuscatedPassword();
		assertEquals(a, Server.getServerFromObfuscatedPassword(a.getName(), a.getIP(), a.getPort(), password));
	}
}