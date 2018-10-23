package com.ggs.springfx.demo.model;


import javax.persistence.*;

@Entity
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, columnDefinition = "BIGINT")
  private Long id;
  @Column(nullable = false)
  private String content;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


}
