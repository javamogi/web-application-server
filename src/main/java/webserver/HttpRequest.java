package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String requestFirstLine;

    private Map<String, String> headers;

    private Map<String, String> params;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        requestFirstLine = line;
        headers = new HashMap<>();
        line = br.readLine();
        while(line != null && !line.equals("")){
            String[] tokens = line.split(":");
            headers.put(tokens[0], tokens[1].trim());
            line = br.readLine();
        }

        String queryString = null;
        if (getMethod().equals("GET")){
            String[] tokens = requestFirstLine.split(" ");
            queryString = tokens[1].substring(tokens[1].indexOf("?") + 1);
        } else {
            queryString = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }
        params = HttpRequestUtils.parseQueryString(queryString);
    }

    public String getMethod() throws IOException {
        String[] tokens = requestFirstLine.split(" ");
        return tokens[0];
    }

    public String getPath() throws IOException {
        String[] tokens = requestFirstLine.split(" ");
        if(!tokens[1].contains("?")){
            return tokens[1];
        }
        return tokens[1].substring(0, tokens[1].indexOf("?"));
    }

    public String getHeader(String connection) throws IOException {
        return headers.get(connection);
    }

    public String getParameter(String param) {
        return params.get(param);
    }
}
