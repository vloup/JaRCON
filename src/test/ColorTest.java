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
import jarcon.util.BashColor;
import jarcon.util.Q3Color;
import jarcon.util.Q3ColorsToBash;

import org.junit.Test;

/**
 * Test for Colors
 * 
 * @author Barto
 */
public class ColorTest {
	@Test
	public void testBashColor() {
		assertEquals("\033[0;31m", BashColor.RED.getBashCode());
	}

	@Test
	public void testQ3Color() {
		assertEquals("^4", Q3Color.BLUE.getColorCode());

		char red = '1';
		assertEquals(Q3Color.getColorFromChar(red).getColorCode(), "^" + red);
	}

	@Test
	public void testRemoveAllColors() {
		String coloredText = "^1RED^2BLUE^3GREEN^7";
		assertEquals("REDBLUEGREEN", Q3ColorsToBash.removeColors(coloredText));
	}

	@Test
	public void testConvertToBash() {
		String coloredText = "^4A^5B^6C^7";
		String expected = BashColor.BLUE.getBashCode() + "A"
				+ BashColor.LIGHT_BLUE.getBashCode() + "B"
				+ BashColor.PURPLE.getBashCode() + "C"
				+ BashColor.NONE.getBashCode();
		assertEquals(expected, Q3ColorsToBash.convertToBash(coloredText));
	}

	@Test
	public void testConvertToBashNoMatch() {
		String q3color = "^@";
		assertEquals(BashColor.NONE.getBashCode(), Q3ColorsToBash.convertToBash(q3color));
	}
}
