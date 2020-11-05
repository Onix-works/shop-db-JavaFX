package com.me.example.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.example.model.ProductService;
import com.me.example.supplementary.ProdBean;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/fxml/addCategories.fxml")
public class AddCategoriesController {	
	
    @FXML 
    private TextField nameTextField;
    @FXML 
    private Label nameErrorLabel;
    @FXML 
    private Button saveButton;
    @FXML 
    private Button cancelButton;
    
	@Autowired
	public ProdBean productSelected;
	
	@FXML
	public void initialize() {
		
	saveButton.setOnAction((ActionEvent e) -> {		
		if (!nameTextField.getText().equals("")) {
		List<String> list = productSelected.getCategories();
		list.add(nameTextField.textProperty().get());
		productSelected.setCategories(list);
		((Stage)((Node)e.getSource()).getScene().getWindow()).close();
		}
		else{
			nameErrorLabel.setVisible(true);
		}
	});
	
	cancelButton.setOnAction((ActionEvent e) -> {		
		((Stage)((Node)e.getSource()).getScene().getWindow()).close();
	});

	}
}
