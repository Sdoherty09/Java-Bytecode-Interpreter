package bytecodeInterpreter;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Stack {
	private ArrayList<String> list;

	public Stack(ArrayList<String> list) {
		setList(list);
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}
	
	public ArrayList<String> push(String toPush) {
		list.add(0, toPush);
		return list;
	}
}