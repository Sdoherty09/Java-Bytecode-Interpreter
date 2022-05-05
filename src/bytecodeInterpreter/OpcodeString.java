package bytecodeInterpreter;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class OpcodeString {
	private byte[] bytes;
	private String className;
	
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
	public OpcodeString(byte[] bytes, String className) {
		setBytes(bytes);
		setClassName(className);
	}

	public OpcodeString(byte[] bytes) {
		setBytes(bytes);
	}
	
	public ArrayList<String> stringify() throws UnsupportedEncodingException
	{
		ArrayList<String> temp = new ArrayList<String>();
		if(className!=null) temp.add(className+":");		
		for(int index=0;index<bytes.length;index++)
		{
			switch(bytes[index] & 0xff)
			{
			case 42:
				temp.add(index+ ": aload_0");
				break;
			case 183:
				temp.add(index+": invokespecial\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 177:
				temp.add(index+": return\n");
				break;
			case 178:
				temp.add(index+ ": getstatic\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 18:
				temp.add(index+ ": ldc\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[bytes[index+1]-1].getInfo()[0]-1].getBytes()));
				index++;
				break;
			case 182:
				temp.add(index+ ": invokevirtual\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 3:
				temp.add(index+ ": iconst_0");
				break;
			case 60:
				temp.add(index+": istore_1");
				break;
			case 61:
				temp.add(index+": istore_2");
				break;
			case 28:
				temp.add(index+": iload_2");
				break;
			case 16:
				temp.add(index+": bipush\t "+bytes[index+1]);
				index++;
				break;
			case 162:
				temp.add(index+": if_icmpge\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 132:
				temp.add(index+": iinc\t\t "+bytes[index+1]+", "+bytes[index+2]);
				index+=2;
				break;
			case 167:
				temp.add(index+": goto\t "+(index+(short)((bytes[index+1]& 0xff << 8)+(bytes[index+2] & 0xff))));
				System.out.println((bytes[index+1])+" "+bytes[index+2]);
				index+=2;
				break;
			case 187:
				temp.add(index+": new\t " +byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			default:
				temp.add(""+(bytes[index] & 0xff));
				break;
			}
			System.out.println(temp.get(temp.size()-1));
		}
		return temp;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className=className;
	}
	public ArrayList<String> getOpcodes() {
		return BytecodeParse.opcodes;
	}
	public void setOpcodes(ArrayList<String> opcodes) {
		BytecodeParse.opcodes = opcodes;
	}
	
	@Override
	public String toString()
	{
		String output="";
		ArrayList<String> opcodes;
		try {
			opcodes = stringify();
			for(int index=0;index<opcodes.size();index++)
			{
				output+=opcodes.get(index)+"\n";
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return output;
	}
}
