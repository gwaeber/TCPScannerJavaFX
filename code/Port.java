package s04;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Port {
  
  private SimpleBooleanProperty state;
  private SimpleIntegerProperty port;
  private SimpleStringProperty service;

  public Port(boolean state, int port, String service) {
    this.state = new SimpleBooleanProperty(state);
    this.port = new SimpleIntegerProperty(port);
    this.service = new SimpleStringProperty(service);
  }

  public boolean getState() {
    return state.get();
  }

  public void setState(boolean state) {
    this.state.set(state);
  }

  public int getPort() {
    return port.get();
  }

  public void setPort(int port) {
    this.port.set(port);
  }
  
  public String getService() {
    return service.get();
  }

  public void setService(String service) {
    this.service.set(service);
  }

}