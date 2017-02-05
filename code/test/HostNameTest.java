package s04.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameTest {

  public static void main(String[] args) throws UnknownHostException {
    
    
    //InetAddress addr = InetAddress.getByName("160.98.31.207");
    InetAddress addr = InetAddress.getByName("8.8.8.8");
    String host = addr.getHostName();
    System.out.println(host);

  }

}
