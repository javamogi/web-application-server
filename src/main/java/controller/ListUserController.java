package controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ListUserController implements Controller{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
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
        response.forwardBody(sb.toString());
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
}
