package team.tw.newten.tang;

import team.tw.newten.tang.command.*;
import java.util.*;

public class CPU extends Observable implements Observer {
	ArrayList<Free_area_head> free_area_list;
	ArrayList<Block> recentUsedBlock;
	ArrayList<boolean[]> bitmap;

	public CPU(ArrayList<Free_area_head> free_area_list) {
		this.free_area_list = free_area_list;
		recentUsedBlock = new ArrayList<Block>();
		bitmap = new ArrayList<boolean[]>();
		refresh();
	}

	public void release(int releaseNum) throws Exception {
		if (releaseNum > recentUsedBlock.size())
			throw new Exception("Ҫ���ͷŵ��ڴ��������������ռ�õ�����");
		for (int i = 0; i < releaseNum; i++) {
			// ��Ҫ�ͷŵ��ڴ�����ҵ�б���ժ��
			Block targetBlock = recentUsedBlock.get(0);
			recentUsedBlock.remove(0);
			targetBlock.setUsed(false);
			// �ϲ��ڴ�
			mergeMemory(targetBlock);
		}
	}
	//����Ϊ�˵õ�ÿһ���ͷŵ��ڴ�����Ϣ�����صĺ���
	public Block release() throws Exception {
		if (recentUsedBlock.size() <= 0)
			throw new Exception("û��ʹ���е��ڴ��");
		// ��Ҫ�ͷŵ��ڴ�����ҵ�б���ժ��
		Block targetBlock = recentUsedBlock.get(0);
		recentUsedBlock.remove(0);
		targetBlock.setUsed(false);
		Block copyBlock = new Block(targetBlock);
		// �ϲ��ڴ�
		mergeMemory(targetBlock);
		return copyBlock;
	}

	public void mergeMemory(Block targetBlock) throws Exception {
		// �ҵ���������Ȩ
		int blockSize = targetBlock.getSize();
		int targetOrder = calOrder(blockSize);
		// �鿴λͼ���޸�λͼ
		int bitMapIndex = 0;
		bitMapIndex = calBitMapIndex(targetBlock.getAddress(), targetOrder);
		boolean canBeMerged = bitmap.get(targetOrder)[bitMapIndex];
		switchBitMap(targetBlock);
		// ������Ժϲ����ӿ����б�ȡ������ϲ�������������ϼ������б���������Ժϲ������Ѿ���Ϊ�����飬��������б�
		if (canBeMerged && targetOrder < Constant.ORDER - 1) {
			Block buddyBlock = null;
			ArrayList<Block> targetBlockList = free_area_list.get(targetOrder).getBlockList();
			int ListSize = targetBlockList.size();
			// ������ӿ����б�ȡ��
			for (int i = 0; i < ListSize; i++) {
				Block comparingBlock = targetBlockList.get(i);
				// ��һ��ɸѡ�����Ƿ����ڣ��򵥱Ƚϵ�ַ����ʡȥ��������
				int ditance = comparingBlock.getAddress() - targetBlock.getAddress();
				if (ditance == blockSize || ditance == -blockSize) {
					// �ڶ���ɸѡ�����Ƿ��ǻ�飨�Ƿ�ͬ��һ�����λ��,�ǵĻ��ӿ����б�ժ��
					int comparingBlockBitMapIndex;
					comparingBlockBitMapIndex = calBitMapIndex(comparingBlock.getAddress(), targetOrder);
					if (comparingBlockBitMapIndex == bitMapIndex) {
						buddyBlock = comparingBlock;
						free_area_list.get(targetOrder).mergeBlock(i);
						break;
					}
				}
			}
			// ��������ϲ������µ�ַ��С����һ��
			targetBlock = targetBlock.getAddress() > buddyBlock.getAddress() ? buddyBlock : targetBlock;
			targetBlock.setSize(blockSize * 2);
			mergeMemory(targetBlock);
		} else {
			free_area_list.get(targetOrder).addBlock(targetBlock);
		}
	}

	/**
	 * ���Ƿ����ڴ�ĺ��� ���ݴ���������С��������ĺ���
	 * 
	 * @param size
	 * @throws Exception
	 */
	public void allocate(int size) throws Exception {
		// targetOrder�Ƿ���Ҫ�����С�ڴ����ţ�chooseOrder��ʵ�ʷ��䵽���ڴ�����
		int targetOrder = calOrder(size);
		int chooseOrder = targetOrder;
		// �鿴�Ƿ��п����ڴ�飬û�еĻ�order����һ��Ѱ��
		while (free_area_list.get(chooseOrder).getNum() == 0) {
			chooseOrder++;
			if (chooseOrder >= Constant.ORDER)
				throw new Exception("û���㹻�����ڴ���");
		}
		// ��ʼ���䣬����պú�����ֱ�ӷ����ڴ�飬����ָ�
		Block targetBlock = free_area_list.get(chooseOrder).allcBlock();
		switchBitMap(targetBlock);
		while (chooseOrder > targetOrder) {
			chooseOrder--;
			Block splitBlock = targetBlock.split();
			free_area_list.get(chooseOrder).addBlock(splitBlock);
			// ��ʱҪ��¼λͼ�ı任
			switchBitMap(targetBlock);
		}
		targetBlock.setUsed(true);
		recentUsedBlock.add(targetBlock);
	}

