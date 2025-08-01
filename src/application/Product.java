package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Product {
	private int id;
	private String name;
	private Double cost;
	private Double price;
	private Integer stocks=0;
	private Integer  initialStocks;
	private Integer sales=0;
	private Integer salesYesterday=0;
	private String supplier;
	private String dop;
	private String description;
	private String today;
	private String yesterday;
   public Product(int id, String name,double cost,double price,int initialStocks,String supplier,String dop,String description) {
	this.id=id;
	this.name=name;
	this.cost=cost;
	this.price=price;
	this.initialStocks=initialStocks;
	this.supplier=supplier;
	this.dop=dop;
	this.description=description;
	
   }
   public void getDate() {
	   LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		this.today=String.valueOf(today);
		this.yesterday=String.valueOf(yesterday);
   }
   public HBox getName() {
	   return gettblData(this.name);
   }
   public int getSale(String date) {
	   int i=0;
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT * FROM order_table WHERE order_date LIKE ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1,  date + "%");
	        ResultSet res=statement.executeQuery();
	        //statement.executeUpdate("INSERT INTO `product`( `name`, `cost`, `price`, `stocks`, `sales`, `supplier`, `date_of_purchase`, `description`) VALUES ('cement','12','22','100','76','C Manufaturing','6/16/2025','n/a')");
	       
	        while (res.next()) {
	        	i+=getSold(res.getInt("order_id"));
	        }
	        res.close();
	        statement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return i;
   }
   public int getSold(int order_id) {
	   int i=0;
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT * FROM ordered_product WHERE order_id = ? AND product_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, order_id);
	        statement.setInt(2, this.id);
	        ResultSet res=statement.executeQuery();
	        while (res.next()) {
	        	i+=res.getDouble("qty");
	        }
	        res.close();
	        statement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return i;
   }
   public void getCurrentStocks() {
	  
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT `stocks` FROM product WHERE product_id = ? ";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, this.id);
	        ResultSet res=statement.executeQuery();
	        while(res.next()) {
	        	this.stocks=res.getInt("stocks");
	        }
	        res.close();
	        statement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
   }
   
   public HBox getCost() {
	   DecimalFormat formatter = new DecimalFormat("#,##0.00");
       String cost = formatter.format(this.cost);
	   return gettblData(cost);
   }
   public HBox getStocks() {
	   getDate();
	   getCurrentStocks();
	   HBox stocks=new HBox();
	   Label stockslbl=new Label(""+this.stocks);
	   Label initialstockslbl=new Label(""+this.initialStocks);
	   initialstockslbl.setTextFill(Color.GRAY);
	   stocks.getChildren().addAll(stockslbl,new Label(" / "),initialstockslbl);
	   stocks.setAlignment(Pos.CENTER);
	   return stocks;
   }
   public HBox getPrice() {
	   DecimalFormat formatter = new DecimalFormat("#,##0.00");
       String price = formatter.format(this.price);
	   return gettblData(price);
   }
   public HBox getSupplier() {
	   return gettblData(this.supplier);
   }
   public HBox getDop() {
	   
	   return gettblData(this.dop);
   }
   public HBox getSalesHBox() {
	   getDate();
	   this.sales=getSale(String.valueOf(today));
		this.salesYesterday=getSale(String.valueOf(yesterday));
	   HBox sales=dataHBox(""+this.sales,true,(this.sales>this.salesYesterday)?true:false);
	   return sales;
   }
   public HBox getDescription() {
	   return gettblData(this.description);
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
	   
	   Separator separator=new Separator();
	   separator.setOrientation(Orientation.HORIZONTAL);
	   separator.setVisible(false);
	   
	   HBox hbox=new HBox();
	   hbox.setPadding(new Insets(6)); 
	   HBox.setHgrow(separator, Priority.ALWAYS);
	   hbox.getChildren().addAll(lbl,separator);
	   if(hasGraph==true) {
		   HBox salesHbox=new HBox();
		   ImageView graph=new ImageView();
		   graph.setFitWidth(16);  // Set the width to 16 pixels
		   graph.setFitHeight(16);
		   Image image = new Image((upOrDown==true)?"up.png":"downtrend.png"); // Replace with your image file path or URL
		   graph.setImage(image);
		   Label saleCount=new Label(((this.salesYesterday<this.sales)?"+ ":"- ")+" "+Math.abs(this.sales-this.salesYesterday)+" ");
		   salesHbox.getChildren().addAll(saleCount,graph);
		   hbox.getChildren().add(salesHbox);
	   }
	   
	   return hbox;
   }
}
