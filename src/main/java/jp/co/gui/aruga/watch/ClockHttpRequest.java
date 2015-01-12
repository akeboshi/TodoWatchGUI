/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.gui.aruga.watch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import jp.co.gui.aruga.watch.entity.Todo;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author akari
 */
public class ClockHttpRequest {
    private String host = "localhost:8084";
    private String dir = "TodoWatch";
    private String url = "http://" + host + "/" + dir + "";
    private String encode = "UTF-8";
    private DefaultHttpClient httpClient = new DefaultHttpClient();

    public List<Todo> get() throws IOException{
        HttpGet request = new HttpGet(url + "/json");
        
        HttpResponse hr = httpClient.execute(request);
        String result = EntityUtils.toString(hr.getEntity());
        System.out.println(result);
        ObjectMapper om = new ObjectMapper();
        List<Todo> todo = om.readValue(result, new TypeReference<List<Todo>>() {});
        return todo;
    }
    
    public void delete(String id) throws IOException{
    }
    
    public boolean login(String user, String passwd) throws IOException {
        HttpPost request = new HttpPost(url + "/stlogin");
        
        StringEntity body = new StringEntity("{\"user\":\""+ user +"\",\"passwd\":\""+ passwd +"\"}");
        request.addHeader("Content-type", "application/json");
        request.setEntity(body);
        HttpResponse hr = httpClient.execute(request);

        String result = EntityUtils.toString(hr.getEntity());
        return result.equals("1");
    }
}
