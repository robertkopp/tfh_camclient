/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.httpsender;

import com.google.inject.Inject;
import dev.robertkopp.autocamclient.ClientConfig;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author robert kopp
 */
public class HttpPingSender implements IHttpPingSender {

   
    
    private final ClientConfig config;
    

    @Inject
    public HttpPingSender(ClientConfig config) {
        this.config = config;
        
    }
    
    @Override
    public void sendHttpRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            String url = String.format("http://%s:%s/api/ping", config.SERVER_IP, config.SERVER_PORT);
            HttpPost httppost = new HttpPost(url);

            String jsonString = String.format("{\"ip\":\"%s\",\"time\":%s}",config.THIS_CLIENTS_IP,System.currentTimeMillis());

            StringEntity jsonEntity = new StringEntity(jsonString);
            httppost.addHeader("content-type", "application/json");

            httppost.setEntity(jsonEntity);

            System.out.println("Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
