package assign1;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import base.Compressor;	

/**
 * Assignment 1
 * Submitted by: 
 * Student 1.Sagi David 	ID# 207597576
 * Student 2.Tzlil Shtam 	ID# 205779309
 * Student 3.Liran Shoshana ID# 312557978
 */

public class LZSSEncoderDecoder implements Compressor {
	int minimunLengthToStartCompress = 3; // Encode when the length is 3 or more
	int lookAHeadRange = 258; // Range of look ahead 
	int searchBufferRange = 32768; // Range of search Buffer

	@Override
	public void Compress(String[] input_names, String[] output_names) throws IOException {
		String lookAHead = "",searchBuffer= "",compressedText="",uncompressedText=""; // Empty strings 
		FileInputStream input_stream = null;
		FileOutputStream output_stream = null;
		int current_char; //Running index in the main loop
		boolean definitiveTransition=false; // Check the end of text
		System.out.println("Hello, The compression process begins :)");

		input_stream = new FileInputStream(input_names[0]);
		output_stream = new FileOutputStream(output_names[0]);

		while ((current_char = input_stream.read()) != -1 || lookAHead.length() > minimunLengthToStartCompress) 
		{
			if(current_char == -1){ // End of Text
				definitiveTransition = true;
			}
			uncompressedText+=(char)current_char; // all the text

			if (uncompressedText.length() == minimunLengthToStartCompress) { // Enter the minimum substring to start Encode 
				searchBuffer=uncompressedText.substring(0,minimunLengthToStartCompress); // Put in searchBuffer the first lower limit chars because we can't start compress before.
				for (int i=0;i<minimunLengthToStartCompress;i++) {
					output_stream.write(uncompressedText.charAt(i)); 
				}
				compressedText=searchBuffer; // put in compressedText the first lower limit chars because we can't start compress before.
			}

			else if (uncompressedText.length() > minimunLengthToStartCompress){ // The rest of the text 
				if (definitiveTransition == false && lookAHead.length() < lookAHeadRange) { // while the text isn't over and look ahead isn't full 
					lookAHead+=(char)current_char; // put in lookahead the rest of uncompress text in the length of the limit.
				}

				if (lookAHead.length() == lookAHeadRange || definitiveTransition ){// if we got in look ahead to the range we start compress
					int [] token = new int [2] ; 
					token = isToken(searchBuffer,lookAHead); // getting the distance and lenth of the token
					int distance=token[1]; 
					if (token[1] > 2) { // if the length of the token is more then 2 

						output_stream.write('1'); // write 1
						StringBuilder craeteText = new StringBuilder("("+(token[0])+","+token[1]+")"); 
						int temp=0; 
						int counter = 0;
						int counterNumOfBits=0;

						while (token[0]!=0) {
							if(counter == 8) {
								if (temp == 48) {
									output_stream.write(48);
								}
								output_stream.write(temp);
								counterNumOfBits++;
								temp=0;
								counter=0;
							}
							temp+=token[0] % 2 * Math.pow(2, (double)counter);
							token[0]=token[0]/2;
							counter++;
						}
						if (counterNumOfBits!=1) {
							output_stream.write('0');
						}

						output_stream.write(temp);

						temp=0;
						counter=7;

						while (token[1]!=0) {
							if(counter == -1) {
								output_stream.write(temp);
								temp=0;
								counter=7;
							}
							temp+=token[1]%2 * Math.pow(2, (double)counter);
							token[1]=token[1]/2;
							counter--;
						}

						output_stream.write(temp);		
						compressedText+=craeteText.toString();
						if(searchBuffer.length() < searchBufferRange) { // check if search buffer is full
							searchBuffer+=lookAHead.substring(0,distance); // if not then put in the search buffer the token letters
						}
						else {
							searchBuffer=searchBuffer.substring(0,distance); 
						}
						lookAHead = lookAHead.substring(distance); // make look ahead smaller
					}
					else {
						output_stream.write('0'); // not token
						compressedText+=lookAHead.charAt(0); // get it into the compressed text
						if (searchBuffer.length() < searchBufferRange) {
							searchBuffer += lookAHead.charAt(0);
						}
						else {
							searchBuffer = searchBuffer.substring(1);
						}
						output_stream.write(lookAHead.charAt(0));
						lookAHead=lookAHead.substring(1);
					}

				}
			}
		}
	
		/*System.out.println(compressedText);
		while (!compressedText.isEmpty()) {
			output_stream.write(compressedText.charAt(0));
			compressedText=compressedText.substring(1);
		}*/
	}




