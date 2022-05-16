package bytecodeInterpreter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class BytecodeParse {
	private byte[] bytecodeBytes;
	public static DataInputStream inputStream;
	public static ConstantPool[] constantPool;
	public String opcodeString="";
	private OpcodeString opcodeStringify;
	public static ArrayList<String> opcodes;
	private ArrayList<byte[]> codeBytes;
	private ArrayList<Method> codeMethods;
	private HashMap<byte[], String> classNames;
	
	public BytecodeParse(byte[] bytecodeBytes) {
		setBytecodeBytes(bytecodeBytes);
		codeBytes = new ArrayList<byte[]>();
		codeMethods = new ArrayList<Method>();
		classNames = new HashMap<byte[], String>();
	}
	
	
	
	public HashMap<byte[], String> getClassNames() {
		return classNames;
	}



	public void setClassNames(HashMap<byte[], String> classNames) {
		this.classNames = classNames;
	}



	public ArrayList<Method> getCodeMethods() {
		return codeMethods;
	}

	public void setCodeMethods(ArrayList<Method> codeMethods) {
		this.codeMethods = codeMethods;
	}

	public ArrayList<byte[]> getCodeBytes() {
		return codeBytes;
	}

	public void setCodeBytes(ArrayList<byte[]> codeBytes) {
		this.codeBytes = codeBytes;
	}

	public String getOpcodeString() {
		return opcodeString;
	}

	public void setOpcodeString(String opcodeString) {
		this.opcodeString = opcodeString;
	}

	public byte[] getBytecodeBytes() {
		return bytecodeBytes;
	}

	public void setBytecodeBytes(byte[] bytecodeBytes) {
		this.bytecodeBytes = bytecodeBytes;
	}
	
	public OpcodeString getOpcodeStringify() {
		return opcodeStringify;
	}

	public void setOpcodeStringify(OpcodeString opcodeStringify) {
		this.opcodeStringify = opcodeStringify;
		opcodes=null;
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
		//inputStream = new DataInputStream((ByteArrayInputStream)Object.class.getResource("StringBuilder.class").getContent());
		//System.out.println(Object.class.getResource("String.class").getPath());
		inputStream = new DataInputStream(new ByteArrayInputStream(bytecodeBytes));
		int accessFlags=0;
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
				index++;
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
				//System.out.println(index);
				//System.out.println(index+": "+byteToString(bytes));
				break;
			case 15:
				info=new int[2];
				info[0]=inputStream.readUnsignedByte();
				info[1]=inputStream.readUnsignedShort();
				break;
			case 0:
				accessFlags=inputStream.readUnsignedByte();
				break;
			default:
				System.out.println("NOT HERE");
				break;
			}
			if (tag==1) constantPool[index] = new ConstantPool(tag, bytes);
			else constantPool[index] = new ConstantPool(tag, info);
		}
		if(accessFlags==0)
		{
			accessFlags=inputStream.readUnsignedShort();
		}
		
		int thisClass=inputStream.readUnsignedShort();
		//System.out.println("this class: "+byteToString(constantPool[constantPool[thisClass-1].getInfo()[0]-1].getBytes()));
		int superClass=inputStream.readUnsignedShort();
		int interfacesCount=inputStream.readUnsignedShort();
		int[] interfaces = new int[interfacesCount];
		//System.out.println(interfacesCount);
		//ConstantPool interfaces[] = new ConstantPool[interfacesCount];
		for(int index=0;index<interfacesCount;index++)
		{
			interfaces[index]=inputStream.readUnsignedShort();
			//System.out.println(byteToString(constantPool[interfaces[index]].getBytes()));
		}
		int fieldsCount=inputStream.readUnsignedShort();
		Field fields[] = new Field[fieldsCount];
		for(int index=0; index<fieldsCount;index++)
		{
			int fieldAccessFlags=inputStream.readUnsignedShort();
			int fieldNameIndex=inputStream.readUnsignedShort();
			int fieldDescriptorIndex=inputStream.readUnsignedShort();
			System.out.println(byteToString(constantPool[fieldDescriptorIndex-1].getBytes()));
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
			int methodNameIndex=inputStream.readUnsignedShort(); //correct prob		
			int methodDescriptorIndex=inputStream.readUnsignedShort(); //not??
			//System.out.println(byteToString(constantPool[methodNameIndex-1].getBytes())+byteToString(constantPool[methodDescriptorIndex-1].getBytes()));
			int methodAttributesCount=inputStream.readUnsignedShort();
			Attribute[] methodAttributes=new Attribute[methodAttributesCount];
			for(int j=0;j<methodAttributesCount;j++)
			{
				methodAttributes[j]=new Attribute();
				if(byteToString(constantPool[methodAttributes[j].getNameIndex()-1].getBytes()).equals("Code"))
				{
					//System.out.println("access flags: "+methodAccessFlags);
					codeBytes.add((byte[])methodAttributes[j].getInfo()[3]);
					classNames.put((byte[])methodAttributes[j].getInfo()[3], byteToString(constantPool[methodNameIndex-1].getBytes())+byteToString(constantPool[methodDescriptorIndex-1].getBytes()));
					opcodeStringify=new OpcodeString((byte[])methodAttributes[j].getInfo()[3], byteToString(constantPool[methodNameIndex-1].getBytes())+byteToString(constantPool[methodDescriptorIndex-1].getBytes()));
					opcodeString += opcodeStringify.toString();
					codeMethods.add(new Method(methodAccessFlags, methodNameIndex, methodDescriptorIndex, methodAttributesCount, methodAttributes));
				}
			}
			methods[index] = new Method(methodAccessFlags, methodNameIndex, methodDescriptorIndex, methodAttributesCount, methodAttributes);
		}
	}
}
//readUnsignedByte, readUnsignedShort, and readInt 