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

public class Builder {

	protected Shell shell;
	private Text text;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	private Table table_1;
	private BytecodeParse bytecodeParse;
	int highlightSelection=1;

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
		shell.setSize(609, 484);
		shell.setText("Bytecode Interpreter");
		
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
		table.setBounds(301, 50, 279, 357);
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
		
		TableColumn tblclmnBytecode = new TableColumn(table, SWT.NONE);
		tblclmnBytecode.setWidth(table.getBounds().width/2);
		tblclmnBytecode.setText("Bytecode");
		
		
		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(table.getBounds().width/2);
		tblclmnValue.setText("Value");
			
		TableItem item1 = new TableItem(table, SWT.NONE);
	    item1.setText(new String[] { "Bytecode", "Stack"});
	    Font boldFont = new Font( item1.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
	    item1.setFont( boldFont );
	
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setBounds(301, 50, 279, 357);
		formToolkit.adapt(table_1);
		formToolkit.paintBordersFor(table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<TableItem> tableItems = null;
				try
				{
					
					ArrayList<ArrayList<String>> opcodes=BytecodeParse.opcodes;
					tableItems = new ArrayList<TableItem>();
					for(int index=1;index<opcodes.size();index++)
					{
						for(int j=1;j<opcodes.get(index).size();j++)
						{
							TableItem tableItem=new TableItem(table, SWT.NONE);
							tableItem.setText(opcodes.get(index).get(j));
							tableItems.add(tableItem);
						}
						
					}
				}
				catch(NullPointerException e1)
				{
					
				}
				Display display = Display.getDefault();
				
				table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
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
				for(int index=1;index<table.getItemCount();index++)
				{
					table.getItem(index).setText("");
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
				Display display = Display.getDefault();
				table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 255, 255));
				highlightSelection++;
				table.getItem(highlightSelection).setBackground(0, new Color(display, 255, 0, 0));
			}
		});
		button_1.setBounds(90, 10, 34, 34);
		formToolkit.adapt(button_1, true, true);
		
	}
}
