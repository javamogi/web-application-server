package webserver;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RequestLineTest {
    @Test
    public void create_method(){
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertEquals(HttpMethod.GET, line.getMethod());
        assertEquals("/index.html", line.getPath());
        assertFalse(line.getMethod().isPost());

        line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals("/index.html", line.getPath());
    }

    @Test
    public void create_path_and_params(){
        RequestLine line = new RequestLine("GET /user/create?userId=javamogi&password=pass HTTP/1.1");
        assertEquals(HttpMethod.GET, line.getMethod());
        assertEquals("/user/create", line.getPath());
        Map<String, String> params = line.getParams();
        assertEquals(2, params.size());
    }
}