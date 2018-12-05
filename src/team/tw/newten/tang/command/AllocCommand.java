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
		// ��������ȡ����
		int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
		for (int i = 0; i < num; i++) {
			int allocBlockSize = (int) (Math.random() * 1023 + 1);
//			window.printConsole("��ʼ�����" + (i+1) + "����������");
			window.printlnConsole("�����СΪ" + allocBlockSize + "��ҳ ");
			waitContinue();
			try {
				cpu.allocate(allocBlockSize);
				window.printConsole("����ɹ���");
			} catch (Exception e) {
				e.printStackTrace();
				window.printlnConsole("����ʧ�ܣ�" + e.getMessage() + ":" + "�����СΪ" + allocBlockSize + "��ҳ");
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
