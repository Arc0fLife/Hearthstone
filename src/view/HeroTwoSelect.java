package view;

import javafx.application.Application; 
import javafx.scene.Scene;  
import javafx.scene.layout.*; 
import javafx.stage.Screen;
import javafx.stage.Stage;  
import javafx.scene.image.*;  
import javafx.geometry.*; 
 
public class HeroTwoSelect extends Application {
	
	public HBox getHbox() {
		return hbox;
	}
	public HeroTwoSelect(){
		start(stage);
	}
	double w = Screen.getPrimary().getVisualBounds().getWidth();
	double h = Screen.getPrimary().getVisualBounds().getHeight();
	HBox hbox = new HBox(w/(w/10));
	Stage stage = new Stage();
	StackPane stack = new StackPane();
	ImageView info=new ImageView();
	 
    public void start(Stage stage){ 
        try { 
        	this.stage=stage;
                            /////BUTTONS CONTAINERRRR
            hbox.setAlignment(Pos.CENTER); 
                        
            // create a scene 
          	Scene scene = new Scene(stack,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight()); 
            
            Image image = new Image("assets/hero2select.png"); 
  
            // create a background image 
            BackgroundImage backgroundimage = new BackgroundImage(image,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundPosition.DEFAULT,  
                                             new BackgroundSize(1.0, 1.0, true, true, false, false)); 
  
            // create Background 
            Background background = new Background(backgroundimage); 

            // set background 
            hbox.setPadding(new Insets(0,0,h/(h/50),0));
            stack.getChildren().addAll(info,hbox);
            stack.setBackground(background); 
            stage.setScene(scene);
          	stage.setFullScreen(true);
          	stage.setFullScreenExitHint("");
          	stage.setTitle("Hearthstone");
          	stage.getIcons().add(new Image("assets/logo.png"));
            //stage.show(); 
        } 
        catch (Exception e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
  
    public ImageView getInfo() {
		return info;
	}
	public Stage getStage() {
		return stage;
	}
	// Main Method 
    public static void main(String[] args){
        launch(args); 
    } 
} 