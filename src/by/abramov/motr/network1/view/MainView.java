package by.abramov.motr.network1.view;

import java.util.List;

import by.abramov.motr.network1.Main;
import by.abramov.motr.network1.util.Utils;
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
        
        
        List<Double> imageData = Utils.getImageData(image1, 10, 10);
        outputImage.setImage(Utils.restoreImageData(imageData, 10, 10, (int)image1.getWidth(), (int)image1.getHeight()));
        rootPanep.setRight(outputImage);
        
        StackPane buttonsPane = new StackPane();
        buttonsPane.getChildren().add(btn);
        btn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        rootPanep.setBottom(buttonsPane);

        
        return rootPanep;
		
	}
}
