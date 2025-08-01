package application;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Creditor {
	int id;
	String fn;
	String ln;
	int gender;
	String address;
	String contactNumber;
   public Creditor(int id,String fn, String ln, int gender, String address, String contactNumber) {
	this.id=id;
	this.fn=fn;
	this.ln=ln;
	this.gender=gender;
	this.address=address;
	this.contactNumber=contactNumber;
   }
   public HBox getFn() {
	   
	   return gettblData(this.fn);
   }
   public HBox getLn() {
	   return gettblData(this.ln);
   }
   public HBox getGender() {
	   return gettblData((this.gender==0)?"Male":"Female");
   }
   public HBox getAddress() {
	   return gettblData(this.address);
   }
   public HBox getContactNumber() {
	   return gettblData(this.contactNumber);
   }
   public HBox gettblData(String x) {
	   HBox hbox=new HBox();
	   Label lbl=new Label(x);
	   hbox.getChildren().add(lbl);
	   hbox.setAlignment(Pos.CENTER);
	   return hbox;
   }
  
   
   public HBox dataHBox(String record,boolean hasGraph, boolean upOrDown) {
	   Label lbl=new Label(record);
	   lbl.setStyle("-fx-text-fill: #000000d6; -fx-font-size: 14px;");
	   Separator separator=new Separator();
	   separator.setOrientation(Orientation.HORIZONTAL);
	   separator.setVisible(false);
	   
	   HBox hbox=new HBox();
	   hbox.setPadding(new Insets(6)); 
	   HBox.setHgrow(separator, Priority.ALWAYS);
	   hbox.getChildren().addAll(lbl,separator);
	  if(hasGraph==true) {
		   ImageView graph=new ImageView();
		   graph.setFitWidth(16);  // Set the width to 16 pixels
		   graph.setFitHeight(16);
		   Image image = new Image((upOrDown==true)?"\\resources\\up.png":"\\resources\\downtrend.png"); // Replace with your image file path or URL
		   graph.setImage(image);
		   hbox.getChildren().add(graph);
	   }
	  
	   
	   return hbox;
   }
}
