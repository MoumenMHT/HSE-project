package Contreleur;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import model.Entity.Filiale;
import model.Entity.Unite;
import model.Facade.FilialeFacade;
import model.Facade.UniteFacade;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Data
@Named(value = "uniteContreleur")
@ViewScoped
public class UniteContreleur implements Serializable {



    private Unite unites;
    private List<Unite> listunite;
    private List<Unite> listParentUnite;


    private List<Filiale> allFiliales; // Property to hold all filiales


    @Inject
    private FilialeFacade filialeFacade;
    @Inject
    private UniteFacade uniteFacade;

    private int message = 0;

    @PostConstruct
    public void init() {
        unites = new Unite();

        unites.setFiliale(new Filiale());
        refreshUnite();
        loadAllFiliales(); // Load all filiales when initializing the controller

    }

    public void createUnite() {

        try {
                uniteFacade.create(unites);
                refreshUnite();
                System.out.println("Unit est ajouté!");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unit est ajoutée"));

        } catch (Exception e) {
            System.out.println("Impossbile de ajouter une unit: " + e.getMessage());
        }
    }





    public void refreshUnite() {
        listunite = uniteFacade.findAllUnite(); // Method to refresh the list of unites
    }


    public void loadAllFiliales() {
        allFiliales = filialeFacade.findAllFilials(); // Assuming you have a method to retrieve all filiales from the database
    }

    public void deleteunite() {

        try{
            System.out.println(unites);
            uniteFacade.remove(unites);
            refreshUnite();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Unit est supprimé", ""));

        }catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "impossible de supprimer l'unite: " + e.getMessage()));
        }

    }
    public void updateUnite() {
        try {
            if (unites != null) {
                uniteFacade.edit(unites);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit est ajouté", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "veuillez sélectionner une unité."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Impossible de modifier l'unit: " + e.getMessage()));
        }
    }



}
