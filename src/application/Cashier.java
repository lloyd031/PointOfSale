package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Cashier {
	private int id;
	private String fn;
	private String ln;
	private int gender;
	private String address;
	private String contactNumber;
	private String uname;
	int sales=0;
	private Integer salesYesterday=0;
	private String today;
	private String yesterday;
   public Cashier(int id,String fn, String ln, int gender, String address, String contactNumber,String uname) {
	this.id=id;
	this.fn=fn;
	this.ln=ln;
	this.gender=gender;
	this.address=address;
	this.contactNumber=contactNumber;
	this.uname=uname;
   }
   public void getDate() {
	   LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		this.today=String.valueOf(today);
		this.yesterday=String.valueOf(yesterday);
		this.sales=getSale(String.valueOf(today));
		this.salesYesterday=getSale(String.valueOf(yesterday));
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
   public HBox  getUname() {
	   return gettblData(this.uname);
   }
   public HBox getSalesHBox() {
	   getDate();
	   HBox sales=dataHBox(""+this.sales,true,(this.sales>this.salesYesterday)?true:false);
	   return sales;
   }
   public HBox gettblData(String x) {
	   HBox hbox=new HBox();
	   Label lbl=new Label(x);
	   hbox.getChildren().add(lbl);
	   hbox.setAlignment(Pos.CENTER);
	   return hbox;
   }
   public int getSale(String date) {
	   int i=0;
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT * FROM order_table WHERE order_date LIKE ? AND cashier_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1,  date + "%");
	        statement.setInt(2, 10);
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
	        String query="SELECT * FROM ordered_product WHERE order_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, order_id);
	        ResultSet res=statement.executeQuery();
	        //statement.executeUpdate("INSERT INTO `product`( `name`, `cost`, `price`, `stocks`, `sales`, `supplier`, `date_of_purchase`, `description`) VALUES ('cement','12','22','100','76','C Manufaturing','6/16/2025','n/a')");
	        
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
