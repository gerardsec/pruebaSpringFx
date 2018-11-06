package com.ggs.springfx.demo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {
  private static final Logger logPersonaService = LoggerFactory.getLogger(PersonaService.class);
  @Autowired
  PersonaRepository personaRepository;

  public List<Persona> findAll() {
    return personaRepository.findAll();
  }

  public Integer agrega(Persona persona) {
    Persona personaSave = personaRepository.save(persona);
    return personaSave != null ? personaSave.getClave() : 0;
  }

  public Integer buscaClaveMayor() {
    final Integer claveM = personaRepository.maxClave();
    return personaRepository.maxClave();
    //return 1;
  }

  public Integer borraPersona(Integer clave) {
    Optional<Persona> encontrado;
    encontrado = personaRepository.findById(clave);
    if(!encontrado.isPresent()){
      return 0;
    }
    Persona personaBorrar = encontrado.get();
    personaRepository.delete(personaBorrar);
    return 1;
  }
  public Integer actualizaPersona(Persona persona){
    if(!personaRepository.existsById(persona.getClave())){
      return 0;
    }
    personaRepository.save(persona);
    return 1;
  }
}
