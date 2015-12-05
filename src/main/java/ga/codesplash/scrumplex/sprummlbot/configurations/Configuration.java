package ga.codesplash.scrumplex.sprummlbot.configurations;

import ga.codesplash.scrumplex.sprummlbot.Commands;
import ga.codesplash.scrumplex.sprummlbot.Vars;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class
 */
public class Configuration {

    /**
     * Loads Config File
     *
     * @param f File, which will be loaded
     * @throws Exception
     */
    public static void load(File f) throws Exception {
        System.out.println("Updating Config File config.ini");
        if (!f.exists()) {
            if (!f.createNewFile()) {
                System.out.println("Could not create " + f.getName());
            }
        }
        Ini ini = new Ini(f);
        updateCFG(ini);
        Section connection = ini.get("Connection");

        if (!connection.containsKey("ip") || !connection.containsKey("port")) {
            throw new ConfigException("Connection not defined carefully!");
        }
        Vars.SERVER = connection.get("ip");
        Vars.PORT_SQ = connection.get("port", int.class);

        Section login = ini.get("Login");

        if (!login.containsKey("username") || !login.containsKey("password") || !login.containsKey("server-id")) {
            throw new ConfigException("Login not defined carefully!");
        }

        Vars.LOGIN[0] = login.get("username");
        Vars.LOGIN[1] = login.get("password");
        Vars.SERVER_ID = login.get("server-id", int.class);

        Section webinterface = ini.get("Webinterface");

        if (!webinterface.containsKey("port")) {
            throw new ConfigException("Webinterface not defined carefully!");
        }

        Vars.WEBINTERFACE_PORT = webinterface.get("port", int.class);

        Section appearance = ini.get("Appearance");

        if (!appearance.containsKey("nickname")) {
            throw new ConfigException("Appearance not defined carefully!");
        }

        Vars.NICK = appearance.get("nickname");

        Section afkmover = ini.get("AFK Mover");

        if (!afkmover.containsKey("enabled") || !afkmover.containsKey("channelid")
                || !afkmover.containsKey("maxafktime") || !afkmover.containsKey("afk-allowed-channel-id")) {
            throw new ConfigException("AFK Mover not defined carefully!");
        }

        Vars.AFK_ENABLED = afkmover.get("enabled", boolean.class);
        Vars.AFK_CHANNEL_ID = afkmover.get("channelid", int.class);
        Vars.AFK_TIME = afkmover.get("maxafktime", int.class) * 1000;
        int[] dontmove = afkmover.getAll("afk-allowed-channel-id", int[].class);
        for (int id : dontmove) {
            Vars.AFKALLOWED.add(id);
        }

        Section supportreminder = ini.get("Support Reminder");

        if (!supportreminder.containsKey("enabled") || !supportreminder.containsKey("channelid")) {
            throw new ConfigException("Support Reminder not defined carefully!");
        }

        Vars.SUPPORT_ENABLED = supportreminder.get("enabled", boolean.class);
        Vars.SUPPORT_CHANNEL_ID = supportreminder.get("channelid", int.class);

        Section antirec = ini.get("Anti Recording");

        if (!antirec.containsKey("enabled")) {
            throw new ConfigException("Anti Recording not defined carefully!");
        }

        Vars.ANTIREC_ENABLED = antirec.get("enabled", boolean.class);

        Section protector = ini.get("Server Group Protector");
        if (!protector.containsKey("enabled")) {
            throw new ConfigException("Server Group Protector not defined carefully!");
        }
        Vars.GROUPPROTECT_ENABLED = protector.get("enabled", boolean.class);

        Section broadcasts = ini.get("Broadcasts");
        if (!broadcasts.containsKey("enabled") || !broadcasts.containsKey("interval")) {
            throw new ConfigException("Broadcasts not defined carefully!");
        }

        Vars.BROADCAST_ENABLED = broadcasts.get("enabled", boolean.class);
        Vars.BROADCAST_INTERVAL = broadcasts.get("interval", int.class);

        Section misc = ini.get("Misc");

        if (!misc.containsKey("language") || !misc.containsKey("debug") || !misc.containsKey("check-tick")
                || !misc.containsKey("update-notification")) {
            throw new ConfigException("Misc not defined carefully!");
        }

        if (Language.fromID(misc.get("language")) != null) {
            Messages.setupLanguage(Language.fromID(misc.get("language")));
        } else {
            System.out.println("You defined a not supported language in config! Setting to EN!");
            Messages.setupLanguage(Language.EN);
        }

        Vars.UPDATE_ENABLED = misc.get("update-notification", boolean.class);

        Vars.TIMER_TICK = misc.get("check-tick", int.class);

        Vars.DEBUG = misc.get("debug", int.class);

        Section messages = ini.get("Messages");

        if (!messages.containsKey("skype-id") || !messages.containsKey("website") || !messages.containsKey("youtube")) {
            throw new ConfigException("Messages not defined carefully!");
        }

        Messages.add("skype", messages.get("skype-id"));
        Messages.add("website", messages.get("website"));
        Messages.add("youtube", messages.get("youtube"));

        Vars.AFKALLOWED.add(Vars.AFK_CHANNEL_ID);
        Vars.AFKALLOWED.add(Vars.SUPPORT_CHANNEL_ID);

        Section commands = ini.get("Commands");
        if (commands.containsKey("disabled")) {
            Commands.setup(commands.getAll("disabled", String[].class));
        } else {
            Commands.setup(new String[0]);
        }

        if (Vars.DEBUG == 2) {
            for (String str : ini.keySet()) {
                for (String out : ini.get(str).keySet()) {
                    System.out.println("[DEBUG] [CONF] [config.ini] " + str + "." + out + ": " + ini.get(str).get(out));
                }
            }
        }

        Clients.load(new File("clients.ini"));
        Broadcasts.load(new File("broadcasts.ini"));
        ServerGroupProtector.load(new File("groupprotect.ini"));
        System.out.println("Config loaded!");

    }

