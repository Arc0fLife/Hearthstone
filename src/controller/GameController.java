package controller;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import view.GameView;
import view.MainMenu;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.CurseOfWeakness;
import model.cards.spells.DivineSpirit;
import model.cards.spells.FieldSpell;
import model.cards.spells.Flamestrike;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.HolyNova;
import model.cards.spells.KillCommand;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.MultiShot;
import model.cards.spells.Polymorph;
import model.cards.spells.Pyroblast;
import model.cards.spells.SealOfChampions;
import model.cards.spells.Spell;
import model.cards.spells.TwistingNether;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

public class GameController implements GameListener{
	
	Game model;
	GameView view;
	boolean power1inuse=false;
	boolean power2inuse=false;
	boolean hero2Casting=false;
	boolean hero1Casting=false;
	boolean power1Use=true;
	boolean power2Use=true;
	HeroTargetSpell herotargetspell=null;
	Spell spellOnMinion=null;
	Minion attacker1=null;
	Minion attacker2=null;
	ImageView heroHP1 = new ImageView(new Image("assets.data/hero30.png"));
	ImageView heroHP2 = new ImageView(new Image("assets.data/hero30.png"));
	int turn1 = 0;
	int turn2 = 0;
	boolean t1 = true;
	boolean t2 = true;
	
	File musicPath = new File("src/assets.sounds/crowd.wav");
	AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
	Clip clip = AudioSystem.getClip();
	
    private static int TIMEOUT = 1800;
    private static int CTIMEOUT = 2800;
    private static int STIMEOUT = 1300;
    private static int PTIMEOUT = 500;
    
    private static double W = 6.8;
    private static double V = 1.2;
    private static double warlock = 1.0;
    private static double priest = 2.2;
    private static double mage = 2.2;
    private static double hunter = 1.2;
    private static double paladin = 2.4;
	
	public void refreshGame(Hero hero1,Hero hero2){	
		
		view.getHero1manabox().getChildren().clear();
		view.getHero1manabox().getChildren().add(new ImageView(new Image("assets.data/mana"+hero1.getCurrentManaCrystals()+"x"+hero1.getTotalManaCrystals()+".png")));
		heroHP1.setImage(new Image("assets.data/hero"+hero1.getCurrentHP()+".png"));
		
		
		view.getHero2manabox().getChildren().clear();
		view.getHero2manabox().getChildren().add(new ImageView(new Image("assets.data/mana"+hero2.getCurrentManaCrystals()+"x"+hero2.getTotalManaCrystals()+".png")));
		heroHP2.setImage(new Image("assets.data/hero"+hero2.getCurrentHP()+".png"));
		
		view.getHero1handbox().getChildren().clear();
        view.getHero2handbox().getChildren().clear();
		addToHands(hero1,hero2);
		
		refreshFields(hero1,hero2);
		power1inuse=false;
		power2inuse=false;
		hero2Casting=false;
		hero1Casting=false;
		herotargetspell=null;
		spellOnMinion=null;
		attacker1=null;
		attacker2=null;	
		
		if(turn1==3 && t1){
			if(hero1 instanceof Hunter && model.getCurrentHero().equals(hero1))
				hunterTurn();
			else if(hero1 instanceof Mage && model.getCurrentHero().equals(hero1))
				mageTurn();
			else if(hero1 instanceof Paladin && model.getCurrentHero().equals(hero1))
				paladinTurn();
			else if(hero1 instanceof Priest && model.getCurrentHero().equals(hero1))
				priestTurn();
			else if(hero1 instanceof Warlock && model.getCurrentHero().equals(hero1))
				warlockTurn();
			t1=false;
		}
		if(turn2==3 && t2){
			if(hero2 instanceof Hunter && model.getCurrentHero().equals(hero2))
				hunterTurn();
			else if(hero2 instanceof Mage && model.getCurrentHero().equals(hero2))
				mageTurn();
			else if(hero2 instanceof Paladin && model.getCurrentHero().equals(hero2))
				paladinTurn();
			else if(hero2 instanceof Priest && model.getCurrentHero().equals(hero2))
				priestTurn();
			else if(hero2 instanceof Warlock && model.getCurrentHero().equals(hero2))
				warlockTurn();
			t2=false;
		}
	}
	
