package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Entity.TypeUnite;

@Stateless
public class TypeUniteFacade extends AbstractFacade<TypeUnite> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public TypeUniteFacade() {
        super(TypeUnite.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<TypeUnite> parameter if needed
    public TypeUniteFacade(Class<TypeUnite> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
