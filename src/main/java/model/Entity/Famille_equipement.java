package model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;




@Getter
@Setter
@Entity
@Table(name = "famille_equipement")
public class Famille_equipement implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_familleEquipement")
    private Integer id_familleEquipement;

    @Column(name = "nom_famille")
    private String nom_famille;

    @Column(name = "peropdicite")
    private String peropdicite;


    @ManyToOne
    @JoinColumn(name = "id")
    private User user;


}
