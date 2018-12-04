package team.tw.newten.tang.command;

import javax.swing.JOptionPane;

import team.tw.newten.tang.CPU;
import team.tw.newten.tang.Window;

public class AllocCommand implements Command{
	Window window;
	public AllocCommand(Window window){
		this.window = window;
	}
	@Override
	public void execute(CPU cpu) {
			//��������ȡ����
			int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
			for (int i = 0; i < num; i++) {
				int allocBlockSize = (int)(Math.random()*1023+1);
//				window.printConsole("��ʼ�����" + (i+1) + "����������");
//				window.printConsole("�����СΪ" + allocBlockSize +"��ҳ");
				try {
					cpu.allocate(allocBlockSize);
					window.printlnConsole("����ɹ���" +  "�����СΪ" + allocBlockSize +"��ҳ");
				} catch (Exception e) {
					e.printStackTrace();
					window.printlnConsole("����ʧ�ܣ�" + e.getMessage() + ":" +  "�����СΪ" + allocBlockSize +"��ҳ");
				}
			}
			window.refreshInfo();
	}

	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub
		
	}

}
