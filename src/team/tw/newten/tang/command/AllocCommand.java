package team.tw.newten.tang.command;

import javax.swing.JOptionPane;

import team.tw.newten.tang.CPU;
import team.tw.newten.tang.Constant;
import team.tw.newten.tang.Window;

public class AllocCommand implements Command, Runnable {
	Window window;
	CPU cpu;
	public static Object obj = new Object();

	public AllocCommand(Window window) {
		this.window = window;
	}

	@Override
	public void execute(CPU cpu) {
		this.cpu = cpu;
		new Thread(this).start();
	}

	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// 从输入框获取数字
		int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
		for (int i = 0; i < num; i++) {
			int allocBlockSize = (int) (Math.random() * 1023 + 1);
//			window.printConsole("开始处理第" + (i+1) + "个申请请求，");
			window.printlnConsole("申请大小为" + allocBlockSize + "个页 ");
			waitContinue();
			try {
				cpu.allocate(allocBlockSize);
				window.printConsole("分配成功！");
			} catch (Exception e) {
				e.printStackTrace();
				window.printlnConsole("分配失败：" + e.getMessage() + ":" + "申请大小为" + allocBlockSize + "个页");
			}
			window.refreshInfo();
			waitContinue();
		}
	}

	private void waitContinue() {
		if (Constant.isSingleStep) {
			try {
				synchronized (obj) {
					obj.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
