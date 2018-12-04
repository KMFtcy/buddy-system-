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
		// ��������ȡ����
		int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
		for (int i = 0; i < num; i++) {
			try {
				Block releaseBlock = cpu.release();
				window.printlnConsole(
						"�ͷųɹ�:" + "�ͷ��ڴ���С[" + releaseBlock.getSize() + "]" + "λ��[" + releaseBlock.getAddress() + "]");
			} catch (Exception e) {
				window.printlnConsole("�ͷ�ʧ�ܣ�" + e.getMessage() + "");
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
