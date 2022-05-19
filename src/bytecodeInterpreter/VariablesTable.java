package bytecodeInterpreter;

import java.util.HashMap;

public class VariablesTable {
	private HashMap<Integer, Object> variableItems;
	public VariablesTable() {
		variableItems=new HashMap<Integer, Object>();
	}
	public HashMap<Integer, Object> getVariableItems() {
		return variableItems;
	}
	public void setVariableItems(HashMap<Integer, Object>variableItems) {
		this.variableItems = variableItems;
	}
	public int getLength() {
		return getVariableItems().size();
	}
	public Object get(int index) {
		return(getVariableItems().get(index));
	}
	public void replace(int index, Object replace) {
		getVariableItems().replace(index, replace);
	}
	public void put(int key, Object element) {
		variableItems.put(key, element);
	}
}
