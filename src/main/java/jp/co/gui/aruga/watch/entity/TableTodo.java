/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.gui.aruga.watch.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    // 優先度
    private Integer level;
    // category
    private String category;
    // 状態
    private Integer status;
    // 締め切り
    private Date deadline;
    // 作成日
    private Date created;
    // 締め切り string
    private StringProperty deadlineString;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");

    private TableTodo() {
    }

    public TableTodo(String id, String title, String description, Integer level,
            String category, Integer status, Date deadline, Date created) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLevel(level);
        setCategory(category);
        setStatus(status);
        setDeadline(deadline);
        setCreated(created);
    }

    public TableTodo(Todo todo) {
        this(todo.getId(), todo.getTitle(),
                todo.getDescription(), todo.getLevel(), todo.getCategory(),
                todo.getStatus(), todo.getDeadline(), todo.getCreated());
    }

    public StringProperty deadlineProperty() {
            if (deadline != null)
                return new SimpleStringProperty(sdf.format(deadline));
            else
                return new SimpleStringProperty("");
     }

    /**
     * @return the title
     */
    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.getValue();
    }

    /**
     * @param title the title to set
     */
    private void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    /**
     * @return the description
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescrption() {
        return description.getValue();
    }

    /**
     * @param description the description to set
     */
    private void setDescription(String description) {
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
    private void setId(String id) {
        this.id = id;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    private void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    private void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    private void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    private void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    private void setCreated(Date created) {
        this.created = created;
    }

}
