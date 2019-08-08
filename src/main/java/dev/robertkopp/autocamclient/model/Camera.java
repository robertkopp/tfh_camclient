/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.model;


/**
 *
 * @author robert kopp
 */
public class Camera {

    
    private String cameraId;
    
    
    private String settingsJson;

    public Camera() {
    }

    public Camera(String cameraId) {
        this.cameraId = cameraId;
    }

   
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

}
