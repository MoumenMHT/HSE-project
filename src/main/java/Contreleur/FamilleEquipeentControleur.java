package Contreleur;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;
import model.Entity.Famille_equipement;
import model.Entity.User;
import model.Facade.FamilleFacade;
import model.Facade.UserFacade;

import java.io.Serializable;
import java.util.List;


@Data
@Named(value ="familleEquipeentControleur")
@ViewScoped
public class FamilleEquipeentControleur implements Serializable {

    private Famille_equipement famille_equipement;
    private List<Famille_equipement> familleEquipementList;

    @Inject
    private FamilleFacade familleFacade;

    @Inject
    private UserFacade userFacade;


    @PostConstruct
    public void init() {
        famille_equipement = new Famille_equipement();
        refreshFamille();

    }

    public void submit() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            System.out.println("session not null");

            if (userId != null) {
                // Assuming you have a method in your UserFacade to find a user by ID
                User user = userFacade.find(userId); // Adjust this line to match how you can fetch a User entity by ID
                System.out.println("id passed");

                if (user != null) {
                    famille_equipement.setUser(user); // Set the fetched user to the famille_equipement
                    System.out.println("id found");
                    System.out.println(famille_equipement);
                    familleFacade.create(famille_equipement);
                    familleEquipementList.add(famille_equipement);
                    refreshFamille();

                    System.out.println("famille created");

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "secces", "Famille équipement  ajoutée"));

                } else {
                    // Handle the case where the user is not found
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "User not found."));
                     // Exit the method to prevent saving famille_equipement without a user
                }
            }
        }

            famille_equipement = new Famille_equipement();

        // Optionally, add a message indicating success
    }    public void deletefamille() {

        System.out.println(famille_equipement);
        try {
            familleFacade.remove(famille_equipement);
            refreshFamille();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Famille a était supprimé", ""));

        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de supprimer la famille", e.getMessage()));
        }

       //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Filial Removed", ""));
    }
    public void refreshFamille() {
        familleEquipementList = familleFacade.findAll(); // Method to refresh the list of filials
    }


    public void updateFamille() {
        try {
            if (famille_equipement != null) {
                familleFacade.edit(famille_equipement);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "famille modifié", ""));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Impossible de modifié la famille: " + e.getMessage()));
        }
    }

}
