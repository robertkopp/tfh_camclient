/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.mocks;

import com.github.sarxos.webcam.Webcam;
import dev.robertkopp.autocamclient.camera.IShotTakerStrategy;
import java.awt.image.BufferedImage;

/**
 *
 * @author robert kopp
 */
public class ShotTakerMock implements IShotTakerStrategy{

    
    public int shotsTaken=0;
    
    @Override
    public BufferedImage takeShot(Webcam w) {
        shotsTaken++;
        return new BufferedImage(100, 100, 1);
    }
    
}
