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
import model.Facade.FilialeFacade;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Named(value = "FilialeControleur")
@Data
@ViewScoped
public class FilialeControleur implements Serializable {

    private Filiale filiale;
    private List<Filiale> listFilial;

    @Inject
    private FilialeFacade filialeFacade;

    private int message = 0;

    @PostConstruct
    public void init(){

        filiale=new Filiale();


        refreshFilials(); // Fetch the list of filials from the database on initialization
    }

    public void submit(){
        try {
            System.out.println(filiale);
            filialeFacade.create(filiale);
            addMessage(FacesMessage.SEVERITY_INFO, "Success ", "Filial created successfully ");

            listFilial.add(filiale);
            filiale=new Filiale();
        }catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Success ", "impossible de ajouté une filiale "+e.getMessage());
        }


    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }


    public void refreshFilials() {
        listFilial = filialeFacade.findAllFilials(); // Method to refresh the list of filials
    }

    public void deletefilial() {
    try {
        System.out.println(filiale);
        filialeFacade.remove(filiale);
        refreshFilials();

        addMessage(FacesMessage.SEVERITY_INFO, "Success ", "Filial est supprimé ");
    }catch (Exception e) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Success ", "impossible de supprier la filiale "+e.getMessage());
    }

    }

    public void updateFilial() {
        try {
                filialeFacade.edit(filiale);
                addMessage(FacesMessage.SEVERITY_INFO, "Success ", "Filial est modifié ");

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erreur ", "impossible de modifier la filial "+e.getMessage());
        }
    }
}
