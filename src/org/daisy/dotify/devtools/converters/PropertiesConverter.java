package org.daisy.dotify.devtools.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConverter {

	/**
	 * Converts a java properties file to or from XML. If
	 * the file ends with .properties it is read as a properties
	 * file and stored as XML. If the file ends with .xml it is read
	 * as XML and stored as properties.
	 * @param in the input file
	 * @param out the output
	 * @throws FileNotFoundException if a file cannot be converted
	 * @throws IOException if a file cannot be converted
	 */
	public static void convert(File in, File out) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		if (in.getName().endsWith(".properties")) {
			p.load(new FileInputStream(in));
			p.storeToXML(new FileOutputStream(out), "Converted to XML");
		} else if (in.getName().endsWith(".xml")) {
			p.loadFromXML(new FileInputStream(in));
			p.store(new FileOutputStream(out), "Converted from XML");
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args.length<2) {
			System.out.println("Expected two arguments: input_file output_file");
			System.exit(-1);
		}
		convert(new File(args[0]), new File(args[1]));
	}
}
