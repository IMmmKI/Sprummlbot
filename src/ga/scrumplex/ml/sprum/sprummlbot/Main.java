package ga.scrumplex.ml.sprum.sprummlbot;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import ga.scrumplex.ml.sprum.sprummlbot.stuff.ConfigException;
import ga.scrumplex.ml.sprum.sprummlbot.stuff.Exceptions;
import ga.scrumplex.ml.sprum.sprummlbot.stuff.PermissionModifier;

public class Main extends Config{
	
	public static void main(String[] args) {
		
		File f = new File("config.ini");
		Logger.out("Loading Config!");
		if(f.exists() == false) {
			Exceptions.handle(new ConfigException("Config File doesn't exist!"), "Config Files doesnt exist!");
		}
		try {
			Configuration.load(f);
		} catch (Exception e) {
			Exceptions.handle(e, "CONFIG LOADING FAILED!");
		}
		
		if(Config.updater) {
			Logger.out("Checking for updates!");
			Updater update = new Updater("http://main.linevast.scrumplex.ga/updateinfo.txt", Config.versionid);
			try {
				if(update.isupdateavailable()) {
					Logger.out("[UPDATER] UPDATE AVAILABLE!");
					Logger.out("[UPDATER] Download here: https://github.com/Scrumplex/Sprummlbot");
					System.exit(0);
				}
			} catch (Exception e1) {
				Exceptions.handle(e1, "UPDATER ERROR", true);
			}
		}
		
		Logger.out("Hello! Sprummlbot v" + Config.version + " is starting...");
		Logger.out("");
		
		try {
			Startup.start();
		} catch (UnknownHostException e2) {
			Exceptions.handle(e2, "UNKNOWN HOST: " + Config.server + " is invalid!");
		} catch (IOException e2) {
			Exceptions.handle(e2, "CONNECTION ERROR! Is host up?");
		}
		if(Config.webport != 0) {
			try {
				WebGUI.start();
			} catch (IOException e) {
				Exceptions.handle(e, "Webinterface couldn't start. Port: " + Config.webport + " already bound?");
			}
			Logger.out("Started WebGUI on port " + Config.webport);
		}
		
		Logger.out("Lastly changing ServerQuery Rights");
		PermissionModifier.allow();
		
		for(Client c : api.getClients()) {
			if(admins.contains(c.getUniqueIdentifier())) {
				api.sendPrivateMessage(c.getId(), "Sprummlbot is running!");
			}
		}
		Logger.out("DONE!");
		Logger.out("Available Commands: login, list, stop");
		Console.runReadThread();
	}
}
