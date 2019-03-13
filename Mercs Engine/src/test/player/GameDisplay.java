package test.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;



public class GameDisplay extends JTextArea {
	private static final long serialVersionUID = -7498727892886155678L;


	public GameDisplay(char[][] view) {
		super();

		String text = "";
		for(char[] line : view) {
			text += line.toString() + '\n';
		}
		text = text.substring(0, text.length() - 1);
		setText("testing testing testing testing");
		setEditable(false);

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		setFont(font);

		setPreferredSize(new Dimension(500, 300));
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
	}


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		char[][] view = {{}};
		GameDisplay display = new GameDisplay(view);
		panel.add(display);
		
		panel.setPreferredSize(new Dimension(500, 300));
		frame.setContentPane(panel);
		frame.getContentPane();
		frame.setPreferredSize(new Dimension(500, 300));

		frame.setBackground(Color.BLACK);
		frame.pack();
		frame.setVisible(true);
	}
}
