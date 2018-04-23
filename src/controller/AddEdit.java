package controller;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Task;
public class AddEdit{
	
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
    @FXML
    DatePicker DatePicker;
    @FXML
    Slider Slider;
    @FXML
    TextArea Description;
    @FXML
    TextField Title;
    @FXML
    Button Close;  
    @FXML
    Button AddModify;
    

    @FXML
    public void CloseButton()
    {
    	Stage stage = (Stage) Close.getScene().getWindow();
		stage.close();
		
    }




    public void PassTask(Task task,ObservableList<Task> task_list, TableView<Task> table)
    {
    	AddModify.setText("Modify");
    	Slider.setValue(task.getComplete());
    	Description.setText(task.getDescription());
    	Title.setText(task.getTitle());
    	DatePicker.setValue(task.getDate());
        AddModify.setOnAction(new EventHandler<ActionEvent>() {
           	public void handle(ActionEvent event) {
           		if (DatePicker.getValue()==null)
           			{
               			Alert alert = new Alert(AlertType.INFORMATION,"Date is not chosen");
                        alert.show();
           			}
           		else
           		{
                	Double temp_double = Slider.getValue();
                	task.ModifyTask(DatePicker.getValue().format(dateFormat),Title.getText(),temp_double.intValue(),Description.getText());
                    table.setItems(task_list);
                	Stage stage = (Stage) Close.getScene().getWindow();
            		stage.close();
           		}
            }
        });
    }

    public void AddModify(ObservableList<Task> task_list, TableView<Task> table)
    {
        AddModify.setText("Add");
        AddModify.setOnAction(new EventHandler<ActionEvent>() {
               	public void handle(ActionEvent event) {
               		if (DatePicker.getValue()==null)
               			{
	               			Alert alert = new Alert(AlertType.INFORMATION,"Date is not chosen");
	                        alert.show();
               			}
               		else
               		{
                    	Double temp_double = Slider.getValue();
                    	Task new_task = new Task(DatePicker.getValue().format(dateFormat),Title.getText(),temp_double.intValue(),Description.getText());
                    	task_list.add(new_task);
                        table.setItems(task_list);
                    	Stage stage = (Stage) Close.getScene().getWindow();
                		stage.close();
               		}
                }
            });

    }
    
}
