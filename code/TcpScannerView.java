package s04;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TcpScannerView extends Application{

  private static final String ICON  = "/resources/TcpScanner_Icon.png";

  private VBox            root            = new VBox();
  private MenuBar         menu            = new MenuBar();
  private VBox            settings        = new VBox();
  private HBox            settingsL1      = new HBox();
  private HBox            settingsL2      = new HBox();
  private HBox            progressHb      = new HBox();
  private Label           hostLbl         = new Label("Host IP : ");
  private TextField       host            = new TextField("160.98.31.207");
  private Label           portsFromLbl    = new Label("Ports from : ");
  private TextField       portsFrom       = new TextField("1");
  private Label           portsToLbl      = new Label("to");
  private TextField       portsTo         = new TextField("100");
  private Label           hideClosedPLbl  = new Label("Hide closed ports");
  private CheckBox        hideClosedP     = new CheckBox();
  private Button          start           = new Button("start");
  private TableView<Port> table           = new TableView<Port>();
  private Label           progressLbl     = new Label("Progress : ");
  private ProgressBar     progress        = new ProgressBar(0.0);

  private TcpScannerModel        model      = new TcpScannerModel();
  private TcpScannerController   controller = new TcpScannerController(model, this);


  @Override
  public void start(Stage mainStage) throws Exception {

    // Stage
    mainStage.setTitle("TCP Scanner v1.0");
    mainStage.setHeight(600);
    mainStage.setWidth(800);
    mainStage.setResizable(false);
    mainStage.getIcons().add(new Image(ICON));

    // Root
    root.setSpacing(0);
    root.setPadding(new Insets(0, 0, 0, 0));
    root.getChildren().addAll(menu, settings, table, progressHb);


    // Menu --------------------------------------------------------------------

    // Menu File
    Menu menuFile = new Menu("File");
    Menu menuFileExport = new Menu("Export scan");
    MenuItem exportToTxt = new MenuItem("Text");
    MenuItem exportToCsv = new MenuItem("CSV");
    MenuItem exportToXml = new MenuItem("XML");
    menuFile.getItems().addAll(menuFileExport);
    menuFileExport.getItems().addAll(exportToTxt, exportToCsv, exportToXml);

    // Menu File controllers
    exportToTxt.setOnAction(event-> {
      try {
        controller.exportToText("txt");
      } catch (Exception e) {
        displayExportAlert("txt");
      }
    });

    exportToCsv.setOnAction(event-> {
      try {
        controller.exportToText("csv");
      } catch (Exception e) {
        displayExportAlert("csv");
      }
    });

    exportToXml.setOnAction(event-> {
      try {
        controller.exportToXml();
      } catch (Exception e) {
        displayExportAlert("xml");
      }
    });

    // Menu Settings
    Menu menuSettings = new Menu("Settings");
    MenuItem setTimeout = new MenuItem("Configure timeout");
    menuSettings.getItems().addAll(setTimeout);

    // Menu Settings controllers
    setTimeout.setOnAction(event-> {
      controller.setTimeout();
    });

    // Menu About
    Menu menuAbout = new Menu();
    Label lblMenuAbout = new Label("About");
    menuAbout.setGraphic(lblMenuAbout);

    // Menu About controllers
    lblMenuAbout.setOnMouseClicked(event-> {
      Alert dialog= new Alert(AlertType.INFORMATION);
      dialog.setTitle("About");
      dialog.setHeaderText("TCP Scanner Application");
      dialog.setContentText("Version : 1.0\n" + "Date : 2015-06-16\n\n" + "Gilles Waeber & Benoit Repond\n" + "HEIA-FR IHM");
      dialog.showAndWait();
    });

    menu.getMenus().addAll(menuFile, menuSettings, menuAbout);


    // Settings ----------------------------------------------------------------
    settings.getChildren().addAll(settingsL1, settingsL2);
    settings.setPadding(new Insets(10, 10, 10, 10));

    // Row 1
    settingsL1.getChildren().addAll(hostLbl, host, portsFromLbl, portsFrom, portsToLbl, portsTo);
    settingsL1.setAlignment(Pos.BASELINE_LEFT);
    settingsL1.setPadding(new Insets(0, 0, 10, 0));
    host.setPrefWidth(160);
    portsFromLbl.setPadding(new Insets(0, 0, 0, 20));
    portsFrom.setPrefWidth(60);
    portsToLbl.setPadding(new Insets(0, 10, 0, 10));
    portsTo.setPrefWidth(60);

    // Row 2
    settingsL2.getChildren().addAll(hideClosedPLbl, hideClosedP, start);
    settingsL2.setAlignment(Pos.BASELINE_LEFT);
    hideClosedPLbl.setPadding(new Insets(0,  5,  0,  0));
    hideClosedP.setPadding(new Insets(0,  20,  0,  0));
    start.setDefaultButton(true);

    // Hide closed ports controller
    hideClosedP.setOnAction(event-> {
      if(hideClosedP.isSelected()){
        table.setItems(model.getPortOListOpen());
      }else{
        table.setItems(model.getPortOList());
      }
    });

    // Start button controller
    start.setOnAction(event-> {

      // Test scan params
      if(controller.testParams(getHost(), getPortsFrom(), getPortsTo())){

        // Clear and set data source for table
        model.clearPortOList();
        if(hideClosedP.isSelected()){
          table.setItems(model.getPortOListOpen());
        }else{
          table.setItems(model.getPortOList());
        }

        // Scan ports
        controller.scan();

        // Get host informations
        host.setTooltip(null);
        controller.updateHostName();

        // Start button
        disableStartButton(true);

      }else{
        
        // Bad scan params
        Alert dialog= new Alert(AlertType.ERROR);
        dialog.getDialogPane().setPrefSize(400, 260);
        dialog.setTitle("TCP Scanner");
        dialog.setHeaderText("Paramètres incorrectes");
        dialog.setContentText("Un des paramètres suivants suivant est incorect:\n\n"
            + "- Addresse IP (au format 127.0.0.1)\n"
            + "- Port de départ (min. 0)\n"
            + "- Port de fin (max. 65535)\n"
            );
        dialog.showAndWait();

      }

    });


    // Table -------------------------------------------------------------------

    TableColumn serviceCol = new TableColumn("Service");
    serviceCol.setMinWidth(350);
    serviceCol.setCellValueFactory(new PropertyValueFactory<Port, String>("service"));

    TableColumn portCol = new TableColumn("Port");
    portCol.setMinWidth(100);
    portCol.setCellValueFactory(new PropertyValueFactory<Port, Integer>("port"));

    TableColumn stateCol = new TableColumn("State");
    stateCol.setMinWidth(100);
    stateCol.setCellValueFactory(new PropertyValueFactory<Port, Boolean>("state"));

    stateCol.setCellFactory(new Callback<TableColumn, TableCell>() {
      public TableCell call(TableColumn param) {
        return new TableCell<Port, Boolean>() {

          public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {

              if(item){
                this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                setText("TRUE");
              }else{
                this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                setText("FALSE");
              }   

            }else{
              this.setBackground(new Background(new BackgroundFill(null, null, null)));
              setText(null);
            }

          }
        };
      }
    });

    table.setEditable(true);
    table.setItems(model.getPortOList());
    table.getColumns().addAll(serviceCol, portCol, stateCol);
    table.setPadding(new Insets(0, 0, 0, 0));


    // Progress ---------------------------------------------------------------
    progressLbl.setPadding(new Insets(0, 0, 3, 10));
    progress.setPadding(new Insets(10, 10, 10, 10));
    progress.setPrefSize(710, 50);
    progressHb.getChildren().addAll(progressLbl, progress);
    progressHb.setAlignment(Pos.CENTER_LEFT);


    // App launch --------------------------------------------------------------
    mainStage.setScene(new Scene(root));
    mainStage.show();

  }


  // Get "host" from text field
  public String getHost() {
    return host.getText();
  }

  // Get "port from" from text field
  public String getPortsFrom() {
    return portsFrom.getText();
  }

  // Get "port to" from text field
  public String getPortsTo() {
    return portsTo.getText();
  }

  // Update progress bar
  public void updateProgress(double value){
    progress.setProgress(value);
  }

  // Disable or enable start button
  public void disableStartButton(boolean b){
    start.setDisable(b);
  }

  // Display error alert for export function
  private void displayExportAlert(String fileFormat){
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Export scan");
    alert.setHeaderText(null);
    alert.setContentText("Error while exporting scan results to " + fileFormat + " file !");
    alert.showAndWait();
  }

  // Update hostname tooltip
  public void updateHostNameTooltip(String hostNameS){
    if(!getHost().equals(hostNameS)){
      host.setTooltip(new Tooltip("IP address : " + getHost() + "\nHost name : " + hostNameS));
    }else{
      host.setTooltip(new Tooltip("IP address : " + getHost() + "\nHost name : unknown"));
    }
  }

  // Test if "hide closed ports" check box is selected
  public boolean hideClosedPorts(){
    return hideClosedP.isSelected();
  }

}
