package util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestUtilsTest {

    @Test
    public void getUrl(){
        String rq = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", RequestUtils.getUrl(rq));
    }
}