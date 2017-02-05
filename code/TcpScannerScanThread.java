package s04;

import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpScannerScanThread extends Thread {

  private int firstPort;
  private int lastPort;
  private int timeOut;
  private String ip;
  private TcpScannerModel model;
  private TcpScannerView view;

  public TcpScannerScanThread(int firstPort, int lastPort, int timeOut, String ip, TcpScannerModel model, TcpScannerView view) {
    this.firstPort = firstPort;
    this.lastPort = lastPort;
    this.timeOut = timeOut;
    this.ip = ip;
    this.model = model;
    this.view = view;
  }

  // Run a TCP scan
  public void run() {

    int first = firstPort;
    while(firstPort <= lastPort){

      String service = model.getServiceByPort(firstPort);
      boolean state = portIsOpen(ip, firstPort, timeOut);
      model.addPort(new Port(state, firstPort, service));
      
      double progress = (double)(firstPort-first) / (double)(lastPort-first);
      view.updateProgress(progress);

      firstPort++;

    }

    view.disableStartButton(false);

  }

  // Test if port is open
  public static boolean portIsOpen(String ip, int port, int timeout) {
    
    try {
      
      Socket socket = new Socket();
      socket.connect(new InetSocketAddress(ip, port), timeout);
      socket.close();
      return true;
      
    } catch (Exception e) {
      
      return false;
      
    }
    
  }

}
