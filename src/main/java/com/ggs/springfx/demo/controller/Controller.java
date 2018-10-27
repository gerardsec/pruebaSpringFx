package com.ggs.springfx.demo.controller;

import com.ggs.springfx.demo.model.ArticleRepository;
import com.ggs.springfx.demo.model.Persona;
import com.ggs.springfx.demo.model.PersonaService;
import com.sun.prism.impl.Disposer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class Controller {

  public String numCasosVar;
  @FXML
  public TableColumn<Persona, Integer> claveTColumn;
  @FXML
  public TableColumn<Persona, String> nombreTColumn;
  @FXML
  public TableColumn<Persona, Integer> edadTColumn;
  @FXML
  public TableColumn<Persona, LocalDate> nacimientoTColumn;
  public ObservableList<Persona> personaObservableList;
  @FXML
  TableView<Persona> personaTableView = new TableView<>();
  @Autowired
  private ArticleRepository articleRepository;
  @FXML
  private TextField numCasos;

  @Autowired
  private PersonaService personaService;

  public ObservableList<Persona> generaPersonas() {
    //ejercicio para recuperar datos de la base de datos
    List<Persona> personaList = personaService.findAll();
    ObservableList<Persona> personaObservableList0 = FXCollections.observableList(personaList);
    for (Persona persona : personaObservableList0) {
      System.out.println(persona.getClave() + "," + persona.getNombre() + "," + persona.getNacimiento()
              + ", edad:" + persona.getEdad());
    }
    System.out.println("***resultado BD");
    for (Persona persona : personaList) {
      System.out.println("clave:" + persona.getClave());
    }
    return personaObservableList0;
  }

  //@Override
  public void initialize() {
    numCasos.setPromptText("Clave a buscar");
    //numCasos solo acepta números
    numCasos.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
    personaObservableList = generaPersonas();
    personaTableView.setEditable(true);
    personaTableView.setItems(personaObservableList);
    //personaTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    claveTColumn.setCellValueFactory(new PropertyValueFactory<>("clave"));
    nombreTColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    //crea un campo text para hacer editable la celda
    nombreTColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    edadTColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
    IntegerStringConverter converterInt = new IntegerStringConverter();
    edadTColumn.setCellFactory(TextFieldTableCell.<Persona, Integer>forTableColumn(converterInt));
    edadTColumn.setOnEditCommit(dataInt -> {
      dataInt.getRowValue().setEdad(dataInt.getNewValue());
    });
    //Uso de converter para editar fechas
    LocalDateStringConverter converter = new LocalDateStringConverter();
    nacimientoTColumn.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
    nacimientoTColumn.setCellFactory(TextFieldTableCell.<Persona, LocalDate>forTableColumn(converter));
    nacimientoTColumn.setOnEditCommit(data -> {
      data.getRowValue().setNacimiento(data.getNewValue());
    });
    //Agrega una columna con un botón
    TableColumn col_action = new TableColumn("Action");
    personaTableView.getColumns().add(col_action);
    col_action.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {
      @Override
      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {
        return new SimpleBooleanProperty(param.getValue() != null);
      }
    });
    //Agrega el botón a la celda
    col_action.setCellFactory(new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
      @Override
      public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {
        return new ButtonCell();
      }
    });
    personaTableView.setItems(personaObservableList);
  }

  @FXML
  public void recibirNumero(ActionEvent actionEvent) {
    //generaPersonas();
    numCasosVar = "ok";
    String cadena = numCasos.getText();
    Integer largo = numCasos.getLength();
    System.out.println(cadena + " largo:" + largo);
    Integer intNumCasos = Integer.parseInt(cadena);
    System.out.println("recibe número:" + intNumCasos);
    numCasos.textProperty().setValue(numCasosVar);
    for (Persona persona : personaObservableList) {
      System.out.println(persona.getClave() + "," + persona.getNombre() + "," + persona.getNacimiento() + ", edad:" + persona.getEdad());
    }

  }

  public void clickOnTable(MouseEvent mouseEvent) {
    System.out.println("Clic");
  }

  public void editNombre(TableColumn.CellEditEvent<Persona, String> personaStringCellEditEvent) {
    System.out.println("Se modificó nombreTColumn:" + personaStringCellEditEvent.getOldValue() + " a:" + personaStringCellEditEvent.getNewValue());
    //personaStringCellEditEvent.getRowValue()
    Persona p = personaStringCellEditEvent.getRowValue();
    p.setNombre(personaStringCellEditEvent.getNewValue());
  }

  public void agregaRegistro(ActionEvent actionEvent) {
    System.out.println("Agrega Registro");
    Integer claveMayor = personaService.buscaClaveMayor();
    //TEMPORAL
    Integer clave;
    String nombre;
    Integer edad;
    LocalDate nacimiento;
    clave = 15;
    nombre = "Juan";
    edad = 20;
    nacimiento = LocalDate.now();
    Persona personaNueva = new Persona(clave, nombre, edad, nacimiento);
    personaService.agrega(personaNueva);
    System.out.println("Persona nueva:" + personaNueva.getClave());
  }

  public ButtonType mensaje(String tipo, String title, String header, String content) {
    Alert.AlertType alertType;
    switch (tipo) {
      case "C":
        alertType = Alert.AlertType.CONFIRMATION;
        break;
      case "E":
        alertType = Alert.AlertType.ERROR;
        break;
      case "I":
        alertType = Alert.AlertType.INFORMATION;
        break;
      case "W":
        alertType = Alert.AlertType.WARNING;
        break;
      default:
        alertType = Alert.AlertType.INFORMATION;
    }
    Alert alert = new Alert(alertType);
    alert.setAlertType(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
    ButtonType buttonType = alert.getResult();
    return buttonType;
  }

  private class ButtonCell extends TableCell<Disposer.Record, Boolean> {
    final Button cellButton = new Button("Borrar");

    ButtonCell() {
      //Action when the button is pressed
      cellButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
          //Diálogo de confirmación para borrar un registro de la tabla
          ButtonType buttonType = mensaje("C", "Título", "Se borrará el registro de forma definitiva", "¿Está seguro?");
          if (buttonType != ButtonType.OK) return;
          // get Selected Item
          Persona currentPerson = (Persona) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
          //remove selected item from the table list
          personaObservableList.remove(currentPerson);
          personaTableView.refresh();
        }
      });
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
      super.updateItem(t, empty);
      if (!empty) {
        setGraphic(cellButton);
      }
    }
  }
}