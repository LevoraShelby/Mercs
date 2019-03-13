package test.player;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Consumer;
import java.util.function.Function;



public class CurryTest implements Function<String, Consumer<Integer>> {
	public Consumer<Integer> apply(String t) {
		return num -> {
			System.out.println(t + num);
		};
	}


	public static void main(String[] args) {
		char c = ' ';
		Reader in = System.console().reader();
		try {
			while(c != 'q') {
				c = (char) in.read();
				System.out.println(c);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
