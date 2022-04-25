package bytecodeInterpreter;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class OpcodeString {
	private byte[] bytes;
	public static String opcodes;
	
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
	public OpcodeString(byte[] bytes) {
		setBytes(bytes);
	}

	private String stringify() throws UnsupportedEncodingException
	{
		for(int index=0;index<bytes.length;index++)
		{
			switch(bytes[index] & 0xff)
			{
			case 42:
				opcodes+="aload_0\n";
				break;
			case 183:
				opcodes+="invokespecial "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"\n";
				index+=2;
				break;
			case 177:
				opcodes+="return\n";
				break;
			case 178:
				opcodes+="getstatic "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"\n";
				index+=2;
				break;
			case 18:
				opcodes+="ldc "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[bytes[index+1]-1].getInfo()[0]-1].getBytes())+"\n";
				index++;
				break;
			case 182:
				opcodes+="invokevirtual "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[((bytes[index+1] << 8)+bytes[index+2])-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"\n";
				index+=2;
				break;
			default:
				opcodes+=bytes[index] & 0xff;
				opcodes+="\n";
			}
		}
		
		return opcodes;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String toString()
	{
		try {
			return stringify();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}