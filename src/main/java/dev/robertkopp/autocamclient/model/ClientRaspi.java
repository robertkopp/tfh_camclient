/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.model;

import java.util.Set;

/**
 *
 * @author robert kopp
 */
public class ClientRaspi{
    
    
    private String ipAdress;
    private String hostId;
    private Set<Camera> cameras;
    private RaspiState raspiState;

    public ClientRaspi(){};

    public ClientRaspi(String ipAdress, String hostId, Set<Camera> cameras, RaspiState raspiState) {
        this.ipAdress = ipAdress;
        this.hostId = hostId;
        this.cameras = cameras;
        this.raspiState = raspiState;
    }
    
    
 

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Set<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(Set<Camera> cameras) {
        this.cameras = cameras;
    }

    public RaspiState getRaspiState() {
        return raspiState;
    }

    public void setRaspiState(RaspiState raspiState) {
        this.raspiState = raspiState;
    }
    
    
}
