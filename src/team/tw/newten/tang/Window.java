package team.tw.newten.tang;

import team.tw.newten.tang.command.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Window extends Observable implements Observer {
	JFrame frame;// 窗口
	Container container;// 窗口容器
	JPanel leftArea;// 左边栏容器
	JPanel centerArea;// 中间容器
	JPanel centerBottomArea;// 底部容器
	int[] freeAreaNum;
	JLabel[] freeAreaNumLabel;//显示空闲内存块数量的标签
	int UsedAreaNum;
	JLabel UsedAreaNumLabel;//显示使用中内存块数量的标签
	JComboBox<Integer> memorySizeComboBox;// 选择内存大小
	JComboBox<Integer> pageSizeComboBox;// 选择页大小
	JPanel memoryPanel;
	JTextField operateBlockNumTextField;
	JTextArea console;//窗口中的监视器
	JButton allocButton;// 分配内存的按钮
	JButton releaseButton;// 释放内存的按钮
	JButton refreshButton;// 刷新内存的按钮
	JButton resetButton;// 重置内存的按钮

	public Window() {
		initComponent();
		setComponent();
	}

	private void setComponent() {
		leftArea = new JPanel();
		centerArea = new JPanel();
		JPanel centerCenterArea = new JPanel();
		centerBottomArea = new JPanel();

		container.setLayout(new BorderLayout());
		leftArea.setLayout(new GridLayout(14, 2, 20, 20));
		centerArea.setLayout(new BorderLayout());
		centerCenterArea.setLayout(new GridLayout(1, 5,10,20));

		for (int i = 0; i < freeAreaNum.length; i++) {
			leftArea.add(new JLabel("  第" + (i + 1) + "组空闲内存块数量："));
			leftArea.add(freeAreaNumLabel[i]);
		}
		leftArea.add(new JLabel("  使用中存块数量："));
		leftArea.add(UsedAreaNumLabel);
		leftArea.add(new JLabel("  内存大小"));
		leftArea.add(memorySizeComboBox);
		leftArea.add(new JLabel("  页面大小"));
		leftArea.add(pageSizeComboBox);
		leftArea.add(refreshButton);
		leftArea.add(resetButton);
		centerCenterArea.add(new JLabel("要对",null,JLabel.RIGHT));
		centerCenterArea.add(operateBlockNumTextField);
		centerCenterArea.add(new JLabel("个内存块进行："));
		centerCenterArea.add(allocButton);
		centerCenterArea.add(releaseButton);
		centerBottomArea.add(new JScrollPane(console));
		JScrollPane memoryPanelContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		memoryPanelContainer.setPreferredSize(new Dimension(900,100));
		centerArea.add(new JScrollPane(memoryPanel),BorderLayout.NORTH);
		centerArea.add(centerCenterArea, BorderLayout.CENTER);
		centerArea.add(centerBottomArea, BorderLayout.SOUTH);
		container.add(centerArea, BorderLayout.CENTER);
		container.add(leftArea, BorderLayout.WEST);
		container.add(new JLabel("  版本：1.0.1"), BorderLayout.SOUTH);
		container.add(new JLabel("  2018.12.3"), BorderLayout.NORTH);

		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void initComponent() {
		// 初始化窗口和窗口容器
		frame = new JFrame();
		container = frame.getContentPane();
		memoryPanel = new JPanel();
		JPanel initBlcok = new JPanel();
		initBlcok.setPreferredSize(new Dimension(900, 120));
		memoryPanel.add(initBlcok);
		// 初始化多选框
		memorySizeComboBox = new JComboBox<Integer>();
		for (int i : Constant.MEMORY_SIZE_OPTION) {
			memorySizeComboBox.addItem(i);
		}
		pageSizeComboBox = new JComboBox<Integer>();
		for (int i : Constant.PAGE_SIZE_OPTION) {
			pageSizeComboBox.addItem(i);
		}
		// 初始化按钮
		resetButton = new JButton("重置");
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetInfo();
			}
		});
		refreshButton = new JButton("刷新");
		refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshInfo();
			}
		});
		allocButton = new JButton("分配");
		allocButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				allocBlock();
			}
		});
		releaseButton = new JButton("释放");
		releaseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				releaseBlock();
			}
		});
		// 初始化内存块信息标签
		freeAreaNum = new int[Constant.ORDER];
		freeAreaNumLabel = new JLabel[Constant.ORDER];
		for (int i = 0; i < freeAreaNum.length; i++) {
			freeAreaNum[i] = -1;
			freeAreaNumLabel[i] = new JLabel(freeAreaNum[i] + "块");
		}
		UsedAreaNum = -1;
		UsedAreaNumLabel = new JLabel(UsedAreaNum + "块");
		//初始化监视器的TextArea
		console = new JTextArea(15,36);
		console.setEditable(false);
		console.setFont(new Font("微软雅黑",0,25));
		console.append("系统初始化完成，请刷新");
		//初始化输入框
		operateBlockNumTextField = new JTextField(10);
		// **初始化完成**
	}

	//将整个系统刷新
	public void refreshInfo() {
		setChanged();
		notifyObservers(new RefreshWindowCommand(this));
	}
	//重新设置整个系统
	public void resetInfo() {
		setChanged();
		notifyObservers(new ResetCpuCommand(this));
	}
	//分配随机内存
	public void allocBlock() {
		setChanged();
		notifyObservers(new AllocCommand(this));
	}
	//随机释放内存块
	protected void releaseBlock() {
		setChanged();
		notifyObservers(new ReleaseCommand(this));
	}
	public static void main(String[] args) {
		Window ONE = new Window();
	}
	public void printlnConsole(String message) {
		console.append("\n" + message);
		console.setCaretPosition(console.getText().length());
	}
	public void printConsole(String message) {
		console.append(message);
		console.setCaretPosition(console.getText().length());
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		Command command = (Command) arg1;
		command.execute(this);
	}

	public JComboBox<Integer> getMemorySizeComboBox() {
		return memorySizeComboBox;
	}

	public void setMemorySizeComboBox(JComboBox<Integer> memorySizeComboBox) {
		this.memorySizeComboBox = memorySizeComboBox;
	}

	public JComboBox<Integer> getPageSizeComboBox() {
		return pageSizeComboBox;
	}

	public void setPageSizeComboBox(JComboBox<Integer> pageSizeComboBox) {
		this.pageSizeComboBox = pageSizeComboBox;
	}

	public JPanel getLeftArea() {
		return leftArea;
	}

	public void setLeftArea(JPanel leftArea) {
		this.leftArea = leftArea;
	}

	public int[] getFreeAreaNum() {
		return freeAreaNum;
	}

	public void setFreeAreaNum(int[] freeAreaNum) {
		this.freeAreaNum = freeAreaNum;
	}

	public JLabel[] getFreeAreaNumLabel() {
		return freeAreaNumLabel;
	}

	public void setFreeAreaNumLabel(JLabel[] freeAreaNumLabel) {
		this.freeAreaNumLabel = freeAreaNumLabel;
	}

	public int getUsedAreaNum() {
		return UsedAreaNum;
	}

	public void setUsedAreaNum(int usedAreaNum) {
		UsedAreaNum = usedAreaNum;
	}

	public JLabel getUsedAreaNumLabel() {
		return UsedAreaNumLabel;
	}

	public void setUsedAreaNumLabel(JLabel usedAreaNumLabel) {
		UsedAreaNumLabel = usedAreaNumLabel;
	}

	public JPanel getMemoryPanel() {
		return memoryPanel;
	}

	public void setMemoryPanel(JPanel memoryPanel) {
		this.memoryPanel = memoryPanel;
	}

	public JTextField getOperateBlockNumTextField() {
		return operateBlockNumTextField;
	}

	public JPanel getCenterArea() {
		return centerArea;
	}
	
}
