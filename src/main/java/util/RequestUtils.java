package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

    public static String getUrl(String line){
        String[] tokens = line.split(" ");
        String url = tokens[1];
        if(url.contains(".html")){
            return tokens[1];
        }
        int index = url.indexOf("?");
        String requestPath = url.substring(0, index);
        String params = url.substring(index+1);
        Map<String, String> values = HttpRequestUtils.parseQueryString(params);
        User user = new User(values.get("userId"), values.get("password"), values.get("name"), values.get("email"));
        log.debug("user : {}", user);
        return requestPath;
    }
}
