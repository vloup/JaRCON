package jarcon.util;

import java.util.HashMap;

/**
 * Represent a Q3Color
 * 
 * @author Barto
 */
public enum Q3Color {
	RED('1'),
	GREEN('2'),
	YELLOW('3'),
	BLUE('4'),
	LIGHT_BLUE('5'),
	PINK('6'),
	WHITE('7'),
	ORANGE('8'),
	GREY('9'),
	BLACK('0'),
	DARK_ORANGE('a'),
	LIGHT_GREEN('b'),
	DARK_PURPLE('c'),
	DARK_LIGHT_BLUE('d'),
	PURPLE('e'),
	BLUE2('f'),
	LIGHT_GREEN2('g'),
	MEDIUM_GREEN('h'),
	MEDIUM_RED('i'),
	DARK_RED('j'),
	BROWN('k'),
	MEDIUM_ORANGE('l'),
	VERY_LIGHT_GREEN('m'),
	LIGHT_YELLOW('n'),
	DARK_YELLOW('o'),
	BLACK2('p'),
	DARK_RED2('q'),
	CYAN('r'),
	MEDIUM_YELLOW('s'),
	MEDIUM_BLUE('t'),
	VERY_LIGHT_BLUE('u'),
	LIGHT_PINK('v'),
	LIGHT_GREY('w'),
	DARK_ORANGE2('x'),
	DARK_GREY('y'),
	MEDIUM_GREY('z'),
	MEDIUM_GREY2(';'),
	LIGHT_ORANGE(','),
	LIGHT_YELLOW2('.'),
	MEDIUM_YELLOW2('/'),
	SORT_OF_GREEN('-'),
	SORT_OF_GREEN2('='),
	GREEN2('\\'),
	LIGHT_GREY2('['),
	SORT_OF_GREEN3(']'),
	LIGHT_CYAN('\''),
	DARK_RED3('*');

	private final static HashMap<Character, Q3Color> reverseTable; //we can use a map, there are no doubled chars
	private final char q3Code;

	/*
	 * Create the reversed table for the colorFromChar function 
	 */
	static {
		reverseTable = new HashMap<Character, Q3Color>();
		Q3Color[] allColors = values();
		for (int i = 0; i < allColors.length; i++) {
			reverseTable.put(allColors[i].q3Code, allColors[i]);
		}
	}

	/**
	 * Constructor of the color
	 * 
	 * @param code		code correspondant in Q3
	 */
	private Q3Color(char code) {
		q3Code = code;
	}

	/**
	 * Return the color code
	 * 
	 * @return		color code
	 */
	public String getColorCode() {
		return "^" + q3Code;
	}

	/**
	 * Return the Q3Color from the given char
	 * 
	 * @param c		char that identifies the Q3 color
	 * @return		the corresponding Q3 color
	 */
	public static Q3Color getColorFromChar(char c) {
		if (!reverseTable.containsKey(Character.toLowerCase(c))) {
			return Q3Color.WHITE;
		}
		return reverseTable.get(Character.toLowerCase(c));
	}
}