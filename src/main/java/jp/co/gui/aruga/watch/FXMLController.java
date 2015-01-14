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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import jp.co.gui.aruga.watch.entity.Category;
import jp.co.gui.aruga.watch.entity.TableTodo;
import jp.co.gui.aruga.watch.entity.Todo;

public class FXMLController implements Initializable {
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
    
    // 更新系
    @FXML
    private AnchorPane updateTodoPane;    
    @FXML
    private TextField updateTitleText;
    @FXML
    private DatePicker updateDeadlinePicker;
    @FXML
    private ChoiceBox<String> updateCategoryChoice;
    @FXML
    private ChoiceBox<String> updateStatusChoice;
    @FXML
    private TextArea updateDescriptionText;
    @FXML
    private Text updateCreatedLabel;
    
    ClockHttpRequest httpRequest = new ClockHttpRequest();

    Map<Tab, Category> tabMap = new HashMap<>();
    
    @FXML
    private void handleUpdateRequest(ActionEvent event) {
        updateTodoPane.setVisible(false);
    }
    
    @FXML
    private void handleUpdateCancel(ActionEvent event) {
        updateTodoPane.setVisible(false);
    }
    
    @FXML
    private void handleCreateTodoButton(ActionEvent event) {
        createTodoPane.setVisible(true);
    }
    
    @FXML
    private void todoViewClicked (MouseEvent event){
         if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
           // ダブルクリック
             System.out.println("double");
             updateTodoPane.setVisible(true);
             TableTodo tt = getSelectedTableTodo();
        }
        else if (event.getButton().equals(MouseButton.PRIMARY)  && event.getClickCount() == 1) {
           // シングルクリック
        }
    }
    
    @FXML
    private void handleDeleteTodoButton (ActionEvent event) throws IOException {

        TableTodo tt = getSelectedTableTodo();
        httpRequest.delete(tt.getId());
        getSelectedTableView().getItems().remove(tt);
        List<TableTodo> ltt = todoView.getItems();
        for (TableTodo ltt1 : ltt) {
            if (ltt1.getId().equals(tt.getId())){
                todoView.getItems().remove(ltt1);
                break;
            }
        }
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
        Tab t = getSelectedTab();
        String categoryId = tabMap.get(t).getId();
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
        tabMap.put(t, ca);
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
                TableTodo todo = new TableTodo(todo1);
                todoView.getItems().add(todo);
            }
        }
        
        // カテゴリのタブの作成と、それぞれのtodoの作成
        if (categories != null) {
            for (Category category : categories) {
                try {
                    Tab t = getTab(category.getBody());
                    tabMap.put(t, category);
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
        to.setOnMouseClicked((MouseEvent event) -> {
            todoViewClicked(event);
        });
        to.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
        to.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        return t;
    }

    private void setTabContents(Tab tab, Todo todo) {
        ObservableList<Tab> ol = clockTabPane.getTabs();

        TableTodo tt = new TableTodo(todo);
        AnchorPane ap = (AnchorPane) tab.getContent();
        TableView<TableTodo> to = (TableView<TableTodo>) ap.getChildren().get(0);
        to.getItems().add(tt);
    }
    
    private Tab getSelectedTab(){
        int tabNum = clockTabPane.getSelectionModel().getSelectedIndex();
        return clockTabPane.getTabs().get(tabNum); 
    }
    
    private TableView<TableTodo> getSelectedTableView(){
        Tab t = getSelectedTab();
        AnchorPane ap = (AnchorPane) t.getContent();
        return (TableView<TableTodo>) ap.getChildren().get(0); 
    }
    
    private TableTodo getSelectedTableTodo() {
        TableView<TableTodo> to = getSelectedTableView();
        int viewNum = to.getSelectionModel().getSelectedIndex();
        return to.getItems().get(viewNum);
    }

}
