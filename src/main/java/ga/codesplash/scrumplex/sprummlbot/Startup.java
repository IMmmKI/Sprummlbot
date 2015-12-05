package ga.codesplash.scrumplex.sprummlbot;

import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.CommandFuture;
import com.github.theholywaffle.teamspeak3.api.exception.TS3ConnectionFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerQueryInfo;

import java.util.logging.Level;

/**
 * Connection phase.
 * This class connects to the Teamspeak 3 Server
 */
class Startup {

    /**
     * Startup
     *
     * @throws TS3ConnectionFailedException
     */
    public static void start() throws TS3ConnectionFailedException {
        Tasks.init();
        final TS3Config config = new TS3Config();
        config.setHost(Vars.SERVER);
        config.setQueryPort(Vars.PORT_SQ);
        System.out.println("Debug Mode: " + Vars.DEBUG);
        switch (Vars.DEBUG) {
            case 0:
                config.setDebugLevel(Level.OFF);
                break;

            case 1:
                config.setDebugLevel(Level.WARNING);
                break;

            case 2:
                config.setDebugLevel(Level.ALL);
                break;
        }
        config.setFloodRate(FloodRate.UNLIMITED);
        config.setLoginCredentials(Vars.LOGIN[0], Vars.LOGIN[1]);

        System.out.println("Connecting to " + Vars.SERVER + ":" + Vars.PORT_SQ + " with credentials: " + Vars.LOGIN[0]
                + ", ******");
        Vars.QUERY = new TS3Query(config);
        Vars.QUERY.connect();
        System.out.println("Selecting Server " + Vars.SERVER_ID);
        Vars.API = Vars.QUERY.getAsyncApi();
        Vars.API.selectVirtualServerById(Vars.SERVER_ID);
        Vars.API.setNickname(Vars.NICK);
        System.out.println("Changing ServerQuery Rights");
        ServerOptimization.permissions();
        if (Vars.DEBUG > 1)
            System.out.println(Vars.API.whoAmI().toString());

        Vars.API.whoAmI().onSuccess(new CommandFuture.SuccessListener<ServerQueryInfo>() {
            @Override
            public void handleSuccess(ServerQueryInfo serverQueryInfo) {
                Vars.QID = serverQueryInfo.getId();
            }
        });
        if (Vars.AFK_ENABLED)
            System.out.println("Starting AFK process...");
        if (Vars.SUPPORT_ENABLED)
            System.out.println("Starting Support process...");
        if (Vars.ANTIREC_ENABLED)
            System.out.println("Starting Anti Record process...");
        if (Vars.GROUPPROTECT_ENABLED)
            System.out.println("Starting Groupprotect process...");
        if (Vars.AFK_ENABLED || Vars.SUPPORT_ENABLED || Vars.ANTIREC_ENABLED || Vars.GROUPPROTECT_ENABLED)
            Tasks.startService();

        if (Vars.BROADCAST_ENABLED) {
            System.out.println("Starting Broadcast Service");
            Tasks.startBroadCast();
        }

        System.out.println("Events are being registered...");
        Events.start();

        System.out.println("Starting Keep Alive Process");
        Tasks.startKeepAlive();
    }
}