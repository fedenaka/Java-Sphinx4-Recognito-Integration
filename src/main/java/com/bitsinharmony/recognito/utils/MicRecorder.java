package com.bitsinharmony.recognito.utils;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MicRecorder {
	
	private File file;
	private Thread t;
	private TargetDataLine line;
	private AudioInputStream inFile;
	private AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
	
	public MicRecorder() {
		
		try {
			
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			
			if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
			
			line = (TargetDataLine) AudioSystem.getLine(info);
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void recordMicrophone(String fileName) {
		
		try {
			
			t = new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						AudioSystem.write(inFile, Type.WAVE, file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			file = new File(fileName);
			
			line.open(format);
			line.start();
			
			inFile = new AudioInputStream(line);
			
			t.start();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stopMicrophone() {
		line.stop();
		line.close();
	}

}
