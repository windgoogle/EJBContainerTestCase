/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tongweb.ejbs;

import javax.ejb.Remote;
import javax.ejb.Remove;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
@Remote
public interface QuerySessionRemote {
    public int sum(int add1, int add2);
    public int multiply(int mul1, int mul2);
    public void flush() throws IOException;
    public void testGC();
    
    @Remove
    public void clean();
 //   public void DelBean();
}