	@SuppressWarnings("static-access")
	public GameController(Hero hero1, Hero hero2) throws Exception {
		
		model=new Game(hero1, hero2);
		model.setListener(this);
		view=new GameView();
				
		refreshGame(hero1,hero2);
		
		clip.open(audioInput);
		clip.start();
		clip.loop(clip.LOOP_CONTINUOUSLY);
		new Timeline(new KeyFrame(
					Duration.seconds(0.5),
					ae -> playWelcome(hero1,hero2))).play();
		
		
		view.getHero1hpbox().getChildren().add(heroHP1);
		view.getHero2hpbox().getChildren().add(heroHP2);
		
		view.getDeck1image().addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	        	Tooltip.install(view.getDeck1image(), new Tooltip("This deck has "+hero1.getDeck().size()+" card(s) left."));
        	        	view.getDeck1image().setCursor(Cursor.HAND);
        	        }
        });
		view.getDeck2image().addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	        	Tooltip.install(view.getDeck2image(), new Tooltip("This deck has "+hero2.getDeck().size()+" card(s) left."));
        	        	view.getDeck2image().setCursor(Cursor.HAND);
        	        }
        });
		
		
	
		ImageView Hero1Button;
		if(hero1 instanceof Warlock)
			Hero1Button=new ImageView(new Image("assets/warlockG1.png"));
		else if(hero1 instanceof Paladin)
			Hero1Button=new ImageView(new Image("assets/paladinG1.png"));
		else if(hero1 instanceof Mage)
			Hero1Button=new ImageView(new Image("assets/mageG1.png"));
		else if(hero1 instanceof Hunter)
			Hero1Button=new ImageView(new Image("assets/hunterG1.png"));
		else
			Hero1Button=new ImageView(new Image("assets/priestG1.png"));
		Group blend1 = new Group();
		blend1.getChildren().add(Hero1Button);
		Hero1Button.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	if(power1inuse){
            		if(hero1 instanceof Priest){
            			try{
            				((Priest)hero1).useHeroPower(hero1);
            				playHeal();
            				if(power1Use){
            					priestPower();
            					power1Use=false;
            				}
            				refreshGame(hero1,hero2);
            			} 
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            				refreshGame(hero1,hero2);
            			}
            			
            		}
            		else if(hero1 instanceof Mage){
            			try{
            				((Mage)hero1).useHeroPower(hero1);
            				if(power1Use){
            					magePower();
            					power1Use=false;
            				}
            				blend1.getChildren().add(new ImageView(new Image("assets/damageH1.png")));
            				playDamage();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
            				new Timeline(new KeyFrame(
    				                Duration.millis(PTIMEOUT),
    				                ae -> blend1.getChildren().clear())).play();
            				new Timeline(new KeyFrame(
    				                Duration.millis(PTIMEOUT),
    				                ae -> blend1.getChildren().add(Hero1Button))).play();
            			}
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            				refreshGame(hero1,hero2);
            			}          			
            		}
            	}
            	else if(power2inuse){
            	   if(hero2 instanceof Priest){
            		   try{
            			   ((Priest)hero2).useHeroPower(hero1);
            			   playHeal();
            			   if(power2Use){
            				   priestPower();
            				   power2Use=false;
            			   }
            		   }
            		   catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            				   | NotYourTurnException | FullHandException
            				   | FullFieldException | CloneNotSupportedException e) {
            			   displayError(e.getMessage());
            		   }
            		   refreshGame(hero1,hero2);
            	   }
            	   else if(hero2 instanceof Mage){
            		   try{
      						((Mage)hero2).useHeroPower(hero1);
      						if(power2Use){
             				   magePower();
             				   power2Use=false;
             			   }
      						blend1.getChildren().add(new ImageView(new Image("assets/damageH1.png")));
      						playDamage();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
            				new Timeline(new KeyFrame(
    				                Duration.millis(PTIMEOUT),
    				                ae -> blend1.getChildren().clear())).play();
            				new Timeline(new KeyFrame(
    				                Duration.millis(PTIMEOUT),
    				                ae -> blend1.getChildren().add(Hero1Button))).play();
            		   } 
            		   catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            				   | NotYourTurnException | FullHandException
            				   | FullFieldException | CloneNotSupportedException e) {
            			   displayError(e.getMessage());
            			   refreshGame(hero1,hero2);
            		   }
            	  }
            	}
            	else if(hero1Casting){
            		try{
            			hero1.castSpell(herotargetspell, hero1);
            			if(herotargetspell instanceof KillCommand)
            				blend1.getChildren().add(new ImageView(new Image("assets/damageH3.png")));
            			else
            				blend1.getChildren().add(new ImageView(new Image("assets/damageH10.png")));
            			playDamage();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> refreshGame(hero1,hero2))).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().add(Hero1Button))).play();
            		}
            		catch (NotYourTurnException | NotEnoughManaException e) {
            			displayError(e.getMessage());
            			refreshGame(hero1,hero2);
            		}
            	}
            	else if(hero2Casting){
            		try {
            			hero2.castSpell(herotargetspell, hero1);
            			playDamage();
            			if(herotargetspell instanceof KillCommand)
            				blend1.getChildren().add(new ImageView(new Image("assets/damageH3.png")));
            			else
            				blend1.getChildren().add(new ImageView(new Image("assets/damageH10.png")));
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> refreshGame(hero1,hero2))).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().add(Hero1Button))).play();
            		} 
            		catch (NotYourTurnException | NotEnoughManaException e) {
            			displayError(e.getMessage());
            			refreshGame(hero1,hero2);
            		}
               }
            	else if(attacker1!=null){
            		try {
						hero1.attackWithMinion(attacker1, hero1);						
					} catch (CannotAttackException | NotYourTurnException
							| TauntBypassException | NotSummonedException
							| InvalidTargetException e) {
						displayError(e.getMessage());
					}
            		attacker1=null;
            	}
            	else if(attacker2!=null){
            		try {
						hero2.attackWithMinion(attacker2, hero1);
            			blend1.getChildren().add(new ImageView(new Image("assets/damageH"+attacker2.getAttack()+".png")));
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> heroHP1.setImage(new Image("assets.data/hero"+hero1.getCurrentHP()+".png")))).play();						
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend1.getChildren().add(Hero1Button))).play();
        				playDamage();
            		} catch (CannotAttackException | NotYourTurnException
							| TauntBypassException | NotSummonedException
							| InvalidTargetException e) {
						displayError(e.getMessage());
					}
            		attacker2=null;
            	}
            }
        });
		Hero1Button.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	Tooltip.install(Hero1Button, new Tooltip(hero1.getName()));
	        	Hero1Button.setCursor(Cursor.HAND);
	        }
		});
		view.getHero1box().getChildren().add(blend1);

		
		ImageView Hero2Button;
		if(hero2 instanceof Warlock)
			Hero2Button=new ImageView(new Image("assets/warlockG2.png"));
		else if(hero2 instanceof Paladin)
			Hero2Button=new ImageView(new Image("assets/paladinG2.png"));
		else if(hero2 instanceof Mage)
			Hero2Button=new ImageView(new Image("assets/mageG2.png"));
		else if(hero2 instanceof Hunter)
			Hero2Button=new ImageView(new Image("assets/hunterG2.png"));
		else
			Hero2Button=new ImageView(new Image("assets/priestG2.png"));
		Group blend=new Group();
		blend.getChildren().add(Hero2Button);
		Hero2Button.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	if(power2inuse){
            		if(hero2 instanceof Priest){
            			try {
            				((Priest)hero2).useHeroPower(hero2);
            				playHeal();
            				if(power2Use){
             				   priestPower();
             				   power2Use=false;
             			   }
            			} 
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            			}
            			refreshGame(hero1,hero2);
            		}
            		else if(hero2 instanceof Mage){
            			try {
            				((Mage)hero2).useHeroPower(hero2);
            				if(power2Use){
             				   magePower();
             				   power2Use=false;
             			   }
            				blend.getChildren().add(new ImageView(new Image("assets/damageH1.png")));
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> blend.getChildren().clear())).play();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> blend.getChildren().add(Hero2Button))).play();
            				playDamage();
            			} 
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            				refreshGame(hero1,hero2);
            			}
            		}
            	}
            	else if(power1inuse){
            		if(hero1 instanceof Priest){
            			try {
            				((Priest)hero1).useHeroPower(hero2);
            				playHeal();
            				if(power1Use){
             				   priestPower();
             				   power1Use=false;
             			   }
            			} 
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            			}
            			refreshGame(hero1,hero2);
            		}
            		if(hero1 instanceof Mage){
            			try {
            				((Mage)hero1).useHeroPower(hero2);
            				if(power1Use){
             				   magePower();
             				   power1Use=false;
             			   	}
            				blend.getChildren().add(new ImageView(new Image("assets/damageH1.png")));
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> blend.getChildren().clear())).play();
            				new Timeline(new KeyFrame(
					                Duration.millis(PTIMEOUT),
					                ae -> blend.getChildren().add(Hero2Button))).play();
            				playDamage();
            			} 
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());
            				refreshGame(hero1,hero2);
            			}
            		}
            	}
            	else if(hero1Casting){
            		try{
            			hero1.castSpell(herotargetspell, hero2);
            			if(herotargetspell instanceof KillCommand)
            				blend.getChildren().add(new ImageView(new Image("assets/damageH3.png")));
            			else
            				blend.getChildren().add(new ImageView(new Image("assets/damageH10.png")));
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> refreshGame(hero1,hero2))).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().add(Hero2Button))).play();
        				playDamage();
            		} 
            		catch (NotYourTurnException | NotEnoughManaException e) {
            			displayError(e.getMessage());
            			refreshGame(hero1,hero2);
            		}
            	}
            	else if(hero2Casting){
            		try{
            			hero2.castSpell(herotargetspell, hero2);
            			if(herotargetspell instanceof KillCommand)
            				blend.getChildren().add(new ImageView(new Image("assets/damageH3.png")));
            			else
            				blend.getChildren().add(new ImageView(new Image("assets/damageH10.png")));
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> refreshGame(hero1,hero2))).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().add(Hero2Button))).play();
        				playDamage();
            		} 
            		catch (NotYourTurnException | NotEnoughManaException e) {
            			displayError(e.getMessage());
            			refreshGame(hero1,hero2);
            		}
            	}
            	else if(attacker2!=null){
            		try {
						hero2.attackWithMinion(attacker2, hero2);
					} catch (CannotAttackException | NotYourTurnException
							| TauntBypassException | NotSummonedException
							| InvalidTargetException e) {
						displayError(e.getMessage());
					}
            		attacker2=null;
            	}
            	else if(attacker1!=null){
            		try {
						hero1.attackWithMinion(attacker1, hero2);
						blend.getChildren().add(new ImageView(new Image("assets/damageH"+attacker1.getAttack()+".png")));
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> heroHP2.setImage(new Image("assets.data/hero"+hero2.getCurrentHP()+".png")))).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().clear())).play();
        				new Timeline(new KeyFrame(
				                Duration.millis(PTIMEOUT),
				                ae -> blend.getChildren().add(Hero2Button))).play();
        				playDamage();
					} catch (CannotAttackException | NotYourTurnException
							| TauntBypassException | NotSummonedException
							| InvalidTargetException e) {
						displayError(e.getMessage());
					}
            		attacker1=null;
            	}
            }
        });
		Hero2Button.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	Tooltip.install(Hero2Button, new Tooltip(hero2.getName()));
	        	Hero2Button.setCursor(Cursor.HAND);
	        }
		});
		view.getHero2box().getChildren().add(blend);
		
		ImageView power1;
		String path1;
		if(hero1 instanceof Warlock)
			path1="assets/warlockPower";
		else if(hero1 instanceof Paladin)
			path1="assets/paladinPower";
		else if(hero1 instanceof Mage)
			path1="assets/magePower";
		else if(hero1 instanceof Hunter)
			path1="assets/hunterPower";
		else
			path1="assets/priestPower";
		power1=new ImageView(new Image(path1+".png"));
		power1.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	    		power1.setImage(new Image(path1+"H.png"));
        	          power1.setCursor(Cursor.HAND);
        	        }
        });
        power1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
            	if(hero1 instanceof Hunter||hero1 instanceof Paladin||hero1 instanceof Warlock){
            			try{           				
            				hero1.useHeroPower();
            				if(hero1 instanceof Hunter){
            					blend.getChildren().add(new ImageView(new Image("assets/damageH2.png")));
            					playDamage();
            					new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> refreshGame(hero1,hero2))).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend.getChildren().clear())).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend.getChildren().add(Hero2Button))).play();
                				if(power1Use){
                					new Timeline(new KeyFrame(
        					                Duration.seconds(1),
        					                ae -> hunterPower())).play();
                					power1Use=false;
                				}

                				
            				}
            				else if(hero1 instanceof Warlock){
            					blend1.getChildren().add(new ImageView(new Image("assets/damageH2.png")));
            					playDamage();
            					new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> refreshGame(hero1,hero2))).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend1.getChildren().clear())).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend1.getChildren().add(Hero1Button))).play();
                				if(power1Use){
                					warlockPower();
                					power1Use=false;
                				}
            				}
            				else{
            					refreshGame(hero1,hero2);
            					if(power1Use){
                					paladinPower();
                					power1Use=false;
            					}
            				}
            			}
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());	
            			        if(e instanceof FullHandException){
            	    				   view.getZoomBurned().setImage(new Image("assets.cards/"+((FullHandException) e).getBurned().getName()+".png",150,225,false,false));
            	    				   view.getCardBurned().setText("Card Burned:");
            	    				   new Timeline(new KeyFrame(
            	   			                Duration.millis(CTIMEOUT),
            	   			                ae -> view.getZoomBurned().setImage(null))).play();
            	    				   new Timeline(new KeyFrame(
            	      			                Duration.millis(CTIMEOUT),
            	      			                ae -> view.getCardBurned().setText(null))).play();
            	    			   }
            			     refreshGame(hero1,hero2);
            			}
            			
            	}
               else{
            	   if(model.getCurrentHero().equals(hero1)){
            		   if(hero1.getCurrentManaCrystals()>1){
            			   power1inuse=true;
            			   displayTarget();
            		   }
            		   else
            			   displayError("I don't have enough mana");
            	   }
            	   else
            		   displayError("You can not do any action in your opponent's turn");
               }  
            }
        });
        power1.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {        	        	
        	        	power1.setImage(new Image(path1+".png"));
        	        }
        });
        
        ImageView power2;
		String path2;
		if(hero2 instanceof Warlock)
			path2="assets/warlockPower";
		else if(hero2 instanceof Paladin)
			path2="assets/paladinPower";
		else if(hero2 instanceof Mage)
			path2="assets/magePower";
		else if(hero2 instanceof Hunter)
			path2="assets/hunterPower";
		else
			path2="assets/priestPower";
		power2=new ImageView(new Image(path2+".png"));
		power2.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	          power2.setImage(new Image(path2+"H.png"));
        	          power2.setCursor(Cursor.HAND);
        	        }
        });
        power2.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	        	power2.setImage(new Image(path2+".png"));
        	        }
        });
        power2.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {         	
            	if(hero2 instanceof Hunter||hero2 instanceof Paladin||hero2 instanceof Warlock){
            			try{
            				hero2.useHeroPower();
            				if(hero2 instanceof Hunter){
            					blend1.getChildren().add(new ImageView(new Image("assets/damageH2.png")));
            					playDamage();
            					new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> refreshGame(hero1,hero2))).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend1.getChildren().clear())).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend1.getChildren().add(Hero1Button))).play();
                				if(power2Use){
                					new Timeline(new KeyFrame(
        					                Duration.seconds(1),
        					                ae -> hunterPower())).play();
                					power2Use=false;
                				}
            				}
            				else if(hero2 instanceof Warlock){
            					blend.getChildren().add(new ImageView(new Image("assets/damageH2.png")));
            					playDamage();
            					new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> refreshGame(hero1,hero2))).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend.getChildren().clear())).play();
                				new Timeline(new KeyFrame(
        				                Duration.millis(PTIMEOUT),
        				                ae -> blend.getChildren().add(Hero2Button))).play();
                				if(power2Use){
                					warlockPower();
                					power2Use=false;
                				}
            				}
            				else{
            					if(power2Use){
                					paladinPower();
                					power2Use=false;
            					}
            					refreshGame(hero1,hero2);
            				}
            			}
            			catch (NotEnoughManaException | HeroPowerAlreadyUsedException
            					| NotYourTurnException | FullHandException
            					| FullFieldException | CloneNotSupportedException e) {
            				displayError(e.getMessage());	
            			        if(e instanceof FullHandException){
            	    				   view.getZoomBurned().setImage(new Image("assets.cards/"+((FullHandException) e).getBurned().getName()+".png",150,225,false,false));
            	    				   view.getCardBurned().setText("Card Burned:");
            	    				   new Timeline(new KeyFrame(
            	   			                Duration.millis(CTIMEOUT),
            	   			                ae -> view.getZoomBurned().setImage(null))).play();
            	    				   new Timeline(new KeyFrame(
            	      			                Duration.millis(CTIMEOUT),
            	      			                ae -> view.getCardBurned().setText(null))).play();
            	    			   }
            			        refreshGame(hero1,hero2);
            			}
            			
               }
               else{
            	   if(model.getCurrentHero().equals(hero2)){
            		   if(hero2.getCurrentManaCrystals()>1){
            			   power2inuse=true;
            			   displayTarget();
            		   }
            		   else
            			   displayError("I don't have enough mana");
            	   	}
            	   else
            		   displayError("You can not do any action in your opponent's turn");
              }
            }
        });
        
		view.getHero1powerbox().getChildren().add(power1);
		view.getHero2powerbox().getChildren().add(power2);
		
		ImageView endTurnButton=new ImageView(new Image("assets/endTurn.png"));
		endTurnButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
					model.endTurn();
				} 
                catch (FullHandException | CloneNotSupportedException e) {
                	displayError(e.getMessage());
    			   if(e instanceof FullHandException){
    				   view.getZoomBurned().setImage(new Image("assets.cards/"+((FullHandException) e).getBurned().getName()+".png",150,225,false,false));
    				   view.getCardBurned().setText("Card Burned:");
    				   new Timeline(new KeyFrame(
   			                Duration.millis(CTIMEOUT),
   			                ae -> view.getZoomBurned().setImage(null))).play();
    				   new Timeline(new KeyFrame(
      			                Duration.millis(CTIMEOUT),
      			                ae -> view.getCardBurned().setText(null))).play();
    			   }
                }
                if(model.getCurrentHero().equals(hero1) && turn1<3)
                	turn1++;
                else if(turn2<3)
                	turn2++;
                refreshGame(hero1,hero2);
                playendTurn();          
            }
        });
		endTurnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	          endTurnButton.setImage(new Image("assets/endTurnH.png"));
        	          endTurnButton.setCursor(Cursor.HAND);
        	        }
        });
        endTurnButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	         endTurnButton.setImage(new Image("assets/endTurn.png"));
        	        }
        });
		view.getEndturnbox().getChildren().add(endTurnButton);	
		
		if(hero1 instanceof Mage)
			Tooltip.install(power1, new Tooltip("Inflict one damage point to a specific target (a hero or a minion)."));
		else if(hero1 instanceof Hunter)
			Tooltip.install(power1, new Tooltip("Inflict two damage points to the opponent hero."));
		else if(hero1 instanceof Priest)
			Tooltip.install(power1, new Tooltip("Restore two health points to a specific target (a hero or a minion)."));
		else if(hero1 instanceof Warlock)
			Tooltip.install(power1, new Tooltip("Draw an extra card and inflict two damage points to the hero."));
		else
			Tooltip.install(power1, new Tooltip("Creates a new Silver Hand Recruit and adds it to the field."));
		
		if(hero2 instanceof Mage)
			Tooltip.install(power2, new Tooltip("Inflict one damage point to a specific target (a hero or a minion)."));
		else if(hero2 instanceof Hunter)
			Tooltip.install(power2, new Tooltip("Inflict two damage points to the opponent hero."));
		else if(hero2 instanceof Priest)
			Tooltip.install(power2, new Tooltip("Restore two health points to a specific target (a hero or a minion)."));
		else if(hero2 instanceof Warlock)
			Tooltip.install(power2, new Tooltip("Draw an extra card and inflict two damage points to the hero."));
		else
			Tooltip.install(power2, new Tooltip("Creates a new Silver Hand Recruit and adds it to the field."));
	}

	public void refreshFields(Hero hero1, Hero hero2){
		view.getHero1fieldbox().getChildren().clear();
		view.getHero2fieldbox().getChildren().clear();
		if(!hero1.getField().isEmpty()){
			for(int i=0;i<hero1.getField().size();i++){
				Minion m=hero1.getField().get(i);
				ImageView card=new ImageView(new Image("assets.field/"+m.getName()+m.isTaunt()+m.isDivine()+".png"));
				card.setEffect(null);
				ImageView attack=new ImageView(new Image("assets.data/attack"+m.getAttack()+".png"));
				ImageView hp=new ImageView(new Image("assets.data/hp"+m.getCurrentHP()+".png"));
				attack.setBlendMode(BlendMode.SRC_OVER);
				hp.setBlendMode(BlendMode.SRC_OVER);
				Group blend = new Group(card,attack,hp);
				ImageView sleeping=new ImageView(new Image("assets.field/sleeping.gif"));
				if(m.isSleeping()){
					sleeping.setBlendMode(BlendMode.SRC_OVER);
					blend.getChildren().add(sleeping);
				}
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+m.getName()+".png",150,225,false,false));
	            	        }
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          view.getZoomImage().setImage(null);
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event) {
						if(power1inuse){
							if(hero1 instanceof Priest){
								try{
									((Priest)hero1).useHeroPower(m);
									playHeal();
									if(power1Use){
			             				   priestPower();
			             				   power1Use=false;
			             			   	}
									refreshFields(hero1,hero2);
									view.getHero1manabox().getChildren().clear();
									view.getHero1manabox().getChildren().add(new ImageView(new Image("assets.data/mana"+hero1.getCurrentManaCrystals()+"x"+hero1.getTotalManaCrystals()+".png")));
								}
								catch (NotEnoughManaException | HeroPowerAlreadyUsedException
										| NotYourTurnException | FullHandException
										| FullFieldException | CloneNotSupportedException e) {
									displayError(e.getMessage());
								}
								power1inuse=false;								
							}
							else if(hero1 instanceof Mage){
								try{
									boolean divinem = m.isDivine();
									
									((Mage)hero1).useHeroPower(m);
									if(power1Use){
			             				   magePower();
			             				   power1Use=false;
			             			   	}
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(!divinem){	
										blend.getChildren().add(new ImageView(new Image("assets.data/damage1.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
		      						else{
		      							refreshGame(hero1,hero2);
		      						}
								} 
								catch (NotEnoughManaException | HeroPowerAlreadyUsedException
										| NotYourTurnException | FullHandException
										| FullFieldException | CloneNotSupportedException e) {
									displayError(e.getMessage());
								}
								power1inuse=false;
							}
						}
						else if(power2inuse){
							if(hero2 instanceof Priest){
								try{
									((Priest)hero2).useHeroPower(m);
									playHeal();
									if(power2Use){
			             				   priestPower();
			             				   power2Use=false;
			             			   	}
									refreshFields(hero1,hero2);
									view.getHero2manabox().getChildren().clear();
									view.getHero2manabox().getChildren().add(new ImageView(new Image("assets.data/mana"+hero2.getCurrentManaCrystals()+"x"+hero2.getTotalManaCrystals()+".png")));
								}
								catch (NotEnoughManaException | HeroPowerAlreadyUsedException
										| NotYourTurnException | FullHandException
										| FullFieldException | CloneNotSupportedException e) {
									displayError(e.getMessage());
								}
								power2inuse=false;
							}
							else if(hero2 instanceof Mage){
								try{
									boolean divinem = m.isDivine();
									((Mage)hero2).useHeroPower(m);
									if(power2Use){
			             				   magePower();
			             				   power2Use=false;
			             			   	}
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(!divinem){	
										blend.getChildren().add(new ImageView(new Image("assets.data/damage1.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
		      						else{
		      							refreshGame(hero1,hero2);
		      						}
								} 
								catch (NotEnoughManaException | HeroPowerAlreadyUsedException
										| NotYourTurnException | FullHandException
										| FullFieldException | CloneNotSupportedException e) {
									displayError(e.getMessage());
								}
								power2inuse=false;
								
							}
						}
						else if(hero1Casting){
							if(spellOnMinion instanceof LeechingSpell){
								try {
									hero1.castSpell((LeechingSpell)spellOnMinion, m);
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									
								} catch (NotYourTurnException
										| NotEnoughManaException e) {
									displayError(e.getMessage());
								}
								spellOnMinion=null;
								hero1Casting=false;
							}
							else{
								try {
									boolean divinem = m.isDivine();
									hero1.castSpell((MinionTargetSpell) spellOnMinion, m);
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(spellOnMinion instanceof Polymorph){
										int i = hero1.getField().indexOf(m);
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> card.setImage(new Image("assets.field/Sheepfalsefalse.png")))).play();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> hp.setImage(new Image("assets.data/hp1.png")))).play();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> attack.setImage(new Image("assets.data/attack1.png")))).play();
										((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/polymorph.gif")));		
										playPolymorph();
										new Timeline(new KeyFrame(
								                Duration.millis(STIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof KillCommand && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage5.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof Pyroblast && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage10.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else{
										if(spellOnMinion instanceof DivineSpirit||spellOnMinion instanceof SealOfChampions)
											playHeal();
										refreshGame(hero1,hero2);
									}
								} catch (NotYourTurnException
										| NotEnoughManaException
										| InvalidTargetException e) {
									displayError(e.getMessage());
									refreshGame(hero1,hero2);
								}								
							}
						}
						else if(hero2Casting){
							if(spellOnMinion instanceof LeechingSpell){
								try {
									hero2.castSpell((LeechingSpell)spellOnMinion, m);
									blend.getChildren().clear();
									blend.getChildren().add(card);
									card.setImage(new Image("assets.field/explode.gif"));
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshGame(hero1,hero2))).play();
									minionDeath();
									
								} catch (NotYourTurnException
										| NotEnoughManaException e) {
									displayError(e.getMessage());
								}
								spellOnMinion=null;
								hero1Casting=false;							
							}
							else{
								try {
									boolean divinem = m.isDivine();
									hero2.castSpell((MinionTargetSpell) spellOnMinion, m);
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(spellOnMinion instanceof Polymorph){
										int i = hero1.getField().indexOf(m);
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> card.setImage(new Image("assets.field/Sheepfalsefalse.png")))).play();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> hp.setImage(new Image("assets.data/hp1.png")))).play();
										playPolymorph();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> attack.setImage(new Image("assets.data/attack1.png")))).play();
										((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/polymorph.gif")));		
										new Timeline(new KeyFrame(
								                Duration.millis(STIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof KillCommand && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage5.png")));
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										playDamage();
									}
									else if(spellOnMinion instanceof Pyroblast && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage10.png")));
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										playDamage();
									}
									else{
										if(spellOnMinion instanceof DivineSpirit||spellOnMinion instanceof SealOfChampions)
											playHeal();
										refreshGame(hero1,hero2);
										
									}
								} catch (NotYourTurnException
										| NotEnoughManaException
										| InvalidTargetException e) {
									displayError(e.getMessage());
									refreshGame(hero1,hero2);
								}								
							}
						}
						else if(attacker1!=null){
							try {
								hero1.attackWithMinion(attacker1,m);
							} catch (CannotAttackException
									| NotYourTurnException
									| TauntBypassException
									| InvalidTargetException
									| NotSummonedException e) {
								displayError(e.getMessage());
							}
							attacker2=null;
							attacker1=null;;
						}
						else if (attacker2!=null){
							try {
								boolean divinem = m.isDivine();
								boolean divinea = attacker2.isDivine();
								int i = hero2.getField().indexOf(attacker2);
								hero2.attackWithMinion(attacker2, m);
								if(m.getCurrentHP()==0){
									blend.getChildren().clear();
									blend.getChildren().add(card);
									card.setImage(new Image("assets.field/explode.gif"));
									minionDeath();
									if(attacker2.getCurrentHP()==0){									
										Group kk = (Group)view.getHero2fieldbox().getChildren().get(i);
										kk.getChildren().clear();
										kk.getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
									}
									else if(!divinea){
										Group kk = (Group)view.getHero2fieldbox().getChildren().get(i);
										kk.getChildren().add(new ImageView(new Image("assets.data/damage"+m.getAttack()+".png")));
										playDamage();
									}
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshFields(hero1,hero2))).play();							
								}
								else if(attacker2.getCurrentHP()==0){
									Group kk = (Group)view.getHero2fieldbox().getChildren().get(i);
									kk.getChildren().clear();
									kk.getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
									minionDeath();
									if(!divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage"+attacker2.getAttack()+".png")));
										playDamage();
									}
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshFields(hero1,hero2))).play();
								}
								else{
									if(divinea && divinem)
										refreshFields(hero1,hero2);
									else{
										if(!divinea){
											Group kk = (Group)view.getHero2fieldbox().getChildren().get(i);
											kk.getChildren().add(new ImageView(new Image("assets.data/damage"+m.getAttack()+".png")));
										}
										if(!divinem)
											blend.getChildren().add(new ImageView(new Image("assets.data/damage"+attacker2.getAttack()+".png")));
										new Timeline(new KeyFrame(
												Duration.millis(TIMEOUT),
												ae -> refreshFields(hero1,hero2))).play();
										playDamage();
									}
								}
							} catch (CannotAttackException
									| NotYourTurnException
									| TauntBypassException
									| InvalidTargetException
									| NotSummonedException e) {
								displayError(e.getMessage());
							}
							attacker2=null;
							attacker1=null;
						}
						else if(attacker1==null){
							attacker1=m;
							displayTarget();
						}
						
					}
	        });
			view.getHero1fieldbox().getChildren().add(blend);
			}
		}
		if(!hero2.getField().isEmpty()){
			for(int i=0;i<hero2.getField().size();i++){
				Minion m=hero2.getField().get(i);
				ImageView card=new ImageView(new Image("assets.field/"+m.getName()+m.isTaunt()+m.isDivine()+".png"));
				card.setEffect(null);
				ImageView attack=new ImageView(new Image("assets.data/attack"+m.getAttack()+".png"));
				ImageView hp=new ImageView(new Image("assets.data/hp"+m.getCurrentHP()+".png"));
				attack.setBlendMode(BlendMode.SRC_OVER);
				hp.setBlendMode(BlendMode.SRC_OVER);
				Group blend = new Group(card,attack,hp);
				ImageView sleeping=new ImageView(new Image("assets.field/sleeping.gif"));
				if(m.isSleeping()){
					sleeping.setBlendMode(BlendMode.SRC_OVER);
					blend.getChildren().add(sleeping);
				}
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+m.getName()+".png",150,225,false,false));
	            	        }
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          view.getZoomImage().setImage(null);
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
		            @Override
		            public void handle(MouseEvent event){
		               if(power1inuse){
		            	   if(hero1 instanceof Priest){
		            		   try{
		            			   ((Priest)hero1).useHeroPower(m);
		            			   playHeal();
		            			   if(power1Use){
		             				   priestPower();
		             				   power1Use=false;
		             			   	}
		            		   }
		            		   catch (NotEnoughManaException | HeroPowerAlreadyUsedException
		            				   | NotYourTurnException | FullHandException
		            				   | FullFieldException | CloneNotSupportedException e) {
		            			   displayError(e.getMessage());
		            		   }
	            			   refreshGame(hero1,hero2);
		            	   }
		            	   else if(hero1 instanceof Mage){
		            		  try{
		            			  boolean divinem = m.isDivine();
		      						((Mage)hero1).useHeroPower(m);
		      						if(power1Use){
		              				   magePower();
		              				   power1Use=false;
		              			   	}
		      						if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
		      						else if(!divinem){	
										blend.getChildren().add(new ImageView(new Image("assets.data/damage1.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
		      						else{
		      							refreshGame(hero1,hero2);
		      						}
		            		  } 
		            		  catch (NotEnoughManaException | HeroPowerAlreadyUsedException
		            				  | NotYourTurnException | FullHandException
		      							| FullFieldException | CloneNotSupportedException e) {
		            			  displayError(e.getMessage());
		            			  refreshGame(hero1,hero2);
		      					}	      						
		            	   }
		               }
		               else if(power2inuse){
		            	   if(hero2 instanceof Priest){
		            		   try{
		            			   ((Priest)hero2).useHeroPower(m);
		            			   playHeal();
		            			   if(power2Use){
		             				   priestPower();
		             				   power2Use=false;
		             			   	}
		            		   }
		            		   catch (NotEnoughManaException | HeroPowerAlreadyUsedException
		            				   | NotYourTurnException | FullHandException
		            				   | FullFieldException | CloneNotSupportedException e) {
		            			   displayError(e.getMessage());
		            		   }
	            			   refreshGame(hero1,hero2);
		            	   }
		            	   else if(hero2 instanceof Mage){
		            		   try{
		            			    boolean divinem = m.isDivine();
		      						((Mage)hero2).useHeroPower(m);
		      						if(power2Use){
		              				   magePower();
		              				   power2Use=false;
		              			   	}
		      						if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
		      						else if(!divinem){	
										blend.getChildren().add(new ImageView(new Image("assets.data/damage1.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
		      						else{
		      							refreshGame(hero1,hero2);
		      						}
		      					} 
		            		  catch (NotEnoughManaException | HeroPowerAlreadyUsedException
		            				  | NotYourTurnException | FullHandException
		      							| FullFieldException | CloneNotSupportedException e) {
		            			  displayError(e.getMessage());
		            			  refreshGame(hero1,hero2);
		      					}   						
		            	   }
		               }
		               else if(hero1Casting){
							if(spellOnMinion instanceof LeechingSpell){
								try {
									hero1.castSpell((LeechingSpell)spellOnMinion, m);
									blend.getChildren().clear();
									blend.getChildren().add(card);
									card.setImage(new Image("assets.field/explode.gif"));
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshGame(hero1,hero2))).play();
									minionDeath();
								} catch (NotYourTurnException
										| NotEnoughManaException e) {
									displayError(e.getMessage());
								}
								spellOnMinion=null;
								hero1Casting=false;
							}
							else{
								try {
									boolean divinem = m.isDivine();
									hero1.castSpell((MinionTargetSpell) spellOnMinion, m);
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(spellOnMinion instanceof Polymorph){
										int i = hero2.getField().indexOf(m);
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> card.setImage(new Image("assets.field/Sheepfalsefalse.png")))).play();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> hp.setImage(new Image("assets.data/hp1.png")))).play();
										playPolymorph();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> attack.setImage(new Image("assets.data/attack1.png")))).play();
										((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/polymorph.gif")));		
										new Timeline(new KeyFrame(
								                Duration.millis(STIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof KillCommand && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage5.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof Pyroblast && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage10.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else
										refreshGame(hero1,hero2);
								} catch (NotYourTurnException
										| NotEnoughManaException
										| InvalidTargetException e) {
									displayError(e.getMessage());
									refreshGame(hero1,hero2);
								}
							}
						}
						else if(hero2Casting){
							if(spellOnMinion instanceof LeechingSpell){
								try {
									hero2.castSpell((LeechingSpell)spellOnMinion, m);
									blend.getChildren().clear();
									blend.getChildren().add(card);
									card.setImage(new Image("assets.field/explode.gif"));
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshGame(hero1,hero2))).play();
									minionDeath();
								} catch (NotYourTurnException
										| NotEnoughManaException e) {
									displayError(e.getMessage());
								}
								spellOnMinion=null;
								hero2Casting=false;
							}
							else{
								try {
									boolean divinem = m.isDivine();
									hero2.castSpell((MinionTargetSpell) spellOnMinion, m);
									if(m.getCurrentHP()==0){
										blend.getChildren().clear();
										blend.getChildren().add(card);
										card.setImage(new Image("assets.field/explode.gif"));
										new Timeline(new KeyFrame(
								                Duration.millis(TIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
										minionDeath();
									}
									else if(spellOnMinion instanceof Polymorph ){
										int i = hero2.getField().indexOf(m);
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> card.setImage(new Image("assets.field/Sheepfalsefalse.png")))).play();										
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> hp.setImage(new Image("assets.data/hp1.png")))).play();
										playPolymorph();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> attack.setImage(new Image("assets.data/attack1.png")))).play();
										((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/polymorph.gif")));		
										new Timeline(new KeyFrame(
								                Duration.millis(STIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof KillCommand && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage5.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else if(spellOnMinion instanceof Pyroblast && !divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage10.png")));
										playDamage();
										new Timeline(new KeyFrame(
								                Duration.millis(PTIMEOUT),
								                ae -> refreshGame(hero1,hero2))).play();
									}
									else{
										if(spellOnMinion instanceof DivineSpirit||spellOnMinion instanceof SealOfChampions)
											playHeal();
										refreshGame(hero1,hero2);
										
									}
								} catch (NotYourTurnException
										| NotEnoughManaException
										| InvalidTargetException e) {
									displayError(e.getMessage());
									refreshGame(hero1,hero2);
								}
							}
						}
						else if(attacker2!=null){
							try {
								hero2.attackWithMinion(attacker2, m);
							} catch (CannotAttackException
									| NotYourTurnException
									| TauntBypassException
									| InvalidTargetException
									| NotSummonedException e) {
								displayError(e.getMessage());
							}
							attacker2=null;
							attacker1=null;
						}
						else if (attacker1!=null){
							try {
								boolean divinem = m.isDivine();
								boolean divinea = attacker1.isDivine();
								int i = hero1.getField().indexOf(attacker1);
								hero1.attackWithMinion(attacker1, m);
								if(m.getCurrentHP()==0){
									blend.getChildren().clear();
									blend.getChildren().add(card);
									card.setImage(new Image("assets.field/explode.gif"));
									minionDeath();
									if(attacker1.getCurrentHP()==0){									
										Group kk = (Group)view.getHero1fieldbox().getChildren().get(i);
										kk.getChildren().clear();
										kk.getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
									}
									else if(!divinea){
										Group kk = (Group)view.getHero1fieldbox().getChildren().get(i);
										kk.getChildren().add(new ImageView(new Image("assets.data/damage"+m.getAttack()+".png")));
										playDamage();
									}
									new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshFields(hero1,hero2))).play();							
								}
								else if(attacker1.getCurrentHP()==0){
									Group kk = (Group)view.getHero1fieldbox().getChildren().get(i);
									kk.getChildren().clear();
									kk.getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
									minionDeath();
									if(!divinem){
										blend.getChildren().add(new ImageView(new Image("assets.data/damage"+attacker1.getAttack()+".png")));
										playDamage();
									}
										new Timeline(new KeyFrame(
							                Duration.millis(TIMEOUT),
							                ae -> refreshFields(hero1,hero2))).play();
								}
								else{
									if(divinea && divinem)
										refreshFields(hero1,hero2);
									else{
										if(!divinea){
											Group kk = (Group)view.getHero1fieldbox().getChildren().get(i);
											kk.getChildren().add(new ImageView(new Image("assets.data/damage"+m.getAttack()+".png")));
										}
										if(!divinem)
											blend.getChildren().add(new ImageView(new Image("assets.data/damage"+attacker1.getAttack()+".png")));
										new Timeline(new KeyFrame(
												Duration.millis(TIMEOUT),
												ae -> refreshFields(hero1,hero2))).play();
										playDamage();
									}
								}
							} catch (CannotAttackException
									| NotYourTurnException
									| TauntBypassException
									| InvalidTargetException
									| NotSummonedException e) {
								displayError(e.getMessage());
							}
							attacker2=null;
							attacker1=null;
						}
						else if(attacker2==null){
							attacker2=m;
							displayTarget();
						}
		            }
				});
				view.getHero2fieldbox().getChildren().add(blend);
			}
		}
	}
		
	
	public void addToHands(Hero hero1,Hero hero2){
		for(int i=0;i<hero1.getHand().size();i++){
			if(model.getOpponent().equals(hero1)){
				ImageView c=new ImageView(new Image("assets/cardBack.png",50,75,false,false));
				c.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          c.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets/cardBack.png",150,225,false,false));
	            	        }
	            });
	            c.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	            	        }
	            });
				view.getHero1handbox().getChildren().add(c);
			}
			else if(hero1.getHand().get(i) instanceof Minion){
				Minion m=(Minion) hero1.getHand().get(i);
				ImageView card=new ImageView(new Image("assets.cards/"+m.getName()+".png",50,75,false,false));
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+m.getName()+".png",150,225,false,false));
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                @Override
	                public void handle(MouseEvent event) {
	                	try{
	                		hero1.playMinion(m);
							view.getHero1fieldbox().getChildren().add(new ImageView(new Image("assets.field/summon.gif")));
							new Timeline(new KeyFrame(
					                Duration.millis(STIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
							playSummon();
						} 
	                	catch (NotYourTurnException | NotEnoughManaException
								| FullFieldException e) {
	                		displayError(e.getMessage());
						}
	                }
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	            	        }
	            });
				view.getHero1handbox().getChildren().add(card);
			}
			else if(hero1.getHand().get(i) instanceof Spell){
				Spell s=(Spell)hero1.getHand().get(i);
				ImageView card=new ImageView(new Image("assets.cards/"+s.getName()+".png",50,75,false,false));
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+s.getName()+".png",150,225,false,false));
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                @Override
	                public void handle(MouseEvent event) {
	                	if(s instanceof FieldSpell){
	                		try {
								hero1.castSpell((FieldSpell) s);
								refreshGame(hero1,hero2);
								playHeal();
							} catch (NotYourTurnException
									| NotEnoughManaException e) {
								displayError(e.getMessage());
							}
	                	}
	                	else if(s instanceof AOESpell){
	    	                try {
	    	                	int x = hero1.getField().size();
	                			int y = hero2.getField().size();
	                			int[] field = new int[hero2.getField().size()];
	                			for(int i=0;i<hero2.getField().size();i++)
	                				field[i]=hero2.getField().get(i).getCurrentHP();
	                			boolean[] field1=new boolean[hero2.getField().size()];
	                			for(int i=0;i<hero2.getField().size();i++)
	                				field1[i]=hero2.getField().get(i).isDivine();
	    	               		hero1.castSpell((AOESpell)s, hero2.getField());
	    	               		if(s instanceof TwistingNether){
	    	               			view.getHero1fieldbox().getChildren().clear();
	    	               			view.getHero2fieldbox().getChildren().clear();
	    	               			for(int i=0;i<x;i++)
	    	               				view.getHero1fieldbox().getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               			for(int i=0;i<y;i++)
	    	               				view.getHero2fieldbox().getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               			AOESpellSound();
	    	               		}
	    	               		else if(s instanceof HolyNova && field.length!=0){
	    	               			AOESpellSound();
	    	               			for(int i = 0;i<field.length;i++){
	    	               				if(field[i]<3 && !field1[i]){
	    	               					((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().clear();
	    	               					((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[i])
	    	               						((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.data/damage2.png")));
	    	               				}
	    	               			}
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               		}
	    	               		else if(s instanceof Flamestrike && field.length!=0){
	    	               			AOESpellSound();
	    	               			for(int i = 0;i<field.length;i++){
	    	               				if(field[i]<5 && !field1[i]){
	    	               					((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().clear();
	    	               					((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[i])
	    	               						((Group)view.getHero2fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.data/damage4.png")));
	    	               				}
	    	               			}
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               		}
	    	               		else if(s instanceof MultiShot){
	    	               			AOESpellSound();
	    	               			int r1 = ((MultiShot) s).getR1();
	    	               			int r2 = ((MultiShot) s).getR2();
	    	               			if(field.length!=0){
	    	               				if(field[r1]<5 && !field1[r1]){
	    	               					((Group)view.getHero2fieldbox().getChildren().get(r1)).getChildren().clear();
	    	               					((Group)view.getHero2fieldbox().getChildren().get(r1)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[r1])
	    	               						((Group)view.getHero2fieldbox().getChildren().get(r1)).getChildren().add(new ImageView(new Image("assets.data/damage3.png")));
	    	               				}
	    	               				if(field[r2]<5 && !field1[r2]){
	    	               					((Group)view.getHero2fieldbox().getChildren().get(r2)).getChildren().clear();
	    	               					((Group)view.getHero2fieldbox().getChildren().get(r2)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[r2])
	    	               						((Group)view.getHero2fieldbox().getChildren().get(r2)).getChildren().add(new ImageView(new Image("assets.data/damage3.png")));
	    	               				}
	    	               				new Timeline(new KeyFrame(
	    	               						Duration.millis(TIMEOUT),
	    	               						ae -> refreshGame(hero1,hero2))).play();
	    	               			}
	    	               			else{
	    	               				if(s instanceof CurseOfWeakness)
	    	               					playCurse();
	    	               				refreshGame(hero1,hero2);
	    	               			}
	    	               		}
	    	               		else
	    	               			refreshGame(hero1,hero2);
	    	               	} catch (NotYourTurnException
									| NotEnoughManaException e) {
	    	               		displayError(e.getMessage());
							}
		                }
	                	else if(s instanceof HeroTargetSpell){
	                		if(hero1.getCurrentManaCrystals()>=s.getManaCost()){
	                			hero1Casting=true;
	                			herotargetspell=(HeroTargetSpell) s;
	                			if(s instanceof MinionTargetSpell)
	                				spellOnMinion=s;
	                			displayTarget();
	                		}
	                		else
	                			displayError("I don't have enough mana");
	               		}
	                	else{
	                		if(hero1.getCurrentManaCrystals()>=s.getManaCost()){
	                			spellOnMinion=s;
	                			hero1Casting=true;
	                			displayTarget();
	                		}
	                		else
	                			displayError("I don't have enough mana");
	                	}
	                }	                
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	            	        }
	            });
				view.getHero1handbox().getChildren().add(card);
			}
		}
		
		for(int i=0;i<hero2.getHand().size();i++){
			if(model.getOpponent().equals(hero2)){
				ImageView c=new ImageView(new Image("assets/cardBack.png",50,75,false,false));
				c.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	           	        	 c.setCursor(Cursor.HAND);
	           	        	 view.getZoomImage().setImage(new Image("assets/cardBack.png",150,225,false,false));
	           	        }
	            });
	            c.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	           	        }
	            });
				view.getHero2handbox().getChildren().add(c);
			}
			else if(hero2.getHand().get(i) instanceof Minion){
				Minion m=(Minion) hero2.getHand().get(i);
				ImageView card=new ImageView(new Image("assets.cards/"+m.getName()+".png",50,75,false,false));
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+m.getName()+".png",150,225,false,false));
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                @Override
	                public void handle(MouseEvent event) {
	                	try{	          
							hero2.playMinion(m);
							view.getHero2fieldbox().getChildren().add(new ImageView(new Image("assets.field/summon.gif")));
							new Timeline(new KeyFrame(
					                Duration.millis(STIMEOUT),
					                ae -> refreshGame(hero1,hero2))).play();
							playSummon();
							
						} catch (NotYourTurnException | NotEnoughManaException
								| FullFieldException e) {
							displayError(e.getMessage());
						}
	                	
	                }
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	            	        }
	            });
				view.getHero2handbox().getChildren().add(card);
			}
			else if(hero2.getHand().get(i) instanceof Spell){
				Spell s=(Spell)hero2.getHand().get(i);
				ImageView card=new ImageView(new Image("assets.cards/"+s.getName()+".png",50,75,true,false));
				card.addEventHandler(MouseEvent.MOUSE_ENTERED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	          card.setCursor(Cursor.HAND);
	            	          view.getZoomImage().setImage(new Image("assets.cards/"+s.getName()+".png",150,225,true,false));
	            	        }
	            });
				card.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                @Override
	                public void handle(MouseEvent event) {
	                	if(s instanceof FieldSpell){
	                		try{
								hero2.castSpell((FieldSpell) s);
								refreshGame(hero1,hero2);
								playHeal();
							} 
	                		catch (NotYourTurnException|NotEnoughManaException e) {
	                			displayError(e.getMessage());
							}
	                	}
	                	else if(s instanceof AOESpell){
	                		try{
	                			int x = hero1.getField().size();
	                			int y = hero2.getField().size();
	                			int[] field = new int[hero1.getField().size()];
	                			for(int i=0;i<hero1.getField().size();i++)
	                				field[i]=hero1.getField().get(i).getCurrentHP();
	                			boolean[] field1=new boolean[hero1.getField().size()];
	                			for(int i=0;i<hero1.getField().size();i++)
	                				field1[i]=hero1.getField().get(i).isDivine();
	    	               		hero2.castSpell((AOESpell)s, hero1.getField());
	    	               		if(s instanceof TwistingNether){
	    	               			view.getHero1fieldbox().getChildren().clear();
	    	               			view.getHero2fieldbox().getChildren().clear();
	    	               			for(int i=0;i<x;i++)
	    	               				view.getHero1fieldbox().getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               			for(int i=0;i<y;i++)
	    	               				view.getHero2fieldbox().getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               			AOESpellSound();
	    	               		}
	    	               		else if(s instanceof HolyNova && field.length!=0){
	    	               			for(int i = 0;i<field.length;i++){
	    	               				if(field[i]<3 && !field1[i]){
	    	               					((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().clear();
	    	               					((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[i])
	    	               						((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.data/damage2.png")));
	    	               				}
	    	               			}
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               			AOESpellSound();
	    	               		}
	    	               		else if(s instanceof Flamestrike && field.length!=0){
	    	               			for(int i = 0;i<field.length;i++){
	    	               				if(field[i]<5 && !field1[i]){
	    	               					((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().clear();
	    	               					((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[i])
	    	               						((Group)view.getHero1fieldbox().getChildren().get(i)).getChildren().add(new ImageView(new Image("assets.data/damage4.png")));
	    	               				}
	    	               			}
	    	               			new Timeline(new KeyFrame(
        	      			                Duration.millis(TIMEOUT),
        	      			                ae -> refreshGame(hero1,hero2))).play();
	    	               			AOESpellSound();
	    	               		}
	    	               		else if(s instanceof MultiShot){
	    	               			int r1 = ((MultiShot) s).getR1();
	    	               			int r2 = ((MultiShot) s).getR2();
	    	               			if(field.length!=0){
	    	               				if(field[r1]<5 && !field1[r1]){
	    	               					((Group)view.getHero1fieldbox().getChildren().get(r1)).getChildren().clear();
	    	               					((Group)view.getHero1fieldbox().getChildren().get(r1)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[r1])
	    	               						((Group)view.getHero1fieldbox().getChildren().get(r1)).getChildren().add(new ImageView(new Image("assets.data/damage3.png")));
	    	               				}
	    	               				if(field[r2]<5 && !field1[r2]){
	    	               					((Group)view.getHero1fieldbox().getChildren().get(r2)).getChildren().clear();
	    	               					((Group)view.getHero1fieldbox().getChildren().get(r2)).getChildren().add(new ImageView(new Image("assets.field/explode.gif")));
	    	               				}
	    	               				else{
	    	               					if(!field1[r2])
	    	               						((Group)view.getHero1fieldbox().getChildren().get(r2)).getChildren().add(new ImageView(new Image("assets.data/damage3.png")));
	    	               				}
	    	               				new Timeline(new KeyFrame(
	    	               						Duration.millis(TIMEOUT),
	    	               						ae -> refreshGame(hero1,hero2))).play();
	    	               			}
	    	               			else
	    	               				refreshGame(hero1,hero2);
	    	               			AOESpellSound();
	    	               		}
	    	               		else{
	    	               			if(s instanceof CurseOfWeakness)
	    	               				playCurse();
	    	               			refreshGame(hero1,hero2);
	    	               		}
							} 
	                		catch (NotYourTurnException	| NotEnoughManaException e) {
	                			displayError(e.getMessage());
							}
		                }
	                	else if(s instanceof HeroTargetSpell){
	                		if(hero2.getCurrentManaCrystals()>=s.getManaCost()){
	                			hero2Casting=true;
	                			herotargetspell=(HeroTargetSpell) s;
	                			if(s instanceof MinionTargetSpell)
	                				spellOnMinion=s;
	                			displayTarget();
	                		}
	                		else
	                			displayError("I don't have enough mana");
                		}
	                	else{
	                		if(hero2.getCurrentManaCrystals()>=s.getManaCost()){
	                			spellOnMinion=s;
	                			hero2Casting=true;
	                			displayTarget();
	                		}
	                		else
	                			displayError("I don't have enough mana");
	                	}
	                }
	            });
				card.addEventHandler(MouseEvent.MOUSE_EXITED, 
	            	    new EventHandler<MouseEvent>() {
	            	        @Override public void handle(MouseEvent e) {
	            	         view.getZoomImage().setImage(null);
	            	        }
	            });
				view.getHero2handbox().getChildren().add(card);
			}
		}
	}

	@Override
	public void onGameOver() {
		view.getClip().close();
		clip.close();
		File f;
		if(model.getFirstHero().getCurrentHP()>=1)
			f = new File("src/assets/gameOver1.mp4");
		else
			f = new File("src/assets/gameOver2.mp4");
    	Media media = new Media(f.toURI().toString());  
    	MediaPlayer mediaPlayer = new MediaPlayer(media);
    	MediaView mediaView = new MediaView(mediaPlayer);
    	mediaPlayer.setAutoPlay(true);
    	mediaView.fitWidthProperty().bind(view.getStage().widthProperty());
    	mediaView.fitHeightProperty().bind(view.getStage().heightProperty());
    	Group root = new Group();  
        root.getChildren().add(mediaView);
        view.getStage().setScene(new Scene(root,Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight()));
    	view.getStage().setFullScreen(true);
        view.getStage().setResizable(false);
        view.getStage().toFront();
        mediaPlayer.setOnEndOfMedia(() -> {
        	new MainMenu();
        	
            view.getStage().hide();          
        });
	}
	
	public void displayTarget(){		
		Text message = new Text("Choose Your Target");
		message.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		message.setFill(Paint.valueOf("White"));
		message.setStroke(Color.BLACK);
		view.getTarget().getContent().clear();
		view.getTarget().getContent().add(message);
		view.getTarget().show(view.getStage());
	        new Timeline(new KeyFrame(
	                Duration.millis(TIMEOUT),
	                ae -> view.getTarget().hide())).play();
	}
	public void displayError(String error){
		view.getTarget().getContent().clear();
		Label message = new Label(error);
		message.setGraphic(new ImageView(new Image("assets/redX.png",30,30,false,false)));
		message.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		message.setTextFill(Paint.valueOf("White"));
		view.getException().getContent().clear();
		view.getException().getContent().add(message);
		view.getException().show(view.getStage());
	        new Timeline(new KeyFrame(
	                Duration.millis(TIMEOUT),
	                ae -> view.getException().hide())).play();
	        if(model.getCurrentHero() instanceof Hunter && error.equals("I don't have enough mana")){
				playSound("src/assets.sounds/hunterMana.wav");
			}
			else if(model.getCurrentHero() instanceof Hunter && error.equals("I already used my hero power")){
				playSound("src/assets.sounds/hunterPowerUsed.wav");
			}
			else if(model.getCurrentHero() instanceof Hunter && error.equals("A minion with taunt is in the way")){
				playSound("src/assets.sounds/hunterTaunt.wav");
			}
			else if(model.getCurrentHero() instanceof Hunter && error.equals("No space for this minion")){
				playSound("src/assets.sounds/hunterField.wav");
			}
			else if(model.getCurrentHero() instanceof Mage && error.equals("I don't have enough mana")){
				playSound("src/assets.sounds/mageMana.wav");
			}
			else if(model.getCurrentHero() instanceof Mage && error.equals("Give this minion a turn to get ready")){
				playSound("src/assets.sounds/mageSleeping.wav");
			}
			else if(model.getCurrentHero() instanceof Mage && error.equals("A minion with taunt is in the way")){
				playSound("src/assets.sounds/mageTaunt.wav");
			}
			else if(model.getCurrentHero() instanceof Mage && error.equals("No space for this minion")){
				playSound("src/assets.sounds/mageField.wav");
			}
			else if(model.getCurrentHero() instanceof Mage && !error.equals("You can not do any action in your opponent's turn")){
				playSound("src/assets.sounds/mageGeneral.wav");
			}
			else if(model.getCurrentHero() instanceof Paladin && error.equals("No space for this minion")){
				playSound("src/assets.sounds/paladinField.wav");			
			}
			else if(model.getCurrentHero() instanceof Paladin && error.equals("I don't have enough mana")){
				playSound("src/assets.sounds/paladinMana.wav");
			}
			else if(model.getCurrentHero() instanceof Paladin && error.equals("Give this minion a turn to get ready")){
				playSound("src/assets.sounds/paladinSleeping.wav");
			}
			else if(model.getCurrentHero() instanceof Paladin && error.equals("A minion with taunt is in the way")){
				playSound("src/assets.sounds/paladinTaunt.wav");
			}
			else if(model.getCurrentHero() instanceof Priest && error.equals("A minion with taunt is in the way")){
				playSound("src/assets.sounds/priestTaunt.wav");
			}
			else if(model.getCurrentHero() instanceof Priest && error.equals("This minion has already attacked")){
				playSound("src/assets.sounds/priestAlreadyAttacked.wav");
			}
			else if(model.getCurrentHero() instanceof Priest && error.equals("No space for this minion")){
				playSound("src/assets.sounds/priestField.wav");
			}
			else if(model.getCurrentHero() instanceof Priest && error.equals("Give this minion a turn to get ready")){
				playSound("src/assets.sounds/priestSleeping.wav");
			}
			else if(model.getCurrentHero() instanceof Priest && !error.equals("You can not do any action in your opponent's turn")){
				playSound("src/assets.sounds/priestGeneral.wav");
			}
			else if(model.getCurrentHero() instanceof Warlock && error.equals("I don't have enough mana")){
				playSound("src/assets.sounds/warlockMana.wav");
			}
			else if(model.getCurrentHero() instanceof Warlock && error.equals("I already used my hero power")){
				playSound("src/assets.sounds/warlockPowerUsed.wav");
			}
			else if(model.getCurrentHero() instanceof Warlock && error.equals("A minion with taunt is in the way")){
				playSound("src/assets.sounds/warlockTaunt.wav");
			}
	}
	public static void playStart(){
  		File musicPath = new File("src/assets.sounds/start2.wav");
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
	public static void playStartEnd(){
  		File musicPath = new File("src/assets.sounds/startfinish.wav");
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
	public static void playWelcome(Hero hero1, Hero hero2){
  		File musicPath = new File("src/assets.sounds/welcome.wav");
  		try{
  			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
  			Clip clip = AudioSystem.getClip();
  			clip.open(audioInput);
  			clip.start();
  			new Timeline(new KeyFrame(
						Duration.seconds(4.2),
						ae -> playStart())).play();
  			if(hero1 instanceof Warlock){
  				new Timeline(new KeyFrame(
  						Duration.seconds(W),
  						ae -> playWarlock())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+warlock),
  						ae -> playVersus())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+warlock+V),
  						ae -> playHero2(hero2))).play();
  			}
  			else if(hero1 instanceof Mage){
  				new Timeline(new KeyFrame(
  						Duration.seconds(W),
  						ae -> playMage())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+mage),
  						ae -> playVersus())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+mage+V),
  						ae -> playHero2(hero2))).play();
  			}
  			else if(hero1 instanceof Hunter){
  				new Timeline(new KeyFrame(
  						Duration.seconds(W),
  						ae -> playHunter())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+hunter),
  						ae -> playVersus())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+hunter+V),
  						ae -> playHero2(hero2))).play();
  			}
  			else if(hero1 instanceof Paladin){
  				new Timeline(new KeyFrame(
  						Duration.seconds(W),
  						ae -> playPaladin())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+paladin),
  						ae -> playVersus())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+paladin+V),
  						ae -> playHero2(hero2))).play();
  			}
  			else{
  				new Timeline(new KeyFrame(
  						Duration.seconds(W),
  						ae -> playPriest())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+priest),
  						ae -> playVersus())).play();
  				new Timeline(new KeyFrame(
  						Duration.seconds(W+priest+V),
  						ae -> playHero2(hero2))).play();
  			}
  		}
  		catch(Exception e){
  			e.printStackTrace();;
  		}
  	}
	
	public static void playHero2(Hero hero2){
		if(hero2 instanceof Warlock){
			playWarlock();
			new Timeline(new KeyFrame(
						Duration.seconds(warlock),
						ae -> playStartEnd())).play();
		}
		else if(hero2 instanceof Priest){
			playPriest();
			new Timeline(new KeyFrame(
					Duration.seconds(priest),
					ae -> playStartEnd())).play();
		}
		else if(hero2 instanceof Mage){
			playMage();
			new Timeline(new KeyFrame(
					Duration.seconds(mage),
					ae -> playStartEnd())).play();
		}
		else if (hero2 instanceof Paladin){
			playPaladin();
			new Timeline(new KeyFrame(
					Duration.seconds(paladin),
					ae -> playStartEnd())).play();
		}
		else{
			playHunter();
			new Timeline(new KeyFrame(
					Duration.seconds(hunter),
					ae -> playStartEnd())).play();
		}
	}
	public static void playVersus(){
  		File musicPath = new File("src/assets.sounds/versus.wav");
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
  		File musicPath = new File("src/assets.sounds/warlockG.wav");
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
  		File musicPath = new File("src/assets.sounds/mageG.wav");
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
  		File musicPath = new File("src/assets.sounds/paladinG.wav");
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
  		File musicPath = new File("src/assets.sounds/priestG.wav");
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
  		File musicPath = new File("src/assets.sounds/hunterG.wav");
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
	public static void hunterPower(){
  		File musicPath = new File("src/assets.sounds/hunterPower.wav");
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
	public static void warlockPower(){
  		File musicPath = new File("src/assets.sounds/warlockPower.wav");
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
	public static void magePower(){
  		File musicPath = new File("src/assets.sounds/magePower.wav");
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
	public static void paladinPower(){
  		File musicPath = new File("src/assets.sounds/paladinPower.wav");
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
	public static void priestPower(){
  		File musicPath = new File("src/assets.sounds/priestPower.wav");
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
	public static void playendTurn(){
  		File musicPath = new File("src/assets.sounds/endTurn.wav");
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
	public static void playSummon(){
  		File musicPath = new File("src/assets.sounds/summon.wav");
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
	public static void playDamage(){
  		File musicPath = new File("src/assets.sounds/damage.wav");
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
	
	public static void minionDeath(){
  		File musicPath = new File("src/assets.sounds/minionDeath.wav");
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
	
	public static void playPolymorph(){
  		File musicPath = new File("src/assets.sounds/polymorph.wav");
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
	
	public static void playCurse(){
  		File musicPath = new File("src/assets.sounds/curseofweakness.wav");
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
	
	public static void playSound(String filepath){
  		File musicPath = new File(filepath);
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

	public static void warlockTurn(){
  		File musicPath = new File("src/assets.sounds/warlockR.wav");
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
	public static void mageTurn(){
  		File musicPath = new File("src/assets.sounds/mageR.wav");
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
	public static void playHeal(){
  		File musicPath = new File("src/assets.sounds/heal.wav");
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
	public static void paladinTurn(){
  		File musicPath = new File("src/assets.sounds/paladinR.wav");
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
	public static void priestTurn(){
  		File musicPath = new File("src/assets.sounds/priestR.wav");
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
	public static void hunterTurn(){
  		File musicPath = new File("src/assets.sounds/hunterR.wav");
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
	public static void AOESpellSound(){
  		File musicPath = new File("src/assets.sounds/aoespell.wav");
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
