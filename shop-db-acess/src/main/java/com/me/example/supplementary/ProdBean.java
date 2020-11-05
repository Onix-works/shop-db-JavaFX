package com.me.example.supplementary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.me.example.model.Product;
import com.me.example.model.ProductImage;
import com.me.example.model.ProductService;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javassist.bytecode.ByteArray;

/**
 * mirrors Product class fields with observable properties
 * 
 * @author User
 *
 */
public class ProdBean {

	public ProdBean(String name, double price, int inStock) {
		this.id = new SimpleLongProperty();
		this.name = new SimpleStringProperty(name);
		this.price = new SimpleDoubleProperty(price);
		this.inStock = new SimpleIntegerProperty(inStock);
		this.description = new SimpleStringProperty();

		this.qualities = new HashMap<String, String>();
		this.categories = new ArrayList<String>();
		this.imageObservable = new SimpleObjectProperty<>();
		this.qualitiesObservable = FXCollections.observableArrayList(this.qualities.entrySet());
		this.categoriesObservable = FXCollections.observableArrayList(this.categories);
	}

	public ProdBean() {
		this.id = new SimpleLongProperty();
		this.name = new SimpleStringProperty();
		this.price = new SimpleDoubleProperty();
		this.inStock = new SimpleIntegerProperty();
		this.description = new SimpleStringProperty();

		this.qualities = new HashMap<String, String>();
		this.categories = new ArrayList<String>();
		this.imageObservable = new SimpleObjectProperty<>();
		this.qualitiesObservable = FXCollections.observableArrayList(this.qualities.entrySet());
		this.categoriesObservable = FXCollections.observableArrayList(this.categories);
	}

	@Autowired
	private ProductService productService;

	private final SimpleLongProperty id;

	private final SimpleStringProperty name;

	private final SimpleDoubleProperty price;

	private final SimpleIntegerProperty inStock;

	private final SimpleStringProperty description;

	private Map<String, String> qualities;

	private List<String> categories;

	private byte[] productImage;

	private final ObjectProperty<Image> imageObservable;

	private final ObservableList<Entry<String, String>> qualitiesObservable;

	private final ObservableList<String> categoriesObservable;

	// id
	public SimpleLongProperty idProperty() {
		return this.id;
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public long getId() {
		return id.get();
	}

	// name
	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	// price
	public double getPrice() {
		return price.get();
	}

	public SimpleDoubleProperty priceProperty() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	// instock
	public int getInStock() {
		return inStock.get();
	}

	public SimpleIntegerProperty inStockProperty() {
		return this.inStock;
	}

	public void setInStock(int inStock) {
		this.inStock.set(inStock);
	}

	// description
	public String getDescription() {
		return description.get();
	}

	public StringProperty descriptionProperty() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	// categories
	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
		categoriesObservable.clear();
		categoriesObservable.addAll(categories);
	}

	// qualities
	public Map<String, String> getQualities() {
		return qualities;
	}

	public void setQualities(Map<String, String> qualities) {
		this.qualities = qualities;
		qualitiesObservable.clear();
		qualitiesObservable.addAll(qualities.entrySet());
	}

	// imageProperty
	public Image getImageObservable() {
		return imageObservable.get();
	}

	public ObjectProperty<Image> imageObservableProperty() {
		return this.imageObservable;
	}

	public void setImageProperty(Image imageProperty) {
		this.imageObservable.set(imageProperty);
	}
	// qualitiesObservable

	public Map<String, String> getQualitiesObservable() {
		return qualities;
	}

	public ObservableList<Entry<String, String>> qualitiesObservableProperty() {
		return qualitiesObservable;
	}

	public void setQualitiesObservable(Map<String, String> qualities) {
		qualities.clear();
		qualities.putAll(qualities);
	}

	// categoriesObservable
	public List<String> getCategoriesObservable() {
		return categories;
	}

	public ObservableList<String> categoriesObservableProperty() {
		return categoriesObservable;
	}

	public void setCategoriesObservable(List<String> categories) {
		categories.clear();
		categories.addAll(categories);
	}

	// productImage
	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	

	@Override
	public String toString() {
		return "ProdBean [productService=" + productService + ", id=" + id + ", name=" + name + ", price=" + price
				+ ", inStock=" + inStock + ", description=" + description + ", qualities=" + qualities + ", categories="
				+ categories + "]";
	}

	/**
	 * fills ProdBean with data from given Product
	 * 
	 * @param product
	 * @throws SQLException
	 */
	public void selectProduct(Product product)  {
		setId(product.getId());
		setName(product.getName());
		setPrice(product.getPrice());
		setInStock(product.getInStock());
		setQualities(product.getQualities());
		setCategories(product.getCategories());
		setDescription(product.getDescription());

		ProductImage img = product.getProductImage();
		InputStream in = new ByteArrayInputStream(img.getPhoto());
		Image image = new Image(in);
		setImageProperty(image);
		setProductImage(img.getPhoto());
	}

	/**
	 * transforms current ProdBean into Product instance and saves it in DB
	 * 
	 * @param productSelected
	 */
	public void saveProductSelected(ProdBean productSelected) {
		Product product = productService.findById(productSelected.getId());
		product.setName(productSelected.getName());
		product.setPrice(productSelected.getPrice());
		product.setInStock(productSelected.getInStock());
		product.setDescription(productSelected.getDescription());
		product.setQualities(productSelected.getQualities());
		product.setCategories(productSelected.getCategories());
		product.getProductImage().setPhoto(productSelected.getProductImage());

		productService.update(product);
	}
}