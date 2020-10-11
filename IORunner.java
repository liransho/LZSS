package read_write_tofiles;
import java.io.IOException;
import assign1.LZSSEncoderDecoder;

public class IORunner {

	static String IN_FILE_PATH_1 = "C:\\Romeo and Juliet Entire Play.txt";
	static String OUT_FILE_PATH_1 = "C:\\compress ‏‏Romeo and Juliet Entire Play.txt";
	static String IN_FILE_PATH_2 = "C:\\OnTheOrigin.txt";
	static String OUT_FILE_PATH_2 = "C:\\text.txt";
	static String IN_FILE_PATH_3 = "C:\\Smiley.bmp";
	static String OUT_FILE_PATH_3 = "C:\\Better compress Smiley.bmp";

	public static void main(String[] args)
	{
		
		String[] input_names=new String[1];
		String[] output_names=new String[1];	

		input_names[0]=IN_FILE_PATH_1;
		output_names[0]=OUT_FILE_PATH_1;
		LZSSEncoderDecoder ob=new LZSSEncoderDecoder();

		//Compress file 
		try {
			ob.Compress(input_names, output_names);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		input_names[0] = OUT_FILE_PATH_1;
		output_names[0]= "C:\\Decompress Romeo and Juliet Entire Play.txt";

		try {
			ob.Decompress(input_names, output_names);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		/*input_names[0]=IN_FILE_PATH_2;
		output_names[0]=OUT_FILE_PATH_2;
		//דחיסה של תמונה
		ob.Compress(input_names, output_names);*/

		//input_names[0] = OUT_FILE_PATH_2;
		//output_names[0]= "C:\\Decompress Red_Flowers.bmp";

		//ob.Decompress(input_names, output_names);

		/*HuffmanBetterEnDe ob1 = new HuffmanBetterEnDe();

		input_names[0] = IN_FILE_PATH_3;
		output_names[0]= OUT_FILE_PATH_3;
		//סעיף ב' דחיסה משופרת
		ob1.Compress(input_names, output_names);

		input_names[0] = OUT_FILE_PATH_3;
		output_names[0]= "C:\\first decompress Smiley.bmp";;
		ob.Decompress(input_names, output_names);*/

	}
}
