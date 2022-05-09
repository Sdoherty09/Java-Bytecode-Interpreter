package bytecodeInterpreter;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;

public class ViewCode extends Dialog {

	protected Object result;
	protected Shell shlCode;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ViewCode(Shell parent, int style, String text) {
		super(parent, style);
		setShlCode(text);
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

	private void setShlCode(String text)
	{
		shlCode.setText(text);
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCode = new Shell(getParent(), getStyle());
		shlCode.setSize(450, 300);
		shlCode.setText("Code");
		
		TextViewer textViewer = new TextViewer(shlCode, SWT.BORDER);
		StyledText styledText = textViewer.getTextWidget();
		styledText.setBounds(10, 10, 425, 246);

	}
}
