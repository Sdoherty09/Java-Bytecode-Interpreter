package bytecodeInterpreter;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.graphics.Rectangle;

public class ViewCode extends Dialog {

	protected Object result;
	protected Shell shlCode;
	private String text;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ViewCode(Shell parent, int style, String text) {
		super(parent, style);
		setTextField(text);
		setText("SWT Dialog");
		
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlCode.open();
		shlCode.layout();
		
		Display display = getParent().getDisplay();
		while (!shlCode.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	private void setTextField(String text)
	{
		this.text=text;
	}
	private String getTextField()
	{
		return text;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCode = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER);
		shlCode.setSize(450, 300);
		shlCode.setText("Code");
		shlCode.setLayout(new FormLayout());
		FormData fd_styledText = new FormData();
		TextViewer textViewer = new TextViewer(shlCode, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		StyledText styledText = textViewer.getTextWidget();	
		shlCode.addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	fd_styledText.bottom = new FormAttachment(0, shlCode.getSize().y-60);
				fd_styledText.right = new FormAttachment(0, shlCode.getSize().x-30);
				fd_styledText.top = new FormAttachment(0, 10);
				fd_styledText.left = new FormAttachment(0, 10);
				styledText.setLayoutData(fd_styledText);
				styledText.setEditable(false);
				styledText.setText(getTextField());
		    }
		  });
		fd_styledText.bottom = new FormAttachment(0, shlCode.getSize().y-60);
		fd_styledText.right = new FormAttachment(0, shlCode.getSize().x-30);
		fd_styledText.top = new FormAttachment(0, 10);
		fd_styledText.left = new FormAttachment(0, 10);
		styledText.setLayoutData(fd_styledText);
		styledText.setEditable(false);
		styledText.setText(getTextField());
		
	}
}
