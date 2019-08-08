/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.httpsender;

import com.google.inject.Inject;
import dev.robertkopp.autocamclient.ClientConfig;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author robert kopp
 */
public class HttpSingleImageSender implements IHttpSingleImageSender {

    private InputStream convertToStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    private final ClientConfig config;

    @Inject
    public HttpSingleImageSender(ClientConfig config) {
        this.config = config;

    }

    @Override
    public void sendHttpRequest(BufferedImage b, String camid, long currentTimeMillis) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URLCodec codec = new URLCodec();
            String url = String.format("http://%s:%s/api/singleimage/snapresult/%s/%s", config.SERVER_IP, config.SERVER_PORT, codec.encode(
                    config.THIS_CLIENTS_HOSTNAME).replaceAll("\\+", "%20"),
                    codec.encode(camid).replaceAll("\\+", "%20")
            );
            HttpPost httppost = new HttpPost(url);

            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("file", convertToStream(b), ContentType.create("application/octet-stream"), "test.png")
                    .build();

            httppost.setEntity(entity);

            System.out.println("Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } catch (EncoderException ex) {
            Logger.getLogger(HttpSingleImageSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            httpclient.close();
        }
    }

}
