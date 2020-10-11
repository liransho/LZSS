package base;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Compressor {
	abstract public void Compress(String[] input_names, String[] output_names) throws FileNotFoundException, IOException ;
	abstract public void Decompress(String[] input_names, String[] output_names) throws FileNotFoundException, IOException;

}
