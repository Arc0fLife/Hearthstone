package controller;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import view.HeroOneSelect;
import view.HeroTwoSelect;
import model.heroes.*;


public class Controller{
	
	
	private Hero hero1;
	private Hero hero2;
	
	double w = Screen.getPrimary().getVisualBounds().getWidth();
	double h = Screen.getPrimary().getVisualBounds().getHeight();
	
	@SuppressWarnings("static-access")
	public Controller() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		File musicPath = new File("src/assets.sounds/heroselection.wav");
		AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
		Clip clip = AudioSystem.getClip();
		clip.open(audioInput);
		clip.start();
		clip.loop(clip.LOOP_CONTINUOUSLY);
		HeroOneSelect P1=new HeroOneSelect();
		HeroTwoSelect P2=new HeroTwoSelect();
		
		ImageView warlock=new ImageView(new Image("assets/warlock.png",w/(w/218),h/(h/308),false,false));
		warlock.setPickOnBounds(true);
		warlock.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero1 = new Warlock();
					playClick();
					playWarlock();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	P1.getStage().hide();
                P2.getStage().show();
            }
        });
		warlock.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          warlock.setImage(new Image("assets/warlockH.png",w/(w/218),h/(h/308),false,false));
	          warlock.setCursor(Cursor.HAND);
	          P1.getInfo().setImage(new Image("assets/warlockinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		warlock.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           warlock.setImage(new Image("assets/warlock.png",w/(w/218),h/(h/308),false,false));
	           P1.getInfo().setImage(null);
	        }
		});
		ImageView hunter=new ImageView(new Image("assets/hunter.png",w/(w/218),h/(h/308),false,false));
		hunter.setPickOnBounds(true);
		hunter.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero1 = new Hunter();
					playClick();
					playHunter();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	P1.getStage().hide();
                P2.getStage().show();
            }
        });
		hunter.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          hunter.setImage(new Image("assets/hunterH.png",w/(w/218),h/(h/308),false,false));
	          hunter.setCursor(Cursor.HAND);
	          P1.getInfo().setImage(new Image("assets/hunterinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		hunter.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           hunter.setImage(new Image("assets/hunter.png",w/(w/218),h/(h/308),false,false));
	           P1.getInfo().setImage(null);
	        }
		});
		ImageView mage=new ImageView(new Image("assets/mage.png",w/(w/218),h/(h/308),false,false));
		mage.setPickOnBounds(true);
		mage.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero1 = new Mage();
					playClick();
					playMage();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	P1.getStage().hide();
                P2.getStage().show();
            }
        });
		mage.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          mage.setImage(new Image("assets/mageH.png",w/(w/218),h/(h/308),false,false));
	          mage.setCursor(Cursor.HAND);
	          P1.getInfo().setImage(new Image("assets/mageinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		mage.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           mage.setImage(new Image("assets/mage.png",w/(w/218),h/(h/308),false,false));
	           P1.getInfo().setImage(null);
	        }
		});
		ImageView paladin=new ImageView(new Image("assets/paladin.png",w/(w/218),h/(h/308),false,false));
		paladin.setPickOnBounds(true);
		paladin.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero1 = new Paladin();
					playClick();
					playPaladin();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	P1.getStage().hide();
                P2.getStage().show();
            }
        });
		paladin.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          paladin.setImage(new Image("assets/paladinH.png",w/(w/218),h/(h/308),false,false));
	          paladin.setCursor(Cursor.HAND);
	          P1.getInfo().setImage(new Image("assets/paladininfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		paladin.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           paladin.setImage(new Image("assets/paladin.png",w/(w/218),h/(h/308),false,false));
	           P1.getInfo().setImage(null);
	        }
		});
		ImageView priest=new ImageView(new Image("assets/priest.png",w/(w/218),h/(h/308),false,false));
		priest.setPickOnBounds(true);
		priest.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero1 = new Priest();
					playClick();
					playPriest();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	P1.getStage().hide();
                P2.getStage().show();
            }
        });
		priest.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          priest.setImage(new Image("assets/priestH.png",w/(w/218),h/(h/308),false,false));
	          priest.setCursor(Cursor.HAND);
	          P1.getInfo().setImage(new Image("assets/priestinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		priest.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           priest.setImage(new Image("assets/priest.png",w/(w/218),h/(h/308),false,false));
	           P1.getInfo().setImage(null);
	        }
		});
		//-------------------------------------------------------------
		ImageView warlock1=new ImageView(new Image("assets/warlock.png",w/(w/218),h/(h/308),false,false));
		warlock1.setPickOnBounds(true);
		warlock1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero2 = new Warlock();
					playClick();
					playWarlock();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	try {
					new GameController(hero1,hero2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				P2.getStage().hide();
				clip.close();
            }
        });
		warlock1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          warlock1.setImage(new Image("assets/warlockH.png",w/(w/218),h/(h/308),false,false));
	          warlock1.setCursor(Cursor.HAND);
	          P2.getInfo().setImage(new Image("assets/warlockinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		warlock1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           warlock1.setImage(new Image("assets/warlock.png",w/(w/218),h/(h/308),false,false));
	           P2.getInfo().setImage(null);
	        }
		});
		ImageView hunter1=new ImageView(new Image("assets/hunter.png",w/(w/218),h/(h/308),false,false));
		hunter1.setPickOnBounds(true);
		hunter1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero2 = new Hunter();
					playClick();
					playHunter();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	try {
					new GameController(hero1,hero2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				P2.getStage().hide();
				clip.close();
            }
        });
		hunter1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          hunter1.setImage(new Image("assets/hunterH.png",w/(w/218),h/(h/308),false,false));
	          hunter1.setCursor(Cursor.HAND);
	          P2.getInfo().setImage(new Image("assets/hunterinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		hunter1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           hunter1.setImage(new Image("assets/hunter.png",w/(w/218),h/(h/308),false,false));
	           P2.getInfo().setImage(null);
	        }
		});
		ImageView mage1=new ImageView(new Image("assets/mage.png",w/(w/218),h/(h/308),false,false));
		mage1.setPickOnBounds(true);
		mage1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero2 = new Mage();
					playClick();
					playMage();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	try {
					new GameController(hero1,hero2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				P2.getStage().hide();
				clip.close();
            }
        });
		mage1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          mage1.setImage(new Image("assets/mageH.png",w/(w/218),h/(h/308),false,false));
	          mage1.setCursor(Cursor.HAND);
	          P2.getInfo().setImage(new Image("assets/mageinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		mage1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           mage1.setImage(new Image("assets/mage.png",w/(w/218),h/(h/308),false,false));
	           P2.getInfo().setImage(null);
	        }
		});
		ImageView paladin1=new ImageView(new Image("assets/paladin.png",w/(w/218),h/(h/308),false,false));
		paladin1.setPickOnBounds(true);
		paladin1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero2 = new Paladin();
					playClick();
					playPaladin();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	try {
					new GameController(hero1,hero2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				P2.getStage().hide();
				clip.close();
            }
        });
		paladin1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          paladin1.setImage(new Image("assets/paladinH.png",w/(w/218),h/(h/308),false,false));
	          paladin1.setCursor(Cursor.HAND);
	          P2.getInfo().setImage(new Image("assets/paladininfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		paladin1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           paladin1.setImage(new Image("assets/paladin.png",w/(w/218),h/(h/308),false,false));
	           P2.getInfo().setImage(null);
	        }
		});
		ImageView priest1=new ImageView(new Image("assets/priest.png",w/(w/218),h/(h/308),false,false));
		priest1.setPickOnBounds(true);
		priest1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	try{
					hero2 = new Priest();
					playClick();
					playPriest();
				}catch(IOException | CloneNotSupportedException e){
					e.printStackTrace();
				}
            	try {
					new GameController(hero1,hero2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				P2.getStage().hide();
				clip.close();
            }
        });
		priest1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	          priest1.setImage(new Image("assets/priestH.png",w/(w/218),h/(h/308),false,false));
	          priest1.setCursor(Cursor.HAND);
	          P2.getInfo().setImage(new Image("assets/priestinfo.png",w/(w/1584),h/(h/890),false,false));
	          playHover();
	        }
		});
		priest1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	           priest1.setImage(new Image("assets/priest.png",w/(w/218),h/(h/308),false,false));
	           P2.getInfo().setImage(null);
	        }
		});
		
		
		P1.getHbox().getChildren().addAll(warlock,hunter,mage,paladin,priest);
		P2.getHbox().getChildren().addAll(warlock1,hunter1,mage1,paladin1,priest1);
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
	
	public static void playClick(){
  		File musicPath = new File("src/assets.sounds/select.wav");
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
	
	public static void playPriest(){
  		File musicPath = new File("src/assets.sounds/priest.wav");
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
	public static void playPaladin(){
  		File musicPath = new File("src/assets.sounds/paladin.wav");
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
	public static void playHunter(){
  		File musicPath = new File("src/assets.sounds/hunter.wav");
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
	public static void playWarlock(){
  		File musicPath = new File("src/assets.sounds/warlock.wav");
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
	public static void playMage(){
  		File musicPath = new File("src/assets.sounds/mage.wav");
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
	

	
}
