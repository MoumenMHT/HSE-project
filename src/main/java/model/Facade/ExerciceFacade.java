package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.Exercice;

@Stateless
public class ExerciceFacade extends AbstractFacade<Exercice> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public ExerciceFacade() {
        super(Exercice.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<exercice> parameter if needed
    public ExerciceFacade(Class<Exercice> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
