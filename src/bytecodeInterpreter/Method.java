package bytecodeInterpreter;

import java.util.Arrays;

public class Method {
	private int accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private int attributesCount;
	private Attribute[] attributes;
	public Method(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount, Attribute[] attributes) {
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
	public Attribute[] getAttributes () {
		return attributes;
	}
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	@Override
	public String toString() {
		return "Method [accessFlags=" + accessFlags + ", nameIndex=" + nameIndex + ", descriptorIndex="
				+ descriptorIndex + ", attributesCount=" + attributesCount + ", attributes="
				+ Arrays.toString(attributes) + "]";
	}
	
	
}
