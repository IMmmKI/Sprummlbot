package ga.scrumplex.ml.sprum.sprummlbot.Configurations;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

import ga.scrumplex.ml.sprum.sprummlbot.Config;
import ga.scrumplex.ml.sprum.sprummlbot.Logger;

public class Broadcasts {

	public static void load(File f) throws Exception {
		Logger.out("Updating Config File " + f.getName());
		updateCFG(f);
		Ini ini = new Ini(f);

		Section sec = ini.get("Messages");
		String[] messages = sec.getAll("msg", String[].class);
		for (String msg : messages) {
			Config.BROADCASTS.add(msg);
		}
	}

	public static void updateCFG(File f) throws InvalidFileFormatException, IOException {
		if(!f.exists()) {
			f.createNewFile();
		}
		Ini ini = new Ini(f);
		if (!ini.containsKey("Messages")) {
			Section sec = ini.add("Messages");
			ini.putComment("Messages", "You need to put the broadcast messages into the list below");
			sec.put("msg", "Visit our Website!");
		}
		Logger.out("Saving updated config...");
		ini.store();
		Logger.out("Done! Please setup the new Configuration Sections!");
	}
}
