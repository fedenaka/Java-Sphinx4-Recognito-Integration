package com.bitsinharmony.recognito.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

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
			tempBuffer = new byte[line.getBufferSize() / 5];
			
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
			System.out.println("Readed: " + line.getFramePosition());
			
			if (level >= 40 && recording)
				out.write(tempBuffer, 0, numBytesRead);
			if (level >= 40 && !recording) {
				
				System.out.println("Recording.. ");
				recording = true;
				
				
			} else if (level < 40 && recording) {
				
				System.out.println("Saving.. ");
				recording = false;
				
				saveFile(fileName);
				
				// InputStream input = new ByteArrayInputStream(out.toByteArray());
				
				// inFile = new AudioInputStream(input, format, out.toByteArray().length / format.getFrameSize());
				// file = new File(fileName);
				/*
				try {
					AudioSystem.write(inFile, Type.WAVE, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
				
				out.reset();
				
				return;
				
			}
			
		}
	}
	
	private boolean saveFile(String fileName) {
		
        try {
			DataOutputStream outFile = new DataOutputStream(new FileOutputStream(fileName));
            
            // write the wav file per the wav file format
            outFile.writeBytes("RIFF");                 // 00 - RIFF
            outFile.write(getIntByteArray(0, 4), 0, 4);     // 04 - how big is the rest of this file?
            outFile.writeBytes("WAVE");                 // 08 - WAVE
            outFile.writeBytes("fmt ");                 // 12 - fmt
            outFile.write(getIntByteArray(16, 4), 0, 4); // 16 - size of this chunk
            outFile.write(getShortByteArray((short) 1, 2), 0, 2);        // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outFile.write(getShortByteArray((short) 1, 2), 0, 2);  // 22 - mono or stereo? 1 or 2?  (or 5 or ???)
            outFile.write(getIntByteArray(16000, 4), 0, 4);        // 24 - samples per second (numbers per second)
            outFile.write(getIntByteArray((16000 * 16 * 1) / 8, 4), 0, 4);      // 28 - bytes per second
            outFile.write(getShortByteArray((short) ((16 * 1) / 8), 2), 0, 2);    // 32 - # of bytes in one sample, for all channels
            outFile.write(getShortByteArray((short) 16, 2), 0, 2); // 34 - how many bits in a sample(number)?  usually 16 or 24
            outFile.writeBytes("data");                 // 36 - data
            outFile.write(getIntByteArray(out.toByteArray().length, 4), 0, 4);      // 40 - how big is this data chunk
            outFile.write(out.toByteArray());                      // 44 - the actual data itself - just a long string of numbers
            outFile.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
	
	private byte[] getIntByteArray(int numero, int bytes) {
		return ByteBuffer.allocate(bytes).putInt(numero).array();
	}
	
	private byte[] getShortByteArray(short numero, int bytes) {
		return ByteBuffer.allocate(bytes).putShort(numero).array();
	}
	
	private int calculateRMSLevel() {
		
		if (numBytesRead > 0) {
            
		    long lSum = 0;
		    for(int i=0; i < tempBuffer.length; i++)
		        lSum = lSum + tempBuffer[i];
	
		    double dAvg = lSum / tempBuffer.length;
		    double sumMeanSquare = 0d;
	
		    for(int j=0; j < tempBuffer.length; j++)
		        sumMeanSquare += Math.pow(tempBuffer[j] - dAvg, 2d);
	
		    double averageMeanSquare = sumMeanSquare / tempBuffer.length;
	
		    return (int) (Math.pow(averageMeanSquare,0.5d) + 0.5);
		    
		}
		
		return 0;
	}
}
