package Contreleur;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import model.Entity.Exercice;
import model.Entity.Unite;
import model.Facade.ExerciceFacade;
import model.Facade.UniteFacade;

import java.io.Serializable;
import java.util.List;

@Data
@Named(value = "exerciceControleur")
@ViewScoped
public class ExerciceControleur  implements Serializable {


    private Exercice exercice;
    private List<Exercice> exerciceList;


    private List<Unite> uniteList;


    @Inject
    private ExerciceFacade exerciceFacade;
    @Inject
    private UniteFacade uniteFacade;


    @PostConstruct
    public void init(){
        exercice = new Exercice();
        refrechExercice();
        LoadAllUnites();
    }


    public void refrechExercice() {
        exerciceList = exerciceFacade.findAll();
    }

    public void LoadAllUnites(){
        uniteList = uniteFacade.findAllUnite();
    }
    public void submit() {

        try {
            exerciceFacade.create(exercice);
            refrechExercice();
            System.out.println("exercice created successfully!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("exercice est créee "));

        } catch (Exception e) {
            System.out.println("eche exercice: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "echec de la creation  d'exercice: " + e.getMessage()));
        }
    }
    public void deleteunite() {
        try{
            exerciceFacade.remove(exercice);
            refrechExercice();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "exercice est supprimé", ""));
        }catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "echec de la suppression  d'exercice: " + e.getMessage()));
        }

    }

    public void updateExercice() {
        try {
            if (exercice != null) {
                exerciceFacade.edit(exercice);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "exercice est modifié", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "veuillez sélectionner un exercice."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "échec de la modification d'exercice: " + e.getMessage()));
        }
    }
}
