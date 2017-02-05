package s04.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PingCmd2 {

  public static void main(String[] args) {

    System.out.println(isReachableByPing("www.google.com"));

  }

  public static boolean isReachableByPing(String host) {
    
    try{
      String cmd = "";
      if(System.getProperty("os.name").startsWith("Windows")) {   
        // For Windows
        cmd = "ping -n 1 " + host;
      } else {
        // For Linux and OSX
        cmd = "ping -c 1 " + host;
      }

      Process myProcess = Runtime.getRuntime().exec(cmd);
      myProcess.waitFor();
      
      BufferedReader reader=new BufferedReader(
          new InputStreamReader(myProcess.getInputStream())
      ); 
      String line; 
      while((line = reader.readLine()) != null) 
      { 
          System.out.println(line);
      }

      if(myProcess.exitValue() == 0) {

        return true;
      } else {

        return false;
      }

    } catch( Exception e ) {

      e.printStackTrace();
      return false;
    }
  }





}