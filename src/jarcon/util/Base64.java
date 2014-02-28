/**
 * Copyright (c) 2014 Barto
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

package jarcon.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Encode or decode Strings in base64
 *
 * @author Barto
 */
public final class Base64 {
	/**
	 * Encode the given sequence of bytes in base64
	 *
	 * @param bytes		bytes to encode
	 * @return			encoded String of the array of bytes
	 */
	public static String encode(byte[] bytes) {
		BASE64Encoder enc = new BASE64Encoder();
		return enc.encode(bytes).replaceAll("\\s", "");
	}

	/**
	 * Decode the given String in base64
	 *
	 * @param s					String to decode
	 * @return					base64 decoding of s
	 * @throws java.io.IOException        if decoding goes wrong
	 */
	public static byte[] decode(String s) throws IOException {
		BASE64Decoder d = new BASE64Decoder();
		return d.decodeBuffer(s);
	}
}
