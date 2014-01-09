package com.verisign.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Support merge many files into a unique list.
 * @author jcolosi
 * @version 1.0 Jan 8, 2014
 */
public class UniqueUnion {

	/**
	 * A file filter to operate on .txt files only.
	 */
	static private final FilenameFilter txtFilesOnly = new FilenameFilter() {
		public boolean accept(File directory, String fileName) {
			return fileName.endsWith(".txt");
		}
	};

	/**
	 * Constants in support of the output file name.
	 */
	static private final String dateFormat = "yyyy-MM-DD_kk-mm-ss_S";
	static private final SimpleDateFormat fileFormat = new SimpleDateFormat(dateFormat);
	static private final String outFilename = "UniqueUnion.%s.out";

	/**
	 * Merge .txt files in the current directory into a unique list.
	 */
	static private void execute() {
		Set<String> uniqueList = new TreeSet<String>();
		Date executeTime = new Date();
		String datetime = fileFormat.format(executeTime);

		/**
		 * Collect .txt files in the current directory
		 */
		File root = new File(System.getProperty("user.dir"));
		File[] files = root.listFiles(txtFilesOnly);

		/**
		 * For each .txt file, normalize and add to the unique list
		 */
		for (File file : files) {
			try {
				for (String item : readFile(file)) {
					item = normalize(item);
					if (item != null) uniqueList.add(item);
				}
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find file: " + file);
			} catch (IOException e) {
				System.err.println("Cannot read file: " + file);
			}
		}

		/**
		 * Write unique list to a single output file
		 */
		File outfile = new File(String.format(outFilename, datetime));
		try {
			writeFile(outfile, uniqueList);
		} catch (IOException e) {
			System.err.println("Cannot write file: " + outfile);
		}
	}

	/**
	 * Perform string normalization in one place.
	 * @param aString The String to be normalized.
	 * @return The normalized String.
	 */
	static private String normalize(String aString) {
		String input = aString;
		input = input.trim();
		input = input.replaceFirst("#.*", "");
		input = toLowerCaseAscii(input);
		return input.length() > 0 ? input : null;
	}

	/**
	 * Lowercase only the Basic Latin alphabet to preserve utf-8.
	 * @param aString The String to be lowercased.
	 * @return The lowercase String.
	 */
	static private String toLowerCaseAscii(String aString) {
		StringBuilder out = new StringBuilder();
		int diff = 'a' - 'A';
		for (char c : aString.toCharArray()) {
			if (c >= 'A' && c <= 'Z') out.append(c + diff);
			else out.append(c);
		}
		return out.toString();
	}

	/**
	 * Write the contents of a Set into an output file.
	 * @param outfile The file into which the set will be written.
	 * @param set The contents to write to the output file.
	 * @throws IOException If the output file cannot be opened.
	 */
	static private void writeFile(File outfile, Set<String> set) throws IOException {
		PrintWriter writer = new PrintWriter(outfile);
		StringBuilder out = new StringBuilder();
		for (String item : set) {
			out.append(item + "\n");
		}
		writer.print(out.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * Read the contents of an input file and store each line as a String object
	 * in an ArrayList.
	 * @param infile The file to be read.
	 * @return An ArrayList of type String.
	 * @throws IOException
	 */
	static private ArrayList<String> readFile(File infile) throws IOException {
		String line = null;
		ArrayList<String> list = new ArrayList<String>();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(infile));
			while ((line = reader.readLine()) != null) {
				list.add(line.trim());
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null) try {
				reader.close();
			} catch (IOException e2) {}
		}

		return list;
	}

	static public void main(String[] args) throws IOException {
		UniqueUnion.execute();
	}

}
