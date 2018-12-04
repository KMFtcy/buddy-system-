package team.tw.ten.tang;

public class Block {
	private String taskName = null;
	private int storage = -1;
	private boolean isUsed = false;
	public Block(){}
	public Block(int initValue){
		storage = initValue;
	}
	public Block(String name,int initValue){
		taskName = name;
		storage = initValue;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	
	
	public int getStorage() {
		return storage;
	}
	public void setStorage(int storage) {
		if(storage>1) {
			this.storage = storage;
		}
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
}
