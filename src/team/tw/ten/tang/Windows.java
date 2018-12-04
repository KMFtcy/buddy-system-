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
		//左边区域，放置参数设置的修改框，右上放内存条的gui示意图，右下放console还有暂停等功能按钮
		JPanel leftArea = new JPanel();
		JPanel rightArea = new JPanel();
		JPanel rightUpArea = new JPanel();
		JPanel rightBottomArea = new JPanel();
		JPanel bottomArea = new JPanel();
		//设置容器的layout
		leftArea.setLayout(new GridLayout(10, 2));
		rightArea.setLayout(new BorderLayout());
		rightUpArea.setLayout(new BorderLayout());
		//将组件添加进容器
		leftArea.add(new JLabel("内存大小"));
		leftArea.add(memorySize);
		leftArea.add(new JLabel("内存块大小"));
		leftArea.add(memoryBlockSize);
		rightBottomArea.add(new JScrollPane(console));
		bottomArea.add(requestButton);
		bottomArea.add(releaseButton);
		//组合容器
		rightUpArea.add(memoryPanel,BorderLayout.CENTER);
		rightArea.add(rightUpArea,BorderLayout.CENTER);
		rightArea.add(rightBottomArea,BorderLayout.SOUTH);
		container.add(leftArea, BorderLayout.WEST);
		container.add(rightArea, BorderLayout.CENTER);
		//container.add(bottomArea, BorderLayout.SOUTH);
		//container.add(memoryPanel, BorderLayout.CENTER);
		JLabel titleLabel = new JLabel("实验十：伙伴堆算法模拟");
		container.add(titleLabel, BorderLayout.NORTH);
	}

	private void initComponent() {
		frame = new JFrame();
		memoryPanel = new JPanel();
		// 取得内容容器
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		// 初始化参数设定输入框
		memorySize = new JTextField(10);
		memorySize.setText(String.valueOf(Constant.MAX_STORAGE));
		memorySize.setEditable(false);
		memoryBlockSize = new JTextField(10);
		memoryBlockSize.setText("1");
		//初始化监视器
		console = new JTextArea(8,40);
		console.setEditable(false);
		console.setFont(new Font("微软雅黑",0,25));
		// 初始化按钮
		requestButton = new JButton("随机请求资源");
		releaseButton = new JButton("随机释放资源");
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
				System.out.println("释放资源");
			}
		});

		JPanel firstBlock = new JPanel();
		firstBlock.setPreferredSize(new Dimension(1024, 150));
		firstBlock.setBackground(new Color(255, 156, 156));
		firstBlock.setLayout(new BorderLayout());
		JLabel BlockInfo = new JLabel("初始化", JLabel.CENTER);
		BlockInfo.setFont(new Font("微软雅黑", Font.BOLD, 40));
		firstBlock.add(BlockInfo, BorderLayout.CENTER);
		memoryPanel.add(firstBlock);
	}

	/**
	 * 这是被观察者更新的函数
	 * 当控制器更新好内存后，通知窗口更新
	 */
	@Override
	public void update(Observable o, Object arg) {
		MessagePackage result = (MessagePackage) arg;
		if (!result.isSucceed) {
			JOptionPane.showMessageDialog(null, "无法调度", "注意！", JOptionPane.INFORMATION_MESSAGE);
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
			BlockInfo.setFont(new Font("微软雅黑", Font.BOLD, 20));
			newBlock.add(BlockInfo);
			memoryPanel.add(newBlock);
		}
		memoryPanel.updateUI();
	}
}
