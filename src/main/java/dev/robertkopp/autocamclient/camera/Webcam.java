/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kopp
 */
public class Webcam {
    ArrayList<String> webcams;
    String name;
    Dimension dimension;
    
    public Webcam(String name) {
        this.name = name;
    }
    
    public BufferedImage getImage(){
     return null;   
    }
    
    public List<String> getWebcams() {
        return webcams;
    }
    public Dimension getViewSize() {
		return dimension;
    }
    public void setViewSize(Dimension dimension) {
        this.dimension = dimension;
    }
}
