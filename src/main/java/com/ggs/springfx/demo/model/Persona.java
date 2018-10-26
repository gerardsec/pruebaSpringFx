package com.ggs.springfx.demo.model;

import javafx.beans.property.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "persona", schema = "public")
public class Persona {
  private IntegerProperty clave;
  private StringProperty nombre;
  private IntegerProperty edad;
  private ObjectProperty<LocalDate> nacimiento;

  public Persona(Integer clave, String nombre, Integer edad, LocalDate nacimiento) {
    claveProperty().set(clave);
    nombreProperty().set(nombre);
    edadProperty().set(edad);
    nacimientoProperty().set(nacimiento);
  }

  public Persona(){};

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)

  public Integer getClave() {
    return claveProperty().get();
  }

  public void setClave(Integer clave) {
    claveProperty().set(clave);
  }

  public IntegerProperty claveProperty() {
    if (clave == null) clave = new SimpleIntegerProperty(this, "clave");
    return clave;
  }


  @Column(nullable = false)
  public String getNombre() {
    return nombreProperty().get();
  }

  public void setNombre(String nombre) {
    this.nombreProperty().set(nombre);
  }


  public StringProperty nombreProperty() {
    if (nombre == null) nombre = new SimpleStringProperty(this, "nombre");
    return nombre;
  }

  @Column(nullable = false)
  public Integer getEdad() {
    return edadProperty().get();
  }

  public void setEdad(Integer edad) {
    edadProperty().set(edad);
  }

  public IntegerProperty edadProperty() {
    if (edad == null) edad = new SimpleIntegerProperty(this, "edad");
    return edad;
  }


  @Column
  public LocalDate getNacimiento() {
    return nacimientoProperty().get();
  }

  public void setNacimiento(LocalDate nacimiento) {
    nacimientoProperty().set(nacimiento);
  }

  public ObjectProperty<LocalDate> nacimientoProperty() {
    if (nacimiento == null) nacimiento = new SimpleObjectProperty<LocalDate>(this, "nacimiento");
    return nacimiento;
  }
}

