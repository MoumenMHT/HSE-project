package model.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.primefaces.shaded.json.JSONPropertyIgnore;


import java.io.Serializable;
import java.util.List;
import java.util.*;

@Data
@EqualsAndHashCode(of = "id", doNotUseGetters = true)
@ToString(of = "id", doNotUseGetters = true)
@Entity
@Table(name = "unite")
public class Unite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unite_id")
    private Long unite_id;

    @Column(name = "nom_unite", insertable = true)
    private String nom_unite;


    @Override
    public String toString() {
        return "Unite{" +
                "unite_id=" + unite_id +
                ", nom_unite='" + nom_unite + '\'' +


                // Add other fields as needed
                '}';
    }

    // Self-referencing relationship
    @ManyToOne
    @JoinColumn(name = "parent_unite_id") // column to store the parent entity id
    private Unite parentUnite;

    @OneToMany(mappedBy = "parentUnite")
    private List<Unite> childUnites;



    @ManyToOne()
    @JoinColumn(name = "filiale_id")
    private Filiale filiale;








}
