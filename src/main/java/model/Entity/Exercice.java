package model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "exercice")
public class Exercice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_exercice;


    @Column(name = "titre",insertable = true)
    private String titre;

    @Column(name="NBexercice", insertable = true)
    private Integer NBexercice;

    @Column(name = "responsable", insertable = true)
    private String responsable;

    @Column(name = "dateDebut", insertable = true)
    private Date dateDebut;

    @Column(name = "dateFin", insertable = true)
    private Date dateFin;

    @Column(name = "statu", insertable = true)
    private String statu;

    @Column(name = "adress", insertable = true)
    private String adress;

    @Column(name = "num_tel", insertable = true)
    private Float Num_tel;

    @Column(name = "scenario", insertable = true)
    private String Scenario;

    @Column(name = "objective", insertable = true)
    private String Objective;

    @Column(name = "participationProtectionCevile", insertable = true)
    private Boolean ParticipationProtectionCevile;

    @Column(name = "modaliteDorganisation", insertable = true)
    private String ModaliteDorganisation;

    @Column(name = "populationCibles", insertable = true)
    private String PopulationCibles;

    @Column(name = "nbDagentCibles", insertable = true)
    private int NbDagentCibles;

    @Column(name = "nbDagentTouche", insertable = true)
    private int NbDagentTouche;

    @Column(name = "lieuDeRassemblement", insertable = true)
    private String LieuDeRassemblement;

    @Column(name = "tempDevacuation", insertable = true)
    private int TempDevacuation;

    @Column(name = "moyenUtilise", insertable = true)
    private String moyenUtilise;



    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;


}
