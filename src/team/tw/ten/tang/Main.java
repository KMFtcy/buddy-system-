package team.tw.ten.tang;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(String[] args) {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Block> RAM = new ArrayList<Block>();
		RAM.add(new Block(256));
		RAM.add(new Block(128));
		RAM.add(new Block(64));
		RAM.add(new Block(64));
		Windows window = new Windows();
		Controller controller = new Controller(RAM);
		window.addObserver(controller);
		controller.addObserver(window);
		System.out.println(controller.RAM);
		while (true) {
			controller.test();
		}
	}
}
