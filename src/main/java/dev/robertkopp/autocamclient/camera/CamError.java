/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.github.sarxos.webcam.Webcam;
import java.io.IOException;

/**
 *
 * @author robert kopp
 */
public class CamError {

    CamError(Webcam webcam, IOException ex, String shotId) {
        message= String.format("kamera:%s konnte kein bild anlegen für shot:%s", webcam.getName(),shotId);
    }
    
    private String message;

    public String getMessage() {
        return message;
    }

   
    
    
}
