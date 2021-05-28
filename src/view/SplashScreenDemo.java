package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
public class SplashScreenDemo {
    JFrame frame;
    JLabel image=new JLabel(new ImageIcon("assets/loading.png"));
    JProgressBar progressBar=new JProgressBar();
    JLabel message=new JLabel();
    SplashScreenDemo() throws IOException
    {    	
        createGUI();     
        addImage();
        addProgressBar();
        addMessage();
        runningPBar();
        
    }
    public void createGUI(){
    	
        frame=new JFrame();
        ImageIcon img = new ImageIcon("src/assets/logo.png");
        frame.setIconImage(img.getImage());
        frame.setUndecorated(true);
        frame.setSize(630,350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
 
    }
    public void addImage() throws IOException{
    	BufferedImage background = ImageIO.read(new File("src/assets/loading.png"));
    	Image image1 = background.getScaledInstance(frame.getWidth(),frame.getHeight(), Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(image1));
    	frame.add(image);
    }

    public void addMessage()
    {
        message.setBounds(250,320,200,40);
        message.setForeground(Color.black);
        message.setFont(new Font("arial",Font.BOLD,15));
        image.add(message);
    }
    Color golden = new Color(212,175,55);
    public void addProgressBar()
    {
        progressBar.setBounds(124,280,400,30);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.BLACK);
        progressBar.setForeground(golden);
        progressBar.setValue(0);
        image.add(progressBar);
    }
    public void runningPBar(){
        int i=0;
        while( i<=100)
        {
            try{
                Thread.sleep(25);
                progressBar.setValue(i);
                message.setText(" ");
                i++;
               // if(i==100)
                   // frame.dispose();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
    	new SplashScreenDemo();
    }
    
	public JFrame getFrame() {
		return frame;
	}
}
