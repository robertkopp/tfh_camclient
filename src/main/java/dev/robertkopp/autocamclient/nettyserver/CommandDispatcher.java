/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.nettyserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.robertkopp.autocamclient.ClientConfig;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.camera.ICameraSettingsManager;
import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robert kopp
 */
@Singleton
public class CommandDispatcher implements ICommandDispatcher{

    
    
    private final ClientConfig config;
    private final ICameraManager manager;
    private final IHttpPingSender httpPingSender;
    private final ICameraSettingsManager camSettingsManager;

    @Inject
    public CommandDispatcher(ClientConfig config, ICameraManager manager, IHttpPingSender httpPingSender, ICameraSettingsManager camSettingsManager) {
        this.config = config;
        this.manager = manager;
        this.httpPingSender = httpPingSender;
        this.camSettingsManager = camSettingsManager;
    }
    
    @Override
    public void dispatch(Message m) {
        byte cmd = m.cmd;
            if (cmd == Commands.Ping) {
                handlePing();
            }
            if (cmd == Commands.TakeShot) {
                   System.out.println("command für photosatz empfangen");
                handleTakeShot(m.payload);
            }
            if (cmd == Commands.TakeSingleShot) {
                System.out.println("command single shot empfangen");
                handleTakeSingleShot(m.payload);
            }
            if (cmd == Commands.SendConfig) {
                
                handleConfig(m.payload);
            }
    }
    
      private void handlePing() {
        try {
            httpPingSender.sendHttpRequest();
        } catch (IOException ex) {
            Logger.getLogger(CommandDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleTakeShot(String shotid) {
        try {
            manager.takeShotsPArallel(shotid);
        } catch (InterruptedException ex) {
            Logger.getLogger(CommandDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleTakeSingleShot(String camId) {
        manager.takeShotLowRes(camId);
    }

    private void handleConfig(String payload) {
        try {
            HashMap<String,Object> result =
                    new ObjectMapper().readValue(payload, HashMap.class);
            
            camSettingsManager.changeSettings(
                    Webcam.getDefault(),
                    //manager.GetCameraByName((String)result.get("camname")),
                    result);
        } catch (IOException ex) {
            Logger.getLogger(CommandDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
