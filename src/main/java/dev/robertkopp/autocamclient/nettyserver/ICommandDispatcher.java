/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.nettyserver;

/**
 *
 * @author robert kopp
 */
public interface ICommandDispatcher {
    
    public void dispatch(Message m);
}
