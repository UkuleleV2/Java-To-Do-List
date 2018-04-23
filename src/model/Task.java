package model;
import java.time.LocalDate;

import javafx.scene.control.CheckBox;
public class Task 
{
    String due_date;
    String title;
    int complete;
    String description;
    LocalDate date;
    CheckBox check;

    public Task()
    {
        this.due_date = "No info";// new String(date_arg);
        this.title = "No info";
        this.complete = 0;
        this.description = "No info";
        this.date = LocalDate.of(1,1,1);
        this.check = new CheckBox();
        this.check.setDisable(false);
        
    }
    //fx:controller="controller.AddEdit"
    public Task(String date_arg, String title_arg, int complete_arg, String description_arg) 
    {
    	String year,month,day;
        this.due_date = date_arg;// new String(date_arg);
        this.title = title_arg;
        this.complete = complete_arg;
        this.description = description_arg;
        year = date_arg.substring(0,4);
        month = date_arg.substring(5,7);
        day = date_arg.substring(8);   
  //      this.date = new Date(Integer.parseInt(year) - 1900,Integer.parseInt(month)-1,Integer.parseInt(day));
        this.date = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        this.check = new CheckBox();
        this.check.setDisable(true);
        if (complete_arg == 100)
        	this.check.setSelected(true);
        else
        	this.check.setSelected(false);
    }
    public void ModifyTask(String date_arg, String title_arg, int complete_arg, String description_arg)
    {
    	String year,month,day;
        this.due_date = date_arg;// new String(date_arg);
        this.title = title_arg;
        this.complete = complete_arg;
        this.description = description_arg;
        year = date_arg.substring(0,4);
        month = date_arg.substring(5,7);
        day = date_arg.substring(8);   
        this.date = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        this.check = new CheckBox();
        this.check.setDisable(true);
        if (complete_arg == 100)
        	this.check.setSelected(true);
        else
        	this.check.setSelected(false);
    }
    public LocalDate getDate() {
    	return this.date;
    }
    public void setDate(LocalDate date)
    {
    	this.date = date;
    }
    public String getDue_date() {
        return this.due_date;
    }
    public CheckBox getCheck() {
    	return this.check;
    }

    public void setDue_date(String date_arg) {
    	this.due_date = date_arg;
    }

    public String getTitle() {
        return this.title;
    }
    public void setCheck(CheckBox check) {
        this.check = check;
    }
    public void setTitle(String title_arg) {
        this.title = title_arg;
    }

    public int getComplete() {
        return this.complete;
    }

    public void setComplete(int complete_arg) {
        this.complete = complete_arg;
    }
    public String getCompleteString()
    {
    	String temp = Integer.toString(this.complete);
    	return temp;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description_arg) {
        this.description = description_arg;
    }
}
