/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.mocks;

import com.github.sarxos.webcam.Webcam;
import dev.robertkopp.autocamclient.camera.CamError;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author robert kopp
 */
public class CameraManagerMock implements ICameraManager {
    public String camByNameCall;
    public int takeShotCalls=0;
    public String takeShotCamId;
    public int takeShotsParallelCalls=0;
    public String ShotId;

    @Override
    public List<String> getCameraNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CamError> getErrors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BufferedImage getShotForCamera(String camName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public BufferedImage takeShot(Webcam w) {
        takeShotCalls++;
        
        return null;
    }

    @Override
    public void takeShotsPArallel(String shotId) throws InterruptedException {
       takeShotsParallelCalls++;
       ShotId=shotId;
    }

    @Override
    public String GetCameraByName(String name) {
        camByNameCall=name;
        return null;
    }

    @Override
    public BufferedImage takeShot(String camId) {
        takeShotCalls++;
        takeShotCamId=camId;
        return null;
    }

    @Override
    public void sendStatus() {
        
    }

    @Override
    public void takeShotLowRes(String camId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
