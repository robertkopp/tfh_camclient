/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.camera;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.robertkopp.autocamclient.CamRunner;
import dev.robertkopp.autocamclient.httpsender.IHttpImageSender;
import dev.robertkopp.autocamclient.httpsender.IHttpRegistrationSender;
import dev.robertkopp.autocamclient.httpsender.IHttpSingleImageSender;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.CodeSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author kopp
 */
@Singleton
public class CameraManagerCommandLine implements ICameraManager {

    ArrayList<String> webcams;
    private LinkedList<CamError> errors;

    private ExecutorService executor;
    private String params;
    private Dimension dimension;
    private String hostname;
    private final Dimension dimensionLOW = new Dimension(640, 320);
    private final IHttpImageSender httpImageSender;
    private final IShotTakerStrategy shotTaker;
    private final IHttpSingleImageSender singleSender;
    private final IHttpRegistrationSender httpStatusSender;

    @Inject
    public CameraManagerCommandLine(IHttpRegistrationSender httpStatusSender, IHttpImageSender httpImageSender, IShotTakerStrategy shotTaker, IHttpSingleImageSender singleSender) {
        this.httpStatusSender = httpStatusSender;
        this.httpImageSender = httpImageSender;
        this.shotTaker = shotTaker;
        initialize();
        this.singleSender = singleSender;
        int c = 0;
        updateConfig();
    }

    private void initialize() {
        r.setSeed(System.currentTimeMillis());
    }

    @Override
    public List<String> getCameraNames() {
        List<String> result = new LinkedList<String>();
        for (String w : webcams) {
            result.add(w);
        }
        return result;
    }

    @Override
    public List<CamError> getErrors() {
        return errors;
    }

    @Override
    public BufferedImage getShotForCamera(String camName) {
        for (String w : webcams) {
            if (w.equals(camName)) {
                return takeShot(w);
            }
        }
        return null;
    }

