/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.mocks;

import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import java.io.IOException;

/**
 *
 * @author robert kopp
 */
public class HttpPingSenderMock implements IHttpPingSender{

    public int calls=0;
    
    @Override
    public void sendHttpRequest() throws IOException {
        calls++;
    }
    
}
