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
public class LinuxSettingsManager implements ICameraSettingsManager{

    @Override
    public void changeSettings(Webcam w, HashMap<String, Object> values) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //v4l2-ctl -d /dev/video1 -c exposure_auto=1 -c exposure_auto_priority=0 -c exposure_absolute=10
//http://www.techytalk.info/webcam-settings-control-ubuntu-fedora-linux-operating-system-cli/
    

}
