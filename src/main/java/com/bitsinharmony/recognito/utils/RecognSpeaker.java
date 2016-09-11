package com.bitsinharmony.recognito.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.bitsinharmony.recognito.MatchResult;
import com.bitsinharmony.recognito.Recognito;

public class RecognSpeaker {
	
	private String[] grabaciones = { "grabacion1", "grabacion2", "grabacion3", "grabacion4", "grabacion5", "grabacion6", "grabacion7", "grabacion8", "grabacion9", "grabacion10" };
	private Recognito<String> recognito = new Recognito<>(16000.0f);
	
	public RecognSpeaker() {
		
		try {
			
			for(String name : grabaciones)
				recognito.createVoicePrint(name, new File("grabaciones/" + name + ".wav"));
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean startRecognition(String fileName) {

		try {
			
			List<MatchResult<String>> matches = recognito.identify(new File(fileName));
			
			int totalMatches = 0, totalLikely = 0;
			for(MatchResult<String> match : matches) {
				totalMatches++;
				totalLikely += match.getLikelihoodRatio();
			}
			
			if(totalLikely / totalMatches > 40)
				return true;
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
