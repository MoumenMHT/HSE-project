package model.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name="filiale")
public class Filiale implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filiale_id")
    private Integer filiale_id;



    @Column(name = "nom_filiale", insertable = true)
    private String nom_filiale;




}
