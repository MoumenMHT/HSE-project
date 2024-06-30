package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import model.Entity.Equipement;
import model.Entity.Famille_equipement;
import model.Entity.Unite;

import java.util.Date;
import java.util.List;

@Stateless
public class EquipementFacade extends AbstractFacade<Equipement> {
    private List<Equipement> equipements;

    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public EquipementFacade() {
        super(Equipement.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<equipement> parameter if needed
    public EquipementFacade(Class<Equipement> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Equipement> findequipmentByFamily(Famille_equipement famille) {
        TypedQuery<Equipement> query = em.createQuery("SELECT e FROM Equipement e WHERE e.famille_equipement = :famille", Equipement.class);
        query.setParameter("famille", famille);
        return query.getResultList();
    }

    public List<Equipement> findFamilyPeriode(Famille_equipement familleEquipement) {

        Query qry= em.createQuery("SELECT e  FROM Famille_equipement e WHERE e.id_familleEquipement = :famille", Equipement.class);
        qry.setParameter("famille", familleEquipement);
        return qry.getResultList();
    }

    public List<Equipement> findEquipmentByUnite(Unite unite) {

        Query qry= em.createQuery("select e from Equipement e where e.unite= :unite");
        qry.setParameter("unite", unite);
        return qry.getResultList();
    }

    public Long countEquipmentControled() {
        return em.createQuery("SELECT COUNT(e) FROM Equipement e WHERE e.statut = true", Long.class)
                .getSingleResult();
    }

    public List<Equipement> getEquipmentPassedControlDate() {
        TypedQuery<Equipement> query = em.createQuery(
                "SELECT e FROM Equipement e WHERE e.dateProchainControle < CURRENT_DATE", Equipement.class);
        return query.getResultList();
    }


    public Long countEquipment() {
        return em.createQuery("SELECT COUNT(e) FROM Equipement e ", Long.class)
                .getSingleResult();
    }

    public Long countEquipmentControledFilterByDate(Date dateDebut, Date datefin) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Equipement e WHERE e.dateProchainControle < :dateFin and e.dateProchainControle > :dateDebut ", Long.class);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", datefin);
        return query.getSingleResult();
    }
    public Long countEquipmentControled(Date datefin) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Equipement e WHERE e.dateProchainControle > :datefin", Long.class);
        query.setParameter("datefin", datefin);
        return query.getSingleResult();
    }


    public Long countEquipmentControledFilterByUnite(Unite unite, Date currentDate) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Equipement e WHERE e.dateProchainControle > :datefin AND e.unite = :unite", Long.class);
        query.setParameter("datefin", currentDate);
        query.setParameter("unite", unite);

        return query.getSingleResult();
    }

    public Long countEquipmentByUnite(Unite unite) {
        Query qry = em.createQuery("SELECT COUNT(e) FROM Equipement e WHERE e.unite = :unite", Long.class);
        qry.setParameter("unite", unite);

        return (Long) qry.getSingleResult();
    }

    public Long countEquipmentControledFilterByFamilly(Famille_equipement famille) {
        Query qry = em.createQuery("SELECT COUNT(e) FROM Equipement e WHERE e.famille_equipement = :famille AND e.statut= true" , Long.class);
        qry.setParameter("famille", famille);

        return (Long) qry.getSingleResult();
    }
    public Long countEquipmentByfamilly(Famille_equipement famille) {
        Query qry = em.createQuery("SELECT COUNT(e) FROM Equipement e WHERE e.famille_equipement = :famille", Long.class);
        qry.setParameter("famille", famille);

        return (Long) qry.getSingleResult();
    }

    public List<Equipement> getEquipmentControled() {
        TypedQuery<Equipement> query = em.createQuery(
                "SELECT e FROM Equipement e WHERE e.dateProchainControle > CURRENT_DATE", Equipement.class);
        return query.getResultList();
    }

    public Long countEquipmentControledFilterByFamillyAndDate(Famille_equipement famille, Date dateDebut, Date datefin ) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Equipement e WHERE e.dateProchainControle < :datefin and e.dateProchainControle > :dateDebut and e.famille_equipement = :famille", Long.class);
            query.setParameter("datefin", datefin);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("famille", famille);

            return query.getSingleResult();

    }
}
