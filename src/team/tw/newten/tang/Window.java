package team.tw.newten.tang;

import team.tw.newten.tang.command.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Window extends Observable implements Observer {
	JFrame frame;// ����
	Container container;// ��������
	JPanel leftArea;// ���������
	JPanel centerArea;// �м�����
	JPanel centerBottomArea;// �ײ�����
	int[] freeAreaNum;
	JLabel[] freeAreaNumLabel;//��ʾ�����ڴ�������ı�ǩ
	JLabel[] UsedAreaNumLabel;//��ʾ�����ڴ�������ı�ǩ
	int UsedAreaNum;
	//���� JLabel UsedAreaNumLabel;//��ʾʹ�����ڴ�������ı�ǩ
	JComboBox<Integer> memorySizeComboBox;// ѡ���ڴ��С
	JComboBox<Integer> pageSizeComboBox;// ѡ��ҳ��С
	JPanel memoryPanel;
	JTextField operateBlockNumTextField;
	JTextArea console;//�����еļ�����
	JButton allocButton;// �����ڴ�İ�ť
	JButton releaseButton;// �ͷ��ڴ�İ�ť
	JButton refreshButton;// ˢ���ڴ�İ�ť
	JButton resetButton;// �����ڴ�İ�ť
	JButton continueButton;//����������ѡ������İ�ť
	JRadioButton SingleStepButton;//ѡ���ѡ���Ƿ񵥲�����

	public Window() {
		initComponent();
		setComponent();
	}

	private void setComponent() {
		leftArea = new JPanel();
		centerArea = new JPanel();
		JPanel centerCenterArea = new JPanel();
		centerBottomArea = new JPanel();
		JPanel singleStepChooseArea = new JPanel();

		container.setLayout(new BorderLayout());
		leftArea.setLayout(new GridLayout(14, 3, 20, 20));
		centerArea.setLayout(new BorderLayout());
		centerCenterArea.setLayout(new GridLayout(1, 5,10,20));
		centerBottomArea.setLayout(new BorderLayout());
		singleStepChooseArea.setLayout(new GridLayout(1, 2));
		
		leftArea.add(new JLabel("  ���"));
		leftArea.add(new JLabel("����"));
		leftArea.add(new JLabel("ʹ����"));
		for (int i = 0; i < freeAreaNum.length; i++) {
			leftArea.add(new JLabel("  ��" + (i + 1) + "�飺"));
			leftArea.add(freeAreaNumLabel[i]);
			leftArea.add(UsedAreaNumLabel[i]);
		}
		leftArea.add(new JLabel("  �ڴ��С"));
		leftArea.add(memorySizeComboBox);
		leftArea.add(new JLabel("kb"));
		leftArea.add(new JLabel("  ҳ���С"));
		leftArea.add(pageSizeComboBox);
		leftArea.add(new JLabel("kb"));
		leftArea.add(refreshButton);
		leftArea.add(resetButton);
		leftArea.add(new JLabel("  "));
		centerCenterArea.add(new JLabel("Ҫ��",null,JLabel.RIGHT));
		centerCenterArea.add(operateBlockNumTextField);
		centerCenterArea.add(new JLabel("���ڴ����У�"));
		centerCenterArea.add(allocButton);
		centerCenterArea.add(releaseButton);
		centerBottomArea.add(new JScrollPane(console),BorderLayout.CENTER);
		singleStepChooseArea.add(SingleStepButton);
		singleStepChooseArea.add(continueButton);
		centerBottomArea.add(singleStepChooseArea,BorderLayout.SOUTH);
		JScrollPane memoryPanelContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		memoryPanelContainer.setPreferredSize(new Dimension(900,100));
		centerArea.add(new JScrollPane(memoryPanel),BorderLayout.NORTH);
		centerArea.add(centerCenterArea, BorderLayout.CENTER);
		centerArea.add(centerBottomArea, BorderLayout.SOUTH);
		container.add(centerArea, BorderLayout.CENTER);
		container.add(leftArea, BorderLayout.WEST);
		container.add(new JLabel("  �汾��1.0.1"), BorderLayout.SOUTH);
		container.add(new JLabel("  2018.12.3"), BorderLayout.NORTH);

		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void initComponent() {
		// ��ʼ�����ںʹ�������
		frame = new JFrame();
		container = frame.getContentPane();
		memoryPanel = new JPanel();
		JPanel initBlcok = new JPanel();
		initBlcok.setPreferredSize(new Dimension(900, 120));
		memoryPanel.add(initBlcok);
		// ��ʼ����ѡ��
		memorySizeComboBox = new JComboBox<Integer>();
		for (int i : Constant.MEMORY_SIZE_OPTION) {
			memorySizeComboBox.addItem(i);
		}
		pageSizeComboBox = new JComboBox<Integer>();
		for (int i : Constant.PAGE_SIZE_OPTION) {
			pageSizeComboBox.addItem(i);
		}
		// ��ʼ����ť
		resetButton = new JButton("����");
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetInfo();
			}
		});
		refreshButton = new JButton("ˢ��");
		refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshInfo();
			}
		});
		allocButton = new JButton("����");
		allocButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				allocBlock();
			}
		});
		releaseButton = new JButton("�ͷ�");
		releaseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				releaseBlock();
			}
		});
		continueButton = new JButton("����");
		continueButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("��������");
				continueWindow();
			}
		});
		// ��ʼ���ڴ����Ϣ��ǩ
		freeAreaNum = new int[Constant.ORDER];
		freeAreaNumLabel = new JLabel[Constant.ORDER];
		for (int i = 0; i < freeAreaNum.length; i++) {
			freeAreaNum[i] = -1;
			freeAreaNumLabel[i] = new JLabel(freeAreaNum[i] + "��");
		}
		UsedAreaNumLabel = new JLabel[Constant.ORDER];
		for (int i = 0; i < UsedAreaNumLabel.length; i++) {
			UsedAreaNumLabel[i] = new JLabel(0 + "��");
		}
		//��ʼ����������TextArea
		console = new JTextArea(15,20);
		console.setEditable(false);
		console.setFont(new Font("΢���ź�",0,25));
		console.append("ϵͳ��ʼ����ɣ���ˢ��");
		//��ʼ������������������
		operateBlockNumTextField = new JTextField(10);
		//��ʼ���Ƿ񵥲����еĵ�ѡ��ť
		SingleStepButton = new JRadioButton("��������");
		SingleStepButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Constant.isSingleStep = !Constant.isSingleStep;
				Constant.canContinue = false;
				System.out.println("��������:" + Constant.isSingleStep);
			}
		});
		// **��ʼ�����**
	}

	//������ϵͳˢ��
	public void refreshInfo() {
		setChanged();
		notifyObservers(new RefreshWindowCommand(this));
	}
	//������������ϵͳ
	public void resetInfo() {
		setChanged();
		notifyObservers(new ResetCpuCommand(this));
	}
	//��������ڴ�
	public void allocBlock() {
		setChanged();
		notifyObservers(new AllocCommand(this));
	}
	//����ͷ��ڴ��
	protected void releaseBlock() {
		setChanged();
		notifyObservers(new ReleaseCommand(this));
	}
	//��������
	private synchronized void continueWindow() {
		synchronized (AllocCommand.obj) {
			AllocCommand.obj.notifyAll();
		}
		System.out.println("�����߳�");
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

	public JLabel[] getUsedAreaNumLabel() {
		return UsedAreaNumLabel;
	}

	public void setUsedAreaNumLabel(JLabel[] usedAreaNumLabel) {
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
