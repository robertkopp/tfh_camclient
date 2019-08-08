/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.httpsender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import dev.robertkopp.autocamclient.ClientConfig;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.model.Camera;
import dev.robertkopp.autocamclient.model.ClientRaspi;
import dev.robertkopp.autocamclient.model.RaspiState;
import java.io.IOException;
import java.util.HashSet;
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
public class HttpRegistrationSender implements IHttpRegistrationSender {

    private final ClientConfig config;
    private final ICameraManager camMan;

    @Inject
    public HttpRegistrationSender(ClientConfig config, ICameraManager camMan) {
        this.config = config;
        this.camMan = camMan;

    }

    @Override
    public void sendHttpRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            String url = String.format("http://%s:%s/api/clientraspi", config.SERVER_IP, config.SERVER_PORT);
            HttpPost httppost = new HttpPost(url);

            ClientRaspi myself= new ClientRaspi();
            myself.setIpAdress(config.THIS_CLIENTS_IP);
            myself.setHostId(config.THIS_CLIENTS_HOSTNAME);
            myself.setRaspiState(RaspiState.Ok);
            myself.setCameras(new HashSet<Camera>());
            for (String name: camMan.getCameraNames()){
               myself.getCameras().add(new Camera(name));
            }
            
            ObjectMapper mapper = new ObjectMapper();

            String jsonString = mapper.writeValueAsString(myself);

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
