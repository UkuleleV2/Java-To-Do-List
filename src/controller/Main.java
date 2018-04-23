package controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import model.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Main extends Application {
	//TableView<Task> table = new TableView<Task>();
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 ObservableList<Task> task_list = FXCollections.observableArrayList();
	 ObservableList<Task> filtered_list = FXCollections.observableArrayList();
	public TableView<Task> table = new TableView();
	
    RadioButton radio1 = new RadioButton("All");
    RadioButton radio2 = new RadioButton("Overdue");
    RadioButton	radio3 = new RadioButton("Today");
    RadioButton	radio4 = new RadioButton("This week");
    CheckBox notCompleted = new CheckBox("Not Completed");
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,900,600);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			root.getStyleClass().add("root");
			
            ToggleGroup tgroup = new ToggleGroup();
            radio1.setToggleGroup(tgroup);
            radio2.setToggleGroup(tgroup);
            radio3.setToggleGroup(tgroup);
            radio4.setToggleGroup(tgroup);
            HBox filtr_hbox = new HBox(10);
            filtr_hbox.getChildren().addAll(radio1,radio2,radio3,radio4,notCompleted);
            root.setTop(filtr_hbox);
			//notCompleted
            notCompleted.getStyleClass().add("check");
            
            
            //Bottom Buttons
		    Button add_button = new Button("Add Task");
		    Button save_button = new Button("Save");
		    add_button.setPadding(new Insets(20, 50, 050, 20));
		    save_button.getStyleClass().add("test");
		    HBox bottom_box = new HBox(150);
		    root.setBottom(bottom_box);
		    bottom_box.getChildren().addAll(add_button,save_button);
		    bottom_box.setPadding(new Insets(20, 50, 050, 225));

            //Table
            TableColumn<Task, String> date_column = new TableColumn("Due Date");
            date_column.setMinWidth(150);
            date_column.setCellValueFactory(
                    new PropertyValueFactory("due_date"));
            TableColumn<Task, String> title_column = new TableColumn("Title");
            title_column.setMinWidth(150);
            title_column.setCellValueFactory(
                    new PropertyValueFactory("title"));
            TableColumn<Task, Integer> complete_column = new TableColumn("%Complete");
            complete_column.setMinWidth(200);
            complete_column.setCellValueFactory(
                    new PropertyValueFactory("complete"));            
            TableColumn<Task, String> description_column = new TableColumn("Description");
            description_column.setMinWidth(150);
            description_column.setCellValueFactory(
                    new PropertyValueFactory("description"));
            TableColumn<Task, CheckBox> checkColumn = new TableColumn<>("IsCompleted");
            checkColumn.setCellValueFactory(
            		new PropertyValueFactory<>("check"));
            checkColumn.setMinWidth(150);
            table.setMaxWidth(900);
            table.getColumns().addAll(date_column,title_column,complete_column,description_column,checkColumn);
            
            VBox box = new VBox();
            box.getChildren().addAll(table);
            box.getStyleClass().add("table");
		    
			//reading file
            File conf_file = new File("tasks.conf");
            int our_sign = 0;
            int next_sign = 0;
            int i= 0;
            String[] info = new String[6];
            BufferedReader buff = new BufferedReader(new FileReader(conf_file));
            String readLine = "";
            while ((readLine = buff.readLine()) != null) {
            	i= 0;
            	our_sign = 0;
    			next_sign = readLine.indexOf('|', our_sign+1);
            	while (next_sign != -1)
            		{	
            			info[i] = readLine.substring(our_sign+1,next_sign);
            			our_sign = next_sign;
            			next_sign = readLine.indexOf('|', our_sign+1);
            			i++;
            		}
            	info[i] = readLine.substring(our_sign+1);
            	task_list.add(new Task(info[1],info[2] ,Integer.parseInt(info[3]) ,info[4]));
            	
            }
            buff.close();
            


           tgroup.selectedToggleProperty().addListener(
                    (ov, oldToggle, newToggle) -> {
                        if (radio1.isSelected())
                        {
                        	table.setItems(AllPressed());
                        }
                        else if (radio2.isSelected())
                        {
                        	table.setItems(OverDueFilter());
                        }
                        else if (radio3.isSelected())
                        {	
                        	table.setItems(TodayFilter());
                        }
                        else if (radio4.isSelected())
                        {
                        	table.setItems(ThisWeek());
                        }
                    }
                    );
            notCompleted.selectedProperty().addListener(
            		event -> {
            			if (radio1.isSelected())
                        {
            				table.setItems(AllPressed());
                        }
                        else if (radio2.isSelected())
                        {
                        	table.setItems(OverDueFilter());
                        }
                        else if (radio3.isSelected())
                        {	
                        	table.setItems(TodayFilter());
                        }
                        else if (radio4.isSelected())
                        	table.setItems(ThisWeek());
            			
                        });

          //Clicking on Rows
            table.setRowFactory(tv -> {
                TableRow<Task> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        try {
                                   Stage stage_dlg = new Stage();
                                   stage_dlg.setTitle("Edit");
                                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEdit.fxml"));
                                   Parent root1 = loader.load();
                                   AddEdit Control = loader.getController();
                                   Control.PassTask(row.getItem(),task_list,table);
                                   stage_dlg.setScene(new Scene(root1));
                                   stage_dlg.show();

                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
                );
                return row;
            }
            );

            add_button.setOnAction(
                    event->{
                        try {
                            Stage stageone = new Stage();
                            stageone.setTitle("Add");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEdit.fxml"));
                            Parent root2 = loader.load();
                            AddEdit Control = loader.getController();
                            Control.AddModify(task_list,table);
                            stageone.setScene(new Scene(root2));
                            stageone.show();
                     }
                     catch(Exception e) {
                         e.printStackTrace();                     
                    }
                    }
                    );
            
            save_button.setOnAction(
            		
            		event ->{
                    	FileOutputStream fos;
						try {
							fos = new FileOutputStream(conf_file);
	                    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	                    	if (task_list.size() > 0)
	                    	{
	                    		bw.write("1"+"|"+ task_list.get(0).getDue_date() + "|" + task_list.get(0).getTitle() + "|" + task_list.get(0).getCompleteString() + "|" + task_list.get(0).getDescription() );
	                    	}
	            	        for (int x = 1; x < task_list.size(); x++) 
	            	        {	
	            	        	bw.newLine();
		            	        bw.write("1"+"|"+ task_list.get(x).getDue_date() + "|" + task_list.get(x).getTitle() + "|" + task_list.get(x).getCompleteString() + "|" + task_list.get(x).getDescription() );
	            	        }
	            			bw.close();
	               			Alert alert = new Alert(AlertType.INFORMATION,"File Saved");
	                        alert.show();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

            		}

            		);

            
            radio1.setSelected(true);
            root.setCenter(box);          
			primaryStage.setScene(scene);
			primaryStage.setTitle("To do list"); 
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	public ObservableList<Task> OverDueFilter()
	{
	    LocalDate current_time = LocalDate.now();
	    ObservableList<Task> new_list = FXCollections.observableArrayList();
	    ObservableList<Task> retlist = FXCollections.observableArrayList();
	    if (notCompleted.isSelected()==true)
	    {
	    	new_list = NotCompleted();
	        for (int x = 0; x < new_list.size(); x++) 
	        {
        		if (new_list.get(x).getDate().isBefore(current_time)==true)
        		{
        			retlist.add(new_list.get(x));
        		}      
	        }
	    }
	    else if(notCompleted.isSelected()==false)
	    {
	        for (int x = 0; x < task_list.size(); x++) 
	        {
        		if (task_list.get(x).getDate().isBefore(current_time)==true)
        		{
        			retlist.add(task_list.get(x));
        		}    
	        }
	    }

	    return retlist;
	    

	}
	public ObservableList<Task> ThisWeek()
	{
	    LocalDate current_time = LocalDate.now();
	    LocalDate temp = current_time.plusDays(7);
	    ObservableList<Task> new_list = FXCollections.observableArrayList();
	    ObservableList<Task> retlist = FXCollections.observableArrayList();
	    if (notCompleted.isSelected()==true)
	    {
	    	
	    	new_list = NotCompleted();
	        for (int x = 0; x < new_list.size(); x++) 
	        {
        		if (new_list.get(x).getDate().isBefore(temp)== true & new_list.get(x).getDate().isAfter(current_time))     			
        		{
        			retlist.add(new_list.get(x));
        		}        
	        }
	    }
	    else if(notCompleted.isSelected()==false)
	    {
	        for (int x = 0; x < task_list.size(); x++) 
	        {
        		if (task_list.get(x).getDate().isBefore(temp)== true & task_list.get(x).getDate().isAfter(current_time))     			
        		{
        			retlist.add(task_list.get(x));
        		}     
	        }
	    }

	    return retlist;
	    
	}
	public ObservableList<Task> TodayFilter()
	{
		ObservableList<Task> new_list = FXCollections.observableArrayList();
		ObservableList<Task> retlist = FXCollections.observableArrayList();
	    LocalDate current_time = LocalDate.now();
	    if (notCompleted.isSelected()==true)
	    {
	    	new_list = NotCompleted();
	        for (int x = 0; x < new_list.size(); x++) 
	        {
	        		if (new_list.get(x).getDate().getYear()==current_time.getYear() &new_list.get(x).getDate().getMonth()==current_time.getMonth() &new_list.get(x).getDate().getDayOfMonth()==current_time.getDayOfMonth() )
	        		{
	        			retlist.add(new_list.get(x));
	        		}         
	        }
	    }
	    else if(notCompleted.isSelected()==false)
	    {
	        for (int x = 0; x < task_list.size(); x++) 
	        {
	        		if (task_list.get(x).getDate().getYear()==current_time.getYear() &task_list.get(x).getDate().getMonth()==current_time.getMonth() &task_list.get(x).getDate().getDayOfMonth()==current_time.getDayOfMonth() )
	        		{
	        			retlist.add(task_list.get(x));
	        		}         
	        }
	    }

	    return retlist;
	}

	public ObservableList<Task> AllPressed()
	{
	    ObservableList<Task> new_list = FXCollections.observableArrayList();
	    ObservableList<Task> retlist = FXCollections.observableArrayList();
	    if (notCompleted.isSelected()==true)
	    {
	    	
	    	new_list = NotCompleted();
	        for (int x = 0; x < new_list.size(); x++) 
	        {
        			retlist.add(new_list.get(x));    
	        }
	    }
	    else if(notCompleted.isSelected()==false)
	    {
	        for (int x = 0; x < task_list.size(); x++) 
	        {
        			retlist.add(task_list.get(x)); 
	        }
	    }

	    return retlist;
    }
	
	
	public ObservableList<Task> NotCompleted()
	{
		ObservableList<Task> new_list = FXCollections.observableArrayList();
        for (int x = 0; x < task_list.size(); x++) 
        {
        		if (task_list.get(x).getComplete()!=100)     			
        		{
        			new_list.add(task_list.get(x));
        		}        
        }
	    return new_list;
    }





	public static void main(String[] args) {
		launch(args);
	}
}
