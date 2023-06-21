package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
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

            // controller 등록
            Map<String, Controller> controllers = new HashMap<>();
            controllers.put("/user/create", new CreateUserController());
            controllers.put("/user/login", new LoginController());
            controllers.put("/user/list", new ListUserController());

            Controller controller = controllers.get(request.getPath());
            if (controller == null){
                response.forward(request.getPath());
                return;
            }
            controller.service(request, response);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
