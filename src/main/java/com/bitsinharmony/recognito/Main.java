package com.bitsinharmony.recognito;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bitsinharmony.recognito.utils.CMUSphinx;
import com.bitsinharmony.recognito.utils.RecognSpeaker;
import com.bitsinharmony.recognito.utils.SoundRecorder;

public class Main {
	
	static SoundRecorder recorder = new SoundRecorder();
	static CMUSphinx sphix = new CMUSphinx();
	static RecognSpeaker recognition = new RecognSpeaker();
	static String nombre = "Fede";
	
	public static void main(String[] args) {
		
		createAndShowGUI();
		
	}
	
	public static void createAndShowGUI() {
		
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
        JPanel pane = new JPanel();
        JTextArea label = new JTextArea("1. Ingresá tu nombre abajo\n2. Presioná Escuchar\n3. Deci el comando", 5, 20);
        JTextField nombreLabel = new JTextField();
        JButton btnGrabar = new JButton("Escuchar");
        
        nombreLabel.setPreferredSize(new Dimension(100, 20));
        label.setEditable(false);
        
        btnGrabar.addActionListener((ActionEvent event) -> {
        	
        	if(!nombreLabel.getText().isEmpty()) {
        	
	        	btnGrabar.setEnabled(false);
	        	
	        	recorder.startListening("test.wav");
	        	
	        	if(recognition.startRecognition("test.wav", nombreLabel.getText())) {
	        		
	        		String txt = sphix.getHypotesis();
	        		
	        		if(txt.isEmpty())
	        			label.setText("No se ha detectado ningún comando..");
	        		else {
	        			
	        			label.setText("Comando: " + txt);
	        			
	        			if(txt.equals("bloquear computadora")) {
	        				
							try {
								Runtime.getRuntime().exec(System.getenv("windir") + "/System32/rundll32.exe user32.dll,LockWorkStation");
							} catch (IOException e) {
								e.printStackTrace();
							}
							
	        			}
	        			
	        		}
					
	        	} else
	        		label.setText("No sos " + nombreLabel.getText() + "..");
	        	
	        	btnGrabar.setEnabled(true);
	        	
        	}
        	else
        		label.setText("Por favor ingresa un nombre..");
        	
        });
        
        pane.add(label);
        pane.add(nombreLabel);
        pane.add(btnGrabar);
        
        frame.add(pane);
        frame.setVisible(true);
        
    }
	
}
