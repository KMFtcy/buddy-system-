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
			//从输入框获取数字
			int num = Integer.parseInt(window.getOperateBlockNumTextField().getText());
			for (int i = 0; i < num; i++) {
				int allocBlockSize = (int)(Math.random()*1023+1);
//				window.printConsole("开始处理第" + (i+1) + "个申请请求，");
//				window.printConsole("申请大小为" + allocBlockSize +"个页");
				try {
					cpu.allocate(allocBlockSize);
					window.printlnConsole("分配成功！" +  "申请大小为" + allocBlockSize +"个页");
				} catch (Exception e) {
					e.printStackTrace();
					window.printlnConsole("分配失败：" + e.getMessage() + ":" +  "申请大小为" + allocBlockSize +"个页");
				}
			}
			window.refreshInfo();
	}

	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub
		
	}

}
