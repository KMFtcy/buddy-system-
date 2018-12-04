package team.tw.newten.tang;

import java.util.*;

public class Free_area_head {
	public int order;// ��¼��һ���ڴ��Ĵ�С��Ȩ
	private int num;// ��¼��һ���ڴ��Ŀ�������
	private ArrayList<Block> blockList;// ���������С���ڴ�������

	public Free_area_head() {
		this.order = 0;
		this.num = 0;
		this.blockList = new ArrayList<Block>();
	}

	public Free_area_head(int order) {
		this.order = order;
		this.num = 0;
		this.blockList = new ArrayList<Block>();
	}

	// ����һ�����ڴ��
	public void addBlock(Block block) {
		int index = -1;
		do {
			index++;
		} while (index < blockList.size() && blockList.get(index).getAddress() < block.getAddress());
		blockList.add(index, block);
		num++;
	}

	// ����һ�����ڴ��
	public Block allcBlock() {
		if (num > 0) {
			Block targetBlock = blockList.get(0);
			blockList.remove(0);
			num--;
			return targetBlock;
		}
		return null;
	}
	/**
	 * ���Ǻϲ��ڴ��ĺ�������Ҫ�б���׼���ϲ����ڴ���λ�ô���
	 * ��������ڴ���׳����ϲ㴦��
	 * ����������һ
	 * @param index
	 * @return �ϲ��õ��ڴ��
	 */
	public Block mergeBlock(int index) {
		Block resultBlock = null;
		resultBlock = blockList.get(index);
		blockList.remove(index);
		num--;
		return resultBlock;
	}
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getNum() {
		return num;
	}

	public ArrayList<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(ArrayList<Block> blockList) {
		this.blockList = blockList;
	}
}
