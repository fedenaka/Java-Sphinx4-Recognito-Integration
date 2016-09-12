package com.bitsinharmony.recognito;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.bitsinharmony.recognito.utils.CMUSphinx;
import com.bitsinharmony.recognito.utils.MicRecorder;
import com.bitsinharmony.recognito.utils.RecognSpeaker;

public class Main {
	
	// static SoundRecorder recorder = new SoundRecorder();
	static CMUSphinx sphix = new CMUSphinx();
	static MicRecorder micRec = new MicRecorder();
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
        JTextArea label = new JTextArea("1. Presiona el botón de grabar\n2. Deci un comando\n3. Paralo y espera el resultado", 5, 20);
        JButton btnGrabar = new JButton("Grabar");
        JButton btnComando = new JButton("Parar");
        
        label.setEditable(false);
        btnComando.setEnabled(false);
        
        btnGrabar.addActionListener((ActionEvent event) -> {
        	btnComando.setEnabled(true);
        	btnGrabar.setEnabled(false);
        	label.setText("Grabando..");
        	
        	micRec.recordMicrophone("test.wav");
        });
        
        btnComando.addActionListener((ActionEvent event) -> {
        	btnComando.setEnabled(false);
        	btnGrabar.setEnabled(true);
        	
        	micRec.stopMicrophone();
        	if(recognition.startRecognition("test.wav", nombre)) {
        		label.setText("Verificado..");
        		
        		try {
        			
        			String txt = sphix.getHypotesis();
        			
        			if(txt.equals("bloquear computadora"))
						Runtime.getRuntime().exec(System.getenv("windir") + "/System32/rundll32.exe user32.dll,LockWorkStation");
					
					label.setText(txt);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
        	} else
        		label.setText("NO SOS EL DUEÑO!");
        });
        
        pane.add(label);
        pane.add(btnGrabar);
        pane.add(btnComando);
        
        frame.add(pane);
        frame.setVisible(true);
        
    }
	
}
