package Contreleur;

import jakarta.annotation.PostConstruct;
import jakarta.el.MethodExpression;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import model.Entity.Equipement;
import model.Entity.Famille_equipement;
import model.Entity.Unite;
import model.Facade.EquipementFacade;
import model.Facade.FamilleFacade;
import model.Facade.UniteFacade;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.*;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Named(value = "chartControleur")
@ViewScoped
public class ChartControler implements Serializable {

    private HorizontalBarChartModel hbarModel;
    private PieChartModel pieModel;


    private Date datedebut;
    private Date datefin;
    private Unite unite;
    private List<Unite> allUnites;
    private Famille_equipement famille;
    private List<Famille_equipement> allFamille;

    @Inject
    private EquipementFacade equipementFacade;
    @Inject
    private UniteFacade uniteFacade;
    @Inject
    private FamilleFacade familleFacade;

    @PostConstruct
    public void init() {

        refreshUnite();
        createHorizontalBarModel();

        getFamilleList();
        createPieModel();

    }

    private void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        Long equipmentControled = null;
        Long equipment= null;
        Long equipementNon= null;
        Long percentageEquipmentControle= null;
        Long percentageEquipmentNonControle= null;
        String pgEquipmentControle = "0";
        String pgEquipmentNonControle = "0";

        if( datefin == null && unite == null){

            System.out.println("date fun and unit are null");
             equipment = equipementFacade.countEquipment();


            Date currentDate = new Date();

             equipmentControled = equipementFacade.countEquipmentControled(currentDate );
            System.out.println(equipmentControled);



             equipementNon = equipment - equipmentControled;

            System.out.println(equipementNon);

        }else if( datefin != null && unite == null) {

            System.out.println("date fin not null and unite null");

             equipmentControled = equipementFacade.countEquipmentControledFilterByDate(datedebut, datefin );
            System.out.println(equipmentControled);


            equipment = equipementFacade.countEquipment();

             equipementNon = equipment - equipmentControled;
        }else if(unite != null && datefin == null) {

            System.out.println("in unite without date");

            Date currentDate = new Date();
            System.out.println(unite);

            equipmentControled = equipementFacade.countEquipmentControledFilterByUnite(unite, currentDate);
            System.out.println(equipmentControled);


            equipment = equipementFacade.countEquipmentByUnite(unite);

            equipementNon = equipment - equipmentControled;
        }else if (unite !=null && datefin != null) {

             equipmentControled = equipementFacade.countEquipmentControledFilterByUnite(unite, datefin );
            System.out.println(equipmentControled);


            equipment = equipementFacade.countEquipmentByUnite(unite);

             equipementNon = equipment - equipmentControled;


        }



        if (equipment == 0){

            percentageEquipmentControle = null;


        }else{
            percentageEquipmentControle = equipmentControled*100/equipment;
             pgEquipmentControle = Long.toString(percentageEquipmentControle);


            percentageEquipmentNonControle = equipementNon*100/equipment;
             pgEquipmentNonControle = Long.toString(percentageEquipmentNonControle);

        }


        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(equipmentControled);
        values.add(equipementNon);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(0,255,0)");
        bgColors.add("rgb(255, 99, 132)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("pourcentage des equipements controlés "+pgEquipmentControle+"%");
        labels.add("pourcentage des equipements non controlés "+pgEquipmentNonControle+"%");

        data.setLabels(labels);

        pieModel.setData(data);

    }
    public void createHorizontalBarModel() {
        hbarModel = new HorizontalBarChartModel();
        ChartData data = new ChartData();

        HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
        if( datefin == null && unite == null && datedebut == null && famille==null){
            Date currentDate = new Date();

            Long equipmentControled = equipementFacade.countEquipmentControled(currentDate );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipment();

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);
        }else if(datefin != null && datedebut != null && unite == null && famille == null) {
            System.out.println("in else");

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByDate( datedebut, datefin );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipment();

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);
            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);
            hbarModel.setOptions(options);




        }else if(unite != null && datefin == null && datedebut== null && famille == null){

            System.out.println("in unite without date");

            Date currentDate = new Date();
            System.out.println(unite);

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByUnite(unite, currentDate );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipmentByUnite(unite);

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);

        } else if (unite == null && datefin == null && datedebut== null && famille != null) {

            System.out.println("in unite with date");

            System.out.println(unite);

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByFamilly(famille );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipmentByfamilly(famille);

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);
        }


        else if (unite !=null && datedebut != null && datefin != null && famille ==null) {

            System.out.println("in unite with date");

            System.out.println(unite);

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByUnite(unite, datefin );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipmentByUnite(unite);

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);


        }
        else if (unite == null && datefin != null && datedebut != null && famille!= null)  {

            System.out.println("in unite with date");

            System.out.println(famille);

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByFamillyAndDate(famille,datedebut, datefin );
            System.out.println(equipmentControled);




            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);


            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement a controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);
        }
        else if (unite != null && datefin != null && datedebut!= null && famille != null) {

            System.out.println("in unite with date");

            System.out.println(unite);

            Long equipmentControled = equipementFacade.countEquipmentControledFilterByUnite(unite, datefin );
            System.out.println(equipmentControled);


            Long equipement = equipementFacade.countEquipmentByUnite(unite);

            Long equipementNon = equipement - equipmentControled;

            System.out.println(equipementNon);

            List<Number> values = new ArrayList<>();
            values.add(equipmentControled);
            values.add(equipementNon);
            hbarDataSet.setData(values);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            List<String> labels = new ArrayList<>();
            labels.add("équipement controlé");
            labels.add("équipement non controlé");
            data.setLabels(labels);
            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            linearAxes.setBeginAtZero(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("statistique des équipements");
            options.setTitle(title);

            hbarModel.setOptions(options);
        }

    }
    public void refreshUnite() {
        allUnites = uniteFacade.findAllUnite(); // Method to refresh the list of filials
    }
    public void getFamilleList() {
        allFamille = familleFacade.findAll(); // Method to refresh the list of filials
    }


    public void Update() {
        createHorizontalBarModel();
        createPieModel();
    }
}
