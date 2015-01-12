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
    
    public TableTodo (String title, String description){
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

}
