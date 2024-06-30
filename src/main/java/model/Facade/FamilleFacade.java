package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.Famille_equipement;
import model.Entity.Filiale;

import java.util.List;

@Stateless
public class FamilleFacade extends AbstractFacade<Famille_equipement> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public FamilleFacade() {
        super(Famille_equipement.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<famille_equipement> parameter if needed
    public FamilleFacade(Class<Famille_equipement> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
