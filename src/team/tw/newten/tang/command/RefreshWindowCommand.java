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
 * �ɴ��ڷ��͸�cpuҪ����´�����Ϣ������
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
		//���������˼·���Ƕ���cpu����ʹ���ڴ�顢�����ڴ���������Ϣ���ڴ��ڽ����Ǹ��£�ͬʱ��϶��ߵĵ�ַ��Ϣ�ڴ��ڻ����ڴ�״̬ͼ
		Free_area_head memoryBlockList = new Free_area_head(0);//���������ڴ������ڴ����Ϣ
		//���Ƚ���ʹ���ڴ�����Ϣ�������������ڴ����Ϣ������
		int[] usedBlockNumList = new int[Constant.ORDER];
		for (int i : usedBlockNumList) {
			i = 0;
		}
		int x = 0;
		for (Block block : cpu.getRecentUsedBlock()) {
			//ÿ�ζ�����ʱ��˳�����һ��ʹ�ÿ����Ϣ
			int order = calOrder(block.getSize());
			usedBlockNumList[order] ++; 
			memoryBlockList.addBlock(block);
			x++;
		}
		//���Ž������ڴ�����Ϣ�������������ڴ����Ϣ������
		ArrayList<Free_area_head> Free_area_list = cpu.getFree_area_list();
		JLabel[] freeAreaNumLabel = window.getFreeAreaNumLabel();
		JLabel[] usedAreaNumLabel = window.getUsedAreaNumLabel();
		for (int i = 0; i < Constant.ORDER; i++) {
			//�ڱ�����ͬʱ��˳�����һ�´��ڿ��к�ռ���ڴ�����Ϣ������һ�α���
			freeAreaNumLabel[i].setText(Free_area_list.get(i).getNum() + " ��");
			usedAreaNumLabel[i].setText(usedBlockNumList[i] + " ��");
			for (Block block : Free_area_list.get(i).getBlockList()) {
				memoryBlockList.addBlock(block);
			}
		}
		//����һ�´����ж�ѡ����ڴ���Ϣ
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
		//�����ڴ���
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
		//���´��ڵ�����
		window.getLeftArea().updateUI();
		window.getMemoryPanel().updateUI();
		//�ڴ��ڼ���������
		window.printlnConsole("ˢ�³ɹ���");
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
