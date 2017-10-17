package org.daisy.dotify.devtools.cli;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.utils.api.table.TableCatalog;
import org.daisy.braille.utils.api.table.TableCatalogService;
import org.daisy.dotify.devtools.regression.DotifyRegressionTesterRunner;

public class DotifyRegressionTesterUI {

	/**
	 * First argument should point to a file containing paths to input xml
	 * and reference output. E.g. DTB00001/DTB00001.xml baseline/P1.pef
	 * Second argument should point to the CLI
	 * 
	 * @param args the application arguments
	 */
	public static void main(String[] args) {
		if (args.length < 6) {
			System.out.println("Not enough arguments. Expected at least six arguments.");
			System.exit(-1);
		}
		try {
			TableCatalogService tcs = TableCatalog.newInstance();
			Collection<String> opts = new ArrayList<>();
			opts.add(args[3]);
			opts.add(args[4]);
			for (int i=8; i<args.length; i++) {
				// add remaining optional arguments
				opts.add(args[i]);
			}
			DotifyRegressionTesterRunner rt = new DotifyRegressionTesterRunner(new File(args[0]), args[1], new File(args[2]), tcs.newTable(args[5]).newBrailleConverter(), opts);
			if (args.length >= 7) {
				int thArg = 6;
				try {
					rt.setThreads(Integer.parseInt(args[thArg]));
				} catch (NumberFormatException e) {
					System.out.println(args[thArg] + " is not an integer.");
				}
			}
			if (args.length >= 8) {
				int errArg = 7;
				//This is a bit unusual, but the default value should be true and if the input is misspelled, it should use the default
				rt.setHaltOnError(!"false".equalsIgnoreCase(args[errArg]));
			}
			rt.run();
		} catch (IOException e) {
			System.exit(-1);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T invokeStatic(String clazz, String method) {
		T instance = null;
		try {
			Class<?> cls = Class.forName(clazz);
			Method m = cls.getMethod(method);
			instance = (T)m.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
}
