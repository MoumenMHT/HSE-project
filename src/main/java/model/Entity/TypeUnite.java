package model.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@Entity
@Table
public class TypeUnite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_TypeUnite;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;
}
