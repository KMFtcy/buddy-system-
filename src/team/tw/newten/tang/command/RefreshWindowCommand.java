package team.tw.newten.tang.command;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.javafx.event.RedirectedEvent;

import team.tw.newten.tang.Block;
import team.tw.newten.tang.CPU;
import team.tw.newten.tang.Constant;
import team.tw.newten.tang.Free_area_head;
import team.tw.newten.tang.Window;

/**
 * 由窗口发送给cpu要求更新窗口信息的命令
 * 
 * @author KMFtang
 *
 */
public class RefreshWindowCommand implements Command {
	Window window;

	public RefreshWindowCommand(Window window) {
		this.window = window;
	}

	@Override
	public void execute(CPU cpu) {
		//这个函数的思路，是读出cpu中已使用内存块、空闲内存块的数量信息，在窗口将他们更新，同时组合二者的地址信息在窗口画出内存状态图
		Free_area_head memoryBlockList = new Free_area_head(0);//这是整个内存条的内存块信息
		//首先将已使用内存块的信息读出，填入总内存块信息的数组
		for (Block block : cpu.getRecentUsedBlock()) {
			memoryBlockList.addBlock(block);
		}
		//在这之后，更新一下窗口中已使用内存块的信息
		int usedAreaNum = cpu.getRecentUsedBlock().size();
		window.getUsedAreaNumLabel().setText(usedAreaNum + " 块");
		//接着将空闲内存块的信息读出，填入总内存块信息的数组
		ArrayList<Free_area_head> Free_area_list = cpu.getFree_area_list();
		JLabel[] freeAreaNumLabel = window.getFreeAreaNumLabel();
		for (int i = 0; i < Constant.ORDER; i++) {
			//在遍历的同时，顺便更新一下窗口空闲内存块的信息，减少一次遍历
			int size = (int) Math.pow(2, Free_area_list.get(i).order);
			freeAreaNumLabel[i].setText(Free_area_list.get(i).getNum() + " 块" + "(大小为" + size + "页)");
			for (Block block : Free_area_list.get(i).getBlockList()) {
				memoryBlockList.addBlock(block);
			}
		}
		//更新一下窗口中多选框的内存信息
		for (int i = 0; i < Constant.MEMORY_SIZE_OPTION.length; i++) {
			if(Constant.MEMORY_SIZE_OPTION[i] == Constant.MEMORY_SIZE) {
				window.getMemorySizeComboBox().setSelectedIndex(i);
				break;
			}
		}
		for (int i = 0; i < Constant.PAGE_SIZE_OPTION.length; i++) {
			if(Constant.PAGE_SIZE_OPTION[i] == Constant.PAGE_SIZE) {
				window.getPageSizeComboBox().setSelectedIndex(i);
				break;
			}
		}
		//绘制内存条
		window.getMemoryPanel().removeAll();
		for (Block block : memoryBlockList.getBlockList()) {
			int size = block.getSize();
			JPanel newBlockPanel = new JPanel();
			newBlockPanel.setPreferredSize(new Dimension((int)size/10,120));
			newBlockPanel.setBackground(getColor(block.isUsed()));
			newBlockPanel.add(new JLabel(String.valueOf(block.getAddress())));
			window.getMemoryPanel().add(newBlockPanel);
		}
		//更新窗口的左栏
		window.getLeftArea().updateUI();
		window.getMemoryPanel().updateUI();
		//在窗口监视器提醒
		window.printlnConsole("刷新成功！");
	}
	private Color getColor(boolean isUsed) {
		if(isUsed) {
			return new Color(62, 237, 231);
		}else {
			return new Color(255,255,224);
		}
	}
	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub
	}

}
