package http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {

    private String id;
    private Map<String, Object> attribute = new HashMap<>();

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setAttribute(String name, Object value){
        attribute.put(name, value);
    }

    public Object getAttribute(String name){
        return attribute.get(name);
    }

    public void removeAttribute(String name){
        attribute.remove(name);
    }

    public void invalidate(){
        attribute.clear();
    }
}
