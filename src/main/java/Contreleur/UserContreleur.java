package Contreleur;

import jakarta.annotation.PostConstruct;
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
import model.Entity.User;
import model.Facade.FilialeFacade;
import model.Facade.UniteFacade;
import model.Facade.UserFacade;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@Named(value = "userContreleur")
@ViewScoped
public class UserContreleur implements Serializable {

    private User user;
    private List<User> users;

    private Unite unite;
    private List<Unite> allUnites;

    private Filiale selectedFiliale;
    private List<Filiale> allFiliales; // Property to hold all filiales


    @Inject
    private UserFacade userFacade;
    @Inject
    private FilialeFacade filialeFacade;
    @Inject
    private UniteFacade uniteFacade;




    private int message=0;



    @PostConstruct
    public void init(){
        user = new User();
        refreshUnite();
        loadAllFiliales();

    }


    public void creat() {
        try {
            System.out.println(user);

            // Convert LocalDate to Date
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            user.setCree_le(date);

            userFacade.create(user);

            // Notify the user of successful insertion
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "agent est ajouté"));

            // Reset the user object for the next use
            user = new User();
        } catch (Exception e) {
            // Handle potential exceptions here
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "impossible d'ajouter un agent."));
        }
    }

    public void loadAllFiliales() {
        allFiliales = filialeFacade.findAllFilials(); // Assuming you have a method to retrieve all filiales from the database
    }
    public void refreshUnite() {
        allUnites = uniteFacade.findAllUnite(); // Method to refresh the list of filials
    }

}
