package bytecodeInterpreter;

public class Method {
	private int accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private int attributesCount;
	public Method(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		setAccessFlags(accessFlags);
		setNameIndex(nameIndex);
		setDescriptorIndex(descriptorIndex);
		setAttributesCount(attributesCount);
	}
	public int getAccessFlags() {
		return accessFlags;
	}
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
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
	public int getAttributesCount() {
		return attributesCount;
	}
	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}
	
}
