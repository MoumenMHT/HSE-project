package Contreleur;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.persistence.Persistence;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author amrani.halim
 */
@FacesConverter("GeneriqueConverter")
public class GeneriqueConverter implements Converter {

    public Class<?> typeClass;

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Field[] fil = value.getClass().getDeclaredFields();
            for (Field unfil : fil) {
                unfil.setAccessible(true);
                for (java.lang.annotation.Annotation annotation : unfil.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equals("Id")) {
                        try {
                            return String.valueOf(unfil.get(value));
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(GeneriqueConverter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        return "";
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            Class<?> entityType = component.getValueExpression("value").getType(context.getELContext());
            try {
                typeClass = Class.forName(entityType.toString().replaceFirst("class ", ""));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GeneriqueConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            Field[] fil = typeClass.getDeclaredFields();
            for (Field unfil : fil) {
                unfil.setAccessible(true);
                for (java.lang.annotation.Annotation annotation : unfil.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equals("Id")) {
                        return Persistence.createEntityManagerFactory("Persistence_Hse").createEntityManager().getReference(entityType, convertOfObject(value, recupTypeFild(typeClass, unfil.getName())));
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param ob   la classe ou l'entitie
     * @param fild le nom du champs de l'entitie que je veux savoir leurs type
     * @return le type du champs exemple int, long
     */
    public Class<?> recupTypeFild(Class ob, String fild) {
        Field[] fil = ob.getDeclaredFields();
        for (Field unfil : fil) {
            String champ = unfil.getName().trim();
            if (champ.compareTo(fild.trim()) == 0) {
                try {
                    switch (unfil.getType().getSimpleName()) {
                        case "int":
                            return Class.forName("java.lang.Integer");
                        case "long":
                            return Class.forName("java.lang.Long");
                        case "double":
                            return Class.forName("java.lang.Double");
                        case "byte":
                            return Class.forName("java.lang.Byte");
                        case "float":
                            return Class.forName("java.lang.float");
                        default:
                            return unfil.getType();
                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GeneriqueConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * @param <T>            type indefini
     * @param fromObjectType l'objet à converter
     * @param ToObjectType   le nouveau type d'objet retourner
     * @return l'objet entrer mais avec un nouveu type ex:from String vers Integer
     */
    public <T> T convertOfObject(Object fromObjectType, Class<T> ToObjectType) {
        if (fromObjectType == null) {
            return null;
        }
        try {
            switch (ToObjectType.getSimpleName()) {
                case "String":
                    return ToObjectType.cast(fromObjectType);
                case "Integer":
                    return ToObjectType.cast(Integer.parseInt(fromObjectType.toString()));
                case "int":
                    return ToObjectType.cast(Integer.parseInt(fromObjectType.toString()));
                case "Long":
                    return ToObjectType.cast(Long.parseLong(fromObjectType.toString()));
                case "long":
                    return ToObjectType.cast(Long.parseLong(fromObjectType.toString()));
                case "Byte":
                    return ToObjectType.cast(Byte.parseByte(fromObjectType.toString()));
                case "Double":
                    return ToObjectType.cast(Double.parseDouble(fromObjectType.toString()));
                case "double":
                    return ToObjectType.cast(Double.parseDouble(fromObjectType.toString()));
                case "Float":
                    return ToObjectType.cast(Float.parseFloat(fromObjectType.toString()));
                case "float":
                    return ToObjectType.cast(Float.parseFloat(fromObjectType.toString()));
                case "Short":
                    return ToObjectType.cast(Short.parseShort(fromObjectType.toString()));
                case "char":
                    return ToObjectType.cast((fromObjectType.toString()).charAt(0));
                default:
                    return ToObjectType.cast(fromObjectType);

            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert from "
                    + fromObjectType.getClass().getName() + " to " + ToObjectType.getName()
                    + ". Conversion failed with " + e.getMessage(), e);
        }
    }

}