    private void updateConfig() {
        webcams = new ArrayList<>();
        params = "-c brightness=0 -c contrast=32 -c saturation=64 -c hue=0 -c gamma=100 -c gain=0 -c power_line_frequency=1 -c white_balance_temperature_auto=0 -c sharpness=3 -c backlight_compensation=0 -c exposure_auto=3 -c exposure_auto_priority=0";
        dimension = new Dimension(3264, 2448);
        hostname = "raspi";
        try {
            CodeSource codeSource = CamRunner.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            Properties props = new Properties();
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(jarFile.getParentFile().getPath() + "/config"));
            props.load(ins);
            ins.close();
            params = props.getProperty("parameter");
            String dim = (String) props.get("resolution");
            dimension = new Dimension(Integer.parseInt(dim.split("x")[0]), Integer.parseInt(dim.split("x")[1]));
            hostname = props.getProperty("hostname");
        } catch (Exception ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String[] devs = new File("/dev/").list();
            Process p;
            for (int i = 0; i < devs.length; i++) {
                if (devs[i].contains("video")) {
                    webcams.add(devs[i]);
                    p = Runtime.getRuntime().exec("/bin/bash \n", null);
                    BufferedWriter InputP2 = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                    InputP2.write("v4l2-ctl -d /dev/" + devs[i] + " " + params + "\n");
                    InputP2.close();
                }
            }
            p = Runtime.getRuntime().exec("rm /home/pi/Desktop/cams");
        } catch (Exception e) {
            System.out.println("konnte die Anzahl der Cams nicht feststellen");
        }
    }

    @Override
    public BufferedImage takeShot(String cam) {
        BufferedImage bi = null;
        try {
            System.out.println("streamer -f jpeg -c /dev/" + cam + " -s " + dimension.width + "x" + dimension.height + " -o /home/pi/Desktop/img-" + hostname + "-" + cam + ".jpeg");
            Process p = Runtime.getRuntime().exec("streamer -f jpeg -c /dev/" + cam + " -s " + dimension.width + "x" + dimension.height + " -o /home/pi/Desktop/img-" + hostname + "-" + cam + ".jpeg");
            System.out.println("/home/pi/Desktop/img-" + hostname + "-" + cam + ".jpeg");
            //bi = ImageIO.read(new File( "/home/pi/Desktop/img-" + hostname + "-" + cam + ".jpeg"));
            bi = ImageIO.read(getClass().getResourceAsStream("img-" + hostname + "-" + cam + ".jpeg"));
            System.out.println("1");
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bi;
    }

    Random r = new Random();

    @Override
    public void takeShotsPArallel(String shotId) throws InterruptedException {
        updateConfig();
        killAlteProzesse();
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
            boolean success = false;
            while (!success) {
                if (CamRunner.useStreamer) {
                    success = startStreamer(cam, time);
                } else {
                    success = startFsWebCam(cam, time);
                }
            }
        }
        //Thread.sleep(500+r.nextInt(1000));
        for (String cam : webcams) {
            try {
                System.out.println("Versuch Bild zu lesen: /home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg");
                tasks.add(ImageIO.read(new File("/home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg")));
            } catch (IOException ex) {
                Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(2000);
        }
        killAlteProzesse();
        for (BufferedImage img : tasks) {
            Random r = new Random();
            String i = "i" + r.nextInt();
            try {
                httpImageSender.sendHttpRequest(img, shotId, System.currentTimeMillis(), i);
            } catch (IOException ex) {
                Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(1000);
        }
    }

    private boolean startStreamer(String cam, String time) {
        //tasks.add(takeShot(cam));
        try {
            Process p = Runtime.getRuntime().exec("streamer -f jpeg -c /dev/" + cam + " -s " + dimension.width + "x" + dimension.height + " -o /home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg");
            p.waitFor(4, TimeUnit.SECONDS);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //fswebcam docs
    //https://books.google.de/books?id=oejWCQAAQBAJ&pg=PA235&lpg=PA235&dq=fswebcam+bmp&source=bl&ots=qmhbLe_WM-&sig=-GWMsKrqZUadXZi9KrQl4123AaM&hl=de&sa=X&ved=0ahUKEwjvxdPkmtbJAhVDSBQKHYTtAToQ6AEIQjAD#v=onepage&q=fswebcam%20bmp&f=true
    //http://manpages.ubuntu.com/manpages/lucid/man1/fswebcam.1.html
    private boolean startFsWebCam(String cam, String time) {
        //tasks.add(takeShot(cam));
        try {
            Process p = Runtime.getRuntime().exec("fswebcam -q -p RGB24 -d /dev/" + cam + " -r " + dimension.width + "x" + dimension.height + " /home/pi/Desktop/img-" + hostname + "-" + cam + "-" + time + ".jpeg");
            p.waitFor(4, TimeUnit.SECONDS);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    void addCam(String webcam) {
        webcams.add(webcam);
        executor = Executors.newFixedThreadPool(webcams.size());
        sendStatus();
    }

    void removeCam(String webcam) {
        webcams.remove(webcam);
        executor = Executors.newFixedThreadPool(Math.max(1, webcams.size()));
        sendStatus();
    }

    @Override
    public void sendStatus() {
        try {
            httpStatusSender.sendHttpRequest();
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String GetCameraByName(String name) {
        for (String w : webcams) {
            if (w.equals(name)) {
                return w;
            }
        }
        return null;
    }

    /*    @Override
     public void takeShot(String camId) {
     Webcam w= GetCameraByName(camId);
     try {
     singleSender.sendHttpRequest(takeShot(w), camId, System.currentTimeMillis());
     } catch (IOException ex) {
     Logger.getLogger(CameraManager.class.getName()).log(Level.SEVERE, null, ex);
     }
        
     }*/
    private BufferedImage takeShotLowResShot(String w) {
        BufferedImage bi = null;
        try {
            Process p = Runtime.getRuntime().exec("streamer -f jpeg -c /dev/" + w + " " + dimensionLOW.width + "x" + dimensionLOW.height + " -o /home/pi/Desktop/img-" + hostname + "-" + w + ".jpeg");
            bi = ImageIO.read(new File("/home/pi/Desktop/img-" + hostname + "-" + w + "-" + System.currentTimeMillis() + ".jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bi;
    }

    @Override
    public void takeShotLowRes(String camId) {
        System.out.println("takeing single shot for cam:'" + camId + "'");
        String w = GetCameraByName(camId);
        try {
            singleSender.sendHttpRequest(takeShotLowResShot(w), camId, System.currentTimeMillis());
        } catch (IOException ex) {
            System.out.println("shot fail");
            ex.printStackTrace();
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void killAlteProzesse() {
        try {
            Process p = Runtime.getRuntime().exec("ps aux | grep streamer | grep video | cut -d \" \" -f 9 >> /home/pi/Desktop/checkFile");
            File prozesse = new File("/home/pi/Desktop/checkFile");
            if (prozesse.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(prozesse));
                String zeile = null;
                do {
                    zeile = br.readLine();
                    if (!zeile.equals(null)) {
                        System.out.println("Kille Prozess: " + zeile);
                        p = Runtime.getRuntime().exec("sudo kill -9 " + zeile);
                    }
                } while (zeile != null);
                br.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Process p = Runtime.getRuntime().exec("rm -f /home/pi/Desktop/checkFile");
        } catch (IOException ex) {
            Logger.getLogger(CameraManagerCommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
