package bytecodeInterpreter;

public class LocalVariableType {
	private int startPc;
	private int length;
	private int nameIndex;
	private int signatureIndex;
	private int index;
	
	public LocalVariableType(int startPc, int length, int nameIndex, int signatureIndex, int index) {
		setStartPc(startPc);
		setLength(length);
		setNameIndex(nameIndex);
		setSignatureIndex(signatureIndex);
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
	public int getSignatureIndex() {
		return signatureIndex;
	}
	public void setSignatureIndex(int signatureIndex) {
		this.signatureIndex = signatureIndex;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
