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
  private LinkedList<Integer> orderTimeline2=new LinkedList<Integer>();
  private LinkedList<Object[]> earningTimeline2=new LinkedList<Object[]>();
  private LinkedList<Object[]> earningTimeline3=new LinkedList<Object[]>();
  private LinkedList<String> orderList2=new LinkedList<String>();
  private LinkedList<Object[]> earningList2=new LinkedList<Object[]>();
  private LinkedList<Object[]> earningList3=new LinkedList<Object[]>();
  DecimalFormat formatter = new DecimalFormat("#,##0");
  public Order(String today) {
	  this.today=today;
	  
  }
public String getNumberOfOrders(){
	  return formatter.format(orderList.size());
  }
public double getEarnings() {
	  double amount=0;
	  for(Object i:this.earningList) {
		 
		  amount += (Double) ((Object[]) i)[0];
	  }
	  return amount;
}
public double getEarnings2() {
	  double amount=0;
	  for(Object i:this.earningList2) {
		 
		  amount += (Double) ((Object[]) i)[0];
	  }
	  return amount;
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
	LocalDate lastMonth = LocalDate.now().minusMonths(1);
	this.today = lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue());
	getOrder();
	LocalDate lastYear = LocalDate.now().minusYears(1);
	this.today = lastYear.getYear() + "-" + String.format("%02d", lastYear.getMonthValue());
	getOrder();
	return this.orderTimeline;
}


public LinkedList<Integer> getOrderTimeline2() {
	for(int i=0; i<32; i++) {
		  
			  orderTimeline2.add(0);
		  
	  }
	LocalDate lastMonth = LocalDate.now().minusMonths(1);
	String date = lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue());
	for(int i=0; i<32; i++) {
		for(String j:orderList2) {
			if(j.contains(date+"-"+String.format("%02d", i))) {
				
				orderTimeline2.set(i, orderTimeline2.get(i)+1);
			}
		}
	}
	return this.orderTimeline2;
}

public LinkedList<Object[]> getEarningTimeline() {
	for(int i=0; i<32; i++) {
		  earningTimeline.add(new Object[]{0.0,""});
	  }
	String date= LocalDate.now().getYear()+"-"+String.format("%02d", LocalDate.now().getMonthValue());
	for(int i=0; i<32; i++) {
		for(Object[] j:earningList) {
			String orderDate=(String) j[1];
			
			if(orderDate.contains(date+"-"+String.format("%02d",i))) {
				double amount=(Double) j[0];
				earningTimeline.set(i, new Object[] {(Double) earningTimeline.get(i)[0]+amount,(String) j[1]});
			}
		}
	}
	return this.earningTimeline;
}
public LinkedList<Object[]> getEarningTimeline2() {
	for(int i=0; i<32; i++) {
		  earningTimeline2.add(new Object[]{0.0,""});
	  }
	LocalDate lastMonth = LocalDate.now().minusMonths(1);
	String date = lastMonth.getYear() + "-" + String.format("%02d", lastMonth.getMonthValue());
	for(int i=0; i<32; i++) {
		for(Object[] j:earningList2) {
			String orderDate=(String) j[1];
			
			if(orderDate.contains(date+"-"+String.format("%02d",i))) {
				double amount=(Double) j[0];
				earningTimeline2.set(i, new Object[] {(Double) earningTimeline2.get(i)[0]+amount,(String) j[1]});
			}
		}
	}
	
	return this.earningTimeline2;
}
public LinkedList<Object[]> getEarningTimeline3() {
	for(int i=0; i<32; i++) {
		  earningTimeline3.add(new Object[]{0.0,""});
	  }
	LocalDate now = LocalDate.now();
	for(int i=0; i<32; i++) {
		for(Object[] j:earningList3) {
			String orderDate=(String) j[1];
			
			if(orderDate.contains(this.today+"-"+String.format("%02d",i))) {
				double amount=(Double) j[0];
				earningTimeline3.set(i, new Object[] {(Double) earningTimeline3.get(i)[0]+amount,(String) j[1]});
			}
		}
	}
	
	return this.earningTimeline3;
}
int rep=0;
public void getOrder() {
	   
	   
	   try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
	        String query="SELECT * FROM order_table WHERE order_date LIKE ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setString(1, this.today + "%");
	        ResultSet res=statement.executeQuery();
	       
	        while (res.next()) {
	        	if(this.rep==0) {
	        		orderList.add(res.getString("order_date"));
		        	earningList.add(new Object[]{res.getDouble("order_total"),res.getString("order_date")});
		        	
	        	}else if (this.rep==1){
	        		orderList2.add(res.getString("order_date"));
		        	earningList2.add(new Object[]{res.getDouble("order_total"),res.getString("order_date")});
	        	}else {
	        		earningList3.add(new Object[]{res.getDouble("order_total"),res.getString("order_date")});
	        	}
	        }
	        res.close();
	        statement.close();
	        connection.close();
	        rep++;
	    } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
}

}
