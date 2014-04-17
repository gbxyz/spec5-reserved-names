package com.verisign.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.vgrs.xcode.common.Unicode;
import com.vgrs.xcode.idna.Idna;
import com.vgrs.xcode.idna.Punycode;
import com.vgrs.xcode.util.XcodeException;

/**
 * Process various text files, converting to A-labels, and creating a single
 * union file with all unique records.
 * <p>
 * @author jcolosi
 * @version 1.0 Jan 8, 2014
 * @version 2.0 Apr 16, 2014
 */
public class UniqueUnion {

	static class Config {

		/**
		 * Date formats
		 */
		static final SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd");

		/**
		 * Dir/Name.Label.Ext
		 */
		static final String actionExt = "txt";
		static final String outDir = "data/" + fileFormat.format(new Date());
		static final String outLabel = "ALabel";
		static final String outExt = "txt";
		static final String mergeDir = "final";
		static final String mergeName = "ReservedNames";
		static final String mergeLabel = fileFormat.format(new Date());
		static final String mergeExt = "txt";
		static final String binDir = "bin";
		static final String TwoCharacterLabels = "S5.2.txt";
		static final String readmeFile = "# Readme.txt";
		static final String readmeContent = ""
				+ "# Warning: Do Not Edit!\n"
				+ "#          The files in this directory are generated from the 'source' files\n"
				+ "#          at the root of the repository. Each source file has been converted\n"
				+ "#          to A-Labels. All source files have been merged into the\n"
				+ "#          ReservedNames.<date>.out file containing all unique Reserved Names.\n";
	}

	/**
	 * A file filter to operate on files with a certain extension.
	 */
	static private final FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File directory, String filename) {
			File file = new File(directory, filename);
			return file.isFile() && filename.endsWith("." + Config.actionExt);
		}
	};

	static private Idna idna;

	static {
		try {
			idna = new Idna(new Punycode());
		} catch (XcodeException e) {
			throw new RuntimeException(e);
		}
	}

	static public void main(String[] args) throws IOException {
		UniqueUnion.execute();
	}

	/**
	 * Process files in the current directory and merge into a unique list.
	 */
	static private void execute() {
		Set<String> uniqueList = new TreeSet<String>();
		StringBuilder fileList;

		/**
		 * Collect files in the current directory
		 */
		File root = new File(System.getProperty("user.dir"));
		if (root.getName().equals(Config.binDir)) {
			root = root.getParentFile();
		}
		File[] files = root.listFiles(filter);

		/**
		 * Establish an output directory
		 */
		File outDir = new File(root, Config.outDir);
		outDir.mkdirs();
		File mergeDir = new File(root, Config.mergeDir);
		mergeDir.mkdirs();

		/**
		 * For each file, normalize and add to the unique list
		 */
		for (File file : files) {
			if (file.getName().equalsIgnoreCase(Config.TwoCharacterLabels)) continue;
			fileList = new StringBuilder();
			try {
				for (String line : readFile(file)) {
					String item = normalize(line);
					if (item == null) continue; // Comment
					item = getALabel(item);
					if (item == null) continue; // Invalid
					fileList.append(item + "\n");
					uniqueList.add(item);
				}
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find file: " + file);
			} catch (IOException e) {
				System.err.println("Cannot read file: " + file);
			}

			/**
			 * Write section file with A-Labels
			 */
			String outfilename = removeExtension(file.getName());
			outfilename += "." + Config.outLabel;
			outfilename += "." + Config.outExt;
			File outfile = new File(outDir, outfilename);
			writeFile(outfile, fileList.toString());
		}

		/**
		 * Write unique list file
		 */
		String outfilename = Config.mergeName;
		outfilename += "." + Config.mergeLabel;
		outfilename += "." + Config.mergeExt;
		File outfile = new File(mergeDir, outfilename);
		writeFile(outfile, toString(uniqueList));

		/**
		 * Write readme files
		 */
		writeFile(new File(outDir, Config.readmeFile), Config.readmeContent);
		writeFile(new File(mergeDir, Config.readmeFile), Config.readmeContent);
	}

	/**
	 * Encode string in Punycode
	 * @param aString The String to be encoded.
	 * @return The Punycode String.
	 */
	static private String getALabel(final String aString) {
		char[] input = aString.toCharArray();
		try {
			return new String(idna.domainToAscii(Unicode.encode(input)));
		} catch (XcodeException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Perform string normalization in one place.
	 * @param aString The String to be normalized.
	 * @return The normalized String.
	 */
	static private String normalize(final String aString) {
		String input = aString;
		input = input.replaceFirst("\\s*#.*", "");
		input = input.trim();
		input = toLowerCaseAscii(input);
		return input.length() > 0 ? input : null;
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
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),
					"UTF-8"));
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

	static private String removeExtension(String filename) {
		int index = filename.lastIndexOf(".");
		if (index == -1) return filename;
		return filename.substring(0, index);
	}

	/**
	 * Lowercase only the Basic Latin alphabet to preserve utf-8.
	 * @param aString The String to be lowercased.
	 * @return The lowercase String.
	 */
	static private String toLowerCaseAscii(String aString) {
		StringBuilder out = new StringBuilder();
		for (char c : aString.toCharArray()) {
			if (c >= 'A' && c <= 'Z') c = Character.toLowerCase(c);
			out.append(c);
		}
		return out.toString();
	}

	static private String toString(Set<String> set) {
		StringBuilder out = new StringBuilder();
		for (String item : set) {
			out.append(item + "\n");
		}
		return out.toString();
	}

	/**
	 * Write the contents of a Set into an output file.
	 * @param outfile The file into which the set will be written.
	 * @param set The contents to write to the output file.
	 * @throws IOException If the output file cannot be opened.
	 */
	static private void writeFile(File outfile, String outmessage) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(outfile);
			writer.print(outmessage);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Cannot write file: " + outfile);
			e.printStackTrace();
		}
	}

}
