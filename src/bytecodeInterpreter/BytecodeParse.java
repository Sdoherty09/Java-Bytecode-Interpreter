package bytecodeInterpreter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class BytecodeParse {
	private byte[] bytecodeBytes;
	public static DataInputStream inputStream;
	public static ConstantPool[] constantPool;

	public BytecodeParse(byte[] bytecodeBytes) {
		setBytecodeBytes(bytecodeBytes);
	}

	public byte[] getBytecodeBytes() {
		return bytecodeBytes;
	}

	public void setBytecodeBytes(byte[] bytecodeBytes) {
		this.bytecodeBytes = bytecodeBytes;
	}
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
	public void parse() throws IOException {
		inputStream = new DataInputStream(new ByteArrayInputStream(bytecodeBytes));
		int magic=inputStream.readInt();
		int minorVersion=inputStream.readUnsignedShort();
		int majorVersion=inputStream.readUnsignedShort();
		int constantPoolCount=inputStream.readUnsignedShort();
		int tag;
		int[] info = null;
		byte[] bytes = null;
		constantPool = new ConstantPool[constantPoolCount-1];
		for(int index=0;index<constantPoolCount-1;index++)
		{
			tag=inputStream.readUnsignedByte();
			switch(tag)
			{
			case 7: case 8: case 16:
				info=new int[1];
				info[0]=inputStream.readUnsignedShort();
				break;
			case 9: case 10: case 11: case 18: case 12:
				info=new int[2];
				info[0]=inputStream.readUnsignedShort();
				info[1]=inputStream.readUnsignedShort();
				break;
			case 3: case 4:
				info=new int[1];
				info[0]=inputStream.readInt();
				break;
			case 5: case 6:
				info=new int[2];
				info[0]=inputStream.readInt();
				info[1]=inputStream.readInt();
				break;
			case 1:
				int length=inputStream.readUnsignedShort();
				bytes = new byte[length];
				for(int j=0;j<length;j++)
				{
					bytes[j]=(byte)inputStream.readUnsignedByte();
				}
				break;
			case 15:
				info=new int[2];
				info[0]=inputStream.readUnsignedByte();
				info[1]=inputStream.readUnsignedShort();
				break;
			}
			if (tag==1) constantPool[index] = new ConstantPool(tag, bytes);
			else constantPool[index] = new ConstantPool(tag, info);
		}
		for(int index=0;index<constantPool.length;index++)
		{
			if(constantPool[index].getTag()==1)
			{
				System.out.println(byteToString(constantPool[index].getBytes()));
			}
		}
		int accessFlags=inputStream.readUnsignedShort();
		System.out.println("access flags: "+accessFlags);
		int thisClass=inputStream.readUnsignedShort();
		//System.out.println("this class: "+byteToString(constantPool[constantPool[thisClass-1].getInfo()[0]-1].getBytes()));
		int superClass=inputStream.readUnsignedShort();
		System.out.println("super class: "+byteToString(constantPool[constantPool[superClass-1].getInfo()[0]-1].getBytes()));
		int interfacesCount=inputStream.readUnsignedShort();
		System.out.println("interfaces count: "+interfacesCount);
		ConstantPool interfaces[] = new ConstantPool[interfacesCount];
		for(int index=0;index<interfacesCount;index++)
		{
			tag=inputStream.readUnsignedByte();
			info=new int[1];
			info[0]=inputStream.readUnsignedShort();
			System.out.println(tag);
			interfaces[index]=new ConstantPool(tag, info);
		}
		int fieldsCount=inputStream.readUnsignedShort();
		Field fields[] = new Field[fieldsCount];
		for(int index=0; index<fieldsCount;index++)
		{
			int fieldAccessFlags=inputStream.readUnsignedShort();
			int fieldNameIndex=inputStream.readUnsignedShort();
			int fieldDescriptorIndex=inputStream.readUnsignedShort();
			int attributesCount=inputStream.readUnsignedShort();
			Attribute attributes[]=new Attribute[attributesCount];
			for(int j=0;j<attributesCount;j++)
			{
				attributes[j]=new Attribute();
			}
		}
		int methodsCount=inputStream.readUnsignedShort();
		Method[] methods=new Method[methodsCount];
		for(int index=0;index<methodsCount;index++)
		{
			int methodAccessFlags=inputStream.readUnsignedShort();
			int methodNameIndex=inputStream.readUnsignedShort();
			int methodDescriptorIndex=inputStream.readUnsignedShort();
			int methodAttributesCount=inputStream.readUnsignedShort();
			//System.out.println("attribute count: " + byteToString(constantPool[inputStream.readUnsignedShort()-1].getBytes()));
			Attribute[] methodAttributes=new Attribute[methodAttributesCount];
			for(int j=0;j<methodAttributesCount;j++)
			{
				methodAttributes[j]=new Attribute();
				System.out.println("method attribute: "+byteToString(constantPool[methodAttributes[j].getNameIndex()-1].getBytes()));
			}
			
		}
		System.out.println("methods count: "+methodsCount);
	}
}
//readUnsignedByte, readUnsignedShort, and readInt 