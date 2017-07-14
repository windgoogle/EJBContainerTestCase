/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tongweb.ejbs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.SessionContext;

import java.io.Flushable;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

@Stateless(mappedName = "ejb/QuerySessionBean")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class QuerySessionBean implements QuerySessionRemote {
	@Resource
    private SessionContext sessionContext;
   
    public int sum(int add1, int add2) {
    	try {
			Thread.sleep(30*1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
        return add1+add2;
    }
    public int multiply(int mul1, int mul2) {
         return mul1*mul2;
    }
    

    
    public void flush()   throws IOException{
    	((Flushable) sessionContext).flush();
    	System.out.println("=================flush=======================");
    }
	
    
    public void testGC() {
    	System.out.println("=============test gc=====================");
		//System.gc();
		long i = 0;
		Map map=new HashMap();
		boolean exit=false;
		try {
			while (!exit) {
				i++;
				SoftReference<String> sr = new SoftReference<String>("usernamesfsfsfsfsfsfsfs" + i + "zsssssssssssssssssssssssssssss");
				map.put("aa"+i,sr);
			}
		}catch(OutOfMemoryError e) {
			e.printStackTrace();
			map.clear();
			System.gc();
			exit=true;
		}
    }
     
    public void clean(){
    	try {
			Thread.sleep(160000);
			System.out.println("Bean cleaned!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
