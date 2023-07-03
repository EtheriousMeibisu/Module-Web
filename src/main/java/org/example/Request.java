package org.example;
import org.apache.http.NameValuePair;
import java.util.List;
import java.util.stream.Collectors;


public class Request {
    private final String method;
    private final String path;
    private final String body;
    private final List<String> headers;
    private List<NameValuePair> queryParams;

    public Request(String method, String path, String body, List<String> headers){
        this.method = method;
        this.path = path;
        this.body = body;
        this.headers =headers;
    }
    public void setQueryParams(List<NameValuePair> params) {
        this.queryParams = params;
    }
    public String getMethod(){
        return method;
    }
    public String getPath(){
        return path;
    }

    public List<NameValuePair> getQueryParams(){
        return queryParams;
    }
    public String getQueryParam(String param){
        return queryParams.stream()
                .filter(p -> p.getName().equals(param))
                .map(p -> p.getValue())
                .collect(Collectors.joining(", "));
    }
    @Override
    public String toString(){
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", queryParams='" + queryParams + '\''+
                '}';
    }
}
