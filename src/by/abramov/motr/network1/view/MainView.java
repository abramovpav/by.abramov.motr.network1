package by.abramov.motr.network1.view;

import java.util.List;

import by.abramov.motr.network1.Main;
import by.abramov.motr.network1.controller.TaskManager;
import by.abramov.motr.network1.model.Network;
import by.abramov.motr.network1.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainView {
	private Image image1;
	private Image image2;
	
	public MainView() {
		image1 = new Image(Main.class.getResourceAsStream("/1.jpg"));
        image2 = new Image(Main.class.getResourceAsStream("/1.jpg"));
	}
	
	public Pane getView(Stage arg0) {
		
		Button btn = new Button("Start");
		Button btn2 = new Button("Stop");
		BorderPane rootPanep = new BorderPane();
		
		StackPane titlePane = new StackPane();
		Label scenetitle = new Label("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		titlePane.getChildren().add(scenetitle);
		rootPanep.setTop(titlePane);

		
		final ImageView inputImage = new ImageView();
		final ImageView outputImage = new ImageView();        
        inputImage.setImage(image1);
        
        rootPanep.setLeft(inputImage);
        
        
        int m =8 , n = 8 , neuronCount = 1;
        double minError = 0.8;
        
        List<double []> imageData = Utils.getImageData(image1, m, n);
        
        
        
//        outputImage.setImage(Utils.restoreImageData(imageData, m, n, (int)image1.getWidth(), (int)image1.getHeight()));
        rootPanep.setRight(outputImage);
        
        TaskManager manager = new TaskManager();
        
        StackPane buttonsPane = new StackPane();
        buttonsPane.getChildren().add(btn);
        buttonsPane.getChildren().add(btn2);
        btn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	List<double []> imageData = Utils.getImageData(image1, m, n);
            	manager.runTask(m, n, neuronCount, imageData, minError, outputImage);
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	List<double[]> newData = manager.stopTask();
//            	outputImage.setImage(Utils.restoreImageData(newData, m, n, (int)image1.getWidth(), (int)image1.getHeight()));

            }
        });
        rootPanep.setBottom(buttonsPane);

        
        return rootPanep;
		
	}
}
