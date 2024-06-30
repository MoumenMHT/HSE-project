package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.Planification;

@Stateless
public class PlanificationFacade extends AbstractFacade<Planification> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public PlanificationFacade() {
        super(Planification.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<planification> parameter if needed
    public PlanificationFacade(Class<Planification> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
