/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.github.sarxos.webcam.Webcam;
import java.util.HashMap;

/**
 *
 * @author robert kopp
 */
public interface ICameraSettingsManager {
    
    
    public void changeSettings(Webcam w, HashMap<String, Object> values);
    
}
