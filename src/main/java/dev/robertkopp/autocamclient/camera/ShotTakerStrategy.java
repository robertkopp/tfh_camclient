/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;

/**
 *
 * @author robert kopp
 */
public class ShotTakerStrategy implements IShotTakerStrategy {
    
    @Override
    public BufferedImage takeShot(Webcam w){
         w.open();
        BufferedImage result = w.getImage();
        w.close();
        return result;
    }
    
}
