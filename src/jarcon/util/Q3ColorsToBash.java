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

package jarcon.util;

import java.util.HashMap;

/**
 * Bridge between Q3 colors and Bash colors
 * 
 * @author Barto
 */
public final class Q3ColorsToBash {
	private final static HashMap<Q3Color, BashColor> decoder;

	/*
	 * Let's build the decoder
	 */
	static {
		decoder = new HashMap<Q3Color, BashColor>();
		//when you need a table, you really need one
		decoder.put(Q3Color.RED, BashColor.RED);
		decoder.put(Q3Color.GREEN, BashColor.GREEN);
		decoder.put(Q3Color.YELLOW, BashColor.YELLOW);
		decoder.put(Q3Color.BLUE, BashColor.BLUE);
		decoder.put(Q3Color.LIGHT_BLUE, BashColor.LIGHT_BLUE);
		decoder.put(Q3Color.PINK, BashColor.PURPLE); //no correspondence
		//decoder.put(Q3Color.WHITE, BashColor.WHITE);
		decoder.put(Q3Color.WHITE, BashColor.NONE); //not original
		decoder.put(Q3Color.ORANGE, BashColor.YELLOW); //no correspondence
		decoder.put(Q3Color.GREY, BashColor.LIGHT_GREY);
		decoder.put(Q3Color.BLACK, BashColor.BLACK);
		decoder.put(Q3Color.DARK_ORANGE, BashColor.YELLOW); //no correspondence
		decoder.put(Q3Color.LIGHT_GREEN, BashColor.LIGHT_GREEN);
		decoder.put(Q3Color.DARK_PURPLE, BashColor.LIGHT_PURPLE);
		decoder.put(Q3Color.DARK_LIGHT_BLUE, BashColor.LIGHT_BLUE);
		decoder.put(Q3Color.PURPLE, BashColor.PURPLE);
		decoder.put(Q3Color.BLUE2, BashColor.BLUE);
		decoder.put(Q3Color.LIGHT_GREEN2, BashColor.LIGHT_GREEN);
		decoder.put(Q3Color.MEDIUM_GREEN, BashColor.GREEN);
		decoder.put(Q3Color.MEDIUM_RED, BashColor.RED);
		decoder.put(Q3Color.DARK_RED, BashColor.RED);
		decoder.put(Q3Color.BROWN, BashColor.RED); //no correspondence
		decoder.put(Q3Color.MEDIUM_ORANGE, BashColor.YELLOW); //no correspondence
		decoder.put(Q3Color.VERY_LIGHT_GREEN, BashColor.LIGHT_GREEN);
		decoder.put(Q3Color.LIGHT_YELLOW, BashColor.YELLOW);
		decoder.put(Q3Color.DARK_YELLOW, BashColor.YELLOW);
		decoder.put(Q3Color.BLACK2, BashColor.BLACK);
		decoder.put(Q3Color.DARK_RED2, BashColor.RED);
		decoder.put(Q3Color.CYAN, BashColor.CYAN);
		decoder.put(Q3Color.MEDIUM_YELLOW, BashColor.YELLOW);
		decoder.put(Q3Color.MEDIUM_BLUE, BashColor.BLUE);
		decoder.put(Q3Color.VERY_LIGHT_BLUE, BashColor.LIGHT_BLUE);
		decoder.put(Q3Color.LIGHT_PINK, BashColor.PURPLE); //no correspondence
		decoder.put(Q3Color.LIGHT_GREY, BashColor.LIGHT_GREY);
		decoder.put(Q3Color.DARK_ORANGE2, BashColor.YELLOW); //no correspondence
		decoder.put(Q3Color.DARK_GREY, BashColor.DARK_GREY);
		decoder.put(Q3Color.MEDIUM_GREY, BashColor.LIGHT_GREY);
		decoder.put(Q3Color.MEDIUM_GREY2, BashColor.LIGHT_GREY);
		decoder.put(Q3Color.LIGHT_ORANGE, BashColor.YELLOW); //no correspondence
		decoder.put(Q3Color.LIGHT_YELLOW2, BashColor.YELLOW);
		decoder.put(Q3Color.MEDIUM_YELLOW2, BashColor.YELLOW);
		decoder.put(Q3Color.SORT_OF_GREEN, BashColor.GREEN);
		decoder.put(Q3Color.SORT_OF_GREEN2, BashColor.GREEN);
		decoder.put(Q3Color.GREEN2, BashColor.GREEN);
		decoder.put(Q3Color.LIGHT_GREY2, BashColor.LIGHT_GREY);
		decoder.put(Q3Color.SORT_OF_GREEN3, BashColor.GREEN);
		decoder.put(Q3Color.LIGHT_CYAN, BashColor.LIGHT_CYAN);
		decoder.put(Q3Color.DARK_RED3, BashColor.RED);
	}

	/**
	 * Remove all colors from a string
	 * 
	 * @param str		string with colors
	 * @return			the same string without any Q3Colors
	 */
	public static String removeColors(String str) {
		return str.replaceAll("\\^.", "");
	}

	/**
	 * Convert the String with Q3 colors to the same String with bash colors
	 * 
	 * @param str		String with Q3 colors
	 * @return			same String with Bash colors
	 */
	public static String convertToBash(String str) {
		String output = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '^' && i + 1 != str.length()) {
				i++; // look for next char
				output += decoder.get(Q3Color.getColorFromChar(str.charAt(i))).getBashCode();
			} else if (str.charAt(i) == '\n') {
				output += str.charAt(i) + BashColor.NONE.getBashCode();
			} else {
				output += str.charAt(i);
			}
		}
		return output;
	}
}