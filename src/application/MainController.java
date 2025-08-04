package application;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainController implements Initializable{
	@FXML 
	private Button btnAddFunction,btnProdSave,btnProdCancel,btnAddCahier,btnCancelCashier,btnErrMsg,btnCreditorSave;
	@FXML
	private ScrollPane scrollPaneProduct,scrollPaneCashier,scrollPaneCreditor,scrollPaneAnalytics;
	@FXML
	private ImageView btnAddProdX,btnAddCashierX,btnAddCreditorX;
	@FXML
	private StackPane addProdPane,addCashierPane,addCreditorPane,errMsg;
    @FXML
    private TableView<Product> prodTableView;
    @FXML
    private TableView<Cashier> cashierTableView;
    @FXML
    TableView<Creditor> creditorTableView;
    @FXML 
    private TableColumn<Product, String> nameColumn,supplierColumn,dopColumn,descriptionColumn;
    @FXML 
    private TableColumn<Product, Double>costColumn,priceColumn;
    @FXML 
    private TableColumn<Product, Integer>stocksColumn,salesColumn;
    @FXML
    private TableColumn<Cashier, Integer> genderColumn,salesCashierColumn;
    @FXML 
    private TableColumn<Cashier, String> fnColumn, lnColumn,contactNumberCashierColumn,addressCashierColumn,unameColumn;
	@FXML 
	private TextField txtProdName,txtProdCost,txtProdPrice,txtProdStocks,txtProdSupplier,txtCashierFn,txtCashierLn,txtCashierAddress,txtCashierContactNo,txtCashierUname,txtCreditorFn,txtCreditorLn,txtCreditorContactNumber,txtCreditorAddress;
	@FXML 
	private TableColumn<Creditor, String> fnColumnCreditor,lnColumnCreditor,contactNumberCreditorColumn,addressCreditorColumn;
	@FXML
	private TableColumn<Creditor, Integer> genderColumnCreditor;
	@FXML
	private ComboBox cbboxCashierGender,cbboxCreditorGender;
	@FXML
	private PasswordField txtCashierPw, txtCashierPw2;
	@FXML
	private DatePicker txtProdDateOfPurchase;
	@FXML
	private TextArea txtProdDescription;
	@FXML
	private Label txtSaveMsg,lblOrderNumber,lblEarnings,lbldailyreport,lblMonthlyOrders,lblMonthlyEarnings,lblEarningInsight;
	@FXML
	private HBox btnProducts,btnCashiers,btnCreditors,btnAnalytics;
	@FXML
	private AreaChart areaChartMonthlyOrders,areaChartMonthlyEarning;
	@FXML
	private LineChart lineChartLastYear;
	LinkedList<ScrollPane> scrollPaneLinkedList=new LinkedList<ScrollPane>();
	
	LinkedList<String> btnLabelLinkedList=new LinkedList<String>();
	String gender[] ={ "Male", "Female" };
	int windowIndex=0;
	private String today;
	Timeline timeline;
	boolean isDailyReport=true;
	Order order;
	DecimalFormat formatter = new DecimalFormat("#,##0");
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		     
		     nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		     costColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("cost"));
		     priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		     stocksColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stocks"));
		     salesColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("salesHBox"));
		     supplierColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("supplier"));
		     dopColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("dop"));
		     descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
		     
		     fnColumn.setCellValueFactory(new PropertyValueFactory<Cashier, String>("fn"));
		     lnColumn.setCellValueFactory(new PropertyValueFactory<Cashier, String>("ln"));
		     addressCashierColumn.setCellValueFactory(new PropertyValueFactory<Cashier, String>("address"));
		     contactNumberCashierColumn.setCellValueFactory(new PropertyValueFactory<Cashier, String>("contactNumber"));
		     unameColumn.setCellValueFactory(new PropertyValueFactory<Cashier, String>("uname"));
		     salesCashierColumn.setCellValueFactory(new PropertyValueFactory<Cashier, Integer>("salesHBox"));
		     genderColumn.setCellValueFactory(new PropertyValueFactory<Cashier, Integer>("gender"));
		     
		     fnColumnCreditor.setCellValueFactory(new PropertyValueFactory<Creditor, String>("fn"));
		     lnColumnCreditor.setCellValueFactory(new PropertyValueFactory<Creditor, String>("ln"));
		     addressCreditorColumn.setCellValueFactory(new PropertyValueFactory<Creditor, String>("address"));
		     contactNumberCreditorColumn.setCellValueFactory(new PropertyValueFactory<Creditor, String>("contactNumber"));
		     genderColumnCreditor.setCellValueFactory(new PropertyValueFactory<Creditor, Integer>("gender"));
            // below two lines are used for connectivity.
		    fetchAllProductData();
		    this.today= String.valueOf(LocalDate.now());
		    startTimerUpdate();
		    
		    
		    List<ScrollPane> scrollPaneList = Arrays.asList(scrollPaneProduct, scrollPaneCashier,scrollPaneCreditor,scrollPaneAnalytics);
		    scrollPaneLinkedList.addAll(scrollPaneList);
		    List<String> btnLabelList = Arrays.asList("Add Product", "Add Cashier","Add Creditor");
		    btnLabelLinkedList.addAll(btnLabelList);
		    btnProducts.setOnMouseClicked((e)->{
		    	setToDaily();
		    	timeline.stop();
		    	this.today= String.valueOf(LocalDate.now());
		    	fetchAllProductData();
		    	startTimerUpdate();
            	viewScrollPane(0);
            });
		    btnAnalytics.setOnMouseClicked((e)->{
		    	this.isDailyReport=false;
		    	lbldailyreport.setText("Monthly Report");
		    	timeline.stop();
		    	this.today= LocalDate.now().getYear()+"-"+String.format("%02d", LocalDate.now().getMonthValue());
		    	startTimerUpdate();
		    	viewScrollPane(3);
		    });
		    btnCashiers.setOnMouseClicked((e)->{
		    	setToDaily();
		    	timeline.stop();
		    	this.today= String.valueOf(LocalDate.now());
		    	fetchAllCashierData();
		    	startTimerUpdate();
            	viewScrollPane(1);
            });
		    btnCreditors.setOnMouseClicked((e)->{
		    	
		    	fetchAllCreditorData();
		    	viewScrollPane(2);
		    });
		    btnAddFunction.setOnAction((e)->{
		    	if(windowIndex==0) {
    				addProdPane.setVisible(true);
    			}else if(windowIndex==1) {
    				addCashierPane.setVisible(true);
    			}else if(windowIndex==2) {
    				addCreditorPane.setVisible(true);
    			}
    			//showAddProductWindow();
    		});
            txtProdCost.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                	txtProdCost.setText(oldValue);
                }
            });
            txtProdPrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                	txtProdPrice.setText(oldValue);
                }
            });
            txtProdStocks.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                	txtProdStocks.setText(oldValue);
                }
            });
            btnProdSave.setOnAction((e)->{
            	if(txtProdName.getText().equals("")) {
            		txtProdName.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtProdCost.getText().equals("")) {
            		txtProdCost.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtProdPrice.getText().equals("")) {
            		txtProdPrice.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtProdStocks.getText().equals("")) {
            		txtProdStocks.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}
				else if(txtProdSupplier.getText().equals("")) {
					txtProdSupplier.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtProdDateOfPurchase.getValue()==null) {
            		txtProdDateOfPurchase.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else {
            		addProductToDB();
            		txtProdName.setText("");
            		txtProdCost.setText("");
            		txtProdPrice.setText("");
            		txtProdStocks.setText("");
            		txtProdSupplier.setText("");
            		txtProdDateOfPurchase.setValue(null);
            		txtProdDescription.setText("");
            		txtSaveMsg.setVisible(true);
                	fetchAllProductData();
            	}
            	
            });
            
            cbboxCreditorGender.setItems(FXCollections.observableArrayList(gender));
            btnCreditorSave.setOnAction((e)->{
            	if(txtCreditorFn.getText().equals("")) {
            		txtCreditorFn.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCreditorLn.getText().equals("")) {
            		txtCreditorLn.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(cbboxCreditorGender.getValue()==null) {
            		cbboxCreditorGender.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCreditorAddress.getText().equals("")) {
            		txtCreditorAddress.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}
				else if(txtCreditorContactNumber.getText().equals("")) {
					txtCreditorContactNumber.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}
            	else {
            		addCreditorToDB();
            		
            		txtCreditorFn.setText("");
            		txtCreditorLn.setText("");
            		cbboxCreditorGender.setValue(null);
            		txtCreditorContactNumber.setText("");
            		txtCreditorAddress.setText("");
            		fetchAllCreditorData();
            		 
            	}
            	
            });
            
           cbboxCashierGender.setItems(FXCollections.observableArrayList(gender));
            btnErrMsg.setOnAction((e)->{
            	errMsg.setVisible(false);
            });
            btnAddCahier.setOnAction((e)->{
            	if(txtCashierFn.getText().equals("")) {
            		txtCashierFn.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCashierLn.getText().equals("")) {
            		txtCashierLn.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(cbboxCashierGender.getValue()==null) {
            		cbboxCashierGender.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCashierAddress.getText().equals("")) {
            		txtCashierAddress.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}
				else if(txtCashierContactNo.getText().equals("")) {
					txtCashierContactNo.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCashierUname.getText().equals("")) {
            		txtCashierUname.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(txtCashierPw.getText().equals("")) {
            		txtCashierPw.setStyle("-fx-border-color:  #fe3d3c; -fx-border-radius: 5; ");
            	}else if(!txtCashierPw.getText().equals(txtCashierPw2.getText())) {
            		errMsg.setVisible(true);
            	}
            	else {
            		addCashierToDB();
            		
            		txtCashierFn.setText("");
            		txtCashierLn.setText("");
            		cbboxCashierGender.setValue(null);;
            		txtCashierContactNo.setText("");
            		txtCashierUname.setText("");
            		txtCashierPw.setText("");
            		txtCashierPw2.setText("");
            		txtCashierAddress.setText("");
            		fetchAllCashierData();
            	}
            	
            });
        
            btnAddProdX.setOnMouseClicked((e)->{
            	closeAddProdPane();
    		});
            btnProdCancel.setOnMouseClicked((e)->{
            	closeAddProdPane();
    		});
            btnAddCashierX.setOnMouseClicked((e)->{
    			addCashierPane.setVisible(false);
    		});
            btnAddCreditorX.setOnMouseClicked((e)->{
    			addCreditorPane.setVisible(false);
            });
            
	}
	public void setToDaily() {
		lbldailyreport.setText("Daily Report");
		this.isDailyReport=true;
	}
	public void startTimerUpdate() {
	    order=new Order(this.today);
	    order.getOrder();
	    reflectAnalytics();
	    setChart();
		timeline = new Timeline(new KeyFrame(
	    		Duration.minutes(10),
	    	    e -> {
	    	        Task<Void> task = new Task<>() {
	    	            @Override
	    	            protected Void call() throws Exception {
	    	            	order=new Order(today);
	    	                order.getOrder();
	    	                
	    	                return null;
	    	            }

	    	            @Override
	    	            protected void succeeded() {
	    	                // Safe to update UI here
	    	            	reflectAnalytics();
	    	        	    prodTableView.refresh();
	    	                cashierTableView.refresh();
	    	                
	    	                setChart();
	    	                
	    	            }
	    	        };
	    	        new Thread(task).start();
	    	    }));
	    	timeline.setCycleCount(Timeline.INDEFINITE);
	    	timeline.play();
	}
	 void reflectAnalytics() {
		lblOrderNumber.setText(order.getNumberOfOrders());
    	lblMonthlyOrders.setText(order.getNumberOfOrders());
        lblMonthlyEarnings.setText("P "+formatter.format(order.getEarnings()));
	    lblEarnings.setText("P "+formatter.format(order.getEarnings()));
	    
	    
        
	}
	void setChart() {
		if(isDailyReport==false) {
			areaChartMonthlyOrders.getData().clear();
			areaChartMonthlyEarning.getData().clear();
			lineChartLastYear.getData().clear();

			LinkedList<Integer> timeline = order.getOrderTimeline();
			LinkedList<Integer> timeline2 = order.getOrderTimeline2();
			LinkedList<Object[]> earningTimeline = order.getEarningTimeline();
			LinkedList<Object[]> earningTimeline2 = order.getEarningTimeline2();
			LinkedList<Object[]> earningTimeline3 = order.getEarningTimeline3();
        
			XYChart.Series<Number, Number> orderSeries = new XYChart.Series<>();
			orderSeries.setName("This Month");
			XYChart.Series<Number, Number> orderSeries2 = new XYChart.Series<>();
			orderSeries2.setName("Last Month");
			XYChart.Series<Number, Number> earningSeries = new XYChart.Series<>();
			earningSeries.setName("This Month");
			XYChart.Series<Number, Number> earningSeries2 = new XYChart.Series<>();
			earningSeries2.setName("Last Month");
			XYChart.Series<Number, Number> earningSeries3 = new XYChart.Series<>();
			earningSeries3.setName("The same month last year");
			for (int j = 0; j < 32; j++) {
			    XYChart.Data<Number, Number> orderData = new XYChart.Data<>(j, timeline.get(j));
			    XYChart.Data<Number, Number> orderData2 = new XYChart.Data<>(j, timeline2.get(j));
			    XYChart.Data<Number, Number> earningData = new XYChart.Data<>(j, (Number) earningTimeline.get(j)[0]);
			    XYChart.Data<Number, Number> earningData2 = new XYChart.Data<>(j, (Number) earningTimeline2.get(j)[0]);
			    XYChart.Data<Number, Number> earningData3 = new XYChart.Data<>(j, (Number) earningTimeline3.get(j)[0]);
			    
			    orderSeries.getData().add(orderData);
			    orderSeries2.getData().add(orderData2);
			    earningSeries.getData().add(earningData);
			    earningSeries2.getData().add(earningData2);
			    earningSeries3.getData().add(earningData3);
			}

			areaChartMonthlyEarning.setLegendVisible(true);
			areaChartMonthlyOrders.setLegendVisible(true);
			XYChart.Series<Number, Number> earningSeriesClone =cloneSeries(earningSeries);
			areaChartMonthlyEarning.getData().addAll(earningSeries2,earningSeries);
			areaChartMonthlyOrders.getData().addAll(orderSeries2,orderSeries);
			lineChartLastYear.getData().addAll(earningSeries3,earningSeriesClone);
			
			setOrderNode(orderSeries,"This month");
			setOrderNode(orderSeries2,"Last month");
			double earnings=order.getEarnings();
			double earnings2=order.getEarnings2();
			setEarningNode(earningSeries,"This month");
			
			setEarningNode(earningSeries2,"Last month");
			setEarningNode(earningSeries3,"The same month last year");
			setEarningNode(earningSeriesClone,"This months");
			lblEarningInsight.setText("Earnings "+((earnings2>earnings)?"dropped":"increased") + " by "+String.format("%.2f", (earnings-earnings2)/earnings2*100)+"% this month compared to last month.");
			
            
        }
	}
	public  XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> original) {
	    XYChart.Series<Number, Number> clone = new XYChart.Series<>();
	    clone.setName(original.getName()); 

	    for (XYChart.Data<Number, Number> data : original.getData()) {
	        clone.getData().add(new XYChart.Data<>(data.getXValue(), data.getYValue()));
	    }

	    return clone;
	}
	public void setEarningNode(XYChart.Series<Number, Number> series, String month) {
		for (XYChart.Data<Number, Number> data : series.getData()) {
		    Node node = data.getNode();
		    node.setStyle("-fx-background-color:rgba(0,0,0,0); -fx-background-radius: 4px; -fx-padding: 10px;");
		    if (node != null) {
		    	int day = data.getXValue().intValue();
	            Number earnings = data.getYValue();
	            Tooltip tooltip =new Tooltip(month + "\nDay: " + day + "\nEarnings: ₱" + earnings);
	            Tooltip.install(node,tooltip);
	            tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px;");
		        node.setOnMouseClicked(event -> {
		        	 tooltip.show(node, event.getScreenX(), event.getScreenY()); 
			            PauseTransition delay = new PauseTransition(Duration.seconds(2));
			            delay.setOnFinished(e -> tooltip.hide());
			            delay.play();
		        });
		    }
		}
	}
	public void setOrderNode(XYChart.Series<Number, Number> series, String month) {
		for (XYChart.Data<Number, Number> data : series.getData()) {
		    Node node = data.getNode();
		    node.setStyle("-fx-background-color:rgba(0,0,0,0); -fx-background-radius: 4px; -fx-padding: 10px;");
		    if (node != null) {
		    	int day = data.getXValue().intValue();
	            int orders = data.getYValue().intValue();
	            
	            Tooltip tooltip = new Tooltip(month + "\nDay: " + day + "\nOrders: " + orders);
	            Tooltip.install(node, tooltip);
	            tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px;");
		        node.setOnMouseClicked(event -> {
		            
		            tooltip.show(node, event.getScreenX(), event.getScreenY()); 
		            PauseTransition delay = new PauseTransition(Duration.seconds(2));
		            delay.setOnFinished(e -> tooltip.hide());
		            delay.play();
		        });
		    }
		}
	}
	void viewScrollPane(int i) {
		for(int j=0;j<scrollPaneLinkedList.size();j++) {
			if(j==i) {
				scrollPaneLinkedList.get(j).setVisible(true);
			}else {
				scrollPaneLinkedList.get(j).setVisible(false);
			}
			if(i<3) {
				btnAddFunction.setVisible(true);
				btnAddFunction.setText("+ "+btnLabelLinkedList.get(i));
			}else {
				btnAddFunction.setVisible(false);
			}
			this.windowIndex=i;
		}
	}
	void closeAddProdPane() {
		addProdPane.setVisible(false);
		txtSaveMsg.setVisible(false);
	}
	void fetchAllProductData() {
		prodTableView.getItems().clear();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            Statement statement;
            statement = connection.createStatement();
            ResultSet res= statement.executeQuery("SELECT * FROM `product`");
            //statement.executeUpdate("INSERT INTO `product`( `name`, `cost`, `price`, `stocks`, `sales`, `supplier`, `date_of_purchase`, `description`) VALUES ('cement','12','22','100','76','C Manufaturing','6/16/2025','n/a')");
            while (res.next()) {
            	int id=res.getInt("product_id");
            	String name=res.getString("name");
            	double cost=res.getDouble("cost");
            	double price=res.getDouble("price");
            	int stocks=res.getInt("stocks");
            	int initialStocks=res.getInt("initial_stocks");
            	String supplier = res.getString("supplier");
            	String dop = res.getString("date_of_purchase");
            	String description = res.getString("description");
            	Product p=new Product(id,name,cost,price,initialStocks,supplier,dop,description);
                ObservableList<Product> productlist=prodTableView.getItems();
                productlist.add(p);
                prodTableView.setItems(productlist);
                }
            res.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void fetchAllCashierData() {
		cashierTableView.getItems().clear();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            Statement statement;
            statement = connection.createStatement();
            ResultSet res= statement.executeQuery("SELECT * FROM `cashier`");
            while (res.next()) {
            	int id=res.getInt("cashier_id");
            	String fn=res.getString("firstName");
            	String ln=res.getString("lastName");
            	int gender=res.getInt("gender");
            	String address=res.getString("address");
            	String contactNumber = res.getString("contact_number");
            	String uname = res.getString("uname");
            	Cashier c=new Cashier(id,fn,ln,gender,address,contactNumber,uname);
                ObservableList<Cashier> cashierlist=cashierTableView.getItems();
                cashierlist.add(c);
                cashierTableView.setItems(cashierlist);
                }
            res.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void fetchAllCreditorData() {
		creditorTableView.getItems().clear();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            Statement statement;
            statement = connection.createStatement();
            ResultSet res= statement.executeQuery("SELECT * FROM `creditor`");
            while (res.next()) {
            	int id=res.getInt("creditor_id");
            	String fn=res.getString("fn");
            	String ln=res.getString("ln");
            	int gender=res.getInt("gender");
            	String address=res.getString("address");
            	String contactNumber = res.getString("contact_number");
            	Creditor creditor=new Creditor(id,fn,ln,gender,address,contactNumber);
                ObservableList<Creditor> creditorlist=creditorTableView.getItems();
                creditorlist.add(creditor);
                creditorTableView.setItems(creditorlist);
                }
            res.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void addCashierToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("connected ");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            
            String query = "INSERT INTO `cashier`(`firstName`, `lastName`, `gender`, `address`, `contact_number`, `uname`, `pword`) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtCashierFn.getText());    
            stmt.setString(2, txtCashierLn.getText());  
            SingleSelectionModel genderSelectionModel = cbboxCashierGender.getSelectionModel();
            int indexGender = genderSelectionModel.getSelectedIndex();
            stmt.setInt(3, indexGender); 
            stmt.setString(4, txtCashierAddress.getText());   
            stmt.setString(5, txtCashierContactNo.getText());     
            stmt.setString(6, txtCashierUname.getText()); 
            String password = txtCashierPw.getText();
            String[] hashedPasswordData = hashPassword(password);
            stmt.setString(7, hashedPasswordData[0]);                    

            stmt.executeUpdate();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void addCreditorToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("connected ");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            
            String query = "INSERT INTO `creditor`( `fn`, `ln`, `gender`, `contact_number`, `address`) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtCreditorFn.getText());    
            stmt.setString(2, txtCreditorLn.getText());  
            SingleSelectionModel genderSelectionModel = cbboxCreditorGender.getSelectionModel();
            int indexGender = genderSelectionModel.getSelectedIndex();
            stmt.setInt(3, indexGender); 
            stmt.setString(4, txtCreditorContactNumber.getText()); 
            stmt.setString(5, txtCreditorAddress.getText());   
            stmt.executeUpdate();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void addProductToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("connected ");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            
            String query = "INSERT INTO `product` (`name`, `cost`, `price`,`initial_stocks`, `stocks`, `supplier`, `date_of_purchase`, `description`) " + "VALUES (?, ?,  ?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtProdName.getText());    
            stmt.setDouble(2, Double.parseDouble(txtProdCost.getText()));   
            stmt.setDouble(3, Double.parseDouble(txtProdPrice.getText()));  
            stmt.setInt(4, Integer.parseInt(txtProdStocks.getText()));  
            stmt.setInt(5, Integer.parseInt(txtProdStocks.getText()));  
            stmt.setString(6, txtProdSupplier.getText());     
            LocalDate selectedDate = txtProdDateOfPurchase.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedDate = selectedDate.format(formatter);
            stmt.setString(7, formattedDate);             
            stmt.setString(8, txtProdDescription.getText());                     

            stmt.executeUpdate();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void showAddProductWindow() {
		try {
			FXMLLoader loader= new FXMLLoader(getClass().getResource("addCreditor.fxml"));
			Parent root=loader.load();
			Stage newStage = new Stage();
            newStage.setTitle("New Window");
            newStage.setResizable(false);
            newStage.setScene(new Scene(root));
            newStage.show();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	private static String[] hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = generateSalt();

            // Define the number of iterations and key length
            int iterations = 10000;
            int keyLength = 256;

            // Create PBKDF2 spec
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Generate the hash
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // Encode salt and hash to Base64 for storage
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            return new String[]{hashBase64, saltBase64};  // Return both hash and salt

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	private static byte[] generateSalt() {
        try {
            SecureRandom sr = new SecureRandom();
            byte[] salt = new byte[16]; // 16-byte salt
            sr.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
