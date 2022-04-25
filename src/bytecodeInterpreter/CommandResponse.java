package bytecodeInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandResponse {
	private File file;

	
	
	public CommandResponse(File file) {
		setFile(file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public String getResponse() {
		String response="";
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "javap -c \""+file.getPath()+"\"");
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
	            response+=line+"\n";
	        }
	    return response;
	}

	@Override
	public String toString() {
		return "CommandResponse [file=" + file + "]";
	}
	
	
}
