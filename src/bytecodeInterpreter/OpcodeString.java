package bytecodeInterpreter;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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
		int bytesNum;
		for(int index=0;index<bytes.length;index++)
		{
			switch(bytes[index] & 0xff)
			{
			case 42:
				temp.add(index+ ": aload_0");
				break;
			case 183:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": invokespecial\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"."+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes())+":"+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[1]-1].getBytes()));
				index+=2;
				break;
			case 177:
				temp.add(index+": return\n");
				break;
			case 178:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": getstatic\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"."+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes())+":"+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[1]-1].getBytes()));
				index+=2;
				break;
			case 18:
				if(BytecodeParse.constantPool[(bytes[index+1] & 0xff)-1].getTag()==8)
				{
					temp.add(index+ ": ldc\t String "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[(bytes[index+1] & 0xff)-1].getInfo()[0]-1].getBytes()));
				}
				else if(BytecodeParse.constantPool[(bytes[index+1] & 0xff)-1].getTag()==1)
				{
					temp.add(index+ ": ldc\t String "+byteToString(BytecodeParse.constantPool[(bytes[index+1] & 0xff)-1].getBytes()));
				}
				else
				{
					temp.add(index+ ": ldc\t int "+BytecodeParse.constantPool[(bytes[index+1] & 0xff)-1].getInfo()[0]);
				}
				index++;
				break;
			case 182:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+ ": invokevirtual\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"."+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes())+":"+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[1]-1].getBytes()));
				index+=2;
				break;
			case 184:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": invokestatic\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"."+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes())+":"+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[1]-1].getBytes()));
				index+=2;
				break;
			case 3:
				temp.add(index+ ": iconst_0");
				break;
			case 76:
				temp.add(index+": astore_1");
				break;
			case 77:
				temp.add(index+": astore_2");
				break;
			case 59:
				temp.add(index+": istore_0");
				break;			
			case 60:
				temp.add(index+": istore_1");
				break;
			case 61:
				temp.add(index+": istore_2");
				break;
			case 62:
				temp.add(index+": istore_3");
				break;
			case 54:
				temp.add(index+": istore\t "+bytes[index+1]);
				index++;
				break;
			case 21:
				temp.add(index+": iload\t "+bytes[index+1]);
				index++;
				break;
			case 28:
				temp.add(index+": iload_2");
				break;
			case 29:
				temp.add(index+": iload_3");
				break;
			case 20: //TODO: finish
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);	
				if(BytecodeParse.constantPool[bytesNum].getTag()==5)
				{
					long bits = (((long)BytecodeParse.constantPool[bytesNum].getInfo()[0]) << 32)+(BytecodeParse.constantPool[bytesNum].getInfo()[1]); //CORRECT
					temp.add(index+": ldc2_w\t " + bits);
				}
				else
				{
					long bits = (((long)BytecodeParse.constantPool[bytesNum].getInfo()[0]) << 32)+(BytecodeParse.constantPool[bytesNum].getInfo()[1]); //CORRECT PROB
					int s = ((bits >> 63) == 0) ? 1 : -1;
					int e = (int)((bits >> 52) & 0x7ffL);
					long m = (e == 0) ?
					           (bits & 0xfffffffffffffL) << 1 :
					           (bits & 0xfffffffffffffL) | 0x10000000000000L;
					double result = s*m*(2^(e-1075));
					temp.add(index+": ldc2_w\t " + (result));
				}
				
				index+=2;
				break;
			case 135:
				temp.add(index+": i2d");
				break;
			case 16:
				temp.add(index+": bipush\t "+bytes[index+1]);
				index++;
				break;
			case 159:
				temp.add(index+": if_icmpeq\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 161:
				temp.add(index+": if_icmplt\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 162:
				temp.add(index+": if_icmpge\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 163:
				temp.add(index+": if_icmpgt\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 164:
				temp.add(index+": if_icmple\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 132:
				temp.add(index+": iinc\t\t "+bytes[index+1]+", "+bytes[index+2]);
				index+=2;
				break;
			case 167:
				temp.add(index+": goto\t "+(index+(short)((bytes[index+1]& 0xff << 8)+(bytes[index+2] & 0xff))));
				index+=2;
				break;
			case 187:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": new\t " +byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 26:
				temp.add(index+": iload_0");
				break;
			case 5:
				temp.add(index+ ": iconst_2");
				break;
			case 112:
				temp.add(index+ ": irem");
				break;
			case 154:
				temp.add(index+": ifne\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 4:
				temp.add(index+ ": iconst_1");
				break;
			case 172:
				temp.add(index+ ": ireturn\n");
				break;
			case 27:
				temp.add(index+": iload_1");
				break;
			case 87:
				temp.add(index+": pop");
				break;
			case 6:
				temp.add(index+": iconst_3");
				break;
			case 189:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": anewarray\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 2:
				temp.add(index+": iconst_m1");
				break;
			case 96:
				temp.add(index+": iadd");
				break;
			case 43:
				temp.add(index+": aload_1");
				break;
			case 44:
				temp.add(index+": aload_2");
				break;
			case 45:
				temp.add(index+": aload_3");
				break;
			case 190:
				temp.add(index+": arraylength");
				break;
			case 89:
				temp.add(index+": dup");
				break;
			case 83:
				temp.add(index+": aastore");
				break;
			case 50:
				temp.add(index+": aaload");
				break;
			case 181:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": putfield\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 160:
				temp.add(index+": if_icmpne\t"+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 180:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": getfield\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 176:
				temp.add(index+": areturn\n");
				break;
			case 185:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": invokeinterface\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getInfo()[0]-1].getBytes())+"."+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes())+":"+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[1]-1].getBytes()));
				index+=4;
				//TODO: finish?
				break;
			case 186:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add(index+": invokedynamic\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[1]-1].getInfo()[0]-1].getBytes()));
				index+=4;
				break;
			case 171:
				temp.add(index+": lookupswitch");
				int offset=index+1;
				while(offset%4!=0)
				{
					offset++;
				}
				int defaultBytes=((bytes[offset] << 24) | (bytes[offset+1] << 16) | (bytes[offset+2] << 8) | (bytes[offset+3] << 0));
				offset+=4;
				int npairs=((bytes[offset] << 24) | (bytes[offset+1] << 16) | (bytes[offset+2] << 8) | (bytes[offset+3] << 0));
				for(int j=0;j<npairs;j++)
				{
					index+=8;
				}
				break;
			case 192:
				bytesNum=(bytes[index+1] << 8)+(bytes[index+2] & 0xff);
				temp.add("checkcast\t "+byteToString(BytecodeParse.constantPool[BytecodeParse.constantPool[bytesNum-1].getInfo()[0]-1].getBytes()));
				index+=2;
				break;
			case 153:
				temp.add(index+": ifeq\t "+(index+(short)((bytes[index+1]& 0xff << 8)+bytes[index+2]& 0xff)));
				index+=2;
				break;
			case 8:
				temp.add(index+": iconst_5");
				break;
			case 7:
				temp.add(index+": iconst_4");
				break;
			case 46:
				temp.add(index+": iaload");
				break;
			case 188:
				String toAdd="newarray\t ";
				switch(bytes[index+1])
				{
				case 4:
					toAdd+="boolean";
					break;
				case 5:
					toAdd+="char";
					break;
				case 6:
					toAdd+="float";
					break;
				case 7:
					toAdd+="double";
					break;
				case 8:
					toAdd+="byte";
					break;
				case 9:
					toAdd+="short";
					break;
				case 10:
					toAdd+="int";
					break;
				case 11:
					toAdd+="long";
					break;				
				}
				temp.add(index+": "+toAdd);
				index++;
				break;
			case 79:
				temp.add(index+": iastore");
				break;
			case 100:
				temp.add(index+": isub");
				break;
			case 88:
				temp.add(index+": pop2");
				break;
			case 95:
				temp.add(index+": swap");
				break;
			case 108:
				temp.add(index+": idiv");
				break;
			case 104:
				temp.add(index+": imul");
				break;
			case 63:
				temp.add(index+": lstore_0");
				break;
			case 64:
				temp.add(index+": lstore_1");
				break;
			case 65:
				temp.add(index+": lstore_2");
				break;
			case 66:
				temp.add(index+": lstore_3");
				break;
			case 145:
				temp.add(index+": i2b");
				break;
			case 146:
				temp.add(index+": i2c");
				break;
			case 134:
				temp.add(index+": i2f");
				break;
			case 133:
				temp.add(index+": i2l");
				break;
			case 147:
				temp.add(index+": i2s");
				break;
			case 126:
				temp.add(index+": iand");
				break;
			case 116:
				temp.add(index+": ineg");
				break;
			case 128:
				temp.add(index+": ior");
				break;
			case 120:
				temp.add(index+": ishl");
				break;
			case 122:
				temp.add(index+": ishr");
				break;
			case 136:
				temp.add(index+": l2i");
				break;
			case 137:
				temp.add(index+": l2f");
				break;
			case 97:
				temp.add(index+": ladd");
				break;
			case 127:
				temp.add(index+": land");
				break;
			case 117:
				temp.add(index+": lneg");
				break;
			case 129:
				temp.add(index+": lor");
				break;
			case 113:
				temp.add(index+": lrem");
				break;
			case 121:
				temp.add(index+": lshl");
				break;
			case 123:
				temp.add(index+": lshr");
				break;
			case 55:
				temp.add(index+": lstore");
				break;
			case 101:
				temp.add(index+": lsub");
				break; //start
			case 138:
				temp.add(index+": l2d");
				break;
			case 109:
				temp.add(index+": ldiv");
				break;
			case 105: //94
				temp.add(index+": lmul");
				break;
			case 0:
				temp.add(index+": nop");
				break;
			case 53:
				temp.add(index+": saload");
				break;
			case 86:
				temp.add(index+": sastore");
				break;
			case 17:
				temp.add(index+": sipush\t "+bytes[index+1]);
				index++;
				break;
			case 1:
				temp.add(index+": aconst_null");
				break;
			case 51:
				temp.add(index+": baload");
				break;
			case 84:
				temp.add(index+": bastore");
				break;
			case 52:
				temp.add(index+": caload");
				break;
			case 85:
				temp.add(index+": castore");
				break;
			case 90:
				temp.add(index+": dup_x1");
				break;
			case 91:
				temp.add(index+": dup_x2");
				break;
			case 92:
				temp.add(index+": dup2");
				break;
			case 199:
				temp.add(index+": ifnonnull\t "+(index+(short)((bytes[index+1]& 0xff << 8)+(bytes[index+2] & 0xff))));
				index+=2;
				break;
			case 198: //108
				temp.add(index+": ifnull\t "+(index+(short)((bytes[index+1]& 0xff << 8)+(bytes[index+2] & 0xff))));
				index+=2;
				break;
			default:
				temp.add(""+(bytes[index] & 0xff));
				break;
			}
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
