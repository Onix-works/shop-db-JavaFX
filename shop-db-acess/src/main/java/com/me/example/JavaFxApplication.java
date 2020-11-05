package com.me.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Lists;
import com.me.example.controllers.HomeController;
import com.me.example.model.Product;
import com.me.example.model.ProductService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class JavaFxApplication extends Application {
	

	private ProductService productService;

    private ConfigurableApplicationContext applicationContext;
    
    Logger logger = LoggerFactory.getLogger(JavaFxApplication.class);
    
    private Scene scene;
    
    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(ShopDbAcessApplication.class)
                .run(args);
    }
    

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		initializeTestData();
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(800);
		FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
	    Parent root = fxWeaver.loadView(HomeController.class);
	    scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	   
	}
	
	@Override
	public void stop() {
	    this.applicationContext.close();
	    Platform.exit();
	}
	
	/**
	 * inserts test data
	 */
	public void initializeTestData() {
		this.productService = applicationContext.getBean(ProductService.class);
		
		  Product prod1 = new Product("Acer Spin 5 SP513-53N-76ZK Laptop", 999.99, 14);
	      Product prod2 = new Product("Microsoft Modern Mobile Mouse", 39.99, 58);
	      Product prod3 = new Product("MSI Trident X Plus 9SE-062US Gaming PC", 2499.99, 7);
	      Product prod4 = new Product("iBUYPOWER Trace PRO9500 Gaming Desktop", 1149.99, 10);
	      Product prod5 = new Product("Acer B247Y 23.8\" Monitor", 199.99, 17);
	      Product prod6 = new Product("Samsung 27\" Odyssey G7 Gaming Monitor", 699.99, 21);
	      Product prod7 = new Product("Jabra Evolve2 85", 449.99, 16);
	      Product prod8 = new Product("Surface Laptop 3", 599.99, 4);
	      
	      prod1.setCategories(Lists.newArrayList("Laptops")); 
	      prod2.setCategories(Lists.newArrayList("Accessories"));
	      prod3.setCategories(Lists.newArrayList("PC"));
	      prod4.setCategories(Lists.newArrayList("PC"));
	      prod5.setCategories(Lists.newArrayList("Monitors"));
	      prod6.setCategories(Lists.newArrayList("Monitors"));
	      prod7.setCategories(Lists.newArrayList("Accessories"));
	      prod8.setCategories(Lists.newArrayList("Laptops"));
		 try {
	      fillInfoFromFile(prod1, "acer_spin");
	      fillInfoFromFile(prod2, "modern_mouse");
	      fillInfoFromFile(prod3, "msi_trident");
	      fillInfoFromFile(prod4, "ibuypower");
	      fillInfoFromFile(prod5, "acer_b247y");
	      fillInfoFromFile(prod6, "odyssey_g7");
	      fillInfoFromFile(prod7, "jabra_evolve");
	      fillInfoFromFile(prod8, "surface_3");
		 }
		 catch (IOException e) {
			 e.printStackTrace();
		 }
		
	}
	/**
     * inserts info into Product from files with given name pattern
     * @param prod
     * @param filename
     */
	private void fillInfoFromFile(Product prod, String filename) throws IOException {
		  InputStream inputStream = new ClassPathResource("/products/" + filename + ".properties").getInputStream();
	      Properties props = new Properties();
	      try {
	      props.load(inputStream);
	      }
	      catch(IOException e){
	    	  e.printStackTrace();
	      }
	      Map<String, String> map = prod.getQualities();
	      for (String key : props.stringPropertyNames()) {
	    	    String value = props.getProperty(key);
	    	    map.put(key, value);
	    	}
	      
	      inputStream = getClass().getClassLoader()
	    		  .getResourceAsStream("products/" + filename + ".desc");
	      String result = new BufferedReader(new InputStreamReader(inputStream))
	    		  .lines().collect(Collectors.joining("\n"));
	      prod.setDescription(result);
	      
	      inputStream = new ClassPathResource("/images/" + filename + ".png").getInputStream();      
	      byte[] fileBytes;
	      fileBytes = inputStream.readAllBytes();
	      if (fileBytes!=null)
	    	  prod.getProductImage().setPhoto(fileBytes);
	      
	      prod = productService.save(prod);
	}
	
}