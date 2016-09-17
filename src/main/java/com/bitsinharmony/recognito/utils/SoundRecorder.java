package com.bitsinharmony.recognito.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorder {
	
	private byte[] tempBuffer;
	private int numBytesRead;
	private boolean recording;
	private File file;
	private TargetDataLine line;
	private AudioInputStream inFile;
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
	
	public SoundRecorder() {
		
		try {
			
			line = AudioSystem.getTargetDataLine(format);
			tempBuffer = new byte[line.getBufferSize() / 4];
			
			line.open();
			line.start();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startListening(String fileName) {
		
		while (true) {
			
			numBytesRead = line.read(tempBuffer, 0, tempBuffer.length);
			int level = calculateRMSLevel();
			
			System.out.println("Level: " + level);
			
			if (level >= 30) {
				
				System.out.println("Recording.. ");
				out.write(tempBuffer, 0, numBytesRead);
				
				recording = true;
			
			} else if (level < 15 && recording) {
				
				System.out.println("Saving.. ");
				recording = false;
				
				InputStream input = new ByteArrayInputStream(out.toByteArray());
				
				inFile = new AudioInputStream(input, format, (long) (out.toByteArray().length / format.getFrameSize()));
				file = new File(fileName);
				
				try {
					AudioSystem.write(inFile, Type.WAVE, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				out.reset();
				
				return;
				
			}
			
			
		}
	}
	
	private int calculateRMSLevel() {
		
		if (numBytesRead > 0) {
			
		    long lSum = 0;
		    for(int i = 0; i < tempBuffer.length; i++)
		        lSum = lSum + tempBuffer[i];
	
		    double dAvg = lSum / tempBuffer.length;
		    double sumMeanSquare = 0d;
	
		    for(int j = 0; j < tempBuffer.length; j++)
		        sumMeanSquare += Math.pow(tempBuffer[j] - dAvg, 2d);
	
		    double averageMeanSquare = sumMeanSquare / tempBuffer.length;
	
		    return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
		    
		}
		
		return 0;
	}
}
