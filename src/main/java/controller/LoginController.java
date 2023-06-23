package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController {

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user == null || !user.getPassword().equals(request.getParameter("password"))) {
            response.forward("/user/login_failed.html");
            return;
        }
        response.addHeader("Set-Cookie", "logined=true; Path=/");
        response.sendRedirect("/index.html");
    }

}