	/**
	 * �����ڴ��ĵ�ַ�����Ĵ�С��order��ĳ�������ϱ������ڴ��Ĵ�С������λͼ
	 * 
	 * @param address
	 * @param order
	 * @throws Exception �Ѽ����ʱ��õ����쳣�׳�
	 */
	private void switchBitMap(Block targetBlock) throws Exception {
		int targetOrder = calOrder(targetBlock.getSize());
		int address = targetBlock.getAddress();
		switchBitMap(address, targetOrder);
	}

	private void switchBitMap(int address, int targetOrder){
		int bitMapIndex = 0;
		bitMapIndex = calBitMapIndex(address, targetOrder);
		bitmap.get(targetOrder)[bitMapIndex] = !bitmap.get(targetOrder)[bitMapIndex];
	}

	public void refresh() {
		// ��������б��ʹ���б�
		free_area_list.clear();
		recentUsedBlock.clear();
		// ����order���ͷ���
		for (int i = 0; i < Constant.ORDER; i++) {
			Free_area_head newHead = new Free_area_head(i);
			free_area_list.add(free_area_list.size(), newHead);
		}
		// ����free_area��Block
		for (int recentMemorySize = 0; recentMemorySize < Constant.MEMORY_SIZE; recentMemorySize += Math.pow(2,
				Constant.ORDER - 1) * Constant.PAGE_SIZE) {
			Block newBlock = new Block((int) Math.pow(2, Constant.ORDER - 1), recentMemorySize);
			free_area_list.get(Constant.ORDER - 1).addBlock(newBlock);
		}
		// ����order�������ɻ��λͼ
		int pageNum = Constant.MEMORY_SIZE / Constant.PAGE_SIZE;
		for (int i = 0; i < Constant.ORDER; i++) {
			int size = 0;
			size = (int) (pageNum / Math.pow(2, i) / 2);
			boolean[] newMap = new boolean[size];
			// ��ʼ��λͼ
			for (boolean j : newMap) {
				j = false;
			}
			bitmap.add(newMap);
		}

	}

	public void displayMemory() {
		System.out.println("δ������ڴ�飺\n");
		for (int i = 0; i < bitmap.size(); i++) {
			boolean[] map = bitmap.get(i);
			int j = 0;
			for (; j < map.length; j++) {
				System.out.print("[" + map[j] + "]");
			}
			System.out.println("\n���鹲" + j + "������");
		}
		for (Free_area_head i : free_area_list) {
			int num = 0;
			System.out.println("��" + i.order + "�飺");
			for (Block j : i.getBlockList()) {
				num++;
				System.out.println("��С��" + j.size + "��ַ��" + j.address + "\n");
			}
			System.out.println("���鹲" + num + "��\n");
		}
		System.out.println("�ѷ�����ڴ�飺\n");
		int i = 0;
		for (Block j : recentUsedBlock) {
			System.out.println("��" + i + "�飺");
			System.out.println("��С��" + j.getSize());
			System.out.println("��ַ��" + j.getAddress() + "\n");
			i++;
		}
	}

	public void displayUsedBlok() {
		for (Block i : recentUsedBlock) {
			System.out.print("[" + i.getAddress() + "," + i.getSize() + "]");
		}
		System.out.println("");
	}

	/**
	 * ������������������ڴ���Ȩ��С�ĺ���
	 * 
	 * @param size Ŀ��Ĵ�С�����뵽���ڴ��Ӧ�ô��ڵ������ֵ
	 * @return ����һ��Ȩorder��pow��2��order��>=order
	 * @throws Exception �����ڴ治����Ҫ���ʱ�򣬻������쳣
	 */
	private int calOrder(int size) throws Exception {
		int order = 0;
		if (size <= 0)
			throw new Exception("������ڴ�С�ڵ���0");
		if (size >= Constant.MEMORY_SIZE)
			throw new Exception("�����ڴ��������");
		while (Math.pow(2, (int) order) < size) {
			order++;
		}
		if (order >= Constant.ORDER)
			throw new Exception("����Ȩʱ����û���㹻�����ڴ���");
		return order;
	}

	/**
	 * ���Ǽ����ڴ���Ӧ���λ�ĺ���
	 * 
	 * @param address �ڴ���ַ
	 * @param order   �ڴ���Ȩ�����Եõ��ڴ���С
	 * @return ���ڴ���Ӧ�Ļ��λ��index
	 */
	private int calBitMapIndex(int address, int order) {
		return (int) ((address / Constant.PAGE_SIZE) / (Math.pow(2, order + 1)));
	}

	@Override
	public void update(Observable o, Object arg) {
		Command command = (Command) arg;
		command.execute(this);
	}

	public ArrayList<Free_area_head> getFree_area_list() {
		return free_area_list;
	}

	public void setFree_area_list(ArrayList<Free_area_head> free_area_list) {
		this.free_area_list = free_area_list;
	}

	public ArrayList<Block> getRecentUsedBlock() {
		return recentUsedBlock;
	}

	public void setRecentUsedBlock(ArrayList<Block> recentUsedBlock) {
		this.recentUsedBlock = recentUsedBlock;
	}

}