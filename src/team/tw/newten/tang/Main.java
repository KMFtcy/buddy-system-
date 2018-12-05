package team.tw.newten.tang;

import java.util.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(String[] args) {
		ArrayList<Free_area_head> free_area_list =new ArrayList<Free_area_head>();
		CPU cpu = new CPU(free_area_list);
		Window window = new Window();
		cpu.addObserver(window);
		window.addObserver(cpu);
		System.out.println("Æô¶¯Íê±Ï");
	}
	
}
