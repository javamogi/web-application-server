package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession getSession(String id){
        return sessions.get(id);
    }

    public static void setSession(String id, HttpSession session){
        sessions.put(id, session);
    }
}
