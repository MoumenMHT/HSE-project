package Contreleur;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import model.Entity.User;
import model.Facade.UserFacade;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

@Named(value = "LoginControleur")
@ViewScoped
@Data
public class LoginControleur implements Serializable {

    private User user1 ;


    @Inject
    private UserFacade userFacade;


    private int erreurMEssage= 0;

    @PostConstruct
    public void init(){
        user1=new User();




    }


    @Inject
    private HttpServletRequest request; // Inject HttpServletRequest

    public void Login() {
        try {
                if (userFacade.comparePassword(user1.getUsername(), user1.getPassword())) {
                    User loggedInUser = userFacade.getUserByUsername(user1.getUsername());
                    System.out.println(loggedInUser);
                    // Set user ID in session
                    request.getSession().setAttribute("userId", loggedInUser.getId());
                    System.out.println(loggedInUser.getType());
                    if(Objects.equals(loggedInUser.getType(), "admin")){
                        System.out.println("user found Admin");


                        // Perform redirect
                        FacesContext.getCurrentInstance().getExternalContext().redirect("Admin/famille_equipement.xhtml");
                    } else if (Objects.equals(loggedInUser.getType(), "filial")) {

                        System.out.println("user found Filial");

                        // Perform redirect
                        FacesContext.getCurrentInstance().getExternalContext().redirect("Filial/equipement.xhtml");

                    } else if (Objects.equals(loggedInUser.getType(), "unite")) {
                        System.out.println("user found Unite");

                        // Perform redirect
                        FacesContext.getCurrentInstance().getExternalContext().redirect("Unite/planification.xhtml");

                    }




                    return; // Stop further execution
                } else {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Nom utilisateur ou mot de passe incorrecte");
                }

        } catch (IOException e) {
            // Handle potential IOException from redirect
            e.printStackTrace();
        }
    }
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void logout() throws IOException {

        user1=new User();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        System.out.println(session);

        if(session != null){

            session.invalidate();
            System.out.println(session);

        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");

        user1=new User();

    }
}


