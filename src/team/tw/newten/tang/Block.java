package team.tw.newten.tang;

public class Block {
	public int size;// 这个内存块的大小
	public int address;// 这个内存块的地址
	public boolean isUsed;

	public Block(int size, int address) {
		this.size = size;
		this.address = address;
		isUsed = false;
	}

	public Block(Block copyBlock) {
		this.size = copyBlock.size;
		this.address = copyBlock.address;
		isUsed = false;
	}

	public Block split() {
		this.setSize(size / 2);
		Block splitBlock = new Block(this);
		this.setAddress(address + size);
		return splitBlock;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
}
