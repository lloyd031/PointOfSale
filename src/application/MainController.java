package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainController implements Initializable{
	@FXML 
	private Button btnAddProduct,btnProdSave,btnProdCancel;
	@FXML
	private ImageView btnAddProdX,btnAddCashierX,btnAddCreditorX;
	@FXML
	private StackPane addProdPane,addCashierPane,addCreditorPane;
    @FXML
    private TableView<Product> prodTableView;
    @FXML 
    private TableColumn<Product, String> nameColumn;
    @FXML 
    private TableColumn<Product, Double>costColumn;
    @FXML 
    private TableColumn<Product, Double>priceColumn;
    @FXML 
    private TableColumn<Product, Integer>stocksColumn;
    @FXML 
    private TableColumn<Product, Integer>salesColumn;
    @FXML 
    private TableColumn<Product, String>supplierColumn;
    @FXML 
    private TableColumn<Product, String>dopColumn;
    @FXML 
    private TableColumn<Product, String>descriptionColumn;
	@FXML 
	private TextField txtProdName,txtProdCost,txtProdPrice,txtProdStocks,txtProdSupplier;
	@FXML
	private DatePicker txtProdDateOfPurchase;
	@FXML
	private TextArea txtProdDescription;
	@FXML
	private Label txtSaveMsg;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		     nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		     costColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("cost"));
		     priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		     stocksColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stocks"));
		     salesColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("sales"));
		     supplierColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("supplier"));
		     dopColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("dop"));
		     descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
            // below two lines are used for connectivity.
		    FetchAllData();
            
            btnAddProduct.setOnAction((e)->{
    			addProdPane.setVisible(true);
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
                	FetchAllData();
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
	void closeAddProdPane() {
		addProdPane.setVisible(false);
		txtSaveMsg.setVisible(false);
	}
	void FetchAllData() {
		prodTableView.getItems().clear();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            Statement statement;
            statement = connection.createStatement();
            ResultSet res= statement.executeQuery("SELECT * FROM `product`");
            //statement.executeUpdate("INSERT INTO `product`( `name`, `cost`, `price`, `stocks`, `sales`, `supplier`, `date_of_purchase`, `description`) VALUES ('cement','12','22','100','76','C Manufaturing','6/16/2025','n/a')");
            while (res.next()) {
            	String name=res.getString("name");
            	double cost=res.getDouble("cost");
            	double price=res.getDouble("price");
            	int stocks=res.getInt("stocks");
            	String supplier = res.getString("supplier");
            	String dop = res.getString("date_of_purchase");
            	String description = res.getString("description");
            	Product p=new Product(name,cost,price,stocks,supplier,dop,description);
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
	void addProductToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("connected ");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/point_of_sale_db","root","");
            
            String query = "INSERT INTO `product` (`name`, `cost`, `price`, `stocks`, `supplier`, `date_of_purchase`, `description`) " + "VALUES (?, ?,  ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtProdName.getText());    
            stmt.setDouble(2, Double.parseDouble(txtProdCost.getText()));   
            stmt.setDouble(3, Double.parseDouble(txtProdPrice.getText()));  
            stmt.setInt(4, Integer.parseInt(txtProdStocks.getText()));   
            stmt.setString(5, txtProdSupplier.getText());     
            LocalDate selectedDate = txtProdDateOfPurchase.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedDate = selectedDate.format(formatter);
            stmt.setString(6, formattedDate);             
            stmt.setString(7, txtProdDescription.getText());                     

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

}
