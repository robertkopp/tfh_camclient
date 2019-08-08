/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.httpsender;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author robert kopp
 */
public interface IHttpImageSender {

    void sendHttpRequest(BufferedImage b, String shotId, long currentTimeMillis, String shotnumber) throws IOException;
    
}
