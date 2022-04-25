package bytecodeInterpreter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Attribute {
	private int nameIndex;
	private int length;
	private Object[] info;
	
	private String byteToString(byte[] bytes) throws UnsupportedEncodingException
	{
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		for(int index=0;index<bytes.length;index++)
		buffer.put(bytes[index]);
		String newContent = null;
		if (buffer.hasArray()) {
	        newContent = new String(buffer.array(), "UTF-8");
	    }
		return newContent;
	}
	
	public Attribute() throws IOException {
		Object attributeInfo[] = null;
		System.out.println("test "+BytecodeParse.constantPool.length);
		int attributeNameIndex=BytecodeParse.inputStream.readUnsignedShort();
		System.out.println("name index: "+attributeNameIndex);
		int attributeLength=BytecodeParse.inputStream.readInt();
		//System.out.println("switch: "+byteToString(BytecodeParse.constantPool[attributeNameIndex-1].getBytes()));
		switch(byteToString(BytecodeParse.constantPool[attributeNameIndex-1].getBytes()))
		{
		case "ConstantValue":
			attributeInfo=new Object[1];
			info[0]=BytecodeParse.inputStream.readUnsignedShort();
			break;
		case "Code":
			attributeInfo=new Object[10]; 
			attributeInfo[0]=BytecodeParse.inputStream.readUnsignedShort();
			attributeInfo[1]=BytecodeParse.inputStream.readUnsignedShort();
			attributeInfo[2]=BytecodeParse.inputStream.readInt();
			byte[] code=new byte[(int)attributeInfo[2]];
			for(int k=0;k<(int)attributeInfo[2];k++)
			{
				code[k]=(byte)BytecodeParse.inputStream.readUnsignedByte();
			}
			attributeInfo[3]=code;
			attributeInfo[4]=BytecodeParse.inputStream.readUnsignedShort(); //exception_table_length
			ExceptionType[] exceptionTable=new ExceptionType[(int)attributeInfo[4]];
			for(int k=0;k<(int)attributeInfo[4];k++)
			{
				int startPc=BytecodeParse.inputStream.readUnsignedShort();
				int endPc=BytecodeParse.inputStream.readUnsignedShort();
				int handlerPc=BytecodeParse.inputStream.readUnsignedShort();
				int catchType=BytecodeParse.inputStream.readUnsignedShort();
				exceptionTable[k]=new ExceptionType(startPc, endPc, handlerPc, catchType);
			}
			attributeInfo[5]=exceptionTable;
			attributeInfo[6]=BytecodeParse.inputStream.readUnsignedShort();
			Attribute codeAttributes[]=new Attribute[(int)attributeInfo[6]];
			for(int k=0;k<(int)attributeInfo[6];k++)
			{
				int codeAttributeNameIndex=BytecodeParse.inputStream.readUnsignedShort();
				int codeAttributeLength=BytecodeParse.inputStream.readInt();
				Object codeAttributeInfo[] = null;
				System.out.println("switch: "+byteToString(BytecodeParse.constantPool[codeAttributeNameIndex-1].getBytes()));
				switch(byteToString(BytecodeParse.constantPool[codeAttributeNameIndex-1].getBytes()))
				{
				case "LineNumberTable":
					codeAttributeInfo = new Object[2];
					codeAttributeInfo[0]=BytecodeParse.inputStream.readUnsignedShort();
					LineNumber[] lineNumbers=new LineNumber[(int) codeAttributeInfo[0]];
					for(int l=0;l<(int)codeAttributeInfo[0];l++)
					{
						int startPc=BytecodeParse.inputStream.readUnsignedShort();
						int lineNumber=BytecodeParse.inputStream.readUnsignedShort();
						lineNumbers[l]=new LineNumber(startPc, lineNumber);
					}
					codeAttributeInfo[1]=lineNumbers;
					break;
				case "LocalVariableTable":
					codeAttributeInfo=new Object[2];
					codeAttributeInfo[0]=BytecodeParse.inputStream.readInt();
					LocalVariable[] localVariables=new LocalVariable[(int)codeAttributeInfo[0]];
					for(int l=0;l<(int)codeAttributeInfo[0];l++)
					{
						int startPc=BytecodeParse.inputStream.readUnsignedShort();
						int thisLength=BytecodeParse.inputStream.readUnsignedShort();
						int thisNameIndex=BytecodeParse.inputStream.readUnsignedShort();
						int descriptorIndex=BytecodeParse.inputStream.readUnsignedShort();
						int codeIndex=BytecodeParse.inputStream.readUnsignedShort();
						localVariables[l]=new LocalVariable(startPc, thisLength, thisNameIndex, descriptorIndex, codeIndex);
					}
					codeAttributeInfo[1]=localVariables;
					break;
				case "LocalVariableTypeTable":
					codeAttributeInfo=new Object[2];
					codeAttributeInfo[0]=BytecodeParse.inputStream.readInt();
					LocalVariableType[] localVariableTypes=new LocalVariableType[(int)codeAttributeInfo[0]];
					for(int l=0;l<(int)codeAttributeInfo[0];l++)
					{
						int startPc=BytecodeParse.inputStream.readUnsignedShort();
						int thisLength=BytecodeParse.inputStream.readUnsignedShort();
						int thisNameIndex=BytecodeParse.inputStream.readUnsignedShort();
						int signatureIndex=BytecodeParse.inputStream.readUnsignedShort();
						int codeIndex=BytecodeParse.inputStream.readUnsignedShort();
						localVariableTypes[l]=new LocalVariableType(startPc, thisLength, thisNameIndex, signatureIndex, codeIndex);
					}
					codeAttributeInfo[1]=localVariableTypes;
					break;
				case "StackMapTable":
					codeAttributeInfo=new Object[2];
					codeAttributeInfo[0]=BytecodeParse.inputStream.readInt();
					for(int l=0;l<(int)codeAttributeInfo[0];l++)
					{
						
					}
				}
			}
			break;
		}
		setNameIndex(attributeNameIndex);
		setLength(attributeLength);
		setInfo(attributeInfo);
	}
	public int getNameIndex() {
		return nameIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Object[] getInfo() {
		return info;
	}
	public void setInfo(Object[] info) {
		this.info = info;
	}
	
	
}
