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

public void agrega(Persona persona) {
    personaRepository.save(persona);
}

  public Integer buscaClaveMayor() {
    //final Optional<Integer> claveM = personaRepository.maxClave();
    //personaRepository.maxClave();
    return 1;
  }
}
