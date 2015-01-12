/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.gui.aruga.watch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author akari
 */
public class ClockHttpRequest {
    private String host = "example.test";
    private String port = "";
    private String encode = "UTF-8";

    
    public void post(String path) throws IOException {
        // TODO Auto-generated method stub

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost request = new HttpPost("http://example.test:9000/post");

        String strJson = "";
        
        path = "/path/to/test.json";
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String str;
        while ((str = br.readLine()) != null) {
            strJson += str;
        }
        System.out.println(strJson);

        StringEntity body = new StringEntity(strJson);
        request.addHeader("Content-type", "application/json");
        request.setEntity(body);

        String result = httpClient.execute(request, new ResponseHandler<String>() {
            public String handleResponse(HttpResponse response) throws IOException {
                switch (response.getStatusLine().getStatusCode()) {
                    case HttpStatus.SC_OK:
                        System.out.println(HttpStatus.SC_OK);
                        return EntityUtils.toString(response.getEntity(), encode);
                    case HttpStatus.SC_NOT_FOUND:
                        System.out.println(HttpStatus.SC_NOT_FOUND);
                        return "404";
                    default:
                        System.out.println("unknown");
                        return "unknown";
                }
            }
        });
        System.out.println(result);
        httpClient.getConnectionManager().shutdown();
    }
}
