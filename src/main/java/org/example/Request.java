package org.example;

import java.io.InputStream;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final InputStream body;
    private final Map<String,String> headers;

    public Request(String method, String path, InputStream body, Map<String,String> headers){
        this.method = method;
        this.path = path;
        this.body = body;
        this.headers =headers;
    }
    public String getMethod(){
        return method;
    }
    public String getPath(){
        return path;
    }
    @Override
    public String toString(){
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
