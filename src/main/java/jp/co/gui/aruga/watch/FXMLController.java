package jp.co.gui.aruga.watch;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    
    // すべて　のTab
    @FXML
    private Tab todoTab;

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
   private TextField titleText;
    @FXML
    private TextArea descriptionText;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private AnchorPane createTodoPane;

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
    
    @FXML
    private void handleCreateTodoButton(ActionEvent event) {
        createTodoPane.setVisible(true);
    }
    @FXML
    private void handleDeleteTodoButton (ActionEvent event) throws IOException {
        int tabNum = clockTabPane.getSelectionModel().getSelectedIndex();
        Tab t = clockTabPane.getTabs().get(tabNum);
        AnchorPane ap = (AnchorPane) t.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>) ap.getChildren().get(0);
        int viewNum = to.getSelectionModel().getSelectedIndex();
        TableTodo tt = to.getItems().get(viewNum);
        httpRequest.delete(tt.getId());
        to.getItems().remove(viewNum);
    }
    
    @FXML
    private void handleCreateRequest(ActionEvent event) throws IOException {
        createTodoPane.setVisible(false);
        String title = titleText.getText();
        String description = descriptionText.getText();
        Date deadline = null;
        if (deadlinePicker.getValue() != null){
            LocalDate ldt = deadlinePicker.getValue();
            deadline = new Date(ldt.getYear() - 1900, ldt.getMonthValue() - 1, ldt.getDayOfMonth());
        }
        int tabNum = clockTabPane.getSelectionModel().getSelectedIndex();
        Tab t = clockTabPane.getTabs().get(tabNum);
        String categoryId = tabMap.get(t);
        AnchorPane ap = (AnchorPane) t.getContent();
        Todo todo = new Todo();
        todo.setCategory(categoryId);
        todo.setTitle(title);
        todo.setDeadline(deadline);
        todo.setDescription(description);
        httpRequest.create(todo);
        setTabContents(t, todo);
        setTabContents(todoTab, todo);
        
        titleText.setText("");
        descriptionText.setText("");
        deadlinePicker.setValue(null);
    }
    
    @FXML
    private void handleTodoCancel(ActionEvent event) {
        createTodoPane.setVisible(false);
        titleText.setText("");
        descriptionText.setText("");
        deadlinePicker.setValue(null);
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
    private void handleCategoryCreate(ActionEvent event) throws IOException {
        String ct = categoryText.getText();
        Category ca = httpRequest.createCategory(ct);
        Tab t = getTab(categoryText.getText());
        tabMap.put(t, ca.getId());
        clockTabPane.getTabs().add(t);
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
                    tabMap.put(t, category.getId());
                    clockTabPane.getTabs().add(t);
                    List<Todo> catTodos = httpRequest.get(category.getId());
                    AnchorPane ap = (AnchorPane) t.getContent();
                    for (Todo cTodo : catTodos) {
                        setTabContents(t, cTodo);
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

    private void setTabContents(Tab tab, Todo todo) {
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
