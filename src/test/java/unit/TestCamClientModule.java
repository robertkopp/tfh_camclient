/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit;

import unit.mocks.HttpSingleImageSenderMock;
import unit.mocks.HttpImageSenderMock;
import unit.mocks.ShotTakerMock;
import unit.mocks.HttpPingSenderMock;
import com.google.inject.AbstractModule;
import dev.robertkopp.autocamclient.ClientConfig;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.camera.ICameraSettingsManager;
import dev.robertkopp.autocamclient.camera.IShotTakerStrategy;
import dev.robertkopp.autocamclient.httpsender.HttpSingleImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import dev.robertkopp.autocamclient.httpsender.IHttpRegistrationSender;
import dev.robertkopp.autocamclient.httpsender.IHttpSingleImageSender;
import dev.robertkopp.autocamclient.nettyserver.CamDataHandler;
import dev.robertkopp.autocamclient.nettyserver.CommandDispatcher;
import dev.robertkopp.autocamclient.nettyserver.ICommandDispatcher;
import dev.robertkopp.autocamclient.nettyserver.MessageDecoder;
import unit.mocks.CameraManagerMock;
import unit.mocks.HttpRegistrationSenderMock;
import unit.mocks.SettingsManagerMock;

/**
 *
 * @author robert kopp
 */
class TestCamClientModule extends AbstractModule {
  
    @Override 
  protected void configure() {

     /*
      * This tells Guice that whenever it sees a dependency on a TransactionLog,
      * it should satisfy the dependency using a DatabaseTransactionLog.
      */
    //bind(TransactionLog.class).to(DatabaseTransactionLog.class);
      
    bind(ClientConfig.class).toInstance(new ClientConfig());
    bind(ICameraManager.class).to(CameraManagerMock.class).asEagerSingleton();
    bind(IShotTakerStrategy.class).to(ShotTakerMock.class).asEagerSingleton();
    bind(IHttpImageSender.class).to(HttpImageSenderMock.class).asEagerSingleton();
    bind(IHttpPingSender.class).to(HttpPingSenderMock.class).asEagerSingleton();
    bind(IHttpSingleImageSender.class).to(HttpSingleImageSenderMock.class);
    bind(ICameraSettingsManager.class).to(SettingsManagerMock.class).asEagerSingleton();
    bind(ICommandDispatcher.class).to(CommandDispatcher.class);
    bind(IHttpRegistrationSender.class).to(HttpRegistrationSenderMock.class);
    bind(CamDataHandler.class);
    bind(MessageDecoder.class);
  }
    
    
}
