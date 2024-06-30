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
import model.Facade.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
@Named(value = "planificationControleur")
@ViewScoped
public class PlanificationControleur implements Serializable {
    private String selectedEquipment;

    @Inject
    private PlanificationFacade planificationFacade;
    @Inject
    private UniteFacade uniteFacade;
    @Inject
    private EquipementFacade equipementFacade;
    @Inject
    private FamilleFacade familleFacade;
    @Inject
    private UserFacade userFacade;

    private List<Famille_equipement> familyList;
    private List<Equipement> equipementList;
    private Planification planification;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Planification planifauditselected = new Planification();
    private List<Unite> uniteList;
    private List<Planification> planificationList;
    private String serverTimeZone = ZoneId.systemDefault().toString();

    private int cp = 1;
    List<Equipement> equipements = new ArrayList<>();
    Equipement equipement= new Equipement();
    @PostConstruct
    public void init() {










        planification = new Planification();
        refreshUnite();
        refreshEquipment();
        loadAllFamily();
        eventModel = new DefaultScheduleModel();
        planificationList = planificationFacade.findAll();

        for (Planification itemplanification : planificationList) {
            LocalDateTime startDate = itemplanification.getDateDebut().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            LocalDateTime endDate = itemplanification.getDateFin().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            cp++;
            event = DefaultScheduleEvent.builder()
                    .title(itemplanification.getTitre())
                    .startDate(startDate)
                    .endDate(endDate.plusDays(1))
                    .overlapAllowed(true)
                    .id(itemplanification.getId_planification().toString())
                    .build();

            planification.setEquipement(itemplanification.getEquipement());
            planification.setUnite(itemplanification.getUnite());

            eventModel.addEvent(event);
        }
    }

    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        planification = new Planification();
        event = DefaultScheduleEvent.builder().startDate(selectEvent.getObject()).endDate(selectEvent.getObject().plusHours(1)).build();
    }
    public void onEventSelect(SelectEvent<ScheduleEvent> selectEvent) {

        event = selectEvent.getObject();
        planification = new Planification();
        planifauditselected = planificationFacade.find(Long.parseLong(event.getId()));
        LocalDateTime endDate = planifauditselected.getDateFin().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        event.setEndDate(endDate);
        long id = Long.parseLong(event.getId());
        planification.setId_planification(id);
        planification.setTitre(planifauditselected.getTitre());
        planification.setUnite(planifauditselected.getUnite());
        planification.setEquipement(planifauditselected.getEquipement() );
        refreshUnite();
    }
    public void test()
    {

        equipementList = new ArrayList<>();
    }

    public void refreshUnite() {
        uniteList = uniteFacade.findAllUnite();
    }

    public void refreshEquipment() {
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

                        equipementList = equipementFacade.findEquipmentByUnite(user.getUnite());

                    }
                }
            }
        }
    }

    public void addEvent() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        if (session != null) {

            if (event.isAllDay()) {
                if (event.getStartDate().toLocalDate().equals(event.getEndDate().toLocalDate())) {
                    event.setEndDate(event.getEndDate().plusDays(1));
                }
            }

            if (event.getId() == null) {
                eventModel.addEvent(event);
                Date startdate = Date.from(event.getStartDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                planification.setDateDebut(startdate);
                Date enddate = Date.from(event.getEndDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                planification.setDateFin(enddate);
                int annee = startdate.getYear() + 1900;


                Long userId = (Long) session.getAttribute("userId");
                if (userId != null) {

                    System.out.println("id gotten");

                    User user = userFacade.find(userId);
                    if (user != null) {
                        System.out.println(userId);
                        User UserUnite = userFacade.getUserUnite(userId);

                        System.out.println(UserUnite);
                        if(UserUnite.getUnite() !=null){
                            planification.setUnite(UserUnite.getUnite());
                            System.out.println(planification);
                            planification.setTitre(event.getTitle() +

                                    "/" + planification.getEquipement().getNom_equipement() +
                                    "/" + annee);



                            try {
                                planificationFacade.create(planification);
                                init();
                                if (planification.getId_planification() != null) {
                                    event.setId(planification.getId_planification().toString());
                                } else {
                                    System.out.println("ID Planification is null");
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(Planification.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            System.out.println(planification);
                            planification.setTitre(event.getTitle() +
                                    "/" + planification.getUnite().getNom_unite() +
                                    "/" + planification.getEquipement().getNom_equipement() +
                                    "/" + annee);


                            try {
                                planificationFacade.create(planification);
                                init();
                                if (planification.getId_planification() != null) {
                                    event.setId(planification.getId_planification().toString());
                                } else {
                                    System.out.println("ID Planification is null");
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(Planification.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }


                    } else {
                        System.out.println("user not found");

                    }

                } else {
                    System.out.println("sesion cant be restored");
                }
            } else {
                planifauditselected = planificationFacade.find(Long.parseLong(event.getId()));
                Date startdate = Date.from(event.getStartDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                planification.setDateDebut(startdate);
                Date enddate = Date.from(event.getEndDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                planification.setDateFin(enddate);
                int annee = startdate.getYear() + 1900;
                planification.setTitre(event.getTitle() + "/" + planification.getUnite().getNom_unite() + "/" + planification.getEquipement().getNom_equipement() + "/" + annee);
                try {
                    planificationFacade.edit(planification);
                } catch (Exception ex) {
                    Logger.getLogger(Planification.class.getName()).log(Level.SEVERE, null, ex);
                }
                eventModel.updateEvent(event);
                init();
            }
        }
    }

    public void refreshPlanification() {
        planificationList = planificationFacade.findAll();
    }

    public void deletePlanification() {
        try {
            planificationFacade.remove(planification);
            refreshPlanification();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "planification est supprimé", ""));

        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "impossible de supprimer la planification"+ ex.getMessage(), ""));
        }

    }

    public void equipmentControled(){
        try{
            Equipement idEquipement = planification.getEquipement();
            idEquipement.setDateDernierControle(planification.getDateFin());
            idEquipement.setStatut(true);
            if (Objects.equals(idEquipement.getFamille_equipement().getPeropdicite(), "trimestrielle")) {
                // Assuming you have a getter for the dateMiseEnService that returns a Date
                Date dateMiseEnService = idEquipement.getDateProchainControle();

                // Adding 3 months to the dateMiseEnService
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateMiseEnService);
                calendar.add(Calendar.MONTH, 3); // Adding 3 months

                // Assuming you have a setter for the end date to store the new calculated date
                idEquipement.setDateProchainControle(calendar.getTime());
            } else if (Objects.equals(idEquipement.getFamille_equipement().getPeropdicite(), "semestriel")) {
                Date dateMiseEnService = idEquipement.getDateMisEnServive();

                // Adding 3 months to the dateMiseEnService
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateMiseEnService);
                calendar.add(Calendar.MONTH, 6); // Adding 3 months

                // Assuming you have a setter for the end date to store the new calculated date
                idEquipement.setDateProchainControle(calendar.getTime());
            }else if (Objects.equals(idEquipement.getFamille_equipement().getPeropdicite(), "annuel")) {
                Date dateMiseEnService = idEquipement.getDateProchainControle();

                // Adding 3 months to the dateMiseEnService
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateMiseEnService);
                calendar.add(Calendar.YEAR, 1); // Adding 3 months

                // Assuming you have a setter for the end date to store the new calculated date
                idEquipement.setDateProchainControle(calendar.getTime());
            }else if (Objects.equals(idEquipement.getFamille_equipement().getPeropdicite(), "5ans")) {
                Date dateMiseEnService = idEquipement.getDateProchainControle();

                // Adding 3 months to the dateMiseEnService
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateMiseEnService);
                calendar.add(Calendar.YEAR, 5); // Adding 3 months

                // Assuming you have a setter for the end date to store the new calculated date
                idEquipement.setDateProchainControle(calendar.getTime());
            }else if (Objects.equals(idEquipement.getFamille_equipement().getPeropdicite(), "10ans")) {
                Date dateMiseEnService = idEquipement.getDateProchainControle();

                // Adding 3 months to the dateMiseEnService
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateMiseEnService);
                calendar.add(Calendar.YEAR, 10); // Adding 3 months

                // Assuming you have a setter for the end date to store the new calculated date
                idEquipement.setDateProchainControle(calendar.getTime());
            }
            Date currentDate = new Date();

            idEquipement.setDateDernierControle(currentDate);
            equipementFacade.edit(idEquipement);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "planification faite", ""));

            planificationFacade.remove(planification);
            refreshPlanification();
        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", ex.getMessage()));
        }


    }

    public void loadAllFamily() {
        familyList = familleFacade.findAll();
    }

    public void updateEquipmentList() {
        System.out.println("ttttttttttt");
        if (planification.getEquipement() != null && planification.getEquipement().getFamille_equipement() != null) {
            Famille_equipement selectedFamily = planification.getEquipement().getFamille_equipement();
            System.out.println("mmmmmmm");
            equipementList = equipementFacade.findequipmentByFamily(selectedFamily);
        }
    }

  /*  public List<String> completeEquipment(String query, Map<String, Object> attributes) {
        Famille_equipement selectedFamily = (Famille_equipement) attributes.get("selectedFamily");
        List<String> filteredEquipment = new ArrayList<>();

        if (selectedFamily != null) {
            List<Equipement> equipements = equipementFacade.findequipmentByFamily(selectedFamily);

            if (equipements != null) {
                for (Equipement equipment : equipements) {
                    if (equipment.getNom_equipement().toLowerCase().startsWith(query.toLowerCase())) {
                        filteredEquipment.add(equipment.getNom_equipement());
                    }
                }
            }
        }

        return filteredEquipment;
    }  */



    public List<Equipement> completeEquipment(String query) {
       return this.equipementList.stream().filter(equipement -> equipement.getNom_equipement().toLowerCase().startsWith(query.toLowerCase())).collect(Collectors.toList());
    }








}
