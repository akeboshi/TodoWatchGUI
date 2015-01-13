/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.gui.aruga.watch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.gui.aruga.watch.entity.Category;
import jp.co.gui.aruga.watch.entity.Todo;
import jp.co.gui.aruga.watch.entity.TodoResponse;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author akari
 */
public class ClockHttpRequest {
    final private String host = "localhost:8084";
    final private String dir = "TodoWatch";
    final private String url = "http://" + host + "/" + dir + "";
    final private String encode = "UTF-8";
    final private DefaultHttpClient httpClient = new DefaultHttpClient();
    final private ObjectMapper om = new ObjectMapper();

    public List<Todo> get(String category) throws IOException{
        String jsonUrl = url + "/json";
        if (category != null)
            jsonUrl = jsonUrl+ "?category=" + category;
        HttpGet request = new HttpGet(jsonUrl);
        
        HttpResponse hr = httpClient.execute(request);
        String result = EntityUtils.toString(hr.getEntity());
        List<TodoResponse> todo = om.readValue(result, new TypeReference<List<TodoResponse>>() {});
        List<Todo> listT = new ArrayList<>();
        for (TodoResponse todo1 : todo) {
              listT.add(todo1.getTodo());
        }
        return listT;
    }
    
    public void delete(String id) throws IOException{
        HttpDelete request = new HttpDelete(url + "/json/" + id);
        
        HttpResponse hr = httpClient.execute(request);
        if (hr.getStatusLine().getStatusCode() >= 400  )
            throw new UnsupportedOperationException();
    }
    
    public Todo create(Todo todo) throws IOException {
        HttpPost request = new HttpPost(url + "/json");
        
        String json = om.writeValueAsString(todo);
        StringEntity se = new StringEntity(json,encode);
        request.addHeader("Content-type", "application/json");
        request.setEntity(se);
        HttpResponse hr = httpClient.execute(request);
        
        String result = EntityUtils.toString(hr.getEntity());
        TodoResponse tResult = om.readValue(result, TodoResponse.class);
        
        return tResult.getTodo();
    }
    
    public void update(Todo todo) throws IOException {
        if (todo.getId() == null)
            throw new UnsupportedOperationException();
        
        HttpPut request = new HttpPut(url + "/json" + todo.getId());
        
        String json = om.writeValueAsString(todo);
        StringEntity se = new StringEntity(json);
        request.addHeader("Content-type", "application/json");
        request.setEntity(se);
        HttpResponse hr = httpClient.execute(request);
    }
    
    public List<Category> getCategory() throws IOException{
        HttpGet request = new HttpGet(url + "/category");
        
        HttpResponse hr = httpClient.execute(request);
        String result = EntityUtils.toString(hr.getEntity());
        List<Category> cat = om.readValue(result, new TypeReference<List<Category>>() {});
        return cat;
    }
    
    public Category createCategory(String body) throws IOException {
        HttpPost request = new HttpPost(url + "/category");
        
        Category ca = new Category();
        ca.setBody(body);
        String json = om.writeValueAsString(ca);
        StringEntity se = new StringEntity(json, encode);
        request.addHeader("Content-type", "application/json");


        request.setEntity(se);
        HttpResponse hr = httpClient.execute(request);
        
        String result = EntityUtils.toString(hr.getEntity());
        Category cResult = om.readValue(result, Category.class);
        
        return cResult;
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
