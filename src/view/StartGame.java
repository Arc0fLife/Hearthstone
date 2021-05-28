package view;


import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
  
public class StartGame extends Application{
	

	@Override
	public void start(Stage stage) throws Exception {
		try{
		stage.setTitle("Hearthstone");
		SplashScreenDemo s = new SplashScreenDemo();
    	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		    @Override
		    protected Void doInBackground() throws IOException {
		        return null;
		    }
		    @Override
		    protected void done() {
		        s.getFrame().dispose();
		    }
		};
		worker.execute();
		
    	File f = new File("src/assets/intro.mp4");
    	Media media = new Media(f.toURI().toString());  
    	MediaPlayer mediaPlayer = new MediaPlayer(media);
    	MediaView mediaView = new MediaView(mediaPlayer);
    	mediaPlayer.setAutoPlay(true);
    	mediaView.fitWidthProperty().bind(stage.widthProperty());
    	mediaView.fitHeightProperty().bind(stage.heightProperty());
    	Group root = new Group();  
        root.getChildren().add(mediaView);  
        Scene scenev = new Scene(root,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());  
        stage.setScene(scenev); 
        stage.setFullScreen(true);
      	stage.setFullScreenExitHint("");
        stage.setResizable(false);
        stage.toFront();
        stage.show();
        stage.getIcons().add(new Image("assets/logo.png"));
        mediaPlayer.setOnEndOfMedia(() -> {
        	new MainMenu();
            mediaPlayer.stop();
            mediaPlayer.dispose();
            stage.hide();
        });
		}
		catch(Exception e){
			System.out.println(e.getMessage()); 
		}
	}
    
	public static void main(String[] args){
		launch(args);
	}

	

}
