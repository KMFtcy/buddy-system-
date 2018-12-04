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
		for (Block block : cpu.getRecentUsedBlock()) {
			memoryBlockList.addBlock(block);
		}
		//����֮�󣬸���һ�´�������ʹ���ڴ�����Ϣ
		int usedAreaNum = cpu.getRecentUsedBlock().size();
		window.getUsedAreaNumLabel().setText(usedAreaNum + " ��");
		//���Ž������ڴ�����Ϣ�������������ڴ����Ϣ������
		ArrayList<Free_area_head> Free_area_list = cpu.getFree_area_list();
		JLabel[] freeAreaNumLabel = window.getFreeAreaNumLabel();
		for (int i = 0; i < Constant.ORDER; i++) {
			//�ڱ�����ͬʱ��˳�����һ�´��ڿ����ڴ�����Ϣ������һ�α���
			int size = (int) Math.pow(2, Free_area_list.get(i).order);
			freeAreaNumLabel[i].setText(Free_area_list.get(i).getNum() + " ��" + "(��СΪ" + size + "ҳ)");
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
			int size = block.getSize();
			JPanel newBlockPanel = new JPanel();
			newBlockPanel.setPreferredSize(new Dimension((int)size/10,120));
			newBlockPanel.setBackground(getColor(block.isUsed()));
			newBlockPanel.add(new JLabel(String.valueOf(block.getAddress())));
			window.getMemoryPanel().add(newBlockPanel);
		}
		//���´��ڵ�����
		window.getLeftArea().updateUI();
		window.getMemoryPanel().updateUI();
		//�ڴ��ڼ���������
		window.printlnConsole("ˢ�³ɹ���");
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
