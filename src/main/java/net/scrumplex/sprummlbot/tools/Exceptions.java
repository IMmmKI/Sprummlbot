package net.scrumplex.sprummlbot.tools;

import net.scrumplex.sprummlbot.Main;
import net.scrumplex.sprummlbot.plugins.SprummlbotPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Exceptions {

    public static void handle(Throwable throwable, String cause) {
        handle(throwable, cause, true);
    }

    public static void handle(Throwable throwable, String cause, boolean shutdown) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d_M_Y__HH_mm_ss");
        File directory = new File("logs");
        directory.mkdir();
        File file = new File(directory, "error_" + sdf.format(cal.getTime()) + ".log");
        int i = 1;
        while (file.exists()) {
            file = new File(directory, "error_" + sdf.format(cal.getTime()) + "." + i + ".log");
            i++;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            throwable.printStackTrace();
        }

        System.err.println(cause + " Full error log at " + file.getAbsolutePath() + ".");

        StringBuilder contents = new StringBuilder();
        contents.append("Error Log from ").append(sdf.format(cal.getTime())).append(".\n");
        contents.append("Custom message: ").append(cause).append("\n");
        contents.append(EasyMethods.convertThrowableToString(throwable));
        contents.append("\n\nPlease contact support!");
        try {
            EasyMethods.writeToFile(file, contents.toString());
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            throwable.printStackTrace();
        }

        if (shutdown)
            Main.shutdown(1);

    }

    public static void handlePluginError(Throwable throwable, SprummlbotPlugin plugin) {
        handlePluginError(throwable, plugin.getPluginInfo().getPluginFile(), "");
    }

    public static void handlePluginError(Throwable throwable, SprummlbotPlugin plugin, String custommsg) {
        handlePluginError(throwable, plugin.getPluginInfo().getPluginFile(), custommsg);
    }

    public static void handlePluginError(Throwable throwable, File jarFile) {
        handlePluginError(throwable, jarFile, "");
    }

    public static void handlePluginError(Throwable throwable, File jarFile, String custommsg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d_M_Y__HH_mm_ss");
        File directory = new File("logs", "plugins");
        directory.mkdirs();
        File file = new File(directory, jarFile.getName() + "_error_" + sdf.format(cal.getTime()) + ".log");
        int i = 1;
        while (file.exists()) {
            file = new File(directory, jarFile.getName() + "_error_" + sdf.format(cal.getTime()) + "." + i + ".log");
            i++;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            throwable.printStackTrace();
        }
        if (custommsg.equalsIgnoreCase(""))
            System.err.println("[Plugins] ERROR! More information in " + file.getAbsolutePath());
        else
            System.err.println("[Plugins] " + custommsg + "! More information in " + file.getAbsolutePath());

        StringBuilder contents = new StringBuilder();
        contents.append("Error Log from ").append(sdf.format(cal.getTime())).append(".\n");
        contents.append(EasyMethods.convertThrowableToString(throwable));
        contents.append("\n\nPlease contact support!");

        try {
            EasyMethods.writeToFile(file, contents.toString());
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            throwable.printStackTrace();
        }
    }


}
