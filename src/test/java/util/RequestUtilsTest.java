package util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestUtilsTest {

    @Test
    public void getUrl_html_요청(){
        String rq = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", RequestUtils.getUrl(rq));
    }

    @Test
    public void getUrl_queryString_요청(){
        String rq = "GET /user/create?userId=javamogi HTTP/1.1";
        assertEquals("/user/create", RequestUtils.getUrl(rq));
    }
}