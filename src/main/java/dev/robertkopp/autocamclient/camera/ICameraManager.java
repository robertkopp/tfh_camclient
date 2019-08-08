/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 *
 * @author robert kopp
 */
public interface ICameraManager {

    List<String> getCameraNames();

    List<CamError> getErrors();

    BufferedImage getShotForCamera(String camName);

    BufferedImage takeShot(String w);
//    File takeShot(String w);
    
    void takeShotsPArallel(String shotId) throws InterruptedException;
    public String GetCameraByName(String name);

//    public void takeShot(String camId);
    public void takeShotLowRes(String camId);
    public void sendStatus();
    
}
