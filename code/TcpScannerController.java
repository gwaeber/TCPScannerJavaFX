package s04;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class TcpScannerController {

  private TcpScannerModel model;
  private TcpScannerView view;

  private Pattern pattern;
  private Matcher matcher;
  private static final String IPADDRESS_PATTERN = 
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

  public TcpScannerController(TcpScannerModel model, TcpScannerView view) {
    this.model = model;
    this.view = view;
    pattern = Pattern.compile(IPADDRESS_PATTERN);
  }

  
  // Test IP address and ports
  public boolean testParams(String ip, String firstPort, String lastPort){

    // Test IP address
    matcher = pattern.matcher(ip);
    if(!matcher.matches()) return false;            

    // Test ports
    if(!isInteger(view.getPortsFrom())) return false;
    if(!isInteger(view.getPortsTo())) return false;
    int firstPortI = Integer.parseInt(view.getPortsFrom());
    int lastPortI = Integer.parseInt(view.getPortsTo());
    if(firstPortI < 0 || lastPortI < 0 || lastPortI > 65535 || firstPortI > lastPortI) return false;

    return true;

  }

  
  // Get scan params and lauch the scan
  public void scan(){

    int firstPort = Integer.parseInt(view.getPortsFrom());
    int lastPort = Integer.parseInt(view.getPortsTo());
    int timeout = model.getTimeout();
    String ip = view.getHost();

    TcpScannerScanThread thread = new TcpScannerScanThread(firstPort, lastPort, timeout, ip, model, view);
    thread.start();

  }

  
  // Configure timeout
  public void setTimeout() {

    TextInputDialog dialog = new TextInputDialog(Integer.toString(model.getTimeout()));
    dialog.setTitle("Settings");
    dialog.setHeaderText("Configure timeout (in ms)");
    dialog.setGraphic(null);
    Optional<String> input = dialog.showAndWait();

    if(input.isPresent()){
      if(isInteger(input.get())){
        int timeout = Integer.valueOf(input.get());
        if(timeout > 0){
          model.setTimeout(timeout);
        }else{
          displayTimeoutAlert();
        }
      }else{
        displayTimeoutAlert();
      }
    }

  }

  // Display alert for wrong timeout 
  private void displayTimeoutAlert(){
    
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Settings");
    alert.setHeaderText(null);
    alert.setContentText("Wrong timeout !");
    alert.showAndWait();
    
  }

  
  // Test if param is an integer
  private static boolean isInteger(String s) {
    try { 
      Integer.parseInt(s); 
    }catch(NumberFormatException e) { 
      return false; 
    }catch(NullPointerException e) {
      return false;
    }
    return true;
  }


  // Export scan results to txt or csv
  public void exportToText(String format) throws IOException{

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date date = new Date();
    String fileName = "TcpScanner_export_ " + dateFormat.format(date) + "." + format;
    PrintWriter writer = new PrintWriter(fileName, "UTF-8");
    if(format == "csv"){
      writer.println("port;state;service");
    }

    ObservableList<Port> portOList;
    if(view.hideClosedPorts()){
      portOList = model.getPortOListOpen();
    }else{
      portOList = model.getPortOList();
    }

    for (Port port : portOList){
      
      String service = "";
      if(port.getService() != null) service = port.getService();
      String s = String.valueOf(port.getPort()) + ";" + port.getState() + ";" + service;
      writer.println(s);
    }
    
    writer.close();

    String fileLocation = new File(fileName).getAbsolutePath();

    Alert dialog= new Alert(AlertType.INFORMATION);
    dialog.setTitle("Export");
    dialog.setHeaderText("Scan result export");
    dialog.setContentText("Scan results exported in " + fileLocation);
    dialog.showAndWait();

  }


  // Export scan result to xml
  public void exportToXml() throws Exception{

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement("tcpscan");
    doc.appendChild(rootElement);

    ObservableList<Port> portOList;
    if(view.hideClosedPorts()){
      portOList = model.getPortOListOpen();
    }else{
      portOList = model.getPortOList();
    }

    for (Port port : portOList){

      Element staff = doc.createElement("port");
      rootElement.appendChild(staff);
      staff.setAttribute("id", String.valueOf(port.getPort()));

      Element portState = doc.createElement("state");
      portState.appendChild(doc.createTextNode(String.valueOf(port.getState())));
      staff.appendChild(portState);

      Element portService = doc.createElement("service");
      if(port.getService() == null){
        portService.appendChild(doc.createTextNode(""));
      }else{
        portService.appendChild(doc.createTextNode(port.getService()));
      }

      staff.appendChild(portService);

    }

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date date = new Date();
    String fileName = "TcpScanner_export_ " + dateFormat.format(date) + ".xml";

    StreamResult result = new StreamResult(new File(fileName));
    transformer.transform(source, result);

    String fileLocation = new File(fileName).getAbsolutePath();

    Alert dialog= new Alert(AlertType.INFORMATION);
    dialog.setTitle("Export");
    dialog.setHeaderText("Scan result export");
    dialog.setContentText("Scan results exported in " + fileLocation);
    dialog.showAndWait();
    
  }


  // Get and update hostname from ip
  public void updateHostName(){
    
    TcpScannerHostThread thread = new TcpScannerHostThread(view);
    thread.start();
    
  }
  

}
