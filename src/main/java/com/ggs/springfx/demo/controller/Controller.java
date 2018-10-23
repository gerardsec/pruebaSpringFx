package com.ggs.springfx.demo.controller;

import com.ggs.springfx.demo.model.ArticleRepository;
import com.ggs.springfx.demo.model.Persona;
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

  @Autowired
  private ArticleRepository articleRepository;

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
  @FXML
  private TextField numCasos;

  static public ObservableList<Persona> generaPersonas() {
    List<Persona> personaList = new ArrayList<Persona>();
    Integer clave;
    String nombre;
    Integer edad;
    LocalDate nacimiento;
    clave = 0;
    nombre = "Juan";
    edad = 20;
    nacimiento = LocalDate.now();
    personaList.add(new Persona(clave, nombre, edad, nacimiento));
    clave = 1;
    nombre = "Pedro";
    edad = 21;
    personaList.add(new Persona(clave, nombre, edad, nacimiento));
    clave = 2;
    nombre = "Luis";
    edad = 22;
    personaList.add(new Persona(clave, nombre, edad, nacimiento));
    ObservableList<Persona> personaObservableList0 = FXCollections.observableList(personaList);
    for (Persona persona : personaObservableList0) {
      System.out.println(persona.getClave() + "," + persona.getNombre() + "," + persona.getNacimiento() + ", edad:" + persona.getEdad());
    }
    return personaObservableList0;
  }

  //@Override
  public void initialize() {
    numCasos.setPromptText("Clave a buscar");
    //numCasos solo acepta números
    numCasos.setTextFormatter(new TextFormatter<>(change ->
            (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
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

    col_action.setCellValueFactory(
            new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {
              @Override
              public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
              }
            }
    );
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
  }

  private class ButtonCell extends TableCell<Disposer.Record, Boolean> {
    final Button cellButton = new Button("Borrar");

    ButtonCell() {
      //Action when the button is pressed
      cellButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
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