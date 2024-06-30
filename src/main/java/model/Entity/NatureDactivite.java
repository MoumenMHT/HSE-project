package model.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "natureDactivite")
public class NatureDactivite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_natureDactivite;


    @Column(name = "libelle", insertable = true)
    private String libelle;

    public String getType() {
        return libelle;
    }

    public void setType(String type) {
        this.libelle = type;
    }
    @Column(name= "code", insertable = true)
    private String code;


    @ManyToOne
    @JoinColumn(name = "filiale_id")
    private Filiale filiale;
}
