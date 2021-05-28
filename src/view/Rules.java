package view;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.application.Application; 
import javafx.scene.Cursor;
import javafx.scene.Scene;  
import javafx.scene.layout.*; 
import javafx.stage.Screen;
import javafx.stage.Stage;  
import javafx.stage.StageStyle;
import javafx.scene.image.*;  
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.*; 
 
  
public class Rules extends Application { 
	
	public static void playHover(){
  		File musicPath = new File("src/assets/hover.wav");
  		try{
  			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
  			Clip clip = AudioSystem.getClip();
  			clip.open(audioInput);
  			clip.start();
  		}
  		catch(Exception e){
  			e.printStackTrace();;
  		}
  	}
	
    Stage stage=new Stage();
    
    public Rules(){
    	start(this.stage);
    }
    
    public Stage getStage() {
		return stage;
	}

	public void start(Stage stage){ 
        try { 
        	
        	this.stage=stage;
        	stage.setTitle("Hearthstone"); 
        	double w = Screen.getPrimary().getVisualBounds().getWidth();
        	double h = Screen.getPrimary().getVisualBounds().getHeight();
        					//PreviousMenu BUTTON
        	ImageView previousButton = new ImageView(new Image("assets/back.png",w/(w/350),h/(h/50),false,false));
            previousButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                	stage.hide();
                }
            });
            previousButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	           previousButton.setImage(new Image("assets/backH.png",w/(w/350),h/(h/50),false,false));
            	           previousButton.setCursor(Cursor.HAND);
            	           playHover();
            	        }
            	});
            previousButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	            previousButton.setImage(new Image("assets/back.png",w/(w/350),h/(h/50),false,false));
            	        }
            	});
            
                            /////BUTTONS CONTAINERRRR
            VBox vbox = new VBox(); //NUMBA HERE IS SPACING
            vbox.getChildren().addAll(previousButton);
            vbox.setAlignment(Pos.BOTTOM_RIGHT); 
            vbox.setPadding(new Insets(0,w/(w/40),h/(h/40),0));
  
            // set background 
            vbox.setBackground(new Background(new BackgroundImage(new Image("assets/rules.png"),  
                    BackgroundRepeat.NO_REPEAT,  
                    BackgroundRepeat.NO_REPEAT,  
                    BackgroundPosition.DEFAULT,  
                    new BackgroundSize(1.0, 1.0, true, true, false, false)))); 
  
            stage.setScene(new Scene(vbox,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight()));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setMaximized(true);
          	stage.setFullScreenExitHint("");
            stage.toFront();
            stage.show();
        } 
        catch (Exception e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
  
    // Main Method 
    public static void main(String[] args){ 
        launch(args); 
    } 
} 