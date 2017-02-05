package s04.test;

import java.io.IOException;
import java.net.*;

public class SocketTest {

  public static void main(String args[]){

    /*for(int i = 50; i < 60; i++){
      System.out.println("Port " + i + " : " + socketConnect("8.8.8.8", i, 1000));
    }*/

    
    Runnable rA = () -> {
      for(int i = 50; i < 60; i++){
        System.out.println("Port " + i + " : " + socketConnect("8.8.8.8", i, 1000));
      }
    };
    
    Runnable rB = () -> {
      for(int i = 60; i < 70; i++){
        System.out.println("Port " + i + " : " + socketConnect("8.8.8.8", i, 1000));
      }
    };
    
    Thread tA= new Thread(rA); // Création du thread
    Thread tB= new Thread(rB);
    tA.start(); // Lancement du thread : méthode run()
    tB.start();


  }


  private static boolean socketConnect(String address, int port, int timeout){

    try{

      Socket socket = new Socket();
      socket.connect(new InetSocketAddress(address, port), timeout);
      return true;

    }catch(IOException ex){

      return false;

    }

  }

}