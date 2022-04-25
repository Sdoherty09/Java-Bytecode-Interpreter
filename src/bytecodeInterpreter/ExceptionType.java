package bytecodeInterpreter;

public class ExceptionType {
	private int startPc;
	private int endPc;
	private int handlerPc;
	private int catchType;
	
	public ExceptionType(int startPc, int endPc, int handlerPc, int catchType) {
		setStartPc(startPc);
		setEndPc(endPc);
		setHandlerPc(handlerPc);
		setCatchType(catchType);
	}
	public int getStartPc() {
		return startPc;
	}
	public void setStartPc(int startPc) {
		this.startPc = startPc;
	}
	public int getEndPc() {
		return endPc;
	}
	public void setEndPc(int endPc) {
		this.endPc = endPc;
	}
	public int getHandlerPc() {
		return handlerPc;
	}
	public void setHandlerPc(int handlerPc) {
		this.handlerPc = handlerPc;
	}
	public int getCatchType() {
		return catchType;
	}
	public void setCatchType(int catchType) {
		this.catchType = catchType;
	}
	
}
