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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.FileDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
	ArrayList<Integer> highlightSelection;
	private Table table_3;
	private Table table_4;
	private ArrayList<VariablesTable> variables = new ArrayList<VariablesTable>();
	ArrayList<StackTable> stack = new ArrayList<StackTable>();
	private int nextStep=-1;
	private Text text_2;
	private String javaFile = null;
	private ArrayList<OpcodeString> opcodeString;
	private Text text_3;
	private Text text_4;
	private String filePath;
	private int fontSize = 9;
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
	private void clearAll()
	{
		try
		{
			highlightSelection=new ArrayList<Integer>();
			opcodeString=new ArrayList<OpcodeString>();
			stack=new ArrayList<StackTable>();
			for(int index=1;index<table_1.getItemCount();index++)
			{
				table_1.getItem(index).setText("");
			}
			Display display = Display.getDefault();
			for(int index=0;index<table_1.getItemCount();index++)
			{
				table_1.getItem(index).setBackground(0, new Color(display, 255, 255, 255));
			}
			table_1.setItemCount(1);
			highlightSelection.add(1);
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
			text_4.setText("");
			stack.add(new StackTable());
			variables.add(new VariablesTable());
		}
		catch(IllegalArgumentException e1)
		{
			e1.printStackTrace();
		}
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
	
	private void fillTable(ArrayList<String> list)
	{
		if(!table_3.getItem(table_3.getItemCount()-1).getText().equals(""))
		{
			table_3.setItemCount(table_3.getItemCount()+5);
		}		
		for(int index=1;index<list.size()+1;index++)
		{
			try
			{
				table_3.getItem(index).setText(list.get(index-1));	
			}
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}
		}
		for(int index=list.size()+1;index<table_3.getItemCount();index++)
		{
			table_3.getItem(index).setText("");
		}
	}
	
	private void fillTable(Table table, ArrayList<String> list)
	{
		table.setItemCount(list.size()+2);
		for(int index=1;index<list.size()+1;index++)
		{
			try
			{
				table.getItem(index).setText(list.get(index-1));	
			}
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}
		}
		for(int index=list.size()+1;index<table.getItemCount();index++)
		{
			table.getItem(index).setText("");
		}
	}
	
	private void highlightTablePurple(Table table, int index, boolean check)
	{
		if(check)
		{
			Display display = Display.getDefault();
			if(table.getItemCount()==1) table.setItemCount(2);
			table.getItem(index).setBackground(0, new Color(display, 210, 0, 120));
		}
	}
	private void highlightTablePurple(TableItem item, boolean check)
	{
		if(check)
		{
			Display display = Display.getDefault();
			item.setBackground(0, new Color(display, 210, 0, 120));
		}
	}
	private void highlightTableGreen(Table table, int index, boolean check)
	{
		if(check)
		{
			Display display = Display.getDefault();
			table.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
		}
	}
	private TableItem getVariableTable(int num)
	{
		for(int index=0;index<table_4.getItemCount();index++)
		{
			if(table_4.getItem(index).getText().startsWith("Variable "+index+":"))
			{
				return table_4.getItem(index);
			}
		}
		return null;
	}
	private void getSelection() {
		String selection = table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).getText();
		try
		{
			selection = selection.substring(selection.indexOf(':')+2);
		}
		catch(StringIndexOutOfBoundsException e) {}
		String parameter = "";
		boolean check=true;
		nextStep=-1;
		if(selection.contains("\t"))
		{
			parameter=selection.substring(selection.lastIndexOf("\t")+2);
			selection=selection.substring(0, selection.indexOf("\t"));
		}
		TableItem item;
		Display display = Display.getDefault();
		Object[] array;
		int num1;
		int num2;
		switch(selection) {
		case "aload_0":
			stack.get(stack.size()-1).push((String)variables.get(variables.size()-1).get(0));
			highlightTablePurple(table_3, 1, check);
			table_3.getItem(1).setBackground(0, new Color(display, 210, 0, 120));		
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(0)+" from variable 0, pushing it to the stack");
			break;
		case "aload_1":
			stack.get(stack.size()-1).push((String)variables.get(variables.size()-1).get(1));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(1)+" from variable 1, pushing it to the stack");
			break;
		case "aload_2":
			stack.get(stack.size()-1).push((String)variables.get(variables.size()-1).get(2));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(2)+" from variable 2, pushing it to the stack");
			break;
		case "iconst_0":
			stack.get(stack.size()-1).push("0");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 0 onto the stack");
			break;
		case "iconst_m1":
			stack.get(stack.size()-1).push("-1");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value -1 onto the stack");
			break;
		case "arraylength":
			array=toObjectArray(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push(Integer.toString(array.length));
			text_2.setText("Push the length of the array at the top of the stack");
			highlightTablePurple(table_3, 1, check);
			break;
		case "iconst_1":
			stack.get(stack.size()-1).push("1");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 1 onto the stack");
			break;
		case "iconst_2":
			stack.get(stack.size()-1).push("2");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 2 onto the stack");
			break;
		case "iconst_3":
			stack.get(stack.size()-1).push("3");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 3 onto the stack");
			break;
		case "iconst_4":
			stack.get(stack.size()-1).push("4");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 4 onto the stack");
			break;
		case "iconst_5":
			stack.get(stack.size()-1).push("5");
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the value 5 onto the stack");
			break;
		
		case "istore_1":
			variables.get(variables.size()-1).put(1, Integer.parseInt(table_3.getItem(1).getText()));
			highlightTablePurple(table_4, 1, check);
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			stack.get(stack.size()-1).pop();
			break;
		case "istore_2":
			variables.get(variables.size()-1).getVariableItems().put(2, Integer.parseInt(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 2");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "istore_3":
			variables.get(variables.size()-1).getVariableItems().put(3, Integer.parseInt(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 3");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "lstore_0":
			variables.get(variables.size()-1).getVariableItems().put(0, Long.parseLong(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 0");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "lstore_1":
			variables.get(variables.size()-1).getVariableItems().put(1, Long.parseLong(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "lstore_2":
			variables.get(variables.size()-1).getVariableItems().put(2, Long.parseLong(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 2");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "lstore_3":
			variables.get(variables.size()-1).getVariableItems().put(3, Long.parseLong(table_3.getItem(1).getText()));
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 3");
			highlightTablePurple(table_4, 1, check);
			stack.get(stack.size()-1).pop();
			break;
		case "iadd":
			num1=Integer.parseInt(stack.get(stack.size()-1).pop());
			num2=Integer.parseInt(stack.get(stack.size()-1).pop());
			stack.get(stack.size()-1).push(Integer.toString(num1+num2));
			text_2.setText("Add the values "+num1+" and "+num2+", pushing the value to the stack");
			break;
		case "isub":
			num1=Integer.parseInt(stack.get(stack.size()-1).pop());
			num2=Integer.parseInt(stack.get(stack.size()-1).pop());
			stack.get(stack.size()-1).push(Integer.toString(num1-num2));
			text_2.setText("Subtract "+num2+" from "+num1+", pushing the value to the stack");
			break;
		case "idiv":
			num1=Integer.parseInt(stack.get(stack.size()-1).pop());
			num2=Integer.parseInt(stack.get(stack.size()-1).pop());
			stack.get(stack.size()-1).push(Integer.toString(num1/num2));
			text_2.setText("Divide "+num2+" by "+num2+", pushing the value to the stack");
			break;
		case "imul":
			num1=Integer.parseInt(stack.get(stack.size()-1).pop());
			num2=Integer.parseInt(stack.get(stack.size()-1).pop());
			stack.get(stack.size()-1).push(Integer.toString(num1*num2));
			text_2.setText("Multiply the values "+num1+" and "+num2+", pushing the value to the stack");
			break;
		case "new":
			for(int index=0;index<variables.get(variables.size()-1).getLength();index++)
			{
				try
				{
					if(((String)variables.get(variables.size()-1).get(index)).startsWith("{"+parameter+"}"))
					{
						stack.get(stack.size()-1).push(((String)variables.get(variables.size()-1).get(index)));
						highlightTablePurple(table_3.getItem(1), check);
					}
				}
				catch(NullPointerException e) {}
			}
			break;
		case "aastore":
			String arrayValueString=stack.get(stack.size()-1).get(0);
			int arrayIndex=Integer.parseInt(stack.get(stack.size()-1).get(1));
			Object[] oArray=toObjectArray(stack.get(stack.size()-1).get(2));			
			int variableNum=0;
			for(int index=1;index<variables.get(variables.size()-1).getLength();index++)
			{	
				if(((String)variables.get(variables.size()-1).get(index)).substring((variables.get(variables.size()-1).get(index).toString()).indexOf(":")+1).equals(arrayString(oArray)))
				{
					variableNum=index;
				}
			}
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			oArray[arrayIndex]=arrayValueString;
			variables.get(variables.size()-1).replace(variableNum, arrayString(oArray));
			text_2.setText("Store value "+arrayValueString+" into index "+arrayIndex);
			
			break;
		case "newarray":
			array = new Object[Integer.parseInt(table_3.getItem(1).getText())];
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push(arrayString(array));
			text_2.setText("Create a new array of type "+parameter);
			highlightTablePurple(table_3, 1, check);
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
			stack.get(stack.size()-1).push(Integer.toString((int)variables.get(variables.size()-1).get(0)));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(1)+" from variable 0, pushing it to the stack");
			System.out.println("iload: "+table_3.getItem(1));
			break;
		case "iload_1":
			stack.get(stack.size()-1).push(Integer.toString((int)variables.get(variables.size()-1).get(1)));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(1)+" from variable 1, pushing it to the stack");
			break;
		case "iload_2":
			stack.get(stack.size()-1).push(Integer.toString((int)variables.get(variables.size()-1).get(2)));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(2)+" from variable 2, pushing it to the stack");
			break;
		case "iload_3":
			stack.get(stack.size()-1).push(Integer.toString((int)variables.get(variables.size()-1).get(3)));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Load the value "+variables.get(variables.size()-1).get(3)+" from variable 3, pushing it to the stack");
			break;
		case "bipush":
			stack.get(stack.size()-1).push(parameter);
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push the byte "+parameter+" onto the stack");
			break;
		case "invokespecial":
			stack.get(stack.size()-1).push(parameter);
			highlightTablePurple(table_3, 1, check);
			break;
		case "if_icmpge":
			num1 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			num2 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			if(num2>=num1)
			{
				int index=highlightSelection.get(highlightSelection.size()-1);
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				highlightTableGreen(table_1, index, check);
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection.get(highlightSelection.size()-1)+1;
				highlightTableGreen(table_1, nextStep, check);
			}
			text_2.setText("Check if "+num2+" is greater or equal to "+num1+", jumping to step "+parameter+" if so");
			break;
		case "if_icmpne":
			num1 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			num2 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			if(num2!=num1)
			{
				int index=highlightSelection.get(highlightSelection.size()-1);
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				highlightTableGreen(table_1, index, check);
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection.get(highlightSelection.size()-1)+1;
				highlightTableGreen(table_1, nextStep, check);
			}
			text_2.setText("Check if "+num2+" is not equal to "+num1+", jumping to step "+parameter+" if so");
			break;
		case "if_icmpgt":
			num1 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			num2 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			if(num2>num1)
			{
				int index=highlightSelection.get(highlightSelection.size()-1);
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				highlightTableGreen(table_1, index, check);
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection.get(highlightSelection.size()-1)+1;
				highlightTableGreen(table_1, nextStep, check);
			}
			text_2.setText("Check if "+num2+" is greater than "+num1+", jumping to step "+parameter+" if so");
			break;
		case "iinc":
			num1=Integer.parseInt(parameter.substring(0,parameter.indexOf(",")));
			num2=Integer.parseInt(parameter.substring(parameter.indexOf(" ")+1,parameter.length()));
			variables.get(variables.size()-1).replace(num1, (Object)((int)variables.get(variables.size()-1).get(num1)+num2));
			text_2.setText("Increment the value stored in variable "+num1+" by "+num2);
			break;
		case "getstatic":
			stack.get(stack.size()-1).push(parameter);
			highlightTablePurple(table_3, 1, check);
			break;
		case "ldc":
			stack.get(stack.size()-1).push(parameter.substring(parameter.indexOf(" ")+1));
			highlightTablePurple(table_3, 1, check);
			break;
		case "ldc2_w":
			stack.get(stack.size()-1).push(parameter);
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Push a double or long value onto the stack");
			break;
		case "goto":
			int index=highlightSelection.get(highlightSelection.size()-1);
			System.out.println("count: "+parameter);
			if(Integer.parseInt(parameter)<index)
			{
				while(!table_1.getItem(index).getText().startsWith(parameter+":")) index--;
			}
			else
			{
				while(!table_1.getItem(index).getText().startsWith(parameter+":")) index++;
			}
			highlightTableGreen(table_1, index, check);
			nextStep=index;
			text_2.setText("Jump to step "+parameter);
			break;
		case "astore_1":
			if(!variables.get(variables.size()-1).getVariableItems().containsKey(1))
			{
				item=new TableItem(table_4, SWT.NONE);
				highlightTablePurple(item, check);
				item.setText("Variable 1: "+table_3.getItem(1).getText());		
			}
			else
			{
				for(index=0;index<table_4.getItemCount();index++)
				{
					if(table_4.getItem(index).getText().startsWith("Variable 1:"))
					{
						table_4.getItem(index).setText("Variable 1: "+table_3.getItem(index).getText());
						highlightTablePurple(table_4, index, check);
					}
				}
			}
			variables.get(variables.size()-1).getVariableItems().put(1, table_3.getItem(1).getText());
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 1");
			stack.get(stack.size()-1).pop();
			break;
		case "anewarray":
			array = new Object[Integer.parseInt(table_3.getItem(1).getText())];
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push("{"+parameter+"} "+arrayString(array));
			text_2.setText("Create a new array of type "+parameter);
			highlightTablePurple(table_3, 1, check);
			break;
		case "astore_2":
			if(!variables.get(variables.size()-1).getVariableItems().containsKey(2))
			{
				item=new TableItem(table_4, SWT.NONE);
				highlightTablePurple(item, check);
				item.setText("Variable 2: "+table_3.getItem(1).getText());		
			}
			else
			{
				for(index=0;index<table_4.getItemCount();index++)
				{
					if(table_4.getItem(index).getText().startsWith("Variable 2:"))
					{
						table_4.getItem(index).setText("Variable 2: "+table_3.getItem(index).getText());
						highlightTablePurple(table_4, index, check);
					}
				}
			}
			variables.get(variables.size()-1).getVariableItems().put(2, table_3.getItem(1).getText());
			text_2.setText("Pop the value "+table_3.getItem(1).getText()+" from the stack, storing it in variable 2");
			stack.get(stack.size()-1).pop();
			break;
		case "irem":
			num1=Integer.parseInt(stack.get(stack.size()-1).get(1));
			num2=Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push(Integer.toString(num1%num2));
			highlightTablePurple(table_3, 1, check);
			text_2.setText("Get the remainder of "+num1+" divided by "+num2);
			break;
		case "ifne":
			num1 = Integer.parseInt(parameter);
			num2 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			if(num2!=0)
			{
				index=highlightSelection.get(highlightSelection.size()-1);
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				highlightTableGreen(table_1, index, check);
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection.get(highlightSelection.size()-1)+1;
				highlightTableGreen(table_1, nextStep, check);
			}
			text_2.setText("Check if "+num2+" is not equal to 0, jumping to step "+parameter+" if so");
			break;
		case "ifeq":
			num1 = Integer.parseInt(parameter);
			num2 = Integer.parseInt(stack.get(stack.size()-1).get(0));
			stack.get(stack.size()-1).pop();
			if(num2==0)
			{
				index=highlightSelection.get(highlightSelection.size()-1);
				while(!table_1.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				highlightTableGreen(table_1, index, check);
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection.get(highlightSelection.size()-1)+1;
				highlightTableGreen(table_1, nextStep, check);
			}
			text_2.setText("Check if "+num2+" is equal to 0, jumping to step "+parameter+" if so");
			break;
		case "dup":
			System.out.println(table_3.getItem(1).getText());
			stack.get(stack.size()-1).push(table_3.getItem(1).getText());
			text_2.setText("Duplicate the top value of the stack");
			highlightTablePurple(table_3, 1, check);
			break;
		case "invokestatic":
			String classParameters=parameter.substring(parameter.indexOf("(")+1,parameter.indexOf(")"));
			System.out.println(classParameters);
			
			for(int j=0;j<bytecodeParse.getCodeBytes().size();j++)
			{
				if(bytecodeParse.getClassNames().get(bytecodeParse.getCodeBytes().get(j)).contains(parameter.substring(parameter.indexOf(".")+1,parameter.indexOf(":"))))
				{
					opcodeString.add(new OpcodeString(bytecodeParse.getCodeBytes().get(j)));
					stack.add(new StackTable());
					variables.add(new VariablesTable());
					int parametersIndex=0;
					int variablesIndex=0;
					while(parametersIndex<classParameters.length())
					{
						switch(classParameters.charAt(parametersIndex))
						{
						case 'I':
							variables.get(variables.size()-1).put(variablesIndex, Integer.parseInt(stack.get(stack.size()-2).pop()));
							variablesIndex++;
							break;
						}
						parametersIndex++;
					}
					try {
						fillTable(table_1, opcodeString.get(opcodeString.size()-1).stringify());
						table_1.setItemCount(table_1.getItemCount()+1);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 255, 255));
					highlightSelection.add(1);
					table_1.getItem(1).setBackground(0, new Color(display, 255, 0, 0));
					getSelection();
				}
			}
			break;
		case "invokevirtual":
			if(parameter.contains("java/io/PrintStream.println:"))
			{
				String toPrint=table_3.getItem(1).getText();
				text_4.insert(toPrint+"\n");
				text_2.setText("Print "+toPrint+" to the console with a newline");
			}
			else if(parameter.contains("java/io/PrintStream.print:"))
			{
				String toPrint=table_3.getItem(1).getText();
				text_4.insert(toPrint);
				text_2.setText("Print "+toPrint+" to the console");
			}
			else
			{
				
			}
			stack.get(stack.size()-1).pop();
			break;
		case "pop":
			stack.get(stack.size()-1).pop();
			text_2.setText("Pop the top value from the stack");
			break;
		case "pop2":
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			text_2.setText("Pop the top two values from the stack");
			break;
		case "swap":
			String firstEntry=stack.get(stack.size()-1).pop();
			String secondEntry=stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push(firstEntry);
			stack.get(stack.size()-1).push(secondEntry);
			text_2.setText("Swap the top two values on the stack");
			highlightTablePurple(table_3, 1, check);
			highlightTablePurple(table_3, 2, check);
			break;
		case "iastore":
			int arrayValue=Integer.parseInt(stack.get(stack.size()-1).get(0));
			arrayIndex=Integer.parseInt(stack.get(stack.size()-1).get(1));
			Integer[] iArray=toIntArray(stack.get(stack.size()-1).get(2));			
			variableNum=0;
			for(index=1;index<variables.get(variables.size()-1).getLength();index++)
			{
				
				if(((String)variables.get(variables.size()-1).get(index)).substring((variables.get(variables.size()-1).get(index).toString()).indexOf(":")+1).equals(arrayString(iArray)))
				{
					variableNum=index;
				}
			}
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			iArray[arrayIndex]=arrayValue;
			Object[] objectArray=new Object[iArray.length];
			for(index=0;index<objectArray.length;index++)
			{
				objectArray[index]=(Object)iArray[index];
			}
			variables.get(variables.size()-1).replace(variableNum, arrayString(objectArray));
			text_2.setText("Store value "+arrayValue+" into index "+arrayIndex);
			
			break;
		case "iaload":
			arrayIndex=Integer.parseInt(table_3.getItem(1).getText());
			iArray=toIntArray(table_3.getItem(2).getText());
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).pop();
			stack.get(stack.size()-1).push(Integer.toString(iArray[arrayIndex]));
			text_2.setText("Push value "+iArray[arrayIndex]+" at index "+arrayIndex+" onto the stack");
			highlightTablePurple(table_3, 1, check);
			break;
		case "ireturn\n":
			if(stack.size()>1)
			{
				
				stack.get(stack.size()-2).push(stack.get(stack.size()-1).pop());
				System.out.println("stack before: "+stack.size());
				stack.remove(stack.size()-1);
				System.out.println("stack after: "+stack.size());
				variables.remove(variables.size()-1);
				opcodeString.remove(opcodeString.size()-1);
				try {
					
					fillTable(table_1, opcodeString.get(opcodeString.size()-1).stringify());
					table_1.getItem(highlightSelection.get(highlightSelection.size()-2)).setBackground(0, new Color(display, 255, 0, 0));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try
				{
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 255, 255));
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				System.out.println("table size: "+table_1.getItemCount());				
				highlightSelection.remove(highlightSelection.size()-1);
			}
			break;
		case "return\n":
			System.out.println("stack size: "+stack.size());
			if(stack.size()>1)
			{
				
				stack.get(stack.size()-2).push(stack.get(stack.size()-1).pop());
				System.out.println("stack before: "+stack.size());
				stack.remove(stack.size()-1);
				System.out.println("stack after: "+stack.size());
				variables.remove(variables.size()-1);
				opcodeString.remove(opcodeString.size()-1);
				try {
					
					fillTable(table_1, opcodeString.get(opcodeString.size()-1).stringify());
					table_1.getItem(highlightSelection.get(highlightSelection.size()-2)).setBackground(0, new Color(display, 255, 0, 0));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try
				{
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 255, 255));
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				System.out.println("table size: "+table_1.getItemCount());
				
				highlightSelection.remove(highlightSelection.size()-1);
				
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
		fillTable(stack.get(stack.size()-1).getStackItems());
		
		table_4.setItemCount(variables.get(variables.size()-1).getLength()+1);

		for(int index=1;index<table_4.getItemCount();index++)
		{
			try
			{
				table_4.getItem(index).setText("Variable "+index+": "+variables.get(variables.size()-1).get(index).toString());
			}
			catch(NullPointerException e) {}
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
		Font font = SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL);
		highlightSelection=new ArrayList<Integer>();
		highlightSelection.add(1);
		
		shell = new Shell();
		shell.setSize(1120, 607);
		shell.setText("Bytecode Interpreter");
		
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
		
		MenuItem mntmIncreaseFont = new MenuItem(menu_2, SWT.NONE);
		mntmIncreaseFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fontSize++;
				text_1.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_2.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_3.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_4.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_1.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_3.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_4.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
			}
		});
		mntmIncreaseFont.setText("Increase Font");
		
		MenuItem mntmDecreaseFont = new MenuItem(menu_2, SWT.NONE);
		mntmDecreaseFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fontSize--;
				text_1.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_2.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_3.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				text_4.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_1.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_3.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
				table_4.setFont(SWTResourceManager.getFont("Segoe UI", fontSize, SWT.NORMAL));
			}
		});
		mntmDecreaseFont.setText("Decrease Font");
		
		MenuItem mntmRun = new MenuItem(menu, SWT.CASCADE);
		mntmRun.setText("Run");
		
		Menu menu_3 = new Menu(mntmRun);
		mntmRun.setMenu(menu_3);
		
		MenuItem mntmCompile = new MenuItem(menu_3, SWT.NONE);
		mntmCompile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try
		    	{
					ReadWrite.write(text_3.getText(), filePath.replace(".class", ".java"));
					javaFile=text_3.getText();
					text_3.setText(javaFile);
					while(!ReadWrite.isReady(filePath)) {}
					cmdResponse("javac "+filePath.replace(".class", ".java"));
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
							e1.printStackTrace();
						}
				    	  bytecodeParse = new BytecodeParse(bytes);
				    	  try {
							bytecodeParse.parse();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				    	  text_1.setText(bytecodeParse.getOpcodeString());
			    	  }
			    	  catch(NullPointerException e1)
			    	  {
			    		  e1.printStackTrace();
			    		  file = null;
			    	  }
			    	  
		    	}
	    	  catch(NullPointerException e2)
	    	  {
	    		  e2.printStackTrace();
	    	  }
			}
		});
		mntmCompile.setText("Compile Java");
		
		text_1 = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.FULL_SELECTION);
		text_1.setToolTipText("Raw Bytecode");
		text_1.setBounds(10, 50, (int)(shell.getSize().x*0.2491), (int)(shell.getSize().y*0.4));
		text_1.setEditable(false);
		
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  try
		    	  {
		    		  clearAll();
		    		  FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
			    	  String[] files= {"*.class", "*.java"};
			    	  fileDialog.setFilterExtensions(files);
			    	  filePath = fileDialog.open();
			    	  if(filePath.endsWith(".java"))
			    	  {
			    		  javaFile=ReadWrite.toString(filePath);
			    		  cmdResponse("javac "+filePath);
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
							e1.printStackTrace();
						}
				    	  bytecodeParse = new BytecodeParse(bytes);
				    	  try {
							bytecodeParse.parse();
						} catch (IOException e1) {
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
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER);
		table_1 = tableViewer.getTable();
		table_1.setFont(font);
		table_1.setTouchEnabled(true);
		table_1.setToolTipText("Bytecode");
		table_1.setBounds(301, 50, 252, 356);
		formToolkit.paintBordersFor(table_1);
		
		table_1.addListener(SWT.MouseDown, new Listener(){
			@Override
		    public void handleEvent(Event event) {
				
				Point pt = new Point(event.x, event.y);
	            TableItem item = table_1.getItem(pt);
	            try
	            {
	            	if(item.getText()!="")
		            {
	            		try
	            		{
	            			TableItem selected=table_1.getItem(highlightSelection.get(highlightSelection.size()-1));
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
									if(table_1.getItemCount()!=1&&highlightSelection.get(highlightSelection.size()-1)<table_1.getItemCount()-2)
									{
										display = Display.getDefault();
										table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 255, 255));
										if(nextStep!=-1)
										{
											highlightSelection.set(highlightSelection.size()-1, nextStep);
										}						
										else
										{
											if(table_1.getItem(highlightSelection.get(highlightSelection.size()-1)+1).getText().equals("")) highlightSelection.set(highlightSelection.size()-1, highlightSelection.get(highlightSelection.size()-1)+2);
											else highlightSelection.set(highlightSelection.size()-1, highlightSelection.get(highlightSelection.size()-1)+1);
										}
										
										table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 0, 0));
										getSelection();
									}
									selected=table_1.getItem(highlightSelection.get(highlightSelection.size()-1));
								}
							}
	            		}
		            	catch(IllegalArgumentException e) {}
		            }
	            }
	            catch(NullPointerException e)
	            {
	            	e.printStackTrace();
	            }
	            
		    }
		});
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
				clearAll();
				for(int index=1;index<table_3.getItemCount();index++)
				{
					table_3.getItem(index).setText("");
				}
				table_3.setItemCount(5);
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
							opcodeString.add(new OpcodeString(codeBytes.get(index)));
							
							ArrayList<String> tableBytecode = opcodeString.get(0).stringify();
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
					highlightSelection.set(highlightSelection.size()-1, 1);
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 0, 0));
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
		btnNewButton.setImage(SWTResourceManager.getImage(Builder.class, "/src/swtbuilder/images/play.png"));
		formToolkit.adapt(btnNewButton, true, true);
		
		Button button = formToolkit.createButton(shell, "", SWT.NONE);
		button.setBounds(50, 10, 34, 34);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearAll();
			}
		});
		button.setImage(SWTResourceManager.getImage(Builder.class, "/org/eclipse/jface/wizard/images/stop.png"));
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(90, 10, 34, 34);
		button_1.setImage(SWTResourceManager.getImage(Builder.class, "/src/swtbuilder/images/next.png"));
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
				if(table_1.getItemCount()!=1&&highlightSelection.get(highlightSelection.size()-1)<table_1.getItemCount()-2)
				{
					
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 255, 255));
					if(nextStep!=-1)
					{
						highlightSelection.set(highlightSelection.size()-1, nextStep);;
					}						
					else
					{
						if(table_1.getItem(highlightSelection.get(highlightSelection.size()-1)+1).getText().equals("")) highlightSelection.set(highlightSelection.size()-1, highlightSelection.get(highlightSelection.size()-1)+2);
						else highlightSelection.set(highlightSelection.size()-1, highlightSelection.get(highlightSelection.size()-1)+1);
					}
					
					table_1.getItem(highlightSelection.get(highlightSelection.size()-1)).setBackground(0, new Color(display, 255, 0, 0));
					getSelection();
				}
				
			}
		});
		formToolkit.adapt(button_1, true, true);
		
		TableViewer tableViewer_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_3 = tableViewer_1.getTable();
		table_3.setFont(font);
		table_3.setToolTipText("Stack");
		table_3.setBounds((int)(shell.getSize().x*0.51), 50, (int)(shell.getSize().x*0.225), (int)(shell.getSize().y*0.4));
		formToolkit.paintBordersFor(table_3);
		table_3.addListener(SWT.EraseItem, new Listener() {
		    @Override
		    public void handleEvent(Event event) {
		    	event.detail &= ~SWT.SELECTED;
		    }
		});
		item1 = new TableItem(table_3, SWT.NONE);
	    item1.setText("Stack");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", fontSize+3, SWT.BOLD ) );
	    item1.setFont( boldFont );
		
		TableCursor tableCursor_1 = new TableCursor(table_3, SWT.NONE);
		formToolkit.adapt(tableCursor_1);
		formToolkit.paintBordersFor(tableCursor_1);
		
		TableViewer tableViewer_1_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_4 = tableViewer_1_1.getTable();
		table_4.setToolTipText("Variables");
		table_4.setFont(font);
		table_4.setBounds((int)(shell.getSize().x*0.748), 50, (int)(shell.getSize().x*0.225), (int)(shell.getSize().y*0.4));
		formToolkit.paintBordersFor(table_4);
		
		TableCursor tableCursor_1_1 = new TableCursor(table_4, SWT.NONE);
		formToolkit.adapt(tableCursor_1_1);
		formToolkit.paintBordersFor(tableCursor_1_1);
		
		item1 = new TableItem(table_4, SWT.NONE);
	    item1.setText("Variables");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	    
	    text_2 = new Text(shell, SWT.BORDER | SWT.WRAP);
	    text_2.setToolTipText("Bytecode Information");
	    text_2.setFont(font);
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
	    text_3.setFont(font);
	    text_3.setEditable(true);
	    text_3.setToolTipText("Java Code");
	    text_3.setBounds(10, (int)(shell.getSize().y*0.5), (int)(shell.getSize().x*0.2482), (int)(shell.getSize().y*0.3641));
	    formToolkit.adapt(text_3, true, true);
	    
	    text_4 = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
	    text_4.setToolTipText("Console Output");
	    text_4.setFont(font);
	    text_4.setBounds((int)(shell.getSize().x*0.51), (int)(shell.getSize().y*0.499), (int)(shell.getSize().x*0.463), (int)(shell.getSize().y*0.364));
	    formToolkit.adapt(text_4, true, true);
	    shell.addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	text_1.setBounds(10, 50, (int)(shell.getSize().x*0.2491), (int)(shell.getSize().y*0.4));
		    	text_2.setBounds((int)(shell.getSize().x*0.26875), (int)(shell.getSize().y*0.6804), (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.1812));
		    	text_3.setBounds(10, (int)(shell.getSize().y*0.5), (int)(shell.getSize().x*0.2482), (int)(shell.getSize().y*0.3641));
		    	text_4.setBounds((int)(shell.getSize().x*0.51), (int)(shell.getSize().y*0.499), (int)(shell.getSize().x*0.463), (int)(shell.getSize().y*0.364));
		    	table_1.setBounds((int)(shell.getSize().x*0.26875), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.5881));
		    	table_2.setBounds((int)(shell.getSize().x*0.26875), 50, (int)(shell.getSize().x*0.22589), (int)(shell.getSize().y*0.5881));
		    	table_3.setBounds((int)(shell.getSize().x*0.51), 50, (int)(shell.getSize().x*0.225), (int)(shell.getSize().y*0.4));
		    	table_4.setBounds((int)(shell.getSize().x*0.748), 50, (int)(shell.getSize().x*0.225), (int)(shell.getSize().y*0.4));
		    	
		    }
		  });
	}
}
