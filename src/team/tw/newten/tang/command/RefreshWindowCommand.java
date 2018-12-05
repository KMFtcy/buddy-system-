package team.tw.newten.tang.command;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
		int[] usedBlockNumList = new int[Constant.ORDER];
		for (int i : usedBlockNumList) {
			i = 0;
		}
		int x = 0;
		for (Block block : cpu.getRecentUsedBlock()) {
			//每次读出的时候，顺便更新一下使用块的信息
			int order = calOrder(block.getSize());
			usedBlockNumList[order] ++; 
			memoryBlockList.addBlock(block);
			x++;
		}
		//接着将空闲内存块的信息读出，填入总内存块信息的数组
		ArrayList<Free_area_head> Free_area_list = cpu.getFree_area_list();
		JLabel[] freeAreaNumLabel = window.getFreeAreaNumLabel();
		JLabel[] usedAreaNumLabel = window.getUsedAreaNumLabel();
		for (int i = 0; i < Constant.ORDER; i++) {
			//在遍历的同时，顺便更新一下窗口空闲和占用内存块的信息，减少一次遍历
			freeAreaNumLabel[i].setText(Free_area_list.get(i).getNum() + " 块");
			usedAreaNumLabel[i].setText(usedBlockNumList[i] + " 块");
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
			int blockSize = block.getSize();
			JPanel newBlockPanel = new JPanel();
			newBlockPanel.setPreferredSize(new Dimension(80-(int)(1000/blockSize),120));
			newBlockPanel.setBackground(getColor(block.isUsed()?blockSize:-1));
			newBlockPanel.setLayout(new GridLayout(5, 1));
			JLabel sizeInfo = new JLabel(String.valueOf(blockSize));
			JLabel adressInfo = new JLabel(String.valueOf(block.getAddress()));
			newBlockPanel.add(sizeInfo);
			newBlockPanel.add(adressInfo);
			window.getMemoryPanel().add(newBlockPanel);
		}
		//更新窗口的左栏
		window.getLeftArea().updateUI();
		window.getMemoryPanel().updateUI();
		//在窗口监视器提醒
		window.printlnConsole("刷新成功！");
	}
	private Color getColor(int size) {
		switch (size) {
		case -1:
			return new Color(161, 175, 201);
		default:
			return new Color(51, 153, 204);
		}
	}
	private int calOrder(int size) {
		int order = 0;
		while (Math.pow(2, (int) order) < size) {
			order++;
		}
		return order;
	}
	@Override
	public void execute(Window window) {
		// TODO Auto-generated method stub
	}

}
