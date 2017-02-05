package s04;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TcpScannerModel {

  private ObservableList<Port> portOList = FXCollections.observableArrayList();
  private ObservableList<Port> portOListOpen = FXCollections.observableArrayList();
  private int timeout;
  private String hostName;
  
  private static final HashMap<Integer, String> portsList = new HashMap<Integer, String>(){{
    put(0, "Reserved");
    put(1, "TCPMUX");
    put(2, "CompressNET - Management Utility");
    put(3, "CompressNET - Compression Process");
    put(4, "Unassigned");
    put(5, "Remote Job Entry");
    put(6, "Unassigned");
    put(7, "Echo Protocol");
    put(8, "Unassigned");
    put(9, "Discard Protocol");
    put(10, "Unassigned");
    put(11, "Active Users");
    put(12, "Unassigned");
    put(13, "Daytime Protocol");
    put(14, "Unassigned");
    put(15, "Previously netstat service");
    put(16, "Unassigned");
    put(17, "Quote of the Day");
    put(18, "Message Send Protocol");
    put(19, "Character Generator Protocol (CHARGEN)");
    put(20, "FTP data transfer");
    put(21, "FTP");
    put(22, "SSH");
    put(23, "Telnet");
    put(24, "Priv-mail");
    put(25, "SMTP");
    put(26, "Unassigned");
    put(27, "NSW User System FE");
    put(29, "MSG ICP");
    put(33, "MSG ICP");
    put(35, "Any private printer server protocol");
    put(37, "TIME protocol");
    put(39, "Resource Location Protocol[ (RLP)");
    put(40, "Unassigned");
    put(42, "ARPA Host Name Server Protocol");
    put(43, "WHOIS protocol");
    put(47, "NI FTP");
    put(49, "TACACS Login Host protocol");
    put(50, "Remote Mail Checking Protocol");
    put(51, "IMP Logical Address Maintenance");
    put(52, "XNS (Xerox Network Systems) Time Protocol");
    put(53, "DNS (Domain Name System)"); 
    put(54, "XNS (Xerox Network Systems) Clearinghouse");
    put(55, "ISI Graphics Language (ISI-GL)");
    put(56, "XNS (Xerox Network Systems) Authentication / RAP");
    put(57, "any private terminal access");
    put(58, "XNS (Xerox Network Systems) Mail"); 
    put(64, "CI (Travelport) Comms Integrator");
    put(67, "Bootstrap Protocol (BOOTP) Server / DHCP");
    put(68, "Bootstrap Protocol (BOOTP) Client / DHCP");
    put(69, "TFTP (Trivial File Transfer Protocol)");
    put(70, "Gopher protocol");
    put(71, "NETRJS protocol");
    put(72, "NETRJS protocol");
    put(73, "NETRJS protocol");
    put(74, "NETRJS protocol");
    put(77, "Any private Remote Job Entry");
    put(79, "Finger protocol");
    put(80, "HTTP (Hypertext Transfer Protocol)");
    put(81, "Torpark—Onion routing");
    put(82, "Torpark—Control");
    put(88, "Kerberos—authentication system");
    put(90, "dnsix (DoD Network Security for Information Exchange) Security Attribute Token Map");
    put(99, "WIP Message protocol");
    put(100, "CyberGate RAT protocol");
    put(110, "POP3 (Post Office Protocol v3)");
    put(119, "Network News Transfer Protocol (NNTP)");
    put(123, "NTP (Network Time Protocol)");
    put(137, "NetBIOS Name Service");
    put(138, "NetBIOS Datagram Service");
    put(139, "NetBIOS Session Service");
    put(143, "IMAP (Internet Message Access Protocol)");
    put(161, "SNMP (Simple Network Management Protocol)");
    put(162, "SNMPTRAP (Simple Network Management Protocol Trap)");
    put(194, "IRC (Internet Relay Chat)");
    put(443, "HTTPS (Hypertext Transfer Protocol over TLS/SSL)");
    put(445, "Microsoft-DS Active Directory");
    put(465, "SMTPS ( Simple Mail Transfer Protocol over TLS/SSL)");
    put(587, "STMP (e-mail message submission)");
    put(993, "IMAPS (Internet Message Access Protocol over TLS/SSL)");
    put(994, "IRCS (Internet Relay Chat over TLS/SSL)");
    put(995, "POP3S (Post Office Protocol 3 over TLS/SSL)");
  }};

  public TcpScannerModel() {
    this.timeout = 100;
  }

  // Return ObservableList of Ports
  public ObservableList<Port> getPortOList(){
    return portOList;
  }
  
  // Return ObservableList of opened Ports
  public ObservableList<Port> getPortOListOpen(){
    return portOListOpen;
  }

  // Add a Port in the ObservableList
  public void addPort(Port p){
    portOList.add(p);
    if(p.getState()) portOListOpen.add(p);
  }
  
  // Get service name for a TCP port
  public String getServiceByPort(int firstPort) {
    return portsList.get(firstPort);
  }
  
  // clear ObserveableLists
  public void clearPortOList(){
    portOList.clear();
    portOListOpen.clear();
  }
  
  // Get timeout for the scan
  public int getTimeout(){
    return timeout;
  }
  
  // Set timeout for the scan
  public void setTimeout(int timeout){
    this.timeout = timeout;
  }

  
}
