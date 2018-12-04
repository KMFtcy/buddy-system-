package team.tw.newten.tang.command;

import team.tw.newten.tang.*;

public interface Command {
	public default void execute() {
		System.out.println("ÎŞÃüÁî");
	};
	public void execute(CPU cpu);
	public void execute(Window window);
}
