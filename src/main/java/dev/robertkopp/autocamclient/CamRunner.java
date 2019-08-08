/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.robertkopp.autocamclient.debugtools.ShotTester;
import dev.robertkopp.autocamclient.nettyserver.CamServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robert kopp
 */
public class CamRunner {

    static {
        //Webcam.setDriver(new V4l4jDriver()); // this is important
    }
    public static boolean useStreamer = true;

    public static void main(String args[]) throws IOException, Exception {
        boolean runTest = false;
        if (args.length != 0) {
            for (String arg : args) {
                if ("-debug".equals(arg)) {
                    runTest = true;
                }
                if ("-f".equals(arg)) {
                    useStreamer = false;
                }
            }
        }

        if (runTest) {
            runTest();
            return;
        }
        Injector injector = Guice.createInjector(new CamClientModule());
        CamServer listener = injector.getInstance(CamServer.class);
        listener.run();

    }

    private static void runTest() {
        System.out.println("beginne endlos shots zu machen");
        ShotTester t = new ShotTester();
        while (true) {
            try {
                t.takeShotsPArallel("shot" + Math.random());
            } catch (InterruptedException ex) {
                Logger.getLogger(CamRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
