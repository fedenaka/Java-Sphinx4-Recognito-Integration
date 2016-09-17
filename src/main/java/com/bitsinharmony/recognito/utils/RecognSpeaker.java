package com.bitsinharmony.recognito.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.bitsinharmony.recognito.MatchResult;
import com.bitsinharmony.recognito.Recognito;

public class RecognSpeaker {
	
	private String[] grabaciones = { "Nico1", "Nico2", "Nico3", "Fede1", "Fede2", "Fede3", /* "Fede4", "Fede5", "Fede6", "Fede7" */};
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
			
			// Devuelve la lista con los matcheos de la lista de grabaciones
			List<MatchResult<String>> matches = recognito.identify(new File(fileName));
			MatchResult<String> match = matches.get(0);
			
			System.out.println(match.getKey() + ": " + match.getLikelihoodRatio());
			
			if(match.getKey().contains(nombre) && match.getLikelihoodRatio() > 70)
				return true;
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
