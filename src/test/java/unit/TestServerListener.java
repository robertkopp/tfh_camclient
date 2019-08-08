/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.camera.ICameraSettingsManager;
import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import dev.robertkopp.autocamclient.nettyserver.CamServer;
import dev.robertkopp.autocamclient.nettyserver.Message;
import dev.robertkopp.autocamclient.nettyserver.Commands;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import unit.mocks.CameraManagerMock;
import unit.mocks.HttpPingSenderMock;
import unit.mocks.SettingsManagerMock;
import unit.netty.NettyClient;

/**
 *
 * @author robert kopp
 */
public class TestServerListener {

    public TestServerListener() {
    }

    private static Injector injector;

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    injector = Guice.createInjector(new TestCamClientModule());
                    CamServer listener = injector.getInstance(CamServer.class);
                    listener.run();

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
        t.setDaemon(true);
        t.start();
        while (!checkServerUp()) {
            Thread.sleep(100);
        }
    }

    private static boolean checkServerUp() {
        try {
            NettyClient.send(null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void takeSingleShot() throws Exception {
    Message m = new Message(Commands.TakeSingleShot, "#cam01");
        NettyClient.send(m);
        CameraManagerMock managerMock=(CameraManagerMock) injector.getInstance(ICameraManager.class);
        Assert.assertEquals(1, managerMock.takeShotCalls);
        Assert.assertEquals("#cam01", managerMock.takeShotCamId);
    }
    
    @Test
    public void takeShot() throws Exception {
        Message m = new Message(Commands.TakeShot, "#shotId");
        NettyClient.send(m);
        CameraManagerMock managerMock=(CameraManagerMock) injector.getInstance(ICameraManager.class);
        Assert.assertEquals(1, managerMock.takeShotsParallelCalls);
        Assert.assertEquals("#shotId", managerMock.ShotId);
    }

    @Test
    public void sendPing() throws Exception {
        Message m = new Message(Commands.Ping, null);
        NettyClient.send(m);
        HttpPingSenderMock mock = (HttpPingSenderMock) injector.getInstance(IHttpPingSender.class);
        Assert.assertEquals(1, mock.calls);
    }

    @Test
    public void sendConfig() throws Exception {
        Message m = new Message(Commands.SendConfig, "{\"camname\":\"testscam\",\"someval\":2,\"anotherval\":2.1}");
        NettyClient.send(m);
        SettingsManagerMock mock = (SettingsManagerMock) injector.getInstance(ICameraSettingsManager.class);
        CameraManagerMock managerMock=(CameraManagerMock) injector.getInstance(ICameraManager.class);
        Assert.assertEquals(1, mock.calls);
        Assert.assertEquals("testscam",managerMock.camByNameCall);
        Assert.assertEquals(2, (int) mock.values.get("someval"));
        Assert.assertEquals(2.1, (double) mock.values.get("anotherval"), 0.1);
    }
}
