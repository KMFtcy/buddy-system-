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
		//重新设置Constant中规定的大小
		int selectedMemorySizeIndex = window.getMemorySizeComboBox().getSelectedIndex();
		int selectedPageSizeIndex = window.getPageSizeComboBox().getSelectedIndex();
		Constant.MEMORY_SIZE = Constant.MEMORY_SIZE_OPTION[selectedMemorySizeIndex];
		Constant.PAGE_SIZE = Constant.PAGE_SIZE_OPTION[selectedPageSizeIndex];
		//在窗口监视器提示
		window.printlnConsole("\n重置成功，内存总大小改为：" +Constant.MEMORY_SIZE + "页大小改为：" + Constant.PAGE_SIZE);
		//刷新
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
