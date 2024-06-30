package model.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "utilisateur")

public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "username", unique = true)
    private String username;

       @Column(name = "password", insertable = true)
    private String password;
    @Column(name = "acitve", insertable = true)
    private boolean acitve;

     @Column(name = "adress", insertable = true)
    private String adress;


    @Column(name = "cree_le", insertable = true)
    private Date cree_le;


    @Column(name = "email", insertable = true)
    private String email;


    @Column(name = "nom", insertable = true)
    private String name;



    @Column(name = "prenom", insertable = true)
    private String prenom;


    @Column(name = "type", insertable = true)
    private String type;

    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + acitve +
                ", address='" + adress + '\'' +
                ", createdAt=" + cree_le +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + prenom + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


    @ManyToOne
    @JoinColumn(name = "filiale_id") // Adjust the name of the join column according to your database schema
    private Filiale filiale;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;


    @OneToMany(mappedBy = "user")
    private List<Famille_equipement> familleEquipements;


}
