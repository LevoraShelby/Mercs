package console;



/**
 * A component for console UIs that display a list of Strings where one of them
 * is "selected".
 * @author trevor
 */
public class SelectionView {
	private static final String SGR_SELECTED = "\u001b[7m";
	private static final String SGR_RESET = "\u001b[m";

	private final String[] selections;
	private final int selectedIndex;
	private final int width;


	/**
	 * @param selections The list of outputs for this display. (non-null)
	 * @param selectedIndex The index in these outputs to show as "selected".
	 * @param width The number of characters long this component should be.
	 */
	public SelectionView(
		String[] selections, int selectedIndex, int width
	) {
		handleExceptions(selections);

		this.selections = selections.clone();
		this.selectedIndex = selectedIndex;
		this.width = width;
	}


	/**
	 * @return An array of strings, one for each line, to be displayed.
	 */
	public String[] display() {
		String[] display = new String[selections.length];

		for(int i = 0; i < selections.length; i++) {
			String line = selections[i];
			if(i == selectedIndex) {
				line = SGR_SELECTED + line + SGR_RESET;
			}

			for(int j = width - selections[i].length(); j > 0; j--) {
				line += " ";
			}
			display[i] = line;
		}

		return display;
	}


	/**
	 * @param selectedIndex The new element to be selected.
	 * @return A new display with a different selected element. If the element
	 * to be selected was the same as the last, this method will return the
	 * object that called it for quicker performance.
	 */
	public SelectionView select(int selectedIndex) {
		if(this.selectedIndex == selectedIndex) {
			return this;
		}
		return new SelectionView(selections, selectedIndex, width);
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(String[] selections) {
		if(selections == null) {
			throw new NullPointerException(
				"selection cannot be null."
			);
		}
		for(int i = 0; i < selections.length; i++) {
			if(selections[i] == null) {
				throw new NullPointerException(
					"element in selection at index " + i + " is null."
				);
			}
		}
	}
}
