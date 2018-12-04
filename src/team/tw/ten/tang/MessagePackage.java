package team.tw.ten.tang;

import java.util.ArrayList;

public class MessagePackage {
	ArrayList<Block> RAM = null;
	boolean isSucceed = false;
	//需要打印出来的信息
	String message = null;
	public ArrayList<Block> getRAM() {
		return RAM;
	}
	public void setRAM(ArrayList<Block> rAM) {
		RAM = rAM;
	}
	public boolean isSucceed() {
		return isSucceed;
	}
	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