	public int [] isToken(String searchBuffer, String lookAHead) { // we check the best match by comparing the first letter in the look ahead with all of the search buffer
		int [] arr = new int [] {0,0};
		int minimumDistanceToken=0; //get the best match distance  
		int distanceToken=0; 
		int maxLengthToken=0; // get the best match length 
		int lengthToken=0;
		for (int i=(searchBuffer.length()-1); i >= 0 ;i--) { 
			distanceToken++;
			if((int)searchBuffer.charAt(i) == (int)lookAHead.charAt(0)) {
				for (int j = i ;  j < searchBuffer.length() ; j++ ) {
					if (searchBuffer.charAt(j) == lookAHead.charAt(lengthToken)) {
						lengthToken++;
						if (lengthToken == lookAHead.length()) {
							break;
						}
					}
					else {
						break;
					}
				}
				if (maxLengthToken < lengthToken) { // check if its the max match 
					maxLengthToken = lengthToken;
					lengthToken=0;
					minimumDistanceToken=distanceToken;
				}
				lengthToken=0;
			}
		}
		arr[0]=minimumDistanceToken;
		arr[1]=maxLengthToken;

		return arr;
	}


	@Override
	public void Decompress(String[] input_names, String[] output_names) throws IOException {
		System.out.println("Start Decompress:  ");
		FileInputStream inFile = new FileInputStream(input_names[0]);//files I/O streams and buffers
		FileOutputStream outFile = new FileOutputStream(output_names[0]);
		int current_char;
		int nextIndex;
		String undecompress="";
		String decompress="";
		while((current_char = inFile.read()) != -1) {//read the mode bit
			undecompress+=(char)current_char;
			if (undecompress.length()<=minimunLengthToStartCompress) {
				decompress+=undecompress.charAt(undecompress.length()-1);
				outFile.write(undecompress.charAt(undecompress.length()-1));
			}
			else if(current_char == '1') {
				String toBinary="";
				nextIndex=inFile.read();
				int count=0;
				if (nextIndex!='0') {
					toBinary=Integer.toBinaryString(nextIndex);
					if (toBinary.length()!=8) {
						for (int i=toBinary.length();i<8;i++) {
							toBinary='0'+toBinary;
						}
					}
					nextIndex=inFile.read();
					toBinary=Integer.toBinaryString(nextIndex)+toBinary;
					nextIndex=0;
					for (int i=0;i<toBinary.length();i++) {
						if (toBinary.charAt(toBinary.length()-1-i)=='1') {
							nextIndex+=Math.pow(2, count);
							count++;
						}
						else {
							count++;
						}
					}
				}
				else {
					nextIndex=inFile.read();
					if (nextIndex==48) {
						nextIndex=inFile.read();
						toBinary=Integer.toBinaryString(nextIndex);
						if (toBinary.length()!=8) {
							for (int i=toBinary.length();i<8;i++) {
								toBinary='0'+toBinary;
							}
						}
						nextIndex=inFile.read();
						toBinary=Integer.toBinaryString(nextIndex)+toBinary;
						nextIndex=0;
						for (int i=0;i<toBinary.length();i++) {
							if (toBinary.charAt(toBinary.length()-1-i)=='1') {
								nextIndex+=Math.pow(2, count);
								count++;
							}
							else {
								count++;
							}
						}
					}
				}
				undecompress+=(char)nextIndex;
				count=0;
				int temp=0;
				int dis=0;
				int len=0;
				while (nextIndex!=0) {
					temp+=nextIndex%2 * Math.pow(2, (double)count);
					nextIndex=nextIndex/2;
					count++;
				}
				dis=temp;
				nextIndex=inFile.read();
				undecompress+=nextIndex;
				count=7;
				temp=0;
				while (nextIndex!=0) {
					temp+=nextIndex%2 * Math.pow(2, (double)count);
					nextIndex=nextIndex/2;
					count--;
				}
				len=temp;
				//System.out.println("dis = "+dis);
				if (dis==1120) {
					System.out.println("fsa");
				}
				decompress+=decompress.substring(decompress.length()-dis,decompress.length()-dis+len);
				for (int i=0;i<len;i++) {
					outFile.write(decompress.charAt(decompress.length()-len+i));
				}
			}
			else {
				nextIndex=inFile.read();
				undecompress+=(char)nextIndex;
				outFile.write(undecompress.charAt(undecompress.length()-1));
				decompress+=undecompress.charAt(undecompress.length()-1);
			}

		}

	}

}

