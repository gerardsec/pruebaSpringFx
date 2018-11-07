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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.*;

@Component
public class Controller {

  //Log de esta clase
  private static final Logger logController = LoggerFactory.getLogger(Controller.class);
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
  //Validación de datos
  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  Validator validator = factory.getValidator();
  @Autowired
  private ArticleRepository articleRepository;
  @FXML
  private TextField numCasos;
  //Para insertar un registro
  @FXML
  private TextField nombreNuevo;
  @FXML
  private Spinner<Integer> edadNuevo;
  @FXML
  private DatePicker nacimientoNuevo;
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
//spinner para seleccionar edad;
    SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 150, 18);
    edadNuevo.setValueFactory(valueFactory);
    edadNuevo.setEditable(true);
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
    //Agrega una columna con un botón para borrar
    TableColumn col_delete = new TableColumn("Borrar");
    personaTableView.getColumns().add(col_delete);
    col_delete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {
      @Override
      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {
        return new SimpleBooleanProperty(param.getValue() != null);
      }
    });
    //Agrega el botón a la celda
    col_delete.setCellFactory(new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
      @Override
      public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {
        return new ButtonCellBorrar();
      }
    });
    //Agrega una columna con un botón para editar
    TableColumn col_edit = new TableColumn("Editar");
    personaTableView.getColumns().add(col_edit);
    col_edit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>
            , ObservableValue<Boolean>>() {
      @Override
      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {
        return new SimpleBooleanProperty(param.getValue() != null);
      }
    });
    //Agrega el botón editar a la celda
    col_edit.setCellFactory(new Callback<TableColumn<Disposer.Record, Boolean>,
            TableCell<Disposer.Record, Boolean>>() {
      @Override
      public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {
        return new ButtonCellEditar();
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
    String nombreOld = personaStringCellEditEvent.getOldValue();
    Persona p = personaStringCellEditEvent.getRowValue();
    p.setNombre(personaStringCellEditEvent.getNewValue());
    Integer actualiza = personaService.actualizaPersona(p);
    if (actualiza == 0) {
      mensaje("E", "Actualiza nombre", "Ocurrió un error al actualizar la base de datos",
              "");
      p.setNombre(nombreOld);
      personaTableView.refresh();
      return;
    }
    personaTableView.refresh();
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
    nombre = nombreNuevo.getText();
    edad = edadNuevo.getValue();
    Optional<LocalDate> checaNacimiento = Optional.ofNullable(nacimientoNuevo.getValue());
    nacimiento = (checaNacimiento.isPresent() ? nacimientoNuevo.getValue() : null);
    Persona personaNueva = new Persona(clave, nombre, edad, nacimiento);
    System.out.println("Nuevo registro:" + personaNueva.toString());
    //validación mediante validator
    Set<ConstraintViolation<Persona>> violations = validator.validate(personaNueva);
    String cadenaViolation = "";
    for (ConstraintViolation<Persona> violation : violations) {
      //logController.error(violation.getMessage());
      cadenaViolation += "\n" + violation.getMessage() + "; ";
    }
    System.out.println(violations.isEmpty());
    if (violations.isEmpty()) {
      Integer personaAgregada = personaService.agrega(personaNueva);
      if (personaAgregada != 0) {
        personaNueva.setClave(personaAgregada);
        personaObservableList.add(personaNueva);
        personaTableView.refresh();
        logController.info("Se agregó un nuevo registro:" + personaNueva.toString());
        mensaje("I", "Alta", "Se agregó correctamente el registro", personaNueva.toString());
      } else {
        mensaje("E", "Error al guardar", "Error al guardar en la base de datos",
                personaNueva.toString());
      }
    } else {
      mensaje("E", "Errores en los datos", "Corrija los datos de entrada",
              cadenaViolation);
    }
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

  //Clase para el botón borrar en una celda de la tableview
  private class ButtonCellBorrar extends TableCell<Disposer.Record, Boolean> {
    final Button cellButton = new Button("Borrar");

    ButtonCellBorrar() {
      //Action when the button is pressed
      cellButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
          //Diálogo de confirmación para borrar un registro de la tabla
          ButtonType buttonType = mensaje("C", "Título", "Se borrará el registro de forma definitiva", "¿Está seguro?");
          if (buttonType != ButtonType.OK) return;
          // get Selected Item
          Persona currentPerson = (Persona) ButtonCellBorrar.this.getTableView().getItems().get(ButtonCellBorrar.this.getIndex());
          //remove selected item from the table list
          Integer borrado = personaService.borraPersona(currentPerson.getClave());
          if (borrado == 0) {
            logController.error("No se pudo borrar el registro:" + currentPerson.toString());
            mensaje("E", "Error", "No se pudo borrar el registro en la base de datos",
                    currentPerson.toString());
            return;
          }
          logController.info("Se borró el registro:" + currentPerson.toString());
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

  //Clase para el botón editar en una celda de la tableview
  private class ButtonCellEditar extends TableCell<Disposer.Record, Boolean> {
    final Button cellButton = new Button("Editar");

    ButtonCellEditar() {
      //Action when the button is pressed
      cellButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
          // get Selected Item
          Persona currentPerson =
                  (Persona) ButtonCellEditar.this.getTableView().getItems().get(ButtonCellEditar.this.getIndex());
          ButtonType buttonType = mensaje("I", "Editar", "Registro a editar",
                  currentPerson.toString());
          if (buttonType != ButtonType.OK) return;
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