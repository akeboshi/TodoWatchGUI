package jp.co.gui.aruga.watch;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import jp.co.gui.aruga.watch.entity.Category;
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

    ClockHttpRequest httpRequest = new ClockHttpRequest();

    Map<Tab, String> tabMap = new HashMap<>();

    int i = 0;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        clockTabPane.getTabs().add(getTab("new"));

        Tab t = clockTabPane.getTabs().get(1);
        AnchorPane ap = (AnchorPane) t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>) ap.getChildren().get(0);
        i++;
        to.getItems().add(new TableTodo("aha","oho" + i, "unko" + i));
        TableView.TableViewSelectionModel<TableTodo> sm = to.getSelectionModel();
        to.getItems().remove(sm.getSelectedItem());

    }

    @FXML
    private void handleCategoryButton(ActionEvent event) {
        categoryPane.setVisible(true);
    }

    @FXML
    private void handleCategoryCancel(ActionEvent event) {
        categoryPane.setVisible(false);
        categoryText.setText("");
    }

    @FXML
    private void handleCategoryCreate(ActionEvent event) {
        clockTabPane.getTabs().add(getTab(categoryText.getText()));
        categoryPane.setVisible(false);
        categoryText.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        List<Todo> todos = null;
        List<Category> categories = null;
        // Login
        // すべてのtodoとカテゴリのリストを取得
        try {
            httpRequest.login("test", "test");
            todos = httpRequest.get(null);
            categories = httpRequest.getCategory();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // すべてのtodoをセット
        if (todos != null) {
            for (Todo todo1 : todos) {
                TableTodo todo = new TableTodo();
                todo.setId(todo1.getId());
                todo.setDescription(todo1.getDescription());
                todo.setTitle(todo1.getTitle());
                todoView.getItems().add(todo);
            }
        }
        
        // カテゴリのタブの作成と、それぞれのtodoの作成
        if (categories != null) {
            for (Category category : categories) {
                try {
                    Tab t = getTab(category.getBody());
                    tabMap.put(t, category.getBody());
                    clockTabPane.getTabs().add(t);
                    List<Todo> catTodos = httpRequest.get(category.getId());
                    AnchorPane ap = (AnchorPane) t.getContent();
                    for (Todo cTodo : catTodos) {
                        setTabContents(t, cTodo, category.getBody());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Thread updaterThread;
        updaterThread = new Thread(new ClockUpdater(date, hour,
                dot1, dot2, minute));
        updaterThread.setDaemon(true);
        updaterThread.start();
    }

    private Tab getTab(String title) {
        Tab t = null;
        try {
            t = FXMLLoader.load(getClass().getResource("/fxml/Tab.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.setText(title);
        AnchorPane ap = (AnchorPane) t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>) ap.getChildren().get(0);
        to.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
        to.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        return t;
    }

    private void setTabContents(Tab tab, Todo todo, String tabTitle) {
        ObservableList<Tab> ol = clockTabPane.getTabs();

        TableTodo tt = new TableTodo(todo.getId(), todo.getTitle(), todo.getDescription());
        AnchorPane ap = (AnchorPane) tab.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>) ap.getChildren().get(0);
        to.getItems().add(tt);
    }

    private List<Todo> getTodo() {

        return null;
    }

}
