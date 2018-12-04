package team.tw.newten.tang;

import java.util.*;

public class Free_area_head {
	public int order;// 记录这一组内存块的大小的权
	private int num;// 记录这一组内存块的空闲数量
	private ArrayList<Block> blockList;// 管理这个大小的内存块的数组

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

	// 增加一个空内存块
	public void addBlock(Block block) {
		int index = -1;
		do {
			index++;
		} while (index < blockList.size() && blockList.get(index).getAddress() < block.getAddress());
		blockList.add(index, block);
		num++;
	}

	// 减少一个空内存块
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
	 * 这是合并内存块的函数，需要列表中准备合并的内存块的位置传入
	 * 函数会该内存块抛出给上层处理
	 * 并将数量减一
	 * @param index
	 * @return 合并好的内存块
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
