/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient;

import com.google.inject.Singleton;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robert kopp
 */
@Singleton
public class ClientConfig {

    public String THIS_CLIENTS_HOSTNAME;
    public  String SERVER_IP = "";
    public  String SERVER_PORT = "8080";
    public  int THIS_CLIENTS_PORT = 9999;
    public  String THIS_CLIENTS_IP = "";
    public String THIS_CLIENTS_IP_PREFIX="192.168.178.";

    public ClientConfig(){
        try {
            findOutMyIp();
            //THIS_CLIENTS_IP="127.0.0.1";
            //THIS_CLIENTS_HOSTNAME="testpi";
        } catch (SocketException ex) {
            Logger.getLogger(ClientConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            CodeSource codeSource = CamRunner.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            Properties props = new Properties();
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(jarFile.getParentFile().getPath() + "/config"));
            props.load(ins);
            ins.close();
            this.SERVER_IP = props.getProperty("serverip");
            this.THIS_CLIENTS_HOSTNAME = props.getProperty("hostname");
        } catch (Exception ex) {
            System.out.println("konnte die config-Datei nicht lesen");
        }
    }


    public void findOutMyIp() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                String ip = i.getHostAddress();
                if (ip.startsWith(this.THIS_CLIENTS_IP_PREFIX)) {
                    this.THIS_CLIENTS_IP = ip;
                    this.THIS_CLIENTS_HOSTNAME=i.getHostName();
                    Logger.getLogger(ClientConfig.class.getName()).log(Level.INFO, "Found IP for Client:"+ip);
                    Logger.getLogger(ClientConfig.class.getName()).log(Level.INFO, "Found Hostname for Client:"+this.THIS_CLIENTS_HOSTNAME);
                    return;
                }

            }
        }
    }
}
