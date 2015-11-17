package ga.codesplash.scrumplex.sprummlbot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ga.codesplash.scrumplex.sprummlbot.stuff.Exceptions;
import ga.codesplash.scrumplex.sprummlbot.web.func.Actions;

class WebGUIHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange gui) throws IOException {
        String response = "404";

        String requestURI = gui.getRequestURI().toString();
        HashMap<String, String> args = new HashMap<>();
        String url = requestURI;

        if (requestURI.contains("!")) {
            String argsraw = requestURI.split("!")[1];
            String[] notargs = argsraw.split(",");

            for (String argsy : notargs) {
                String[] arg = argsy.split("=");
                args.put(arg[0], arg[1]);
            }

            url = requestURI.split("!")[0];
        }

        if (!url.endsWith("/")) {
            url = url + "/";
        }

        switch (url) {
            case "/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.Site_index().content;
                break;

            case "/manage/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.manage.Site_index(gui).content;
                break;

            case "/manage/shutdown/":
                response = Actions.shutdown();
                break;

            case "/manage/bans/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.manage.Site_bans().content;
                break;

            case "/manage/action/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.Site_index().content;
                break;

            case "/manage/action/ban/":
                response = Actions.ban(Integer.parseInt(args.get("userid")), convertToNormalText(args.get("msg")),
                        Integer.parseInt(args.get("time")));
                break;

            case "/manage/action/unban/":
                response = Actions.unban(Integer.parseInt(args.get("id")));
                break;

            case "/manage/log/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.manage.Site_log().content;
                break;

            case "/manage/action/kick/":
                response = Actions.kick(Integer.parseInt(args.get("userid")), convertToNormalText(args.get("msg")));
                break;

            case "/manage/action/sendpriv/":
                response = Actions.sendpriv(Integer.parseInt(args.get("userid")), convertToNormalText(args.get("msg")));
                break;

            case "/manage/action/poke/":
                response = Actions.poke(Integer.parseInt(args.get("userid")), convertToNormalText(args.get("msg")));
                break;

            case "/manage/action/clearaccounts/":
                response = Actions.clearAccounts();
                break;

            case "/catchmeifyoucan/":
                response = "<img src=\"data:image/jpeg;base64,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3k4jDXWq61qt00uoa74g1e5v9d1/Vbi61bW9Rv8AU7u5u5fXaKKACiiigAooooAKKKKACiiigAooooA/NX/grf8A8FBvCX/BMz9hL40ftP61cafP4207R38F/A7wvfFH/wCE0+N/i61vLLwFo32Vnja80vSrqK68Y+LI4nWaLwX4Y8R3Fv5lzDBDL/i16nqXxE+NvxM1HWNTuPE3xL+LPxc8dXepahdOt74h8ZeP/iJ4+1+S6u7hkiS41HXPE3irxLq0kjLGk17qeq35CJJPOA3+h5/wcdf8E1f+Czf/AAVU/aY8JeGvgL8C/Dsn7HXwB0UWnwuTWPjp8IPDd58QfH/ieys7rx78T9a8Oan4wtdS01kZLTwR4U07V4nu7DQ/D95rccemXPjPV9Mgo/8ABvB/wbT/ABj/AGQ/2idT/bH/AOCiPhDwfpfj74UXCWX7Nfwl03xR4W+ItpaeK72xD6l8avEmo+HLrV/D9teeGLe5/sj4b6Wt/c6rZ+I5NZ8XXlpot34e8GahqYB+63/BBD/glXpf/BKz9h3w54D8UWOny/tK/GaTTfif+0t4gtGiufK8XXFi8fhz4a2F/Hv8/wAP/CjQ7x/D8BiuLiw1DxXeeNPFGnGG28TC1g/bqiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=\" alt=\"hi m8 xdddd\">";
                break;

            case "/logout/":
                response = new ga.codesplash.scrumplex.sprummlbot.web.Site_logout(gui.getPrincipal().getUsername()).content;
                break;

        }
        gui.getResponseHeaders().add("Content-type", "text/html");
        if (response.equalsIgnoreCase("404")) {
            response = "<h2 style=\"text-align:center\">404 - Resource not found</h2>";
            gui.sendResponseHeaders(404, response.length());
        } else {
            gui.sendResponseHeaders(200, response.length());
        }

        OutputStream os = gui.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static String convertToNormalText(String s) {
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Exceptions.handle(e, "Encoding not supported!");
        }
        return s;
    }

}