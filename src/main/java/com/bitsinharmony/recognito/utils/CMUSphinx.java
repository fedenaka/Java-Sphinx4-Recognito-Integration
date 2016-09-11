package com.bitsinharmony.recognito.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class CMUSphinx {
	
	public String getHypotesis() {
		
		try {
			
			Configuration configuration = new Configuration();
			
			configuration.setAcousticModelPath("es/es");
			configuration.setDictionaryPath("es/es.dict");
			configuration.setLanguageModelPath("es/es.lm");
			
			StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
			
			InputStream stream = new FileInputStream("test.wav");
			recognizer.startRecognition(stream);
			
			SpeechResult result = recognizer.getResult();
			
			if (result != null) {
				recognizer.stopRecognition();
				return result.getHypothesis();
			}
			
			recognizer.stopRecognition();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Ninguna hipotesis";
		
	}
	
}
