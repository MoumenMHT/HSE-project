package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import model.Entity.Filiale;
import model.Entity.Unite;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UniteFacade extends AbstractFacade<Unite> {


    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;


    private Unite uniteToUpdate; // Add a property to hold the entity you want to update

    private static final Logger LOGGER = Logger.getLogger(UniteFacade.class.getName());
    // Constructor required by AbstractFacade
    public UniteFacade() {
        super(Unite.class); // Pass the entity class to the superclass constructor
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Unite> findAllUnite() {
        return super.findAll(); // Utilize the findAll method from AbstractFacade

    }
    public void update(Unite uniteToUpdate) {
        try {
            // Merge the updated entity into the persistence context
            Unite mergedUnite = em.merge(uniteToUpdate);
            // Flush changes to the database
            em.flush();

            // Add a success message
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Unit updated successfully."));
        } catch (Exception e) {
            // Handle any exceptions
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update unit."));
        }
    }






}
