package test.player;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;



/**
 * Represents a singular character preceded and followed by a set of Select
 * Graphic Rendition control sequences. Read https://en.wikipedia.org/w/
 * index.php?title=ANSI_escape_code&oldid=883006508#Escape_sequences for more
 * information on this topic. In effect, an object of this class represents a
 * singular character that when printed by a console, will have an affected
 * appearance.
 * @author trevor
 */
public class SgrCharacter {
	//General Text Effects
	public static final int RESET = 0;
	public static final int BOLD = 1;
	public static final int FAINT = 2;
	public static final int ITALIC = 3;
	public static final int UNDERLINE = 4;
	public static final int SLOW_BLINK = 5;
	public static final int FAST_BLINK = 6;
	public static final int REVERSE = 7;
	public static final int CONCEAL = 8;
	public static final int X_OUT = 9;
	public static final int FRAMED = 51;
	public static final int CIRCLED = 52;
	public static final int OVERLINE = 53;

	//Font Effects
	public static final int DEFAULT_FONT = 10;
	public static final int ALT_FONT_1 = 11;
	public static final int ALT_FONT_2 = 12;
	public static final int ALT_FONT_3 = 13;
	public static final int ALT_FONT_4 = 14;
	public static final int ALT_FONT_5 = 15;
	public static final int ALT_FONT_6 = 16;
	public static final int ALT_FONT_7 = 17;
	public static final int ALT_FONT_8 = 18;
	public static final int ALT_FONT_9 = 19;
	public static final int FRAKTUR = 20;
	public static final int UNDERLINE_2X = 21;

	//General Text Effects Off
	public static final int BOLD_OFF = 21;
	public static final int COLOR_OFF = 22;
	public static final int ITALIC_OFF = 23;
	public static final int FRAKTUR_OFF = 23;
	public static final int UNDERLINE_OFF = 24;
	public static final int BLINK_OFF = 25;
	public static final int REVERSE_OFF = 27;
	public static final int CONCEAL_OFF = 28;
	public static final int X_OUT_OFF = 29;
	public static final int FRAMED_OFF = 54;
	public static final int CIRCLED_OFF = 54;
	public static final int OVERLINE_OFF = 55;

	//Foreground Color Effects (note: bright colors are not standard.)
	public static final int SET_FG = 38;
	public static final int FG_DEFAULT = 39;
	public static final int FG_BLACK = 30;
	public static final int FG_RED = 31;
	public static final int FG_GREEN = 32;
	public static final int FG_YELLOW = 33;
	public static final int FG_BLUE = 34;
	public static final int FG_MAGENTA = 35;
	public static final int FG_CYAN = 36;
	public static final int FG_WHITE = 37;
	public static final int FG_BR_BLACK = 90;
	public static final int FG_BR_RED = 91;
	public static final int FG_BR_GREEN = 92;
	public static final int FG_BR_YELLOW = 93;
	public static final int FG_BR_BLUE = 94;
	public static final int FG_BR_MAGENTA = 95;
	public static final int FG_BR_CYAN = 96;
	public static final int FG_BR_WHITE = 97;

	//Background Color Effects (note: bright colors are not standard.)
	public static final int SET_BG = 48;
	public static final int BG_DEFAULT = 49;
	public static final int BG_BLACK = 40;
	public static final int BG_RED = 41;
	public static final int BG_GREEN = 42;
	public static final int BG_YELLOW = 43;
	public static final int BG_BLUE = 44;
	public static final int BG_MAGENTA = 45;
	public static final int BG_CYAN = 46;
	public static final int BG_WHITE = 47;
	public static final int BG_BR_BLACK = 100;
	public static final int BG_BR_RED = 101;
	public static final int BG_BR_GREEN = 102;
	public static final int BG_BR_YELLOW = 103;
	public static final int BG_BR_BLUE = 104;
	public static final int BG_BR_MAGENTA = 105;
	public static final int BG_BR_CYAN = 106;
	public static final int BG_BR_WHITE = 107;

	//The control sequence introducer
	private static final String CSI = "\u001b" + "[";

