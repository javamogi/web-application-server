package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if(request.getPath().equals("/user/create")){
                User user = new User(request.getParameter("userId"),
                        request.getParameter("password"),
                        request.getParameter("name"),
                        request.getParameter("email"));
                log.debug("user : {}", user);
                DataBase.addUser(user);
                response.sendRedirect("/user/login.html");
            } else if (request.getPath().equals("/user/login")){
                User user = DataBase.findUserById(request.getParameter("userId"));
                if(user == null || !user.getPassword().equals(request.getParameter("password"))){
                    response.forward("/user/login_failed.html");
                    return;
                }
                response.addHeader("Set-Cookie", "logined=true; Path=/");
                response.sendRedirect("/index.html");
            } else if(request.getPath().equals("/user/list")){
                if(!isLogin(request)){
                    response.forward("/user/login.html");
                    return;
                }
                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border'1'>");
                for(User user : users){
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                responseData(out, sb.toString());
            } else {
                response.forward(request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isLogin(HttpRequest request) throws IOException {
        String cookie = request.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
        String value = cookies.get("logined");
        if (value == null){
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private void responseData(OutputStream out, String data) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, data.getBytes().length);
        responseBody(dos, data.getBytes());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
