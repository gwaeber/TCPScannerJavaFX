package s04;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpScannerHostThread extends Thread {

  private TcpScannerView view;

  public TcpScannerHostThread(TcpScannerView view) {
    this.view = view;
  }

  // Run "get hostname"
  public void run() {

    InetAddress addr = null;
    String host = view.getHost();
    String hostName;
    
    try {
      addr = InetAddress.getByName(host);
    } catch (UnknownHostException e) {
      addr = null;
    }
    
    if(addr != null){
      hostName = addr.getHostName();
    }else{
      hostName = "";
    }
    
    view.updateHostNameTooltip(hostName);

  }

}