	private final char character;
	private final int[][] argsForPrecedingSgrSeqs;
	private final int[][] argsForFollowingSgrSeqs;


	/**
	 * @param character The base character for this SgrCharacter.
	 * @param argsForPrecedingSgrSeqs Arguments for each SGR sequence that
	 * will affect the base character when this SgrCharacter is printed.
	 * @param argsForFollowingSgrSeqs Arguments for each SGR sequence that
	 * will affect all characters being printed after this SgrCharacter is.
	 * Normally used to reset back to a "default" state.
	 */
	public SgrCharacter(
		char character,
		int[][] argsForPrecedingSgrSeqs,
		int[][] argsForFollowingSgrSeqs
	) {
		handleExceptions(argsForPrecedingSgrSeqs, argsForFollowingSgrSeqs);

		this.character = character;
		this.argsForPrecedingSgrSeqs = 
			argsForPrecedingSgrSeqs.clone();
		this.argsForFollowingSgrSeqs =
			argsForFollowingSgrSeqs.clone();
	}


	/**
	 * Constructs an SgrCharacter with graphic effects that linger after it's
	 * printed. Normally used when printing a string of SgrCharacters, where
	 * having SGR sequences that reset the console's printing graphics after
	 * each character is printed would be redundant, as the next SgrCharacter
	 * would set those graphics to its own right after.
	 * @param character
	 * @param argsForPrecedingSgrSeqs
	 */
	public SgrCharacter(char character, int[][] argsForPrecedingSgrSeqs) {
		this(character, argsForPrecedingSgrSeqs, new int[0][]);
	}


	/**
	 * Constructs an SgrCharacter with no graphic effects.
	 * @param character
	 */
	public SgrCharacter(char character) {
		this(character, new int[0][], new int[0][]);
	}



	public String toString() {
		String toString = "";
		toString += sgrSequencesFromArguments(argsForPrecedingSgrSeqs);
		toString += character;
		toString += sgrSequencesFromArguments(argsForFollowingSgrSeqs);
		return toString;
	}


	/**
	 * @param argsForEachSgrSeq The arguments for each SGR sequence.
	 * @return A string of SGR sequences.
	 */
	private static String sgrSequencesFromArguments(
		int[][] argsForEachSgrSeq
	) {
		String sgrSeqs = "";
		for(int[] sgrSeqArgs : argsForEachSgrSeq) {
			sgrSeqs += CSI;
			sgrSeqs += Arrays.stream(sgrSeqArgs)
				.mapToObj(arg -> String.valueOf(arg))
				.collect(Collectors.joining(";")
			);
			sgrSeqs += "m";
		}
		return sgrSeqs;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		int[][] argsForPrecedingSgrSeqs,
		int[][] argsForFollowingSgrSeqs
	) {
		if(argsForPrecedingSgrSeqs == null) {
			throw new NullPointerException(
				"argsForPrecedingSgrSeqs is null."
			);
		}
		if(argsForFollowingSgrSeqs == null) {
			throw new NullPointerException(
				"argsForFollowingSgrSeqs is null."
			);
		}
	}


	public static void main(String[] args) {
		int[][][] bombArgs = {
			{{SgrCharacter.FG_BR_BLACK}, {SgrCharacter.BG_WHITE}},
			{{SgrCharacter.FG_RED}, {SgrCharacter.BG_WHITE}},
			{{SgrCharacter.SET_FG, 5, 196}, {SgrCharacter.BG_WHITE},
				{SgrCharacter.SLOW_BLINK}},
			{{SgrCharacter.BG_RED}}
		};
		SgrCharacter[] bombDemo = {
			new SgrCharacter('q', bombArgs[0]),
			new SgrCharacter('q', bombArgs[1]),
			new SgrCharacter('o', bombArgs[2]),
			new SgrCharacter(' ', bombArgs[3])
		};

		System.out.println();
		for(SgrCharacter c : bombDemo) {
			System.out.println(c + CSI + RESET + "m");
			System.out.println();
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {}
			System.out.print(CSI + "A" + CSI + "A" + CSI + "J");
		}
	}
}
