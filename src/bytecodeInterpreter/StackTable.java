package bytecodeInterpreter;

import java.util.ArrayList;

public class StackTable {
	private ArrayList<String> stackItems;
	
	public StackTable() {
		setStackItems(new ArrayList<String>());
	}
	
	public ArrayList<String> getStackItems() {
		return stackItems;
	}
	
	public void setStackItems(ArrayList<String> stackItems) {
		this.stackItems = stackItems;
	}
	
	public int getLength() {
		return getStackItems().size();
	}
	
	public String get(int index) {
		return(getStackItems().get(index));
	}
	
	public void set(int index, String element) {
		getStackItems().set(index, element);
	}
	
	public void push(String toPush) {
		getStackItems().add(0, toPush);
	}
	public String pop() {
		ArrayList<String> temp=new ArrayList<String>();
		for(int index=1;index<getStackItems().size();index++)
		{
			temp.add(getStackItems().get(index));
		}
		String toReturn=getStackItems().get(0);
		setStackItems(temp);
		return toReturn;
	}
}
