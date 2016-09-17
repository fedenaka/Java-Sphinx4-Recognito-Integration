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
			
			// Se setean los modelos que se van a utilizar para el dictado
			configuration.setAcousticModelPath("es/es");
			configuration.setDictionaryPath("es/es.dict");
			configuration.setLanguageModelPath("es/es.lm");
			
			StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
			
			// Se elije el archivo del cual se va a realizar el reconocimiento
			InputStream stream = new FileInputStream("test.wav");
			recognizer.startRecognition(stream);
			
			SpeechResult result = recognizer.getResult();
			
			// Si hay un resultado se devuelve la hipotesis
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
