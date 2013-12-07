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
				+ BashColor.WHITE.getBashCode();
		assertEquals(expected, Q3ColorsToBash.convertToBash(coloredText));
	}

	@Test
	public void testConvertToBashNoMatch() {
		String q3color = "^@";
		assertEquals(BashColor.WHITE.getBashCode(), Q3ColorsToBash.convertToBash(q3color));
	}
}
