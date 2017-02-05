package s04.test;

public class PingCmd {

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
