/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.gui.aruga.watch.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author akari
 */
public class TableTodo {
    private String id;
    private StringProperty title;
    private StringProperty description;
    
    public TableTodo (){
    }
    
    public TableTodo (String id, String title, String description){
        setId(id);
        setTitle(title);
        setDescription(description);
    }
    
    /**
     * @return the title
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    /**
     * @return the description
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
