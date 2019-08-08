/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.mocks;

import com.github.sarxos.webcam.Webcam;
import dev.robertkopp.autocamclient.camera.ICameraSettingsManager;
import java.util.HashMap;

/**
 *
 * @author robert kopp
 */
public class SettingsManagerMock implements ICameraSettingsManager {

    public Webcam w;
    public HashMap<String, Object> values;
    public int calls = 0;

    @Override
    public void changeSettings(Webcam w, HashMap<String, Object> values) {
        this.w = w;
        this.values = values;

        calls++;
    }

}
