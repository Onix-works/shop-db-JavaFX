package com.me.example.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.me.example.model.Product;
import com.me.example.model.ProductService;
import com.me.example.supplementary.ProdBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/fxml/main-stage.fxml")
public class HomeController {

	public HomeController() {
		this.productObservableList = FXCollections.observableArrayList();
	}

	private final ObservableList<Product> productObservableList;

	@FXML
	private TextField searchField;

	@FXML
	private ChoiceBox<String> searchChoiceBox;

	@FXML
	private Button searchButton;

	@FXML
	private Button selectButton;

	@FXML
	private Button newButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Pagination productsPagination;

	@FXML
	private TableView<Product> productsTable;

	@FXML
	private TableColumn<Product, Long> id;
	@FXML
	private TableColumn<Product, String> name;
	@FXML
	private TableColumn<Product, Double> price;
	@FXML
	private TableColumn<Product, Integer> instock;

	@FXML
	private VBox productsVbox;

	@Autowired
	private ProductService productService;

	@Autowired
	private FxWeaver fxWeaver;

	@Autowired
	private ProdBean productSelected;

	@FXML
	public void initialize() {
		initProductsPagination();
		initProductsTable();
		initSearchChoiceBox();
		initSelectButton();
		initSearchButton();
		initDeleteButton();
		initNewButton();

	}

	private void initProductsPagination() {
		productsPagination.setPageFactory(this::createPage);
	}

	private Node createPage(int pageIndex) {

		addToProductObservableList(pageIndex);
		return productsTable;
	}

	/**
	 * adds Products found by criteria set in searchField to productObservableList,
	 * which is bound to productsTable
	 * 
	 * @param pageIndex
	 */
	private void addToProductObservableList(int pageIndex) {
		PageRequest pageRequest;
		switch (searchChoiceBox.getValue()) {
		case "Id":
			pageRequest = PageRequest.of(pageIndex, 5, Sort.by("id"));
			productObservableList.clear();
			productObservableList.addAll(
					productService.findByByIdContaining(searchField.textProperty().get(), pageRequest).getContent());
			break;
		case "Name":
			pageRequest = PageRequest.of(pageIndex, 5, Sort.by("id"));
			productObservableList.clear();
			productObservableList.addAll(
					productService.findByNameContaining(searchField.textProperty().get(), pageRequest).getContent());

			break;
		}
	}

	private void initProductsTable() {
		id.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));
		name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		instock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("inStock"));
		productsTable.setItems(productObservableList);
	}

	private void initSelectButton() {
		selectButton.setOnAction((ActionEvent e) -> {
			Stage stage;
			Parent root;
			TableViewSelectionModel<Product> selectionModel = productsTable.getSelectionModel();
			ObservableList<Product> selectedItems = selectionModel.getSelectedItems();
			if (!selectedItems.isEmpty()) {
				Product product = selectedItems.get(0);
				productSelected.selectProduct(product);

				stage = (Stage) selectButton.getScene().getWindow();
				FxControllerAndView<ProductDetailsController, Node> contview = fxWeaver
						.load(ProductDetailsController.class);
				root = (Parent) contview.getView().get();
				Scene scene = new Scene(root);
				stage.setScene(scene);
			}
		});
	}

	private void initNewButton() {
		newButton.setOnAction((ActionEvent e) -> {
			Stage stage;
			Parent root;
			Product product = new Product();
			product = productService.save(product);
			productSelected.selectProduct(product);
			
			stage = (Stage) newButton.getScene().getWindow();
			FxControllerAndView<ProductDetailsController, Node> contview = fxWeaver
					.load(ProductDetailsController.class);
			root = (Parent) contview.getView().get();
			Scene scene = new Scene(root);
			stage.setScene(scene);

		});
	}

	private void initSearchButton() {
		searchButton.setOnAction((ActionEvent e) -> {
			addToProductObservableList(0);
		});
	}

	private void initDeleteButton() {
		deleteButton.setOnAction((ActionEvent e) -> {
			TableViewSelectionModel<Product> selectionModel = productsTable.getSelectionModel();
			ObservableList<Product> selectedItems = selectionModel.getSelectedItems();
			if (!selectedItems.isEmpty()) {
				Product product = selectedItems.get(0);
				productService.delete(product.getId());
				addToProductObservableList(0);
			}
		});
	}

	private void initSearchChoiceBox() {
		searchChoiceBox.getItems().addAll("Name", "Id");
		searchChoiceBox.setValue("Name");
		searchChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {// reset table
																											// // and
			if (newVal != null) {
				searchField.setText("");
			}
		});
	}

}