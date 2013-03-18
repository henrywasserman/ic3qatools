package com.xstreamcompare.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class ImagePreview extends JLayeredPane implements PropertyChangeListener {
    private JFileChooser jfc;
    private Image img1;
    private Image img2;
    private String fs = "";
    private static FilterDifferent filterdifferent = null;
    private static FilterPng filterexpected = null;
    private Font boldtitle = null;
    private Font regular = null;
    private File response = null;
    private File expectedresponse = null;
    private File missingresponse = null;
    private String responsedir = "";
    private String type = "";
    
    private HashMap <String,ImageTypes> types = new HashMap<String,ImageTypes>();
    
    public ImagePreview(JFileChooser jfc) {
    	
    	types.put("1", new ImageTypes(16,16));
    	types.put("2", new ImageTypes(24,24));
    	types.put("3", new ImageTypes(32,32));
    	types.put("4", new ImageTypes(64,64));
    	types.put("5", new ImageTypes(48,48));
    	types.put("101", new ImageTypes(120,20));
    	types.put("102", new ImageTypes(168,28));
    	types.put("103", new ImageTypes(216,36));
    	types.put("104", new ImageTypes(300,50));
    	types.put("105", new ImageTypes(320,50));
    	types.put("112", new ImageTypes(168,28));
    	types.put("113", new ImageTypes(216,36));
    	types.put("114", new ImageTypes(300,50));
    	types.put("115", new ImageTypes(480,80));
    	types.put("116", new ImageTypes(728,90));
    	types.put("117", new ImageTypes(1024,64));
    	types.put("118", new ImageTypes(1024,64));
    	types.put("201", new ImageTypes(120,120));
    	types.put("202", new ImageTypes(168,168));
    	types.put("203", new ImageTypes(216,216));
    	types.put("204", new ImageTypes(300,300));
    	types.put("301", new ImageTypes(120,120));
    	types.put("302", new ImageTypes(168,168));
    	types.put("303", new ImageTypes(216,216));
    	types.put("304", new ImageTypes(300,300));
    	types.put("500", new ImageTypes(250,250));
    	types.put("501", new ImageTypes(150,150));
    	
    	
        this.jfc = jfc;
        setPreferredSize(new Dimension(991, 452));
        
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        
        JButton btnUpdateExpected = new JButton("Replace Expected Response");
        btnUpdateExpected.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try {
        			FileUtils.copyFile(response, expectedresponse);
        			updateImage(response,expectedresponse);
        			
        		}
        		catch (Exception e) {
        			e.printStackTrace();
        		}
        		transferFocusBackward();
        	}
        });

        springLayout.putConstraint(SpringLayout.SOUTH, btnUpdateExpected, -10, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, btnUpdateExpected, -10, SpringLayout.EAST, this);
        add(btnUpdateExpected);
        
        fs = System.getProperty("file.separator");
        filterdifferent = new FilterDifferent();
        filterexpected = new FilterPng();
        boldtitle = new Font("Arial",Font.BOLD,12);
        regular = new Font("Arial",Font.PLAIN,12);
    	if (System.getProperty("responsedir") == null) {
    		responsedir = System.getProperty("user.dir") 
    		+ fs +".." + fs + "data" + fs + "infogroup" + fs + "response";
    	}
    	else {
    		responsedir = System.getProperty("requestdir") 
    		+ fs + ".." + fs + "data" + fs + "infogroup" + fs + "response";
    	}
    }
    
    public void setFileFilter() {
    	//jfc.removeChoosableFileFilter(filterexpected);
    	jfc.setFileFilter(filterexpected);
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            System.out.println("updating");
            response = jfc.getSelectedFile();
            if (response != null) {
            	expectedresponse = new File(System.getProperty("user.dir") +
            			fs + ".." + fs + "data" + fs + "infogroup" + fs + "expectedresponse" + fs
            			+ response.getName());
            	type = StringUtils.substringAfter(
            			FilenameUtils.removeExtension(expectedresponse.toString()), 
            			"type_");
            }
            
    		if ( expectedresponse == null || !expectedresponse.exists()) {
    			missingresponse = new File(System.getProperty("user.dir")
    					+ fs + ".." + fs + "data" + fs + "infogroup" + fs + "images" 
    					+ fs +"FileMissing.jpg");
    			updateImage(response,missingresponse);
    		}
    		else {
    		
    			updateImage(response,expectedresponse);
    		}

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void updateImage(File response, File expectedresponse) throws IOException {
        if(response == null && expectedresponse == null) {
            return;
        }
        
        if (response != null && expectedresponse != null) {        
        	img1 = ImageIO.read(response);
        	img2 = ImageIO.read(expectedresponse);
        }
        else 
        { 	img1 = null; 
        	img2 = null;
        }
        
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        // fill the background
    	
    	int expected_column = 400;
    	int expected_row = 40;
    	
        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(),getHeight());
        
        if(img1 != null && img2 !=null) {
            // calculate the scaling factor
            int w1 = img1.getWidth(null);
            int h1 = img1.getHeight(null);
            //int side1 = Math.max(w1,h1);
            
            int w2 = img2.getWidth(null);
            int h2 = img2.getHeight(null);
            //int side2 = Math.max(w2,h2);
            
            // draw titles
            
            if (w1 >=480) {
            	expected_column = 0;
            	expected_row = 190;
            }
            
            g.setFont(boldtitle);
            g.setColor(Color.black);
            g.drawString("Actual Response", 0,15);
            g.drawString("Expected Response", expected_column,expected_row - 25 );
            
            // draw the image
            g.drawImage(img1,0,40,w1,h1,null);
            g.drawImage(img2,expected_column,expected_row,w2,h2,null);
            
            // draw the image dimensions
            String dim = w1 + " x " + h1;
            g.setFont(regular);
            g.drawString(dim,0,400);
            g.setColor(Color.white);
            g.drawString(dim,1,401);
          
            if (type.isEmpty()) {
            	return;
            }
            if(types.get(type) != null & types.get(type).getHeight() == h1 &&
            		types.get(type).getWidth() == w1) {
                g.setColor(Color.green);
                g.setFont(boldtitle);
            	g.drawString("Dimension Test Passed", 100, 400);
            } else {
            	g.setColor(Color.red);
            	g.setFont(boldtitle);
            	g.drawString("Dimension Test Failed", 100, 400);
            }
            
        } else {
            
            // print a message
            g.setColor(Color.black);
            g.drawString("Not an image",30,100);
        }
    }
    
    public static void main(String[] args) {
    	String fs = System.getProperty("file.separator");
    	String responsedir = "";
    	if (System.getProperty("requestdir") == null ||
    			System.getProperty("requestdir").toString().equals("none")) {
    		responsedir = System.getProperty("user.dir") + fs + ".." + fs + "data" + fs + "infogroup" + fs + "response";
    	}
    	else {
    		responsedir = System.getProperty("requestdir") 
    		+ fs + ".." + fs + "data" + fs + "infogroup" + fs + "response";
    	}
    			
        JFileChooser jfc = new JFileChooser(responsedir);
        ImagePreview preview = new ImagePreview(jfc);
        jfc.addPropertyChangeListener(preview);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setAccessory(preview);
        jfc.setCurrentDirectory(new File(responsedir));
        jfc.setControlButtonsAreShown(false);
        jfc.setFileFilter(filterdifferent);
        jfc.setFileFilter(filterexpected);
        jfc.showOpenDialog(null);
    }
    
    private class FilterDifferent extends FileFilter {
    	//private File file1 = null;
    	//private File file2 = null;
    	
    	public boolean accept(File file1) {
    		boolean result = false;
    		File file2 = null;
    		
    		if (file1 != null) {
    			file2 = new File(System.getProperty("user.dir") + fs + ".." + fs 
    					+ "data" + fs + "infogroup" + fs + "expectedresponse" +
                		fs + file1.getName());
    		}

    		//compare the files here
    		if (file2.isFile() && FileUtils.sizeOf(file1) != FileUtils.sizeOf(file2)) {
    			result = true;
    		}
    		
    		return result;
    	}
    	
    	public String getDescription() {
    		return "Filters out files that are the same.";
    	}
    }
    
    private class FilterPng extends FileFilter {
    	//private File file1 = null;
    	//private File file2 = null;

    	
    	public boolean accept(File file1) {
        	boolean result = false;
        	
    		if (file1.getName().contains(".png")) {
    			result = true;
    		}
  		
    		return result;
    	}
    	
    	public String getDescription() {
    		return "Chooses png files";
    	}
    }    
    private class ImageTypes {
    	private int width = 0;
    	private int height = 0;
    	
    	ImageTypes(int sizewidth, int sizeheight) {
    		width = sizewidth;
    		height = sizeheight;
    	}
    	
    	int getHeight() {
    		return height;
    	}
    	int getWidth() {
    		return width;
    	}

    }
}
