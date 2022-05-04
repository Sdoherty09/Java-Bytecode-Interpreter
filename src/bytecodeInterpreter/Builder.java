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
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.FileDialog;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.HashMap;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.List;

public class Builder {

	protected Shell shell;
	private Text text;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	private Table table_1;
	private BytecodeParse bytecodeParse;
	int highlightSelection=1;
	private Table table_2;
	private Table table_3;
	private HashMap<Integer, Integer> variables;
	private int nextStep=-1;

	private void getSelection() {
		String selection = table.getItem(highlightSelection).getText();
		selection = selection.substring(selection.indexOf(':')+2);
		String parameter = "";
		nextStep=-1;
		if(selection.contains("\t"))
		{
			parameter=selection.substring(selection.lastIndexOf("\t")+2);
			selection=selection.substring(0, selection.indexOf("\t"));
			System.out.println(parameter);
		}
		ArrayList<TableItem> tableItems=new ArrayList<TableItem>();
		for(int index=1;index<table_2.getItemCount();index++)
		{
			tableItems.add(table_2.getItem(index));
		}
		
		
		ArrayList<String> list = new ArrayList<String>();
		for(int index=0;index<tableItems.size();index++)
		{
			list.add(tableItems.get(index).getText());
		}
		Stack stack=new Stack(list);
		TableItem tableItem;
		System.out.println(selection);
		String newContent;
		TableItem item;
		Display display = Display.getDefault();
		int num1;
		int num2;
		switch(selection) {
		case "aload_0": case "iconst_0":
			list=stack.push("0");
			table_2.setItemCount(table_2.getItemCount()+1);
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
				
			}
			break;
		
		case "iconst_1":
			list=stack.push("1");
			table_2.setItemCount(table_2.getItemCount()+1);
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
				
			}
			break;	
		case "istore_1":
			item=new TableItem(table_3, SWT.NONE);
			item.setText("Variable 1: "+table_2.getItem(1).getText());
			variables.put(1, Integer.parseInt(table_2.getItem(1).getText()));
			table_2.setItemCount(table_2.getItemCount()-1);
			list=stack.pop();
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
				
			}
			break;
		case "istore_2":
			item=new TableItem(table_3, SWT.NONE);
			item.setText("Variable 2: "+table_2.getItem(1).getText());
			variables.put(2, Integer.parseInt(table_2.getItem(1).getText()));
			table_2.setItemCount(table_2.getItemCount()-1);
			list=stack.pop();
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));				
			}
			break;
		case "iload_2":
			list=stack.push(Integer.toString(variables.get(2)));
			table_2.setItemCount(table_2.getItemCount()+1);
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
				
			}
			break;
		
		case "invokespecial": case "bipush":
			list=stack.push(parameter);
			table_2.setItemCount(table_2.getItemCount()+1);
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
			}
			break;
		case "if_icmpge":
			num1 = Integer.parseInt(list.get(0));
			list=stack.pop();
			num2 = Integer.parseInt(list.get(0));
			list=stack.pop();
			if(num2>=num1)
			{
				int index=highlightSelection;
				while(!table.getItem(index).getText().contains(parameter+":"))
				{
					index++;			
				}
				table.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
				nextStep=index;
			}
			else
			{
				nextStep=highlightSelection+1;
				table.getItem(nextStep).setBackground(0, new Color(display, 0, 255, 0));
			}
			table_2.setItemCount(table_2.getItemCount()-2);
			for(int index=1;index<table_2.getItemCount();index++)
			{
				table_2.getItem(index).setText(list.get(index-1));
			}
			break;
		case "iinc":
			num1=Integer.parseInt(parameter.substring(0,parameter.indexOf(",")));
			num2=Integer.parseInt(parameter.substring(parameter.indexOf(" ")+1,parameter.length()));
			variables.replace(num1, variables.get(num1)+num2);
			for(int index=0;index<table_3.getItemCount();index++)
			{
				if(table_3.getItem(index).getText().contains("Variable "+num1))
				{
					table_3.getItem(index).setText("Variable "+num1+": "+variables.get(num1));
				}
			}
			break;
		case "goto":
			int index=highlightSelection;
			System.out.println("test");
			while(!table.getItem(index).getText().contains(parameter+":"))
			{
				index--;			
			}
			table.getItem(index).setBackground(0, new Color(display, 0, 255, 0));
			nextStep=index;
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
		shell.setSize(1120, 484);
		shell.setText("Bytecode Interpreter");
		
		variables = new HashMap<Integer, Integer>();
		
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
		
		MenuItem mntmView = new MenuItem(menu, SWT.NONE);
		mntmView.setText("View");
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("Run");
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setBounds(10, 50, 279, 357);
		
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
		    	  String[] files= {"*.class"};
		    	  fileDialog.setFilterExtensions(files);
		    	  String filePath = fileDialog.open();
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
			    	  text.setText(bytecodeParse.getOpcodeString());
		    	  }
		    	  catch(NullPointerException e1)
		    	  {
		    		  e1.printStackTrace();
		    		  file = null;
		    	  }
		    	  /*CommandResponse commandResponse = new CommandResponse(file);
		    	  String bytecode = commandResponse.getResponse();
		    	  text.setText(bytecode.substring(bytecode.indexOf("\n")+1));*/
		    	  
		      }
		      
		    });
		
		Form form = formToolkit.createForm(shell);
		form.setBounds(414, 21, 0, 0);
		formToolkit.paintBordersFor(form);
		form.setText("New Form");
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setBounds(301, 50, 253, 357);
		formToolkit.paintBordersFor(table);
		table.addListener(SWT.EraseItem, new Listener() {
		    @Override
		    public void handleEvent(Event event) {
		        event.detail &= ~SWT.HIGH;
		    }
		});
		
		TableCursor tableCursor = new TableCursor(table, SWT.NONE);
		formToolkit.adapt(tableCursor);
		formToolkit.paintBordersFor(tableCursor);
			
		TableItem item1 = new TableItem(table, SWT.CENTER);
	    item1.setText("Bytecode");
	    Font boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setBounds(301, 50, 253, 357);
		formToolkit.adapt(table_1);
		formToolkit.paintBordersFor(table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int index=1;index<table_2.getItemCount();index++)
				{
					table_2.getItem(index).setText("");
				}
				table_2.setItemCount(1);
				ArrayList<TableItem> tableItems = null;
				table.setItemCount(1);
				OpcodeString opcodeString;
				TableItem tableItem;
				tableItems = new ArrayList<TableItem>();
				try
				{
					ArrayList<byte[]> codeBytes=bytecodeParse.getCodeBytes();
					for(int index=0;index<codeBytes.size();index++)
					{
						opcodeString=new OpcodeString(codeBytes.get(index));
						
						ArrayList<String> tableBytecode = opcodeString.stringify();
						for(int j=0;j<tableBytecode.size();j++)
						{
							tableItem=new TableItem(table, SWT.NONE);
							tableItem.setText(tableBytecode.get(j));
							tableItems.add(tableItem);
						}
						tableItem=new TableItem(table, SWT.NONE);
						tableItem.setText("");
						tableItems.add(tableItem);
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
					table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
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
		//System.out.println(new File("images/play.png").getAbsolutePath());
		btnNewButton.setBounds(10, 10, 34, 34);
		formToolkit.adapt(btnNewButton, true, true);
		
		Button button = formToolkit.createButton(shell, "", SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try
				{
					for(int index=1;index<table.getItemCount();index++)
					{
						table.getItem(index).setText("");
					}
					Display display = Display.getDefault();
					table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
					highlightSelection=1;
					for(int index=1;index<table_2.getItemCount();index++)
					{
						table_2.getItem(index).setText("");
					}
					table_2.setItemCount(1);
				}
				catch(IllegalArgumentException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		button.setImage(SWTResourceManager.getImage(Builder.class, "/org/eclipse/jface/wizard/images/stop.png"));
		button.setBounds(50, 10, 34, 34);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setImage(SWTResourceManager.getImage(new File("src/swtbuilder/images/next.png").getAbsolutePath()));
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getItemCount()!=1&&highlightSelection<table.getItemCount()-2)
				{
					Display display = Display.getDefault();
					table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
					if(nextStep!=-1)
					{
						highlightSelection=nextStep;
					}						
					else
					{
						if(table.getItem(highlightSelection+1).getText().equals("")) highlightSelection+=2;
						else highlightSelection++;
					}
					
					table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
					getSelection();
				}
				
			}
		});
		button_1.setBounds(90, 10, 34, 34);
		formToolkit.adapt(button_1, true, true);
		
		TableViewer tableViewer_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_2 = tableViewer_1.getTable();
		table_2.setBounds(572, 50, 253, 357);
		formToolkit.paintBordersFor(table_2);
		
		item1 = new TableItem(table_2, SWT.NONE);
	    item1.setText("Stack");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
		
		TableCursor tableCursor_1 = new TableCursor(table_2, SWT.NONE);
		formToolkit.adapt(tableCursor_1);
		formToolkit.paintBordersFor(tableCursor_1);
		
		TableViewer tableViewer_1_1 = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_3 = tableViewer_1_1.getTable();
		table_3.setBounds(839, 50, 253, 357);
		formToolkit.paintBordersFor(table_3);
		
		TableCursor tableCursor_1_1 = new TableCursor(table_3, SWT.NONE);
		formToolkit.adapt(tableCursor_1_1);
		formToolkit.paintBordersFor(tableCursor_1_1);
		
		item1 = new TableItem(table_3, SWT.NONE);
	    item1.setText("Variables");
	    boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	}
}
