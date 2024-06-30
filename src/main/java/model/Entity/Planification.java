package model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "planification")
public class Planification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_planification;




    @Column(name = "dateDebut", insertable = true)
    private Date DateDebut;

    @Column(name = "dateFin", insertable = true)
    private Date DateFin;

    @Column(name = "Titre", insertable = true)
    private String Titre;






    @ManyToOne
    @JoinColumn(name = "id_equipement")
    private Equipement equipement;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;
}