    public static void updateCFG(Ini ini) throws IOException {
        boolean changed = false;
        List<String> list = new ArrayList<>();
        list.add("Connection");
        list.add("Login");
        list.add("Webinterface");
        list.add("Appearance");
        list.add("AFK Mover");
        list.add("Support Reminder");
        list.add("Anti Recording");
        list.add("Broadcasts");
        list.add("Server Group Protector");
        list.add("Commands");
        list.add("Messages");
        list.add("Misc");
        for (String secname : list) {
            if (!ini.containsKey(secname)) {
                System.out.println("Found missing Section! " + secname);
                createSectionItems(ini.add(secname));
                changed = true;
            }
        }
        if (changed) {
            System.out.println("Saving updated config...");
            ini.store();
            System.out.println("Done! Please setup the new Configuration Sections!");
        }
    }

    private static void createSectionItems(Section sec) {
        switch (sec.getName()) {
            case "Connection":
                sec.put("ip", "localhost");
                sec.putComment("ip", "IP of the teamspeak3 server (NO SRV Records)");
                sec.put("port", "10011");
                sec.putComment("port", "Port of Query Login (Leave this normal if you dont know it)");
                break;

            case "Login":
                sec.put("username", "serveradmin");
                sec.putComment("username", "Put the username of ysour server's serveradminquery account");
                sec.put("password", "pass");
                sec.putComment("password", "Put the password of your server's serveradminquery account");
                sec.put("server-id", 1);
                sec.putComment("server-id", "Put the serverid of your server here (On self hosted servers it is 1");
                break;

            case "Webinterface":
                sec.put("port", 9911);
                sec.putComment("port", "Port for the Webinterface. 0=disabled");
                break;

            case "Appearance":
                sec.put("nickname", "Sprummlbot");
                sec.putComment("nickname", "Nickname for the Bot");
                break;

            case "AFK Mover":
                sec.put("enabled", true);
                sec.putComment("enabled", "Defines if it is enabled");
                sec.put("channelid", 0);
                sec.putComment("channelid", "Defines the channel ID of the AFK Channel, where AFKs will be moved to");

                sec.put("maxafktime", 600);
                sec.putComment("maxafktime", "Defines how long someone can be afk, if he is muted. (in seconds 600=10min)");
                sec.put("afk-allowed-channel-id", 1);
                sec.add("afk-allowed-channel-id", 2);
                sec.add("afk-allowed-channel-id", 3);
                sec.putComment("afk-allowed-channel-id",
                        "Put the channel ids of the channels where being afk is allowed. e.g. music channels or support queue. To expand the list add in a new line afk-allowed-channel-id=%CHANNELID%");
                break;

            case "Support Reminder":
                sec.put("enabled", true);
                sec.putComment("enabled", "Defines if it is enabled");
                sec.put("channelid", 0);
                sec.putComment("channelid", "Defines the channel ID of a support queue channel");
                break;

            case "Anti Recording":
                sec.put("enabled", true);
                sec.putComment("enabled", "Defines if it is enabled");
                break;

            case "Broadcasts":
                sec.put("enabled", false);
                sec.putComment("enabled", "This is the broadcast feature. You can add messaegs to the broadcasts.ini");
                sec.put("interval", 300);
                sec.putComment("interval",
                        "This sets the interval when messages will be sent to users. (in seconds! 300=5min)");
                break;

            case "Commands":
                sec.put("disabled", "!COMMAND1");
                sec.add("disabled", "!COMMAND2");
                sec.putComment("disabled",
                        "This list can disable the following commands: !login, !mute, !skype, !support, !web, !yt. These commands will also be disabled automatically if their features are disabled (e.g. !mute will be disabled if Broadcasts are disabled)");

            case "Server Group Protector":
                sec.put("enabled", false);
                sec.putComment("enabled",
                        "Enables the Server Group Protector. This protects users from joining Server Groups. It will be defined in groupprotect.ini");
                break;

            case "Messages":
                sec.put("skype-id", "skypeid");
                sec.putComment("skype-id", "Skype ID for !skype command");
                sec.put("website", "website");
                sec.putComment("website", "Website for !web command");
                sec.put("youtube", "youtube");
                sec.putComment("youtube", "Youtube channel link for !yt command");
                break;

            case "Misc":
                sec.put("language", "en");
                sec.putComment("language", "Language definition. Available languages: de, en");
                sec.put("update-notification", true);
                sec.putComment("update-notification",
                        "Defines if the bot should check for updates (Bot will only send a message to console if an update is available.");
                sec.put("check-tick", 4000);
                sec.putComment("check-tick",
                        "Defines the interval when Sprummlbot will check for AFK, Support or Recorders. Define in milliseconds (1second = 1000milliseconds). If you have problems with the Network Performance put this higher");
                sec.put("debug", 0);
                sec.putComment("debug", "Developers only. xD");
                break;
        }
        System.out.println("Section created successfully!");
    }
}