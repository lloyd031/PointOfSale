package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class Order {
  private String numberOfOrders;
  private String earnings;
  private String today;
  private LinkedList<Integer> orderTimeline=new LinkedList<Integer>();
  private LinkedList<Object[]> earningTimeline=new LinkedList<Object[]>();
  private LinkedList<String> orderList=new LinkedList<String>();
  private LinkedList<Object[]> earningList=new LinkedList<Object[]>();
  DecimalFormat formatter = new DecimalFormat("#,##0");
  public Order(String today) {
	  this.today=today;
	  
  }
public String getNumberOfOrders(){
	  return formatter.format(orderList.size());
  }
public String getEarnings() {
	  double amount=0;
	  for(Object i:this.earningList) {
		 
		  amount += (Double) ((Object[]) i)[0];
	  }
	  return formatter.format(amount);
}
public LinkedList<Integer> getOrderTimeline() {
	for(int i=0; i<32; i++) {
		  orderTimeline.add(0);
	  }
	String date= LocalDate.now().getYear()+"-"+String.format("%02d", LocalDate.now().getMonthValue());
	for(int i=0; i<32; i++) {
		for(String j:orderList) {
			if(j.contains(date+"-"+String.format("%02d", i))) {
				
				orderTimeline.set(i, orderTimeline.get(i)+1);
			}
		}
	}
	return this.orderTimeline;
}
public LinkedList<Object[]> earningsTimeline() {
	for(int i=0; i<32; i++) {
		earningTimeline.add(new Object[] {0.0,""});
	  }
	String date= LocalDate.now().getYear()+"-"+String.format("%02d", LocalDate.now().getMonthValue());
	for (int i = 0; i < 32; i++) {
	    for (Object j : earningList) {
	        Object[] entry = (Object[]) j; 
	        String dateString = (String) entry[0]; 

	        if (dateString.contains(date + "-" + String.format("%02d", i))) {
	            orderTimeline.set(i, orderTimeline.get(i) + 1);
	        }
	    }
	}
	return this.earningTimeline;
}
public void getOrder(boolean a) {
	   
	  
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT * FROM order_table WHERE order_date LIKE ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, this.today + "%");
	        ResultSet res=statement.executeQuery();
	       
	        while (res.next()) {
	        	orderList.add(res.getString("order_date"));
	        	earningList.add(new Object[]{res.getDouble("order_total"),res.getString("order_date")});
	        }
	        res.close();
	        statement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
}

}
