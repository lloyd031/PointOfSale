package application;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Product {
	String name;
	Double cost;
	Double price;
	Integer stocks;
	Integer sales;
	String supplier;
	String dop;
	String description;
   public Product(String name,double cost,double price,int stocks,String supplier,String dop,String description) {
	this.name=name;
	this.cost=cost;
	this.price=price;
	this.stocks=stocks;
	this.supplier=supplier;
	this.dop=dop;
	this.description=description;
	this.sales=0;
   }
   public String getName() {
	   
	   return this.name;
   }
   public double getCost() {
	   return this.cost;
   }
   public int getStocks() {
	   return this.stocks;
   }
   public double getPrice() {
	   return this.price;
   }
   public String getSupplier() {
	   return this.supplier;
   }
   public String getDop() {
	   return this.dop;
   }
   public HBox getSales() {
	   HBox sales=dataHBox(""+this.sales,true,true);
	   return sales;
   }
   public String getDescription() {
	   return this.description;
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
