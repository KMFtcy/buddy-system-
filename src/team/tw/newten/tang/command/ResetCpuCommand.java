package team.tw.newten.tang.command;

import team.tw.newten.tang.CPU;
import team.tw.newten.tang.Constant;
import team.tw.newten.tang.Window;

public class ResetCpuCommand implements Command{
	Window window;
	public ResetCpuCommand(Window window){
		this.window = window;
	}
	@Override
	public void execute(CPU cpu) {
		//��������Constant�й涨�Ĵ�С
		int selectedMemorySizeIndex = window.getMemorySizeComboBox().getSelectedIndex();
		int selectedPageSizeIndex = window.getPageSizeComboBox().getSelectedIndex();
		Constant.MEMORY_SIZE = Constant.MEMORY_SIZE_OPTION[selectedMemorySizeIndex];
		Constant.PAGE_SIZE = Constant.PAGE_SIZE_OPTION[selectedPageSizeIndex];
		//�ڴ��ڼ�������ʾ
		window.printlnConsole("\n���óɹ����ڴ��ܴ�С��Ϊ��" +Constant.MEMORY_SIZE + "ҳ��С��Ϊ��" + Constant.PAGE_SIZE);
		//ˢ��
		cpu.refresh();
		window.refreshInfo();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub
		
	}
	
}
