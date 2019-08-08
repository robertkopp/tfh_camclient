/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.mocks;

import dev.robertkopp.autocamclient.httpsender.IHttpImageSender;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author robert kopp
 */
public class HttpImageSenderMock implements IHttpImageSender{
    private BufferedImage b;
    private String shotId;
    private int count;
    private long currentTimeMillies;

    @Override
    public void sendHttpRequest(BufferedImage b, String shotId, long currentTimeMillis, String imagenumber) throws IOException {
        this.b=b;
        this.shotId=shotId;
        this.currentTimeMillies=currentTimeMillis;
        count++;
    }
    
}
