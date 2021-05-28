package view;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingWorker;

import controller.Controller;
import javafx.application.Application; 
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene; 
import javafx.scene.layout.*; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;  
import javafx.scene.image.*;  
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.*; 
 
  
@SuppressWarnings("unused")
public class MainMenu extends Application { 
	
	Stage stage=new Stage();
	
	public MainMenu(){
		start(stage);
	}
	
    public Stage getStage() {
		return stage;
	}

	// launch the application 
    public void start(Stage stage){ 
        try { 
        	double w = Screen.getPrimary().getVisualBounds().getWidth();
        	double h = Screen.getPrimary().getVisualBounds().getHeight();
        					//EXIT BUTTON 
            ImageView exitButton=new ImageView(new Image("assets/exitButton.png",w/(w/350),h/(h/50),false,false));
            exitButton.setPickOnBounds(true);
            exitButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    System.exit(0);
                }
            });
            exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	        	playHover();
            	          exitButton.setImage(new Image("assets/exitButtonH.png",w/(w/350),h/(h/50),false,false));
            	          exitButton.setCursor(Cursor.HAND);
            	        }
            	});
            exitButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	           exitButton.setImage(new Image("assets/exitButton.png",w/(w/350),h/(h/50),false,false));
            	        }
            	});
            
            
            					//RULES BUTTON
            ImageView rulesButton=new ImageView(new Image("assets/rulesButton.png",w/(w/350),h/(h/50),false,false));
            rulesButton.setPickOnBounds(true);
            rulesButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    new Rules();
                }
            });
            rulesButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	        	playHover();
            	           rulesButton.setImage(new Image("assets/rulesButtonH.png",w/(w/350),h/(h/50),false,false));
            	           rulesButton.setCursor(Cursor.HAND);
            	        }
            	});
            rulesButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	            rulesButton.setImage(new Image("assets/rulesButton.png",w/(w/350),h/(h/50),false,false));
            	        }
            	});
  
            						//PLAY BUTTON
            ImageView playButton=new ImageView(new Image("assets/playButton.png",w/(w/350),h/(h/50),false,false));
            playButton.setPickOnBounds(true);
            playButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                	try {
						new Controller();
					} catch (UnsupportedAudioFileException | IOException
							| LineUnavailableException e) {
						e.printStackTrace();
					}
                	stage.close();
                }
            });
            playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	        	playHover();
            	           playButton.setImage(new Image("assets/playButtonH.png",w/(w/350),h/(h/50),false,false));
            	           playButton.setCursor(Cursor.HAND);          	           
            	        }
            	});
            playButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
            	    new EventHandler<MouseEvent>() {
            	        @Override public void handle(MouseEvent e) {
            	            playButton.setImage(new Image("assets/playButton.png",w/(w/350),h/(h/50),false,false));
            	        }
            	});
            
                            /////BUTTONS CONTAINERRRR
            VBox vbox = new VBox(h/(h/30));
            vbox.getChildren().addAll(playButton, rulesButton, exitButton);
            vbox.setAlignment(Pos.CENTER); 
                        
            //create a scene 
            Scene scene = new Scene(vbox,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            
            //create a background image
            Image image = new Image("assets/Untitled-1.png"); 
            BackgroundImage backgroundimage = new BackgroundImage(image,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundPosition.DEFAULT,  
                                             new BackgroundSize(1.0, 1.0, true, true, false, false)); 
  
            //create Background 
            Background background = new Background(backgroundimage); 
  
            //set background 
            vbox.setBackground(background); 
            
            stage.setScene(scene);
            stage.setFullScreen(true);
          	stage.setFullScreenExitHint("");
            stage.setResizable(false);
            stage.show();
            stage.toFront();
            stage.setTitle("Hearthstone");
            stage.getIcons().add(new Image("assets/logo.png"));
        } 
        catch (Exception e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
    
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
  	

  
    // Main Method 
    public static void main(String[] args){
        launch(args); 
        
    	//new MainMenu();
    } 
} 