package team.tw.ten.tang;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {
	ArrayList<Block> RAM;
	MessagePackage result = new MessagePackage();
	public Controller(ArrayList<Block> RAM) {
		this.RAM = RAM;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("更新数据");
		System.out.println("更新完毕,更新GUI");
		test();
	}

	private void updateGUI() {
		result.setRAM(RAM);
		result.setSucceed(true);
		setChanged();
		notifyObservers(result);
	}

	public boolean BuddyAlloc(Block request) throws Exception {
		int aimSize = 1;
		/**
		 * 这是企图对最佳块组的搜索，位于位置i的最佳块组条件为：2的i-1次方<request<2的i次方
		 */
		while (aimSize < Constant.MAX_STORAGE) {
			if (aimSize > request.getStorage()) {
				for (int i = 0; i < RAM.size(); i++) {
					if (RAM.get(i).getStorage() == aimSize && !RAM.get(i).isUsed()) {
						splitBlock(request, i);
						mergeRAM();
						return true;
					}
				}
			}
			aimSize *= 2;
		}
		throw new Exception("请求大于内存上限");
	}

	/**
	 * 合并RAM的伙伴堆
	 */
	private void mergeRAM() {
		// 这一个boolean变量是确认RAM中确实没有可以合并的块组了
		// 因为第一次合并以后，可能会有新的可以合并的块组,例如
		// 4,4,4,4 --第一次--> 8,8
		boolean flag;
		do {
			flag = false;
			for (int i = 1; i < RAM.size(); i++) {
				if (RAM.get(i).getStorage() == RAM.get(i - 1).getStorage() && !RAM.get(i).isUsed()
						&& !RAM.get(i - 1).isUsed()) {
					Block newBlock = new Block((RAM.get(i).getStorage() * 2));
					RAM.remove(i - 1);
					RAM.remove(i - 1);
					RAM.add(i - 1, newBlock);
					flag = true;
				}
			}
		} while (flag);

	}

	/**
	 * 
	 * @param request 要插入的任务
	 * @param index   要分配的块组的位置
	 */
	private void splitBlock(Block request, int index) {
		while (RAM.get(index).getStorage() / 2 > request.getStorage()) {
			Block splitBlock1 = new Block(RAM.get(index).getStorage() / 2);
			Block splitBlock2 = new Block(RAM.get(index).getStorage() / 2);
			RAM.add(index, splitBlock1);
			RAM.add(index, splitBlock2);
			RAM.remove(index + 2);
		}
		RAM.get(index).setTaskName(request.getTaskName());
		RAM.get(index).setUsed(true);
	}

	private void releaseBlock(int index) {
		int i = 0;
		for(Block x : RAM) {
			if(x.isUsed()) i++;
			if(i>=index) {
				x.setUsed(false);
				x.setTaskName(x.getStorage() + "bytes");
				break;
			}
		}
//		RAM.get(index).setUsed(false);
//		RAM.get(index).setTaskName(RAM.get(index).getStorage() + "bytes");
	}
	static int count = 0;
	public void test() {
		try {
			int newStore = (int) (Math.random() * 250) + 1;
			result.setMessage("新的内存请求 大小为 : " + newStore + "bytes");
			System.out.println("新的内存请求 大小为 : " + newStore + "bytes");
			BuddyAlloc(new Block("X",newStore));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			result.setMessage("请求拒绝，没有找到合适的内存块！");
			System.out.println("请求拒绝，没有找到合适的内存块！");
		}finally {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			if(count > 4) {
				int index = (int) (Math.random() * 4);
				result.setMessage(result.getMessage() + "\n释放内存块 : " + index);
				System.out.println("释放内存块 : " + index);
				releaseBlock(index);
				mergeRAM();
			}
		}
		updateGUI();
	}
}
