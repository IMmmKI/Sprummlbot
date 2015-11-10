package ga.codesplash.scrumplex.sprummlbot;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class WebGUI {
	public static HttpServer SERVER = null;

	public static void start() throws IOException {
		SERVER = HttpServer.create(new InetSocketAddress(Vars.PORT_WI), 0);
		HttpContext hc = SERVER.createContext("/", new WebGUIHandler());
		hc.setAuthenticator(new BasicAuthenticator("") {

			@Override
			public boolean checkCredentials(String user, String pw) {
				if (WebGUILogins.AVAILABLE.containsKey(user) && WebGUILogins.AVAILABLE.get(user).equals(pw)) {
					return true;
				}
				return false;
			}
		});
		SERVER.start();
	}
}