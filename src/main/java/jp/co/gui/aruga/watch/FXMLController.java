package jp.co.gui.aruga.watch;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import jp.co.gui.aruga.watch.entity.TableTodo;
import jp.co.gui.aruga.watch.entity.Todo;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TabPane clockTabPane;
    
    @FXML
    private AnchorPane todoPane;
    
    @FXML
    private AnchorPane categoryPane;
    
    @FXML
    private TableView<TableTodo> todoView;
    
    @FXML
    private TableColumn<TableTodo, String> titleColumn;
    
    @FXML
    private TableColumn<TableTodo, String> descriptionColumn;
    
    @FXML
    private Label date;
    @FXML
    private Label hour;
    @FXML
    private Label minute;
    @FXML
    private Label dot1;
    @FXML
    private Label dot2;
    @FXML
    private TextField categoryText;
    
    
    int i=0;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        clockTabPane.getTabs().add(getTab("new"));
        
        try {
            ClockHttpRequest chr =new ClockHttpRequest();
            chr.login("a", "b");
            chr.get();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Tab t = clockTabPane.getTabs().get(1);
        AnchorPane ap = (AnchorPane)t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>)ap.getChildren().get(0);
        i++;
        to.getItems().add(new TableTodo("oho" + i, "unko" + i));
        TableView.TableViewSelectionModel<TableTodo>  sm = to.getSelectionModel();
        to.getItems().remove(sm.getSelectedItem());
        
    }
    
    @FXML
    private void handleCategoryButton(ActionEvent event) {
        categoryPane.setVisible(true);
    }
    
    @FXML
    private void handleCategoryCancel(ActionEvent event){
        categoryPane.setVisible(false);
        categoryText.setText("");
    }
    
    @FXML
    private void handleCategoryCreate(ActionEvent event){
        clockTabPane.getTabs().add(getTab(categoryText.getText()));
        categoryPane.setVisible(false);
        categoryText.setText("");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        TableTodo todo = new TableTodo();
        todo.setDescription("test");
        todo.setTitle("title");
        
        todoView.getItems().add(todo);
        Thread updaterThread ;
        updaterThread = new Thread(new ClockUpdater(date, hour,
                dot1,dot2, minute));
        updaterThread.setDaemon(true);
        updaterThread.start();
    }
    
    private Tab getTab(String title){
        Tab t = null;
        try {
            t = FXMLLoader.load(getClass().getResource("/fxml/Tab.fxml"));    
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.setText(title);
        AnchorPane ap = (AnchorPane)t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>)ap.getChildren().get(0);
        to.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
        to.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        return t;
    }
    
    private void setTabContents(TableTodo todo, String tabTitle){
        ObservableList<Tab> ol = clockTabPane.getTabs();
        
        Tab t = clockTabPane.getTabs().get(1);
        AnchorPane ap = (AnchorPane)t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>)ap.getChildren().get(0);
        to.getItems().add(todo);
    }
    
    private List<Todo> getTodo(){
        
        return null;
    }
    
}
