package webserver;

import org.junit.Test;

import java.io.*;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws IOException {
        // Http_Forward.txt 결과는 응답 body에 index.html이 포함되어 있어야한다.
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws IOException {
        // Http_Redirect.txt 결과는 응답 header에
        // Location 정보가 /index.html로 포함되어 있어야 한다.
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws IOException {
        // Http_Cookie.txt 결과는 응답 header에 Set-Cookie 값으로
        // logined=true 값이 포함되어 있어야 한다.
        HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String fileName) throws IOException {
        return new FileOutputStream(new File(testDirectory + fileName));
    }
}