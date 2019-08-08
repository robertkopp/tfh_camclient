/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;

/**
 *
 * @author robert kopp
 */
class WebCamDiscoverListener implements WebcamDiscoveryListener {
    private final CameraManagerCommandLine cameraManager;       // <--- hier geaendert

    public WebCamDiscoverListener(CameraManagerCommandLine cameraManager) {     // <--- hier geaendert
        this.cameraManager = cameraManager;
    }

    @Override
    public void webcamFound(WebcamDiscoveryEvent wde) {
      cameraManager.addCam(
        wde.getWebcam().toString() //.toString() hinzugefuegt um fehler zu entfernen
      );
    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent wde) {
        cameraManager.removeCam(
                wde.getWebcam().toString() //.toString() hinzugefuegt um fehler zu entfernen
        );
    }
    
}
