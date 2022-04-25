package bytecodeInterpreter;

public class ConstantPool {
	private int tag;
	private int[] info;
	private byte[] bytes;
	
	public ConstantPool(int tag, int[] info) {
		setTag(tag);
		setInfo(info);
	}
	
	public ConstantPool(int tag, byte[] bytes) {
		setTag(tag);
		setBytes(bytes);
	}
	
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int[] getInfo() {
		return info;
	}
	public void setInfo(int[] info) {
		this.info = info;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
