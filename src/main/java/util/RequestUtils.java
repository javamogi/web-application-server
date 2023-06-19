package util;

public class RequestUtils {

    public static String getUrl(String line){
        String[] tokens = line.split(" ");
        return tokens[1];
    }
}
