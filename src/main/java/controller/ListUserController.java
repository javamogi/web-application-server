package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Collection;

public class ListUserController extends AbstractController{

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if(!isLogin(request.getCookie("logined"))){
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

    private boolean isLogin(String value) {
        if (value == null){
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
