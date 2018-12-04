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
			throw new Exception("要求释放的内存块数量大于正在占用的数量");
		for (int i = 0; i < releaseNum; i++) {
			// 将要释放的内存块从作业列表中摘除
			Block targetBlock = recentUsedBlock.get(0);
			recentUsedBlock.remove(0);
			targetBlock.setUsed(false);
			// 合并内存
			mergeMemory(targetBlock);
		}
	}
	//这是为了得到每一个释放的内存块的信息，重载的函数
	public Block release() throws Exception {
		if (recentUsedBlock.size() <= 0)
			throw new Exception("没有使用中的内存块");
		// 将要释放的内存块从作业列表中摘除
		Block targetBlock = recentUsedBlock.get(0);
		recentUsedBlock.remove(0);
		targetBlock.setUsed(false);
		Block copyBlock = new Block(targetBlock);
		// 合并内存
		mergeMemory(targetBlock);
		return copyBlock;
	}

	public void mergeMemory(Block targetBlock) throws Exception {
		// 找到它归属的权
		int blockSize = targetBlock.getSize();
		int targetOrder = calOrder(blockSize);
		// 查看位图并修改位图
		int bitMapIndex = 0;
		bitMapIndex = calBitMapIndex(targetBlock.getAddress(), targetOrder);
		boolean canBeMerged = bitmap.get(targetOrder)[bitMapIndex];
		switchBitMap(targetBlock);
		// 如果可以合并，从空闲列表取出伙伴块合并，并继续检查上级空闲列表；如果不可以合并或者已经成为最大伙伴块，插入空闲列表
		if (canBeMerged && targetOrder < Constant.ORDER - 1) {
			Block buddyBlock = null;
			ArrayList<Block> targetBlockList = free_area_list.get(targetOrder).getBlockList();
			int ListSize = targetBlockList.size();
			// 将伙伴块从空闲列表取出
			for (int i = 0; i < ListSize; i++) {
				Block comparingBlock = targetBlockList.get(i);
				// 第一步筛选，看是否相邻，简单比较地址可以省去大量运算
				int ditance = comparingBlock.getAddress() - targetBlock.getAddress();
				if (ditance == blockSize || ditance == -blockSize) {
					// 第二步筛选，看是否是伙伴（是否同属一个伙伴位）,是的话从空闲列表摘除
					int comparingBlockBitMapIndex;
					comparingBlockBitMapIndex = calBitMapIndex(comparingBlock.getAddress(), targetOrder);
					if (comparingBlockBitMapIndex == bitMapIndex) {
						buddyBlock = comparingBlock;
						free_area_list.get(targetOrder).mergeBlock(i);
						break;
					}
				}
			}
			// 将两个块合并，留下地址较小的那一块
			targetBlock = targetBlock.getAddress() > buddyBlock.getAddress() ? buddyBlock : targetBlock;
			targetBlock.setSize(blockSize * 2);
			mergeMemory(targetBlock);
		} else {
			free_area_list.get(targetOrder).addBlock(targetBlock);
		}
	}

	/**
	 * 这是分配内存的函数 根据传入的请求大小决定分配的函数
	 * 
	 * @param size
	 * @throws Exception
	 */
	public void allocate(int size) throws Exception {
		// targetOrder是符合要求的最小内存块组号，chooseOrder是实际分配到的内存块组号
		int targetOrder = calOrder(size);
		int chooseOrder = targetOrder;
		// 查看是否有空闲内存块，没有的话order往上一级寻找
		while (free_area_list.get(chooseOrder).getNum() == 0) {
			chooseOrder++;
			if (chooseOrder >= Constant.ORDER)
				throw new Exception("没有足够空闲内存了");
		}
		// 开始分配，如果刚好合适则直接分配内存块，否则分割
		Block targetBlock = free_area_list.get(chooseOrder).allcBlock();
		switchBitMap(targetBlock);
		while (chooseOrder > targetOrder) {
			chooseOrder--;
			Block splitBlock = targetBlock.split();
			free_area_list.get(chooseOrder).addBlock(splitBlock);
			// 此时要记录位图的变换
			switchBitMap(targetBlock);
		}
		targetBlock.setUsed(true);
		recentUsedBlock.add(targetBlock);
	}

	/**
	 * 根据内存块的地址和他的大小（order在某种意义上标明了内存块的大小）更改位图
	 * 
	 * @param address
	 * @param order
	 * @throws Exception 把计算的时候得到的异常抛出
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
		// 清理空闲列表和使用列表
		free_area_list.clear();
		recentUsedBlock.clear();
		// 根据order添加头结点
		for (int i = 0; i < Constant.ORDER; i++) {
			Free_area_head newHead = new Free_area_head(i);
			free_area_list.add(free_area_list.size(), newHead);
		}
		// 生成free_area的Block
		for (int recentMemorySize = 0; recentMemorySize < Constant.MEMORY_SIZE; recentMemorySize += Math.pow(2,
				Constant.ORDER - 1) * Constant.PAGE_SIZE) {
			Block newBlock = new Block((int) Math.pow(2, Constant.ORDER - 1), recentMemorySize);
			free_area_list.get(Constant.ORDER - 1).addBlock(newBlock);
		}
		// 根据order数量生成伙伴位图
		int pageNum = Constant.MEMORY_SIZE / Constant.PAGE_SIZE;
		for (int i = 0; i < Constant.ORDER; i++) {
			int size = 0;
			size = (int) (pageNum / Math.pow(2, i) / 2);
			boolean[] newMap = new boolean[size];
			// 初始化位图
			for (boolean j : newMap) {
				j = false;
			}
			bitmap.add(newMap);
		}

	}

	public void displayMemory() {
		System.out.println("未分配的内存块：\n");
		for (int i = 0; i < bitmap.size(); i++) {
			boolean[] map = bitmap.get(i);
			int j = 0;
			for (; j < map.length; j++) {
				System.out.print("[" + map[j] + "]");
			}
			System.out.println("\n本组共" + j + "个伙伴块");
		}
		for (Free_area_head i : free_area_list) {
			int num = 0;
			System.out.println("第" + i.order + "组：");
			for (Block j : i.getBlockList()) {
				num++;
				System.out.println("大小：" + j.size + "地址：" + j.address + "\n");
			}
			System.out.println("本组共" + num + "块\n");
		}
		System.out.println("已分配的内存块：\n");
		int i = 0;
		for (Block j : recentUsedBlock) {
			System.out.println("第" + i + "块：");
			System.out.println("大小：" + j.getSize());
			System.out.println("地址：" + j.getAddress() + "\n");
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
	 * 这是用来计算申请的内存块的权大小的函数
	 * 
	 * @param size 目标的大小，申请到的内存块应该大于等于这个值
	 * @return 返回一个权order，pow（2，order）>=order
	 * @throws Exception 申请内存不符合要求的时候，会跳出异常
	 */
	private int calOrder(int size) throws Exception {
		int order = 0;
		if (size <= 0)
			throw new Exception("申请的内存小于等于0");
		if (size >= Constant.MEMORY_SIZE)
			throw new Exception("申请内存大于上限");
		while (Math.pow(2, (int) order) < size) {
			order++;
		}
		if (order >= Constant.ORDER)
			throw new Exception("计算权时发现没有足够空闲内存了");
		return order;
	}

	/**
	 * 这是计算内存块对应伙伴位的函数
	 * 
	 * @param address 内存块地址
	 * @param order   内存块的权，可以得到内存块大小
	 * @return 该内存块对应的伙伴位的index
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