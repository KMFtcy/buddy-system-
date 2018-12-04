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
		System.out.println("��������");
		System.out.println("�������,����GUI");
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
		 * ������ͼ����ѿ����������λ��λ��i����ѿ�������Ϊ��2��i-1�η�<request<2��i�η�
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
		throw new Exception("��������ڴ�����");
	}

	/**
	 * �ϲ�RAM�Ļ���
	 */
	private void mergeRAM() {
		// ��һ��boolean������ȷ��RAM��ȷʵû�п��Ժϲ��Ŀ�����
		// ��Ϊ��һ�κϲ��Ժ󣬿��ܻ����µĿ��Ժϲ��Ŀ���,����
		// 4,4,4,4 --��һ��--> 8,8
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
	 * @param request Ҫ���������
	 * @param index   Ҫ����Ŀ����λ��
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
			result.setMessage("�µ��ڴ����� ��СΪ : " + newStore + "bytes");
			System.out.println("�µ��ڴ����� ��СΪ : " + newStore + "bytes");
			BuddyAlloc(new Block("X",newStore));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			result.setMessage("����ܾ���û���ҵ����ʵ��ڴ�飡");
			System.out.println("����ܾ���û���ҵ����ʵ��ڴ�飡");
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
				result.setMessage(result.getMessage() + "\n�ͷ��ڴ�� : " + index);
				System.out.println("�ͷ��ڴ�� : " + index);
				releaseBlock(index);
				mergeRAM();
			}
		}
		updateGUI();
	}
}
