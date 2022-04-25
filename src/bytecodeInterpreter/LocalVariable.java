package bytecodeInterpreter;

public class LocalVariable {
	private int startPc;
	private int length;
	private int nameIndex;
	private int descriptorIndex;
	private int index;
	public LocalVariable(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
		setStartPc(startPc);
		setLength(length);
		setNameIndex(nameIndex);
		setDescriptorIndex(index);
		setIndex(index);
	}
	public int getStartPc() {
		return startPc;
	}
	public void setStartPc(int startPc) {
		this.startPc = startPc;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getNameIndex() {
		return nameIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
