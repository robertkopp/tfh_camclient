/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient;

import com.google.inject.AbstractModule;
import dev.robertkopp.autocamclient.camera.CameraManagerCommandLine;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.camera.ICameraSettingsManager;
import dev.robertkopp.autocamclient.camera.IShotTakerStrategy;
import dev.robertkopp.autocamclient.camera.ShotTakerStrategy;
import dev.robertkopp.autocamclient.camera.WindowsSettingsManager;
import dev.robertkopp.autocamclient.httpsender.HttpImageSender;
import dev.robertkopp.autocamclient.httpsender.HttpPingSender;
import dev.robertkopp.autocamclient.httpsender.HttpRegistrationSender;
import dev.robertkopp.autocamclient.httpsender.HttpSingleImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import dev.robertkopp.autocamclient.httpsender.IHttpRegistrationSender;
import dev.robertkopp.autocamclient.httpsender.IHttpSingleImageSender;
import dev.robertkopp.autocamclient.nettyserver.CommandDispatcher;
import dev.robertkopp.autocamclient.nettyserver.ICommandDispatcher;

/**
 *
 * @author robert kopp
 */
public class CamClientModule extends AbstractModule {
  
    @Override 
  protected void configure() {

     /*
      * This tells Guice that whenever it sees a dependency on a TransactionLog,
      * it should satisfy the dependency using a DatabaseTransactionLog.
      */
    //bind(TransactionLog.class).to(DatabaseTransactionLog.class);
      
    bind(ClientConfig.class).toInstance(new ClientConfig());
    //bind(ICameraManager.class).to(CameraManager.class);
    bind(ICameraManager.class).to(CameraManagerCommandLine.class);
    bind(IShotTakerStrategy.class).to(ShotTakerStrategy.class);
    bind(IHttpImageSender.class).to(HttpImageSender.class);
    bind(IHttpPingSender.class).to(HttpPingSender.class);
    bind(ICameraSettingsManager.class).to(WindowsSettingsManager.class);
    bind(ICommandDispatcher.class).to(CommandDispatcher.class);
    bind(IHttpRegistrationSender.class).to(HttpRegistrationSender.class);
    bind(IHttpSingleImageSender.class).to(HttpSingleImageSender.class);
  }
    
    
}
