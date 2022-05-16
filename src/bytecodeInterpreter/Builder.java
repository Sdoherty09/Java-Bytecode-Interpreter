package bytecodeInterpreter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.FileDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Builder {

	protected Shell shell;
	private Text text_1;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table_1;
	private Table table_2;
	private BytecodeParse bytecodeParse;
	int highlightSelection=1;
	private Table table_3;
	private Table table_4;
	private HashMap<Integer, Object> variables;
	private int nextStep=-1;
	private Text text_2;
	private String javaFile = null;
	private OpcodeString opcodeString;
	private Text text_3;
	private String cmdResponse(String command)
	{
		String response="";
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
	    builder.redirectErrorStream(true);
	    Process p=null;
		try {
			p = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
			}
	    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line="";
	    while (true) {
	            try {
					line = r.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            if (line == null) { break; }
	            response+=line;
	        }
	    return response;
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
	private String arrayString(Object[] array)
	{
		String build="[";
		for(int index=0;index<array.length;index++)
		{
			if(array[index]==null) build+=" null,";
			else build+=" "+array[index]+",";
		}
		build=build.substring(0, build.lastIndexOf(","))+" ]";
		return build;
	}
	private boolean canTraverseTo(TableItem start, TableItem end)
	{
		for(int index=0;index<table_1.getItemCount();index++)
		{
			if(table_1.getItem(index)==end) return false;
			if(table_1.getItem(index)==start) return true;
		}
		return false;
	}
	private Integer[] toIntArray(String stringArray)
	{
		Object[] objectArray=toObjectArray(stringArray);
		Integer[] returnArray=new Integer[objectArray.length];
		for(int index=0;index<returnArray.length;index++)
		{
			if(objectArray[index]!=null) returnArray[index]=(int)objectArray[index];
			else returnArray[index] = null;
		}
		return returnArray;
	}
	private Object[] toObjectArray(String stringArray)
	{
		System.out.println("array: "+stringArray);
		stringArray=stringArray.substring(stringArray.indexOf("[")+1);
		ArrayList<Object> arrayList=new ArrayList<Object>();
		String currentNum=null;
		while(stringArray.indexOf(",")!=-1)
		{	
			currentNum=stringArray.substring(1, stringArray.indexOf(","));
			if(!currentNum.equals("null")) arrayList.add(Integer.parseInt(currentNum));
			else arrayList.add(null);
			stringArray=stringArray.substring(stringArray.indexOf(",")+1);
		}
		currentNum=stringArray.substring(1, stringArray.lastIndexOf(" "));
		if(!currentNum.equals("null")) arrayList.add(Integer.parseInt(currentNum));
		else arrayList.add(null);
		return arrayList.toArray();
	}
	private void variableReplace(int num, Object replace)
	{
		variables.replace(num, replace);
		for(int index=0;index<table_4.getItemCount();index++)
		{
			if(table_4.getItem(index).getText().contains("Variable "+num+":"))
			{
				table_4.getItem(index).setText("Variable "+num+": "+variables.get(num));
				Display display = Display.getDefault();
				table_4.getItem(index).setBackground(0, new Color(display, 210, 0, 120));
			}
		}
	}
	private void getSelection() throws UnsupportedEncodingException {
		String selection = table_1.getItem(highlightSelection).getText();
		selection = selection.substring(selection.indexOf(':')+2);
		String parameter = "";
		nextStep=-1;
		if(selection.contains("\t"))
		{
			parameter=selection.substring(selection.lastIndexOf("\t")+2);
			selection=selection.substring(0, selection.indexOf("\t"));
		}
		ArrayList<TableItem> tableItems=new ArrayList<TableItem>();
		for(int index=1;index<table_3.getItemCount();index++)
		{
			tableItems.add(table_3.getItem(index));
		}
		
		
		ArrayList<String> list = new ArrayList<String>();
		for(int index=0;index<tableItems.size();index++)
		{
			list.add(tableItems.get(index).getText());
		}
		Stack stack=new Stack(list);
		TableItem tableItem;
		String newContent;
		TableItem item;
		Display display = Display.getDefault();
		Object[] array;
		HashMap<TableItem, Integer> references = new HashMap<TableItem, Integer>();
		int num1;
		int num2;
		
		switch(selection) {
		case "aload_0":
			list=stack.push((String)variables.get(0));
			table_3.setItemCount(table_3.getItemCount()+1);
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			
			text_2.setText("Load the value "+variables.get(0)+" from variable 0, pushing it to the stack");
			break;
		case "aload_1":
			list=stack.push((String)variables.get(1));
			table_3.setItemCount(table_3.getItemCount()+1);
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(1)+" from variable 1, pushing it to the stack");
			break;
		case "aload_2":
			list=stack.push((String)variables.get(2));
			table_3.setItemCount(table_3.getItemCount()+1);
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(2)+" from variable 2, pushing it to the stack");
			break;
		case "iconst_0":
			list=stack.push("0");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 0 onto the stack");
			break;
		case "arraylength":
			array=toObjectArray(table_3.getItem(1).getText());
			list=stack.pop();
			list=stack.push(Integer.toString(array.length));
			text_2.setText("Push the length of the array at the top of the stack");
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			break;
		case "iconst_1":
			list=stack.push("1");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 1 onto the stack");
			break;
		case "iconst_2":
			list=stack.push("2");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 2 onto the stack");
			break;
		case "iconst_3":
			list=stack.push("3");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 3 onto the stack");
			break;
		case "iconst_4":
			list=stack.push("4");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 4 onto the stack");
			break;
		case "iconst_5":
			list=stack.push("5");
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the value 5 onto the stack");
			break;
		
		case "istore_1":
			item=new TableItem(table_4, SWT.NONE);
			item.setBackground(0, new Color(display, 210, 0, 120));
			item.setText("Variable 1: "+table_3.getItem(1).getText());
			variables.put(1, Integer.parseInt(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			table_3.setItemCount(table_3.getItemCount()-1);
			list=stack.pop();
			
			break;
		case "istore_2":
			item=new TableItem(table_4, SWT.NONE);
			item.setBackground(0, new Color(display, 210, 0, 120));
			item.setText("Variable 2: "+table_3.getItem(1).getText());
			variables.put(2, Integer.parseInt(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 2");
			table_3.setItemCount(table_3.getItemCount()-1);
			list=stack.pop();
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			
			break;
		case "istore_3":
			item=new TableItem(table_4, SWT.NONE);
			item.setBackground(0, new Color(display, 210, 0, 120));
			item.setText("Variable 3: "+table_3.getItem(1).getText());
			variables.put(3, Integer.parseInt(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 3");
			table_3.setItemCount(table_3.getItemCount()-1);
			list=stack.pop();
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			
			break;
		case "newarray":
			System.out.println(table_3.getItem(1).getText());
			array = new Object[Integer.parseInt(table_3.getItem(1).getText())];
			list=stack.pop();
			list=stack.push(arrayString(array));
			text_2.setText("Create a new array of type "+parameter);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			//
			/*switch(parameter)
			{
			case "boolean":
				boolean[] array_bool=(boolean[])array;
				break;
			case "char":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "float":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "double":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "byte":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "short":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "int":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
			case "long":
				boolean[] array=new boolean[Integer.parseInt(table_3.getItem(1).getText())];
				break;
				
			}*/
			break;
		case "iload_0":
			list=stack.push(Integer.toString((int)variables.get(0)));
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));			
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(1)+" from variable 0, pushing it to the stack");
			System.out.println("iload: "+table_3.getItem(1));
			break;
		case "iload_1":
			list=stack.push(Integer.toString((int)variables.get(1)));
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));			
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(1)+" from variable 1, pushing it to the stack");
			break;
		case "iload_2":
			list=stack.push(Integer.toString((int)variables.get(2)));
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));			
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(2)+" from variable 2, pushing it to the stack");
			break;
		case "iload_3":
			list=stack.push(Integer.toString((int)variables.get(3)));
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));			
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Load the value "+variables.get(3)+" from variable 3, pushing it to the stack");
			break;
		case "bipush":
			list=stack.push(parameter);
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			text_2.setText("Push the byte "+parameter+" onto the stack");
			break;
		case "invokespecial":
			list=stack.push(parameter);
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			break;
		case "if_icmpge":
			num1 = Integer.parseInt(list.get(0));
			list=stack.pop();
			num2 = Integer.parseInt(list.get(0));
			list=stack.pop();
			if(num2>=num1)
			{
				int index=highlightSelection;
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				table_1.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection+1;
				table_1.getItem(nextStep).setBackground(0, new Color(display, 0, 255, 0));
			}
			table_3.setItemCount(table_3.getItemCount()-2);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			text_2.setText("Check if "+num2+" is greater or equal to "+num1+", jumping to step "+parameter+" if so");
			break;
		case "if_icmpgt":
			num1 = Integer.parseInt(list.get(0));
			list=stack.pop();
			num2 = Integer.parseInt(list.get(0));
			list=stack.pop();
			if(num2>num1)
			{
				int index=highlightSelection;
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				table_1.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection+1;
				table_1.getItem(nextStep).setBackground(0, new Color(display, 0, 255, 0));
			}
			table_3.setItemCount(table_3.getItemCount()-2);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			text_2.setText("Check if "+num2+" is greater than "+num1+", jumping to step "+parameter+" if so");
			break;
		case "iinc":
			num1=Integer.parseInt(parameter.substring(0,parameter.indexOf(",")));
			num2=Integer.parseInt(parameter.substring(parameter.indexOf(" ")+1,parameter.length()));
			variableReplace(num1, (Object)((int)variables.get(num1)+num2));
			text_2.setText("Increment the value stored in variable "+num1+" by "+num2);
			break;
		case "getstatic":
			list=stack.push(parameter);
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			break;
		case "ldc":
			list=stack.push(parameter);
			table_3.setItemCount(table_3.getItemCount()+1);
			for(int index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			break;
		case "goto":
			int index=highlightSelection;
			System.out.println("count: "+parameter);
			if(Integer.parseInt(parameter)<index)
			{
				while(!table_1.getItem(index).getText().startsWith(parameter+":")) index--;
			}
			else
			{
				while(!table_1.getItem(index).getText().startsWith(parameter+":")) index++;
			}
			table_1.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
			nextStep=index;
			text_2.setText("Jump to step "+parameter);
			break;
		case "astore_1":
			item=new TableItem(table_4, SWT.NONE);
			item.setBackground(0, new Color(display, 210, 0, 120));
			item.setText("Variable 1: "+table_3.getItem(1).getText());
			variables.put(1, table_3.getItem(1).getText());
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			table_3.setItemCount(table_3.getItemCount()-1);
			list=stack.pop();
			break;
		case "astore_2":
			item=new TableItem(table_4, SWT.NONE);
			item.setBackground(0, new Color(display, 210, 0, 120));
			item.setText("Variable 2: "+table_3.getItem(1).getText());
			variables.put(1, table_3.getItem(1).getText());
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			table_3.setItemCount(table_3.getItemCount()-1);
			list=stack.pop();
			break;
		case "irem":
			num1=Integer.parseInt(table_3.getItem(1).getText());
			num2=Integer.parseInt(table_3.getItem(2).getText());
			list=stack.pop();
			System.out.println("num1: "+num1+" num2: "+num2);
			list=stack.pop();
			list=stack.push(Integer.toString(num1%num2));
			table_3.setItemCount(table_3.getItemCount()-1);
			text_2.setText("Get the remainder of "+num1+" divided by "+num2);
			break;
		case "ifne":
			num1 = Integer.parseInt(parameter);
			num2 = Integer.parseInt(list.get(0));
			list=stack.pop();
			if(num2!=num1)
			{
				index=highlightSelection;
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				table_1.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection+1;
				table_1.getItem(nextStep).setBackground(0, new Color(display, 0, 255, 0));
			}
			table_3.setItemCount(table_3.getItemCount()-1);
			for(index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			text_2.setText("Check if "+num2+" is not equal to "+num1+", jumping to step "+parameter+" if so");
			break;
		case "dup":
			System.out.println(table_3.getItem(1).getText());
			list=stack.push(table_3.getItem(1).getText());
			text_2.setText("Duplicate the top value of the stack");
			table_3.setItemCount(table_3.getItemCount()+1);
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));
			for(index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));				
			}
			break;
		case "invokestatic":
			HashMap<Integer, Object> variables_temp = variables;
			variables=new HashMap<Integer, Object>();
			variables.put(0, Integer.parseInt(table_3.getItem(1).getText()));
			list=stack.pop();
			String text_1_temp = text_1.getText();
			String text_2_temp = text_2.getText();
			TableItem[] table_1_temp = table_1.getItems();
			String[] table_1_text = new String[table_1_temp.length];
			for(index=0;index<table_1_text.length;index++)
			{
				table_1_text[index]=table_1_temp[index].getText();
			}
			table_1.setItemCount(1);
			TableItem[] table_2_temp = table_2.getItems();
			String[] table_2_text = new String[table_2_temp.length];
			for(index=0;index<table_2_text.length;index++)
			{
				table_2_text[index]=table_2_temp[index].getText();
			}
			table_2.setItemCount(1);
			TableItem[] table_3_temp = table_3.getItems();
			String[] table_3_text = new String[table_3_temp.length];
			for(index=0;index<table_3_text.length;index++)
			{
				table_3_text[index]=table_3_temp[index].getText();
			}
			table_3.setItemCount(1);
			TableItem[] table_4_temp = table_4.getItems();
			String[] table_4_text = new String[table_4_temp.length];
			for(index=0;index<table_4_text.length;index++)
			{
				table_4_text[index]=table_4_temp[index].getText();
			}
			table_4.setItemCount(1);
			ArrayList<String> list_temp=list;
			list=new ArrayList<String>();
			Stack stack_temp=stack;
			stack=new Stack(list);
			OpcodeString opcodeString_temp=opcodeString;
			int highlightSelection_temp = highlightSelection;
			highlightSelection=1;
			ConstantPool[] constantPoolTemp = BytecodeParse.constantPool;
			byte[] bytes_temp = null;
			BytecodeParse bytecodeParse_temp = null;
			String parameter_temp=parameter;
			System.out.println(parameter.substring(parameter.indexOf("."),parameter.indexOf(":")));
			for(int j=0;j<bytecodeParse.getCodeBytes().size();j++)
			{
				System.out.println(bytecodeParse.getClassNames().get(bytecodeParse.getCodeBytes().get(j)));
				if(bytecodeParse.getClassNames().get(bytecodeParse.getCodeBytes().get(j)).contains(parameter.substring(parameter.indexOf(".")+1,parameter.indexOf(":"))))
				{
					System.out.println("YES");
					bytecodeParse_temp = new BytecodeParse(bytecodeParse.getCodeBytes().get(j));
					bytes_temp=bytecodeParse.getCodeBytes().get(j);
					try {
						bytecodeParse_temp.parse();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}
			tableItems = new ArrayList<TableItem>();
			try
			{
				ArrayList<byte[]> codeBytes_temp=bytecodeParse_temp.getCodeBytes();
						opcodeString=new OpcodeString(bytes_temp);
						
						ArrayList<String> tableBytecode = opcodeString.stringify();
						for(int j=0;j<tableBytecode.size();j++)
						{
							tableItem=new TableItem(table_1, SWT.NONE);
							tableItem.setText(tableBytecode.get(j));
							tableItems.add(tableItem);
						}
						tableItem=new TableItem(table_1, SWT.NONE);
						tableItem.setText("");
						tableItems.add(tableItem);
				/*ArrayList<ArrayList<String>> opcodes=BytecodeParse.opcodes;
				tableItems = new ArrayList<TableItem>();
				for(int index=1;index<opcodes.size();index++)
				{
					for(int j=1;j<opcodes.get(index).size();j++)
					{
						TableItem tableItem=new TableItem(table, SWT.NONE);
						tableItem.setText(opcodes.get(index).get(j));
						tableItems.add(tableItem);
					}
					
				}*/
				display = Display.getDefault();
				highlightSelection=0;
				table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
				//System.out.println("test "+byteToString(BytecodeParse.constantPool[(int)bytecodeParse.getCodeMethods().get(0).getAttributes()[0].getInfo()[0]-1].getBytes()));
				//byteToString(constantPool[constantPool[thisClass-1].getInfo()[0]-1].getBytes()));
				while(!table_1.getItem(highlightSelection).getText().contains("return"))
				{
					for(int j=0;j<table_3.getItemCount();j++)
					{
						table_3.getItem(j).setBackground(0, new Color(display, 255, 255, 255));
					}
					for(int j=0;j<table_4.getItemCount();j++)
					{
						table_4.getItem(j).setBackground(0, new Color(display, 255, 255, 255));
					}	
					if(table_1.getItemCount()!=1&&highlightSelection<table_1.getItemCount()-2)
					{
						display = Display.getDefault();
						table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
						if(nextStep!=-1)
						{
							highlightSelection=nextStep;
						}						
						else
						{
							if(table_1.getItem(highlightSelection+1).getText().equals("")) highlightSelection+=2;
							else highlightSelection++;
						}
						
						table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
						try {
							getSelection();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				String returnValue=table_1.getItem(highlightSelection).getText();
				text_1.setText(text_1_temp);
				text_2.setText(text_2_temp);
				table_1.setItemCount(table_1_text.length);
				for(index=0;index<table_1.getItemCount();index++)
				{
					table_1.getItem(index).setText(table_1_text[index]);
				}
				table_2.setItemCount(table_2_text.length);
				for(index=0;index<table_2.getItemCount();index++)
				{
					table_2.getItem(index).setText(table_2_text[index]);
				}
				/*table_3.setItemCount(table_3_text.length);
				for(index=0;index<table_3.getItemCount();index++)
				{
					table_3.getItem(index).setText(table_3_text[index]);
				}*/
				table_4.setItemCount(table_4_text.length);
				for(index=0;index<table_4.getItemCount();index++)
				{
					table_4.getItem(index).setText(table_4_text[index]);
				}
				variables = variables_temp;
				opcodeString = opcodeString_temp;
				//table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
				highlightSelection = highlightSelection_temp;
				//table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
				BytecodeParse.constantPool = constantPoolTemp;
				list=list_temp;
				list=stack.push(returnValue);
				stack=stack_temp;
				parameter=parameter_temp;
				text_2.setText("Invoke the static method " + parameter);
			}
			catch(NullPointerException | UnsupportedEncodingException e1)
			{
				e1.printStackTrace();
			}	
			break;
		case "pop":
			list=stack.pop();
			table_3.setItemCount(table_3.getItemCount()-1);
			text_2.setText("Pop the top value from the stack");
			break;
		case "iastore":
			int arrayValue=Integer.parseInt(table_3.getItem(1).getText());
			int arrayIndex=Integer.parseInt(table_3.getItem(2).getText());
			Integer[] iArray=toIntArray(table_3.getItem(3).getText());
			int variableNum=0;
			for(index=1;index<variables.size();index++)
			{
				//System.out.println(((String)variables.get(index)).substring(((String)variables.get(index)).indexOf(":")+1));
				if(((String)variables.get(index)).substring(((String)variables.get(index)).indexOf(":")+1).equals(table_3.getItem(3).getText()))
				{
					System.out.println("getting here");
					variableNum=index;
				}
			}
			list=stack.pop();
			list=stack.pop();
			list=stack.pop();			
			iArray[arrayIndex]=arrayValue;
			Object[] objectArray=new Object[iArray.length];
			for(index=0;index<objectArray.length;index++)
			{
				objectArray[index]=(Object)iArray[index];
			}
			list=stack.push(arrayString(objectArray));
			variableReplace(variableNum, arrayString(objectArray));
			text_2.setText("Store value "+arrayValue+" into index "+arrayIndex);
			table_3.setItemCount(table_3.getItemCount()-3);
			for(index=1;index<table_3.getItemCount();index++)
			{
				table_3.getItem(index).setText(list.get(index-1));
			}
			
			break;
		/*default:
			System.out.println("test");
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText("");
			}
			table_2.setItemCount(1);
			break;*/
		}
		for(int index=1;index<table_3.getItemCount();index++)
		{
			table_3.getItem(index).setText(list.get(index-1));				
		}
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Builder window = new Builder();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		//final File f = new File(bytecodeInterpreter.Builder.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		try {
			System.out.println(Object.class.getResource("Object.class").getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1120, 607);
		shell.setText("Bytecode Interpreter");
		
		variables = new HashMap<Integer, Object>();
		shell.setLayout(null);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");		
		MenuItem mntmEdit = new MenuItem(menu, SWT.NONE);
		mntmEdit.setText("Edit");
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");
		
		Menu menu_2 = new Menu(mntmView);
		mntmView.setMenu(menu_2);
		
		MenuItem mntmCode = new MenuItem(menu_2, SWT.NONE);
		mntmCode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//ViewCode viewCode=new ViewCode(shell, SWT.NONE);
			}
		});
		mntmCode.setText("Code");
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("Run");
		
		text_1 = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_1.setBounds(10, 50, (int)(shell.getSize().x*0.2491), (int)(shell.getSize().y*0.4));
		text_1.setEditable(false);
		
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  try
		    	  {
		    		  FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
			    	  String[] files= {"*.class", "*.java"};
			    	  fileDialog.setFilterExtensions(files);
			    	  String filePath = fileDialog.open();
			    	  if(filePath.endsWith(".java"))
			    	  {
			    		  System.out.println(filePath.substring(0, filePath.lastIndexOf("\\")+1));
			    		  System.out.println(cmdResponse("cd /d "+filePath.substring(0, filePath.lastIndexOf("\\")+1)+" && javac "+filePath.substring(filePath.lastIndexOf("\\")+1)));
			    		  javaFile=ReadWrite.toString(filePath);
			    		  filePath=filePath.replace(".java", ".class");
			    	  }
			    	  else
			    	  {
			    		  javaFile=ReadWrite.toString(filePath.replace(".class", ".java"));
			    	  }
			    	  File file = null;
			    	  if(bytecodeParse!=null)
			    	  {
			    		  bytecodeParse.setOpcodeStringify(null);
			    	  }
			    	  try
			    	  {
			    		  file = new File(filePath);
			    		  byte[] bytes = null;
				    	  try {
							bytes = Files.readAllBytes(Paths.get(file.toString()));
						} catch (IOException|NullPointerException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    	  bytecodeParse = new BytecodeParse(bytes);
				    	  try {
							bytecodeParse.parse();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    	  text_1.setText(bytecodeParse.getOpcodeString());
			    	  }
			    	  catch(NullPointerException e1)
			    	  {
			    		  e1.printStackTrace();
			    		  file = null;
			    	  }
			    	  if(javaFile!=null && !javaFile.equals(""))
			    		{
			    		  text_3.setText(javaFile);
			    		}
		    	  }
		    	  catch(NullPointerException e2)
		    	  {
		    		  e2.printStackTrace();
		    	  }
		    	  /*CommandResponse commandResponse = new CommandResponse(file);
		    	  String bytecode = commandResponse.getResponse();
		    	  text.setText(bytecode.substring(bytecode.indexOf("\n")+1));*/
		    	  
		      }
		      
		    });
		
		//Form form = formToolkit.createForm(shell);
		//form.setBounds(414, 21, 0, 0);
		//formToolkit.paintBordersFor(form);
		//form.setText("New Form");
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer.getTable();
		table_1.setBounds(301, 50, 252, 356);
		formToolkit.paintBordersFor(table_1);
		table_1.addListener(SWT.EraseItem, new Listener() {
		    @Override
		    public void handleEvent(Event event) {
				event.detail &= ~SWT.SELECTED;
		    }
		});
		table_1.addListener(SWT.MouseDown, new Listener(){
			@Override
		    public void handleEvent(Event event) {
				event.detail &= ~SWT.FOCUSED;
				event.detail &= ~SWT.SELECTED;
				Point pt = new Point(event.x, event.y);
	            TableItem item = table_1.getItem(pt);
	            try
	            {
	            	if(item.getText()!="")
		            {
		            	TableItem selected=table_1.getItem(highlightSelection);
			            
						
						if(canTraverseTo(selected, item))
						{
							Display display = Display.getDefault();
							item.setBackground(0, new Color(display, 255, 0, 0));
							while(selected!=item)
							{
								for(int index=0;index<table_3.getItemCount();index++)
								{
									table_3.getItem(index).setBackground(0, new Color(display, 255, 255, 255));
								}
								for(int index=0;index<table_4.getItemCount();index++)
								{
									table_4.getItem(index).setBackground(0, new Color(display, 255, 255, 255));
								}	
								if(table_1.getItemCount()!=1&&highlightSelection<table_1.getItemCount()-2)
								{
									display = Display.getDefault();
									table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
									if(nextStep!=-1)
									{
										highlightSelection=nextStep;
									}						
									else
									{
										if(table_1.getItem(highlightSelection+1).getText().equals("")) highlightSelection+=2;
										else highlightSelection++;
									}
									
									table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
									try {
										getSelection();
									} catch (UnsupportedEncodingException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								selected=table_1.getItem(highlightSelection);
							}
						}
		            }
	            }
	            catch(NullPointerException e)
	            {
	            	e.printStackTrace();
	            }
	            
		    }
		});
		table_1.deselect(highlightSelection);
		TableCursor tableCursor = new TableCursor(table_1, SWT.NONE);
		formToolkit.adapt(tableCursor);
		formToolkit.paintBordersFor(tableCursor);
			
		TableItem item1 = new TableItem(table_1, SWT.CENTER);
	    item1.setText("Bytecode");
	    Font boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	
		table_2 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setBounds((int)(shell.getSize().x*0.26875), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.5881));
		formToolkit.adapt(table_2);
		formToolkit.paintBordersFor(table_2);
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(10, 10, 34, 34);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int index=1;index<table_3.getItemCount();index++)
				{
					table_3.getItem(index).setText("");
				}
				table_3.setItemCount(1);
				ArrayList<TableItem> tableItems = null;
				table_1.setItemCount(1);
				TableItem tableItem;
				tableItems = new ArrayList<TableItem>();
				try
				{
					ArrayList<byte[]> codeBytes=bytecodeParse.getCodeBytes();
					for(int index=0;index<codeBytes.size();index++)
					{
						if(bytecodeParse.getClassNames().get(codeBytes.get(index)).contains("main"))
						{
							opcodeString=new OpcodeString(codeBytes.get(index));
							
							ArrayList<String> tableBytecode = opcodeString.stringify();
							for(int j=0;j<tableBytecode.size();j++)
							{
								tableItem=new TableItem(table_1, SWT.NONE);
								tableItem.setText(tableBytecode.get(j));
								tableItems.add(tableItem);
							}
							tableItem=new TableItem(table_1, SWT.NONE);
							tableItem.setText("");
							tableItems.add(tableItem);
						}
						
					}
					/*ArrayList<ArrayList<String>> opcodes=BytecodeParse.opcodes;
					tableItems = new ArrayList<TableItem>();
					for(int index=1;index<opcodes.size();index++)
					{
						for(int j=1;j<opcodes.get(index).size();j++)
						{
							TableItem tableItem=new TableItem(table, SWT.NONE);
							tableItem.setText(opcodes.get(index).get(j));
							tableItems.add(tableItem);
						}
						
					}*/
					Display display = Display.getDefault();
					highlightSelection=1;
					table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
					//System.out.println("test "+byteToString(BytecodeParse.constantPool[(int)bytecodeParse.getCodeMethods().get(0).getAttributes()[0].getInfo()[0]-1].getBytes()));
					//byteToString(constantPool[constantPool[thisClass-1].getInfo()[0]-1].getBytes()));
					getSelection();
				}
				catch(NullPointerException | UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}	
			}
		});
		btnNewButton.setImage(SWTResourceManager.getImage(new File("src/swtbuilder/images/play.png").getAbsolutePath()));
		formToolkit.adapt(btnNewButton, true, true);
		
		Button button = formToolkit.createButton(shell, "", SWT.NONE);
		button.setBounds(50, 10, 34, 34);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try
				{
					for(int index=1;index<table_1.getItemCount();index++)
					{
						table_1.getItem(index).setText("");
					}
					Display display = Display.getDefault();
					table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
					highlightSelection=1;
					for(int index=1;index<table_3.getItemCount();index++)
					{
						table_3.getItem(index).setText("");
					}
					table_3.setItemCount(1);
					for(int index=1;index<table_4.getItemCount();index++)
					{
						table_4.getItem(index).setText("");
					}
					table_4.setItemCount(1);
					text_2.setText("");
				}
				catch(IllegalArgumentException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		button.setImage(SWTResourceManager.getImage(Builder.class, "/org/eclipse/jface/wizard/images/stop.png"));
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(90, 10, 34, 34);
		button_1.setImage(SWTResourceManager.getImage(new File("src/swtbuilder/images/next.png").getAbsolutePath()));
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				for(int index=0;index<table_3.getItemCount();index++)
				{
					table_3.getItem(index).setBackground(0, new Color(display, 255, 255, 255));
				}
				for(int index=0;index<table_4.getItemCount();index++)
				{
					table_4.getItem(index).setBackground(0, new Color(display, 255, 255, 255));
				}
				if(table_1.getItemCount()!=1&&highlightSelection<table_1.getItemCount()-2)
				{
					
					table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
					if(nextStep!=-1)
					{
						highlightSelection=nextStep;
					}						
					else
					{
						if(table_1.getItem(highlightSelection+1).getText().equals("")) highlightSelection+=2;
						else highlightSelection++;
					}
					
					table_1.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
					try {
						getSelection();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		formToolkit.adapt(button_1, true, true);
		
		TableViewer tableViewer_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_3 = tableViewer_1.getTable();
		table_3.setBounds((int)(shell.getSize().x*0.51071), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.58814));
		formToolkit.paintBordersFor(table_3);
		
		item1 = new TableItem(table_3, SWT.NONE);
	    item1.setText("Stack");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
		
		TableCursor tableCursor_1 = new TableCursor(table_3, SWT.NONE);
		formToolkit.adapt(tableCursor_1);
		formToolkit.paintBordersFor(tableCursor_1);
		
		TableViewer tableViewer_1_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_4 = tableViewer_1_1.getTable();
		table_4.setBounds((int)(shell.getSize().x*0.7491), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.58814));
		formToolkit.paintBordersFor(table_4);
		
		TableCursor tableCursor_1_1 = new TableCursor(table_4, SWT.NONE);
		formToolkit.adapt(tableCursor_1_1);
		formToolkit.paintBordersFor(tableCursor_1_1);
		
		item1 = new TableItem(table_4, SWT.NONE);
	    item1.setText("Variables");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	    
	    text_2 = new Text(shell, SWT.BORDER | SWT.WRAP);
	    text_2.setBounds((int)(shell.getSize().x*0.26875), (int)(shell.getSize().y*0.6804), (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.1812));
	    formToolkit.adapt(text_2, true, true);
	    
	    Button button_4 = new Button(shell, SWT.NONE);
	    button_4.setBounds(130, 10, 34, 34);
	    button_4.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if(javaFile!=null && !javaFile.equals(""))
	    		{
	    			ViewCode viewCode=new ViewCode(shell, SWT.NONE, javaFile);
	    			viewCode.open();
	    		}
	    	}
	    });
	    button_4.setImage(SWTResourceManager.getImage(Builder.class, "/org/eclipse/jface/dialogs/images/help.png"));
	    formToolkit.adapt(button_4, true, true);
	    
	    text_3 = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
	    text_3.setBounds(10, (int)(shell.getSize().y*0.5), (int)(shell.getSize().x*0.2482), (int)(shell.getSize().y*0.3641));
	    formToolkit.adapt(text_3, true, true);
	    shell.addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	text_1.setBounds(10, 50, (int)(shell.getSize().x*0.2491), (int)(shell.getSize().y*0.4));
		    	text_2.setBounds((int)(shell.getSize().x*0.26875), (int)(shell.getSize().y*0.6804), (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.1812));
		    	text_3.setBounds(10, (int)(shell.getSize().y*0.5), (int)(shell.getSize().x*0.2482), (int)(shell.getSize().y*0.3641));
		    	table_1.setBounds((int)(shell.getSize().x*0.26875), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.5881));
		    	table_2.setBounds((int)(shell.getSize().x*0.26875), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.5881));
		    	table_3.setBounds((int)(shell.getSize().x*0.51071), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.58814));
		    	table_4.setBounds((int)(shell.getSize().x*0.7491), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.58814));
		    	
		    }
		  });
	}
}
