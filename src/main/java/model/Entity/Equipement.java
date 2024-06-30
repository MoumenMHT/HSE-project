package model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.*;

@Setter
@Getter
@Entity
@EqualsAndHashCode(of = "id", doNotUseGetters = true)
@ToString(of = "id", doNotUseGetters = true)
@Table(name = "equipement")
public class Equipement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipement")
    private Integer id_equipement;


    @Column(name = "nom_equipement", insertable = true)
    private String nom_equipement;

    @Column(name = "codeBare", insertable = true)
    private String codeBare;

    @Column(name = "statu", insertable = true)
    private Boolean statut;

    @Column(name = "dateMisEnService", insertable = true)
    private Date dateMisEnServive;

    @Column(name = "dateDernierControle", insertable = true)
    private Date dateDernierControle;

    @Column(name = "dateProchainControle", insertable = true)
    private Date dateProchainControle;


    @ManyToOne
    @JoinColumn(name = "id_familleEquipement")
    private Famille_equipement famille_equipement;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;

}
