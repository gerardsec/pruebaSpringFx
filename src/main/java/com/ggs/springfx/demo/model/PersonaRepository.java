package com.ggs.springfx.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
  @Query("SELECT coalesce(max(ch.clave), 0) FROM Persona ch")
  Integer maxClave();
}
  //select coalesce(max(CLAVE),0) from PERSONA
