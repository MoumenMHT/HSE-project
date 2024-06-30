package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.Filiale;
import model.Entity.Unite;

import java.util.List;

@Stateless
public class FilialeFacade extends AbstractFacade<Filiale> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;
    private Filiale filiale;

    // Constructor required by AbstractFacade
    public FilialeFacade() {
        super(Filiale.class); // Pass the entity class to the superclass constructor
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }



    // Method to fetch all filials from the database
    public List<Filiale> findAllFilials() {
        return super.findAll(); // Utilize the findAll method from AbstractFacade
    }



}
