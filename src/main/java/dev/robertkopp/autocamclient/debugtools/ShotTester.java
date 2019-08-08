/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.debugtools;

import dev.robertkopp.autocamclient.CamRunner;
import dev.robertkopp.autocamclient.camera.CamError;
import dev.robertkopp.autocamclient.camera.CameraManagerCommandLine;
import dev.robertkopp.autocamclient.camera.IShotTakerStrategy;
import dev.robertkopp.autocamclient.httpsender.IHttpImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpRegistrationSender;
import dev.robertkopp.autocamclient.httpsender.IHttpSingleImageSender;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.CodeSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author robert kopp
 */
public class ShotTester {
    
        ArrayList<String> webcams;
    private LinkedList<CamError> errors;

    
    private ExecutorService executor;
    private String params;
    private Dimension dimension;
    private String hostname;
    private final Dimension dimensionLOW = new Dimension(640,320);
 
    
     public void takeShotsPArallel(String shotId) throws InterruptedException {
        System.out.println("mache shots..");
         updateConfig();
         DateFormat df = new SimpleDateFormat("yyyyMMddHHmmSS");
        Date now = Calendar.getInstance().getTime();        
        String time = df.format(now);
        /*try {
            Process del = Runtime.getRuntime().exec("rm /home/pi/Desktop/img*");
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        ArrayList<BufferedImage> tasks = new ArrayList<BufferedImage>(); 
        for (String cam : webcams) {
            //tasks.add(takeShot(cam));
            try {
                System.out.println("command: "+ "streamer -f jpeg -c /dev/" + cam + " -s " + dimension.width + "x" + dimension.height + " -o /home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg");
                Process p = Runtime.getRuntime().exec("streamer -f jpeg -c /dev/" + cam + " -s " + dimension.width + "x" + dimension.height + " -o /home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg");
                //p.waitFor(10, TimeUnit.SECONDS); eventuell mit timeout?
                p.waitFor(); //ohne timeout, könnte gefährlich sein
                int val= p.exitValue();
                System.out.println("process exit code:"+val);
                
                
                
            } catch (Exception ex) {
                Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Thread.sleep(3000);
        }
        Thread.sleep(500);
        for (String cam : webcams) {
            try {
                System.out.println("Versuch Bild zu lesen: /home/pi/Desktop/img-"+hostname+"-"+cam+"-"+time+".jpeg");
                File f= new File("/home/pi/Desktop/img-"+hostname+"-"+cam+"-"+time+".jpeg");
                System.out.println("Datei gibt es:"+ f.exists());
                if (f.exists()){
                f.delete();}
            } catch (Exception ex) {
                Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(2000);
        }
        }
    
     private void updateConfig()
    {
        webcams = new ArrayList<>();
        params = "-c brightness=0 -c contrast=32 -c saturation=64 -c hue=0 -c gamma=100 -c gain=0 -c power_line_frequency=1 -c white_balance_temperature_auto=0 -c sharpness=3 -c backlight_compensation=0 -c exposure_auto=3 -c exposure_auto_priority=0";
        dimension = new Dimension(3264,2448);
        hostname = "raspi";
        
        try {
            String[] devs = new File("/dev/").list();
            Process p;
            for (int i = 0; i < devs.length; i++) {
                if (devs[i].contains("video")){
                webcams.add(devs[i]);
                p = Runtime.getRuntime().exec("/bin/bash \n",null);
                BufferedWriter InputP2 = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                InputP2.write("v4l2-ctl -d /dev/" + devs[i] + " " + params +"\n");
                InputP2.close();
                }
            }
            p = Runtime.getRuntime().exec("rm /home/pi/Desktop/cams");
        } catch (Exception e) {
            System.out.println("konnte die Anzahl der Cams nicht feststellen");
        }
    }    
    
     }

    

