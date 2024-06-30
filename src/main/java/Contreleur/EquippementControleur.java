package Contreleur;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import model.Entity.*;
import model.Facade.EquipementFacade;
import model.Facade.FamilleFacade;
import model.Facade.UserFacade;

import java.io.Serializable;
import java.util.*;

@Data
@Named(value ="equipeentControleur")
@ViewScoped
public class EquippementControleur implements Serializable {

    private Equipement equipement=new Equipement();
    private Equipement equipementNotcontroled =new Equipement();
    private List<Equipement> equipementList;

    //private Famille_equipement famille;
    private List<Famille_equipement> familleEquipementList=new ArrayList<>();

    @Inject
    private EquipementFacade equipementFacade;
    @Inject
    private FamilleFacade familleFacade;
    @Inject
    private UserFacade userFacade;

    @PostConstruct
    public void init(){
        equipement = new Equipement();
        equipementNotcontroled = new Equipement();
        equipementNotcontroled.setDateProchainControle(new Date());

        List<Equipement> equipmentList = equipementFacade.getEquipmentPassedControlDate();
        System.out.println(equipmentList);


        for (Equipement equipment : equipmentList) {
            equipment.setStatut(false);
            // Assuming you have a method to persist the changes to the database
            equipementFacade.edit(equipment);
        }
        equipmentList = equipementFacade.getEquipmentControled();

        for (Equipement equipment : equipmentList) {
            equipment.setStatut(true);
            // Assuming you have a method to persist the changes to the database
            equipementFacade.edit(equipment);
        }



        // equipement = new Equipement();
        // Initialize famille as well to avoid potential issues
        //famille = new Famille_equipement();
        refrechEquipement();
        refrechFamilly();
    }

    public void refrechEquipement() {
        equipementList = equipementFacade.findAll();
    }

    public void refrechFamilly() {
        familleEquipementList = familleFacade.findAll();
    }

    public void submit() {
        System.out.println("in the submit");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            System.out.println("session not null");
            if (userId != null) {
                // Assuming you have a method in your UserFacade to find a user by ID
                User user = userFacade.find(userId); // Adjust this line to match how you can fetch a User entity by ID
                System.out.println("id passed");
                if (user != null) {

                    Unite unite = user.getUnite();
                    System.out.println(unite);
                    if (unite != null) {

                        equipement.setUnite(unite);
                        System.out.println("Unite set to equipment");

                        try {
                            // Check if the equipment's family periodicity is "trimestriel"
                            if (Objects.equals(equipement.getFamille_equipement().getPeropdicite(), "trimestrielle")) {
                                // Assuming you have a getter for the dateMiseEnService that returns a Date
                                Date dateMiseEnService = equipement.getDateMisEnServive();

                                // Adding 3 months to the dateMiseEnService
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(dateMiseEnService);
                                calendar.add(Calendar.MONTH, 3); // Adding 3 months

                                // Assuming you have a setter for the end date to store the new calculated date
                                equipement.setDateProchainControle(calendar.getTime());
                            } else if (Objects.equals(equipement.getFamille_equipement().getPeropdicite(), "semestriel")) {
                                Date dateMiseEnService = equipement.getDateMisEnServive();

                                // Adding 3 months to the dateMiseEnService
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(dateMiseEnService);
                                calendar.add(Calendar.MONTH, 6); // Adding 3 months

                                // Assuming you have a setter for the end date to store the new calculated date
                                equipement.setDateProchainControle(calendar.getTime());
                            }else if (Objects.equals(equipement.getFamille_equipement().getPeropdicite(), "annuel")) {
                                Date dateMiseEnService = equipement.getDateMisEnServive();

                                // Adding 3 months to the dateMiseEnService
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(dateMiseEnService);
                                calendar.add(Calendar.YEAR, 1); // Adding 3 months

                                // Assuming you have a setter for the end date to store the new calculated date
                                equipement.setDateProchainControle(calendar.getTime());
                            }else if (Objects.equals(equipement.getFamille_equipement().getPeropdicite(), "5ans")) {
                                Date dateMiseEnService = equipement.getDateMisEnServive();

                                // Adding 3 months to the dateMiseEnService
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(dateMiseEnService);
                                calendar.add(Calendar.YEAR, 5); // Adding 3 months

                                // Assuming you have a setter for the end date to store the new calculated date
                                equipement.setDateProchainControle(calendar.getTime());
                            }else if (Objects.equals(equipement.getFamille_equipement().getPeropdicite(), "10ans")) {
                                Date dateMiseEnService = equipement.getDateMisEnServive();

                                // Adding 3 months to the dateMiseEnService
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(dateMiseEnService);
                                calendar.add(Calendar.YEAR, 10); // Adding 3 months

                                // Assuming you have a setter for the end date to store the new calculated date
                                equipement.setDateProchainControle(calendar.getTime());
                            }
                            Date currentDate = new Date(); // Assuming currentDate is today's date
                            if (currentDate.after(equipement.getDateProchainControle())) {
                                // If the current date is after the next control date, set the status to false
                                equipement.setStatut(false);
                            } else {
                                // If the next control date has not passed yet, set the status to true
                                equipement.setStatut(true);
                            }
                            System.out.println(equipement);

                            equipementFacade.create(equipement);
                            refrechEquipement();
                            addMessage(FacesMessage.SEVERITY_INFO, "Success", "Équipement ajouté avec succès");
                        } catch (Exception e) {
                            addMessage(FacesMessage.SEVERITY_ERROR, "Erreur",  "Échec de l'ajout d'équipement: " + e.getMessage());

                        }


                    }else {
                        System.out.println("cant find unite");
                    }
                }else{
                    System.out.println("cant find user");
                }
            }else {
                System.out.println("cant find user");
            }
        }else{
            System.out.println("session est mort");
        }

    }
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void update(){
        try {
            equipementFacade.edit(equipement);
            addMessage(FacesMessage.SEVERITY_INFO, "Success ", "Equipment est modifié");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "échec de la modification de l'équipement: " + e.getMessage());

        }
    }

    public void deleteequipement() {
        try {
            equipementFacade.remove(equipement);
            refrechEquipement();
            addMessage(FacesMessage.SEVERITY_INFO, "success", "l'équipement est supprimé ");
        }catch (Exception e){
            addMessage(FacesMessage.SEVERITY_ERROR, "erreur", "échec de la suppression de l'équipement "+ e.getMessage());

        }

    }
}
