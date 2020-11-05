package com.me.example.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.example.model.Product;
import com.me.example.model.ProductService;
import com.me.example.supplementary.EditingCell;
import com.me.example.supplementary.ProdBean;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/fxml/productDetails.fxml")
public class ProductDetailsController {

	final FileChooser fileChooser = new FileChooser();

	@FXML
	private Label id;
	@FXML
	private TextField name;
	@FXML
	private TextField price;
	@FXML
	private TextField instock;
	@FXML
	private TextArea descriptionTextArea;
	@FXML
	private Button productListButton;
	@FXML
	private Button imageUploadButton;

	@FXML
	private Button addCategoryButton;
	@FXML
	private Button removeCategoryButton;
	@FXML
	private Button addQualitiesButton;
	@FXML
	private Button removeQualityButton;

	@FXML
	private ImageView productImage;

	@FXML
	private TableView<Entry<String, String>> qualitiesTable;
	@FXML
	private TableColumn<Entry<String, String>, String> qualityNameColumn;
	@FXML
	private TableColumn<Entry<String, String>, String> qualityDescColumn;

	@FXML
	private TableView<String> categoriesTable;
	@FXML
	private TableColumn<String, String> categoryColumn;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProdBean productSelected;

	@Autowired
	private FxWeaver fxWeaver;

	@FXML
	public void initialize() {	
		initAddCategoryButton();
		initAddQualitiesButton();
		initCategoriesTable();
		initDescriptionTextArea();
		initImageUploadButton();
		initLabels();
		initProductImage();
		initProductListButton();
		initQualitiesTable();
		initRemoveCategoryButton();
		initRemoveQualitiesButton();
		
	}
	
	private void initProductImage() {
		productImage.imageProperty().bind(productSelected.imageObservableProperty());
		}
	
	private void initProductListButton() {
		productListButton.setOnAction((ActionEvent e) -> {
			productSelected.saveProductSelected(productSelected);
			Stage stage;
			Parent root;
			FxControllerAndView<HomeController, Node> contview = fxWeaver.load(HomeController.class);
			root = (Parent) contview.getView().get();
			Scene scene = new Scene(root);
			stage = (Stage) productListButton.getScene().getWindow();
			stage.setScene(scene);
		});
		}
	
	private void initImageUploadButton() {
		imageUploadButton.setOnAction((ActionEvent e) -> {
			Stage stage = (Stage) imageUploadButton.getScene().getWindow();
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				byte[] fileBytes;
				try {
					fileBytes = Files.readAllBytes(file.toPath());
					productSelected.setProductImage(fileBytes);
					InputStream in = new ByteArrayInputStream(productSelected.getProductImage());
					Image image = new Image(in);
					productSelected.setImageProperty(image);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void initAddQualitiesButton() {
		addQualitiesButton.setOnAction((ActionEvent e) -> {
			Stage stage = new Stage();
			FxControllerAndView<AddQualitiesController, Node> contview = fxWeaver.load(AddQualitiesController.class);
			Parent root = (Parent) contview.getView().get();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		});
	}
	
	private void initRemoveQualitiesButton() {
	removeQualityButton.setOnAction((ActionEvent e) -> {
		Entry<String, String> entry = qualitiesTable.getSelectionModel().getSelectedItem();
		productSelected.getQualities().remove(entry.getKey());
		Map<String, String> map = productSelected.getQualities();
		productSelected.setQualities(map);
	});
	}
	
	private void initAddCategoryButton() {
	addCategoryButton.setOnAction((ActionEvent e) -> {
		Stage stage = new Stage();
		FxControllerAndView<AddCategoriesController, Node> contview =
				fxWeaver.load(AddCategoriesController.class);
		Parent root = (Parent) contview.getView().get();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	});
	}
	
	private void initRemoveCategoryButton() {
	removeCategoryButton.setOnAction((ActionEvent e) -> {
		String entry = categoriesTable.getSelectionModel().getSelectedItem();
		productSelected.getCategories().remove(entry);
		List<String> list = productSelected.getCategories();
		productSelected.setCategories(list);
	});
	}
	
	private void initQualitiesTable() {
	Callback<TableColumn<Entry<String, String>, String>, TableCell<Entry<String, String>, String>> cellFactory = (
			TableColumn<Entry<String, String>, String> param) -> new EditingCell();
	qualityDescColumn.setCellFactory(cellFactory);
	qualityDescColumn.setOnEditCommit((CellEditEvent<Entry<String, String>, String> t) -> {
		Entry<String, String> entry = (Entry<String, String>) t.getTableView().getItems()
				.get(t.getTablePosition().getRow());
		entry.setValue(t.getNewValue());

	});
	qualityNameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getKey()));
	qualityDescColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getValue()));
	qualitiesTable.setItems(productSelected.qualitiesObservableProperty());
	}
	
	private void initCategoriesTable() {
	categoryColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()));
	categoriesTable.setItems(productSelected.categoriesObservableProperty());
	}
	
	private void initDescriptionTextArea() {
	descriptionTextArea.textProperty().bindBidirectional(productSelected.descriptionProperty());
	descriptionTextArea.setText(productSelected.getDescription());
	}
	
	private void initLabels() {
	id.textProperty().bind(productSelected.idProperty().asString());
	name.textProperty().bindBidirectional(productSelected.nameProperty());
	price.textProperty().bindBidirectional(productSelected.priceProperty(), new NumberStringConverter());
	instock.textProperty().bindBidirectional(productSelected.inStockProperty(), new NumberStringConverter());
	}
	

}
