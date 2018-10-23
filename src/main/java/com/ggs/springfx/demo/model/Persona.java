package com.ggs.springfx.demo.model;
import javafx.beans.property.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Persona {

//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(nullable = false, columnDefinition = "BIGINT")
//  private Long id;
//  @Column(nullable = false)
//  private String content;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private IntegerProperty clave;
  @Column(nullable = false)
  private StringProperty nombre;
  @Column(nullable = false)
  private IntegerProperty edad;
  @Column
  private ObjectProperty<LocalDate> nacimiento;

  public Persona(Integer clave, String nombre, Integer edad, LocalDate nacimiento) {
    claveProperty().set(clave);
    nombreProperty().set(nombre);
    edadProperty().set(edad);
    nacimientoProperty().set(nacimiento);
  }


  public IntegerProperty claveProperty() {
    if (clave == null) clave = new SimpleIntegerProperty(this, "clave");
    return clave;
  }

  public Integer getClave() {
    return claveProperty().get();
  }

  public void setClave(Integer clave) {
    claveProperty().set(clave);
  }

  public StringProperty nombreProperty() {
    if (nombre == null) nombre = new SimpleStringProperty(this, "nombre");
    return nombre;
  }

  public String getNombre() {
    return nombreProperty().get();
  }

  public void setNombre(String nombre) {
    this.nombreProperty().set(nombre);
  }

  public IntegerProperty edadProperty() {
    if (edad == null) edad = new SimpleIntegerProperty(this, "edad");
    return edad;
  }

  public Integer getEdad() {
    return edadProperty().get();
  }

  public void setEdad(Integer edad) {
    edadProperty().set(edad);
  }

  public ObjectProperty<LocalDate> nacimientoProperty(){
    if(nacimiento==null) nacimiento=new SimpleObjectProperty<LocalDate>(this,"nacimiento");
    return nacimiento;
  }

  public LocalDate getNacimiento(){
    return nacimientoProperty().get();
  }

  public void setNacimiento(LocalDate nacimiento) {
    nacimientoProperty().set(nacimiento);
  }


}

