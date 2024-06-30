package model.Facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import model.Entity.User;

@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "Persistence_Hse")
    private EntityManager em;

    // Constructor required by AbstractFacade
    public UserFacade() {
        super(User.class); // Pass the entity class to the superclass constructor
    }

    // Optional: You can still keep the constructor with Class<user> parameter if needed
    public UserFacade(Class<User> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean findUser(String username){
        Query qry = em.createQuery("select u from User u where u.username = :username")
                .setParameter("username", username);

        return !qry.getResultList().isEmpty();
    }

    public boolean comparePassword(String username, String pass){
        Query qry = em.createQuery("select u from User u where u.username = :username and u.password = :pass")
                .setParameter("username", username)
                .setParameter("pass", pass);

        return !qry.getResultList().isEmpty();
    }
    public User getUserByUsername(String username) {
        try {
            return em.createQuery("SELECT  u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            // Handle exception if user is not found or any other query issue
            return null;
        }
    }

    public User getUserUnite(Long userId) {
        try {
            return em.createQuery("SELECT  u FROM User u WHERE u.id = :userId", User.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {

            // Handle exception if user is not found or any other query issue
            System.out.println("user not found");
            return null;
        }
    }
}
