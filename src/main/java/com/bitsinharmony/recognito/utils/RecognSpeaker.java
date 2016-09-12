package com.bitsinharmony.recognito.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.bitsinharmony.recognito.MatchResult;
import com.bitsinharmony.recognito.Recognito;

public class RecognSpeaker {
	
	private String[] grabaciones = { "Nico1", "Nico2", "Nico3", "Fede1", "Fede2", "Fede3", "Fede4", "Fede5", "Fede6", "Fede7" };
	private Recognito<String> recognito = new Recognito<>(16000.0f);
	
	public RecognSpeaker() {
		
		try {
			
			for(String name : grabaciones)
				recognito.createVoicePrint(name, new File("grabaciones/" + name + ".wav"));
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean startRecognition(String fileName, String nombre) {

		try {
			
			List<MatchResult<String>> matches = recognito.identify(new File(fileName));
			String nombreParecido = "";
			int mayor = 0, actual = 0;
			
			for(MatchResult<String> match : matches) {
				System.out.println("Nombre audio: " + match.getKey());
				actual = match.getLikelihoodRatio();
				System.out.println("Likehoodratio: " + match.getLikelihoodRatio());
				if (actual > mayor && match.getKey().contains(nombre)){
					nombreParecido = match.getKey();
					mayor = actual;
				}
			}
			System.out.println("Nombre parecido: " + nombreParecido + " %: " + mayor);
			if(mayor > 75){
				return true;
			}
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
