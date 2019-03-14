package display;

import java.awt.Color;



/**
 * A string that prints out with graphical effects. These effects can include a
 * special foreground color, a special background color, a boldening effect,
 * and a "blinking" effect. Information on how this is done can be found here:
 * https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters
 * @author trevor
 */
public final class SgrString {
	private final String base;
	private final Color foreground;
	private final Color background;
	private final Boolean bold;
	private final Boolean blinking;


	/**
	 * @param base The string that SgrString will place SGR effects on.
	 * @param foreground The color of the base text. If null, the color of the
	 * base text is not changed.
	 * @param background The color behind the base text. If null, the color 
	 * behind the base text is not changed.
	 * @param bold Whether or not the base text will be bold. If null, the
	 * bold effect is not changed.
	 * @param blinking Whether or not the base text will be blinking. If
	 * null, the blinking effect is not changed.
	 */
	public SgrString(
		String base,
		Color foreground, Color background, 
		Boolean bold, Boolean blinking
	) {
		this.base = base;
		this.foreground = foreground;
		this.background = background;
		this.bold = bold;
		this.blinking = blinking;
	}


	/**
	 * Constructs an SgrString without bold and blinking effects.
	 * @param base
	 * @param foreground
	 * @param background
	 */
	public SgrString(String base, Color foreground, Color background) {
		this(base, foreground, background, null, null);
	}


	/**
	 * Constructs an SgrString with white foreground, black foreground, and
	 * without bold and blinking effects.
	 * @param base
	 */
	public SgrString(String base) {
		this(base, null, null);
	}


	/**
	 * @return The base string with the SGR effects.
	 */
	public String toString() {
		final String CSI = "\u001b[";

		String toString = "";
		if(foreground != null) {
			toString += CSI + "38;2;" + sgrArgsForColor(foreground) + "m";
		}
		if(background != null) {
			toString += CSI + "48;2;" + sgrArgsForColor(background) + "m";
		}

		if(bold != null && bold) {
			toString += CSI + "1m";
		}
		else if(bold != null && !bold) {
			toString += CSI + "21m";
		}

		if(blinking != null && blinking) {
			toString += CSI + "5m";
		}
		else if(blinking != null && !blinking) {
			toString += CSI + "25m";
		}

		toString += base;
		return toString;
	}


	private static String sgrArgsForColor(Color c) {
		return String.valueOf(c.getRed()) + ";" +
			String.valueOf(c.getGreen()) + ";" +
			String.valueOf(c.getBlue());
	}


	/**
	 * @return The string that SgrString places effects on.
	 */
	public String base() {
		return base;
	}


	/**
	 * @return The color of the base text. If null, the color of the base text
	 * will not be changed.
	 */
	public Color foreground() {
		return foreground;
	}


	/**
	 * @return Equivalent to foreground().
	 */
	public Color fg() {
		return foreground;
	}


	/**
	 * @return The color behind the base text. If null, the color behind the
	 * base text is not changed.
	 */
	public Color background() {
		return background;
	}


	/**
	 * @return Equivalent to background().
	 */
	public Color bg() {
		return background;
	}


	/**
	 * @return Whether or not the base text will be bold. If null, the bold
	 * effect will not be changed.
	 */
	public Boolean bold() {
		return bold;
	}


	/**
	 * @return Whether or not the base text will be bold. If null, the blinking
	 * effect will not be changed.
	 */
	public Boolean blinking() {
		return blinking;
	}


	public SgrString changeBase(String base) {
		if(this.base.equals(base)) {
			return new SgrString(base, foreground, background, bold, blinking);
		}
		return this;
	}


	public SgrString changeForeground(Color foreground) {
		if(this.foreground != null || this.foreground.equals(foreground)) {
			return new SgrString(base, foreground, background, bold, blinking);
		}
		return this;
	}


	public SgrString changeFG(Color foreground) {
		return changeForeground(foreground);
	}


	public SgrString changeBackground(Color background) {
		if(this.background !=null || this.background.equals(background)) {
			return new SgrString(base, foreground, background, bold, blinking);
		}
		return this;
	}


	public SgrString changeBG(Color background) {
		return changeBackground(background);
	}


	public SgrString changeBold(Boolean bold) {
		if(this.bold != null || this.bold.equals(bold)) {
			return new SgrString(base, foreground, background, bold, blinking);
		}
		return this;
	}


	public SgrString changeBlinking(Boolean blinking) {
		if(this.blinking != null || this.blinking.equals(blinking)) {
			return new SgrString(base, foreground, background, bold, blinking);
		}
		return this;
	}
}
