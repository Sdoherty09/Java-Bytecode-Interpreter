package bytecodeInterpreter;

public class LineNumber {
	private int startPc;
	private int lineNumber;
	public LineNumber(int startPc, int lineNumber) {
		setStartPc(startPc);
		setLineNumber(lineNumber);
	}
	public int getStartPc() {
		return startPc;
	}
	public void setStartPc(int startPc) {
		this.startPc = startPc;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
}
