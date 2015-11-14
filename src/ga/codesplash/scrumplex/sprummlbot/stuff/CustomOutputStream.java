package ga.codesplash.scrumplex.sprummlbot.stuff;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomOutputStream extends PrintStream {

	public CustomOutputStream(OutputStream out) {
		super(out);
	}

	public static StringBuilder log = new StringBuilder();

	public void println(String msg) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d.M.Y HH:mm:ss");
		log.append("[" + sdf.format(cal.getTime()) + " " + msg + "\n<br>\n");
		super.println("[" + sdf.format(cal.getTime()) + " " + msg);
	}

}