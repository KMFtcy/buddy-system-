package team.tw.newten.tang;

import javax.swing.ComboBoxModel;

public final class Constant {
	public static int[] MEMORY_SIZE_OPTION = { (int) (512 * Math.pow(2,10)) , (int) (256 * Math.pow(2,10))};
	public static int[] PAGE_SIZE_OPTION = { 4,2,1};
//	static int MEMORY_SIZE = (int) (512 * Math.pow(2,10));
	public static int MEMORY_SIZE = MEMORY_SIZE_OPTION[0];
	public static int PAGE_SIZE = PAGE_SIZE_OPTION[0];
	public final static int ORDER = 10;//代表有几组内存块
	
}
