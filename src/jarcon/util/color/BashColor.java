/**
 * Copyright (c) 2013-2014 Barto
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

package jarcon.util.color;

/**
 * Represent the colors in a shell prompt
 * 
 * @author Barto
 */
public enum BashColor {
	NONE("0"),
	BLACK("0;30"),
	DARK_GREY("1;30"),
	RED("0;31"),
	LIGHT_RED("1;31"),
	GREEN("0;32"),
	LIGHT_GREEN("1;32"),
	BROWN("0;33"),
	YELLOW("1;33"),
	BLUE("0;34"),
	LIGHT_BLUE("1;34"),
	PURPLE("0;35"),
	LIGHT_PURPLE("1;35"),
	CYAN("0;36"),
	LIGHT_CYAN("1;36"),
	LIGHT_GREY("0;37"),
	WHITE("1;37");

	private final String bashCode;

	/**
	 * Constructor of the color
	 * 
	 * @param bashCode		color correspondent in bash
	 */
	private BashColor(String bashCode) {
		this.bashCode = bashCode;
	}

	/**
	 * Return the bash code
	 * 
	 * @return		the current bash color code
	 */
	public String getBashCode() {
		return 	"\033[" + bashCode + "m";
	}
}
