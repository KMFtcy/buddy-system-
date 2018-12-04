package team.tw.ten.tang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class Windows extends Observable implements Observer {

	JButton requestButton;
	JButton releaseButton;
	Container container;
	JFrame frame;
	JPanel memoryPanel;
	JTextField memorySize;
	JTextField memoryBlockSize;
	JTextArea console;

	Windows() {
		setComponent();
		frame.setSize(1400, 600);
//		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void setComponent() {
		initComponent();
		//������򣬷��ò������õ��޸Ŀ����Ϸ��ڴ�����guiʾ��ͼ�����·�console������ͣ�ȹ��ܰ�ť
		JPanel leftArea = new JPanel();
		JPanel rightArea = new JPanel();
		JPanel rightUpArea = new JPanel();
		JPanel rightBottomArea = new JPanel();
		JPanel bottomArea = new JPanel();
		//����������layout
		leftArea.setLayout(new GridLayout(10, 2));
		rightArea.setLayout(new BorderLayout());
		rightUpArea.setLayout(new BorderLayout());
		//�������ӽ�����
		leftArea.add(new JLabel("�ڴ��С"));
		leftArea.add(memorySize);
		leftArea.add(new JLabel("�ڴ���С"));
		leftArea.add(memoryBlockSize);
		rightBottomArea.add(new JScrollPane(console));
		bottomArea.add(requestButton);
		bottomArea.add(releaseButton);
		//�������
		rightUpArea.add(memoryPanel,BorderLayout.CENTER);
		rightArea.add(rightUpArea,BorderLayout.CENTER);
		rightArea.add(rightBottomArea,BorderLayout.SOUTH);
		container.add(leftArea, BorderLayout.WEST);
		container.add(rightArea, BorderLayout.CENTER);
		//container.add(bottomArea, BorderLayout.SOUTH);
		//container.add(memoryPanel, BorderLayout.CENTER);
		JLabel titleLabel = new JLabel("ʵ��ʮ�������㷨ģ��");
		container.add(titleLabel, BorderLayout.NORTH);
	}

	private void initComponent() {
		frame = new JFrame();
		memoryPanel = new JPanel();
		// ȡ����������
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		// ��ʼ�������趨�����
		memorySize = new JTextField(10);
		memorySize.setText(String.valueOf(Constant.MAX_STORAGE));
		memorySize.setEditable(false);
		memoryBlockSize = new JTextField(10);
		memoryBlockSize.setText("1");
		//��ʼ��������
		console = new JTextArea(8,40);
		console.setEditable(false);
		console.setFont(new Font("΢���ź�",0,25));
		// ��ʼ����ť
		requestButton = new JButton("���������Դ");
		releaseButton = new JButton("����ͷ���Դ");
		requestButton.setPreferredSize(new Dimension(120, 50));
		releaseButton.setPreferredSize(new Dimension(120, 50));
		requestButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setChanged();
				notifyObservers();
			}
		});
		releaseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("�ͷ���Դ");
			}
		});

		JPanel firstBlock = new JPanel();
		firstBlock.setPreferredSize(new Dimension(1024, 150));
		firstBlock.setBackground(new Color(255, 156, 156));
		firstBlock.setLayout(new BorderLayout());
		JLabel BlockInfo = new JLabel("��ʼ��", JLabel.CENTER);
		BlockInfo.setFont(new Font("΢���ź�", Font.BOLD, 40));
		firstBlock.add(BlockInfo, BorderLayout.CENTER);
		memoryPanel.add(firstBlock);
	}

	/**
	 * ���Ǳ��۲��߸��µĺ���
	 * �����������º��ڴ��֪ͨ���ڸ���
	 */
	@Override
	public void update(Observable o, Object arg) {
		MessagePackage result = (MessagePackage) arg;
		if (!result.isSucceed) {
			JOptionPane.showMessageDialog(null, "�޷�����", "ע�⣡", JOptionPane.INFORMATION_MESSAGE);
		} else {
			ArrayList<Block> RAM = result.getRAM();
			refresh(RAM);
			console.append(result.getMessage() + '\n');
			console.setCaretPosition(console.getText().length());
		}

	}

	private Color getBlockColor(int storage) {
		switch (storage) {
		case -1:
			return new Color(161, 175, 201);
		case Constant.MAX_STORAGE:
			return new Color(0, 52, 114);
		case Constant.MAX_STORAGE / 2:
			return new Color(6, 82, 121);
		case Constant.MAX_STORAGE / 4:
			return new Color(22, 133, 169);
		case Constant.MAX_STORAGE / 8:
			return new Color(62, 237, 231);
		case Constant.MAX_STORAGE / 16:
			return new Color(68, 206, 246);
		case Constant.MAX_STORAGE / 32:
			return new Color(112, 243, 255);
		default:
			return new Color(255, 255, 255);
		}
	}

	private void refresh(ArrayList<Block> RAM) {
		memoryPanel.removeAll();
		for (Block i : RAM) {
			JPanel newBlock = new JPanel();
			newBlock.setPreferredSize(new Dimension(i.getStorage() * 2, 150));
			if (i.isUsed()) {
				newBlock.setBackground(getBlockColor(-1));
			} else {
				newBlock.setBackground(getBlockColor(i.getStorage()));
			}
			newBlock.setLayout(new BorderLayout());
			JLabel BlockInfo = null;
			BlockInfo = new JLabel(String.valueOf(i.getStorage()) + "bytes", JLabel.CENTER);
			BlockInfo.setFont(new Font("΢���ź�", Font.BOLD, 20));
			newBlock.add(BlockInfo);
			memoryPanel.add(newBlock);
		}
		memoryPanel.updateUI();
	}
}
