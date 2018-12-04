package team.tw.newten.tang.command;

import team.tw.newten.tang.Block;
import team.tw.newten.tang.CPU;
import team.tw.newten.tang.Window;

public class ReleaseCommand implements Command {
	Window window;

	public ReleaseCommand(Window window) {
		this.window = window;
	}

	@Override
	public void execute(CPU cpu) {
		// 从输入框获取数字
		int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
		for (int i = 0; i < num; i++) {
			try {
				Block releaseBlock = cpu.release();
				window.printlnConsole(
						"释放成功:" + "释放内存块大小[" + releaseBlock.getSize() + "]" + "位置[" + releaseBlock.getAddress() + "]");
			} catch (Exception e) {
				window.printlnConsole("释放失败，" + e.getMessage() + "");
				e.printStackTrace();
				break;
			}
		}
		window.refreshInfo();
	}

	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub

	}

}
