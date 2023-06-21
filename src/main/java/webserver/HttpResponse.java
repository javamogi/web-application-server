package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos;
    private Map<String, String> headers;
    public HttpResponse(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
        headers = new HashMap<>();
    }

    public void forward(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        if(path.endsWith(".css")){
            headers.put("Content-Type", "text/css;charset=utf-8");
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
        }
        response200Header(body.length);
        responseBody(body);
    }

    public void forwardBody(String path){
        byte[] body = path.getBytes();
        response200Header(body.length);
        responseBody(body);
    }

    public void sendRedirect(String path) {
        addHeader("Location", path);
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private void response200Header(int lengthOfBodyContent) {
        addHeader("Content-Length", String.valueOf(lengthOfBodyContent));
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void processHeaders(){
        for (String key : headers.keySet()){
            try {
                dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
