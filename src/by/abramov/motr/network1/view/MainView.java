package by.abramov.motr.network1.view;


import java.util.List;

import by.abramov.motr.network1.Main;
import by.abramov.motr.network1.controller.TaskManager;
import by.abramov.motr.network1.model.Network;
import by.abramov.motr.network1.model.Storage;
import by.abramov.motr.network1.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView {
	private Image image1;
	private Image image2;
	
	public MainView() {
//		image1 = new Image(Main.class.getResourceAsStream("/extrasmall.png"));
//		image1 = new Image(Main.class.getResourceAsStream("/smallsmall.png"));
//		image1 = new Image(Main.class.getResourceAsStream("/small.jpg"));
//		image1 = new Image(Main.class.getResourceAsStream("/medium.jpg"));
//		image1 = new Image(Main.class.getResourceAsStream("/big.jpg"));
		image1 = new Image(Main.class.getResourceAsStream("/1.jpg"));
	}
	
	public Pane getView(Stage arg0) {
		
		
		BorderPane superRootPane = new BorderPane();
		BorderPane rootPane = new BorderPane();
		
		StackPane titlePane = new StackPane();
		Label scenetitle = new Label("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		titlePane.getChildren().add(scenetitle);
		rootPane.setTop(titlePane);

		
		final ImageView inputImage = new ImageView();
		final ImageView outputImage = new ImageView();        
        inputImage.setImage(image1);
//        rootPane.setLeft(inputImage);

//        rootPane.setRight(outputImage);
        
        ScrollPane sp = new ScrollPane();
//        sp.setFitToHeight(true);
//        sp.setFitToWidth(true);
        sp.setPrefSize(300, 300);
        
        sp.setContent(new StackPane(inputImage));
        
//        sp.setVmax(400);
        
        
        
        rootPane.setLeft(sp);
        StackPane image2Pane = new StackPane();
        ScrollPane sp2 = new ScrollPane();
        sp2.setPrefSize(300, 300);
        sp2.setContent(outputImage);
//        image2Pane.getChildren().addAll(outputImage, sc2);
        rootPane.setRight(sp2);
        
        List<float []> imageData = Utils.getImageData(image1, 4, 4);
        Image nimage = Utils.restoreImageData(imageData, 4, 4, (int) image1.getWidth(), (int) image1.getHeight());
        outputImage.setImage(nimage);
        
        
        TaskManager manager = new TaskManager();
        
        Button startBtn = new Button("Start");
		Button stopBtn = new Button("Stop");
		

        StackPane buttonsPane = new StackPane();
        buttonsPane.getChildren().add(startBtn);
        buttonsPane.getChildren().add(stopBtn);
        startBtn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        rootPane.setBottom(buttonsPane);
        
        TextField subImageWidth = new TextField("4");
        TextField subImageHeight = new TextField("4");
        TextField neuronCountField = new TextField("24");
        TextField minErrorField = new TextField("25");
        TextField alphaField = new TextField("0.0001");
        Button updateBtn = new Button("Update z");
        Button statusBtn = new Button("Update status");
        VBox parametersPanel = new VBox();
        TextField ZField = new TextField("0");
        ZField.setEditable(false);
        parametersPanel.getChildren().addAll(subImageWidth, subImageHeight, neuronCountField, alphaField, minErrorField, updateBtn, ZField, statusBtn);
        parametersPanel.setStyle("-fx-border-color: black; -fx-border-width: 0 0 0 2px; -fx-padding: 10; -fx-spacing: 8;");
        
        superRootPane.setCenter(rootPane);
        superRootPane.setRight(parametersPanel);
        
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-border-color: black; -fx-border-width: 1px 0 0 0; -fx-background-color: gainsboro;");
        Label statusInfo = new Label("Iteration = 0");
    	statusBar.getChildren().add(statusInfo);
    	superRootPane.setBottom(statusBar);
        
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	int m = Integer.parseInt(subImageWidth.getText());
            	int n = Integer.parseInt(subImageHeight.getText());
            	int neuronCount = Integer.parseInt(neuronCountField.getText());
            	
                float alpha = Float.parseFloat(alphaField.getText());
                float minError = Float.parseFloat(minErrorField.getText());
                Storage.fragmentHeight = n;
                Storage.fragmentWidth = m;
                Storage.originHeight = (int) image1.getHeight();
                Storage.originWidth = (int) image1.getWidth();
            	List<float []> imageData = Utils.getImageData(image1, m, n);
            	manager.runTask(m, n, neuronCount, imageData, minError, alpha, outputImage);
            }
        });
        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	List<float[]> newData = manager.stopTask();
//            	outputImage.setImage(Utils.restoreImageData(newData, m, n, (int)image1.getWidth(), (int)image1.getHeight()));

            }
        });
        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	int m = Integer.parseInt(subImageWidth.getText());
            	int n = Integer.parseInt(subImageHeight.getText());
            	int neuronCount = Integer.parseInt(neuronCountField.getText());
            	int N = m * n * 3;
            	int width = (int) image1.getWidth();
                int height = (int) image1.getHeight();
                int countW = width / m + (width % m != 0 ? 1 : 0);
                int countH = height / n + (height % n != 0 ? 1 : 0);
                int l = countW * countH;
                float out = (float)((N + l) * neuronCount + 2);
                float source =(float)( N * l + 2);
                ZField.setText(Float.toString(out / source));
            }
        });
        
        statusBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	String message = "Iteration = " + Storage.task.iteration  + ", Error = " + Storage.task.errorToShow;
            	if (Storage.task.stopped) {
            		message = "Finished. " + message;
//            		Image nimage = Utils.restoreImageData(Storage.task.getNewData(), Storage.fragmentWidth, Storage.fragmentHeight, Storage.originWidth, Storage.originHeight);
            		int m = Integer.parseInt(subImageWidth.getText());
                	int n = Integer.parseInt(subImageHeight.getText());
                	List<float []> imageData = Utils.getImageData(image1, m, n);
                	
            		List<float[]> newData = Storage.task.network.getImageData(imageData);
    	        	System.out.println(newData.size());
    	        	System.out.println(newData.get(0)[0]);
    	        	System.out.println(newData.get(0).length);
    	        	Image nimage = Utils.restoreImageData(newData, Storage.fragmentWidth, Storage.fragmentHeight, Storage.originWidth, Storage.originHeight);
    	        	outputImage.setImage(nimage);
            	}
            	
                statusInfo.setText(message);
            }
        });
        

        
        
        
        return superRootPane;
		
	}
}
