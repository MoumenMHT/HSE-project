package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.NatureDactivite;

@Stateless
public class NatureDactiviteFacade extends AbstractFacade<NatureDactivite> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public NatureDactiviteFacade() {
        super(NatureDactivite.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<natureDactivite> parameter if needed
    public NatureDactiviteFacade(Class<NatureDactivite> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
