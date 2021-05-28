package view;


import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameView extends Application {
	
	File musicPath = new File("src/assets/battle.wav");
	AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
	Clip clip = AudioSystem.getClip();
	private Stage stage=new Stage();
	private HBox hero1handbox=new HBox(0);
	private HBox hero2handbox=new HBox(0);
	private HBox hero1deckbox=new HBox(25);
	private HBox hero2deckbox=new HBox(25);
	private HBox hero1powerbox=new HBox();
	private HBox hero2powerbox=new HBox();
	private HBox endturnbox= new HBox();
	private HBox hero1box= new HBox();
	private HBox hero2box= new HBox();
	private HBox hero1fieldbox=new HBox(10);
	private HBox hero2fieldbox=new HBox(10);
	private Popup exception=new Popup();
	private Popup target=new Popup();
	private ImageView zoomimage=new ImageView();
	private ImageView zoomBurned=new ImageView();
	private ImageView deck1image=new ImageView(new Image("assets/deck.png",70,128,false,false));
	private ImageView deck2image=new ImageView(new Image("assets/deck.png",70,128,false,false));
	Pane pane=new Pane();
	
	public Pane getPane() {
		return pane;
	}

	private HBox hero1hpbox=new HBox();
	private HBox hero1manabox=new HBox();
	
	private HBox hero2hpbox=new HBox();
	private HBox hero2manabox=new HBox();
	
	private Text cardBurned=new Text();
	private HBox cardBurnedBox=new HBox();
	
	private Button deck1=new Button();
	private Button deck2=new Button();
	
	public Button getDeck1() {
		return deck1;
	}


	public Button getDeck2() {
		return deck2;
	}

	
	public File getMusicPath() {
		return musicPath;
	}


	public AudioInputStream getAudioInput() {
		return audioInput;
	}


	public ImageView getZoomimage() {
		return zoomimage;
	}


	public HBox getHero1hpbox() {
		return hero1hpbox;
	}


	public HBox getHero1manabox() {
		return hero1manabox;
	}


	public HBox getHero2hpbox() {
		return hero2hpbox;
	}


	public HBox getHero2manabox() {
		return hero2manabox;
	}


	public HBox getCardBurnedBox() {
		return cardBurnedBox;
	}


	public ImageView getZoomImage() {
		return zoomimage;
	}


	public Popup getException() {
		return exception;
	}


	public Stage getStage() {
		return stage;
	}


	public HBox getHero1fieldbox() {
		return hero1fieldbox;
	}


	public HBox getHero2fieldbox() {
		return hero2fieldbox;
	}

	public HBox getEndturnbox() {
		return endturnbox;
	}


	public HBox getHero1box() {
		return hero1box;
	}


	public HBox getHero2box() {
		return hero2box;
	}


	public GameView() throws Exception {
		start(stage);
	}



	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception {
		
		clip.open(audioInput);
		clip.start();
		clip.loop(clip.LOOP_CONTINUOUSLY);
		
		this.stage=stage;
		
		stage.setFullScreenExitHint("");
        stage.setResizable(false);
        
		
        hero1box.setLayoutX(615);
        hero1box.setLayoutY(559);
        hero2box.setLayoutX(614);
        hero2box.setLayoutY(60);
        
        hero1powerbox.setLayoutX(766);
        hero1powerbox.setLayoutY(560);
        hero2powerbox.setLayoutX(771);
        hero2powerbox.setLayoutY(87);
        
        hero1hpbox.setLayoutX(712);
        hero1hpbox.setLayoutY(627);
        hero2hpbox.setLayoutX(711);
        hero2hpbox.setLayoutY(130);
        
        hero1manabox.setLayoutX(1077);
        hero1manabox.setLayoutY(535);
        hero2manabox.setLayoutX(193);
        hero2manabox.setLayoutY(154);
        
        hero1handbox.setLayoutX(780);
        hero1handbox.setLayoutY(690);
        hero1handbox.setPrefSize(281, 77);
        hero2handbox.setLayoutX(50);
        hero2handbox.setLayoutY(0);
        hero2handbox.setPrefSize(281, 77);
        
        hero1fieldbox.setLayoutX(265);
        hero1fieldbox.setLayoutY(388);
        hero1fieldbox.setPrefSize(281, 77);
        hero2fieldbox.setLayoutX(265);
        hero2fieldbox.setLayoutY(250);
        hero2fieldbox.setPrefSize(281, 77);

        endturnbox.setLayoutX(1102);
        endturnbox.setLayoutY(351);
        
        deck1image.setLayoutX(1167);
        deck1image.setLayoutY(424);
        deck2image.setLayoutX(1168);
        deck2image.setLayoutY(192);
        
        zoomimage.setTranslateX(4);
        zoomimage.setTranslateY(277);
        
        zoomBurned.setTranslateX(4);
        zoomBurned.setTranslateY(270);
        cardBurned.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		cardBurned.setStroke(Color.BLACK);
		cardBurned.setFill(Paint.valueOf("White"));
		cardBurned.setTextAlignment(TextAlignment.JUSTIFY);
        cardBurnedBox.getChildren().add(cardBurned);
        cardBurnedBox.setLayoutX(4);
        cardBurnedBox.setLayoutY(250);
        
        pane.getChildren().addAll(hero1box, hero2box, hero1powerbox, hero2powerbox, 
        		hero1hpbox, hero2hpbox, endturnbox, hero1manabox, hero2manabox,
        		hero1handbox, hero2handbox, hero1fieldbox, hero2fieldbox, zoomimage,
        		deck1image, deck2image,zoomBurned,cardBurnedBox);
        
        exception.setOnShown(e -> {
            exception.setX(stage.getX() + stage.getWidth() / 2 - exception.getWidth() / 2);
            exception.setY(359);
        });
        exception.setAutoFix(true);
        
        target.setOnShown(e -> {
            target.setX(555);
            target.setY(359);
        });
        target.setAutoFix(true);
        
        
        
        Image image = new Image("assets/gameV.jpg"); 
        BackgroundImage backgroundimage = new BackgroundImage(image,  
                                         BackgroundRepeat.NO_REPEAT,  
                                         BackgroundRepeat.NO_REPEAT,  
                                         BackgroundPosition.DEFAULT,  
                                         new BackgroundSize(1.0, 1.0, true, true, false, false));  
        Background background = new Background(backgroundimage);
        pane.setBackground(background);
       
        Scene scene = new Scene(pane,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
       
     
        stage.show(); 
        stage.setTitle("Hearthstone");
        stage.getIcons().add(new Image("assets/logo.png"));

	}

	public Popup getTarget() {
		return target;
	}


	public Clip getClip() {
		return clip;
	}


	public Text getCardBurned() {
		return cardBurned;
	}


	public ImageView getZoomBurned() {
		return zoomBurned;
	}


	public ImageView getDeck1image() {
		return deck1image;
	}


	public ImageView getDeck2image() {
		return deck2image;
	}


	public HBox getHero1powerbox() {
		return hero1powerbox;
	}


	public HBox getHero2powerbox() {
		return hero2powerbox;
	}


	public HBox getHero1handbox() {
		return hero1handbox;
	}
	public HBox getHero2handbox() {
		return hero2handbox;
	}

	public HBox getHero1deckbox() {
		return hero1deckbox;
	}

	public HBox getHero2deckbox() {
		return hero2deckbox;
	}

}
