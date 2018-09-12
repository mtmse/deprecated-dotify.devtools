package org.daisy.dotify.devtools.regression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.daisy.dotify.api.table.BrailleConverter;
import org.daisy.dotify.devtools.jvm.ProcessStarter;

public class DotifyRegressionTesterRunner implements RegressionInterface {
	private static final Logger logger = Logger.getLogger(DotifyRegressionTesterRunner.class.getCanonicalName());
	private final String argsSeparator = "\\t";
	private final String pathToDotifyCli;
	private final int maxThreads;
	private final File pathToOutput; 
	private final BrailleConverter table;
	private int timeout = 60;
	private int threads;
	private boolean haltOnError = true;
	private final File pathToCommandsList;
	private final List<ProcessStarter> pool;
	private final Collection<String> opts;
	private boolean errors;

	public DotifyRegressionTesterRunner(File commandList, String pathToCli, File pathToOutput, BrailleConverter table, Collection<String> opts) {
		this.pathToCommandsList = commandList;
		this.pathToDotifyCli = pathToCli;
		this.maxThreads = Runtime.getRuntime().availableProcessors();
		this.threads = maxThreads;
		this.pathToOutput = pathToOutput;
		this.table = table;
		this.opts = opts;
		Logger.getLogger(this.getClass().getCanonicalName()).info("Default is " + threads + " threads.");
		if (!pathToCommandsList.isFile()) {
			System.out.println("Cannot find file: " + pathToCommandsList);
			System.exit(-1);
		}
		if (!pathToOutput.isDirectory()) {
			System.out.println("Output is not a directory.");
			System.exit(-2);
		}
		pool = Collections.synchronizedList(new ArrayList<ProcessStarter>());
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		if (threads > 0 && threads < maxThreads) {
			Logger.getLogger(this.getClass().getCanonicalName()).info("Setting number of threads to " + threads + ".");
			this.threads = threads;
		} else {
			Logger.getLogger(this.getClass().getCanonicalName()).warning("Unable to set number of threads to " + threads + ". Resetting to default value: " + maxThreads);
			this.threads = maxThreads;
		}
	}

	public boolean isHaltOnError() {
		return haltOnError;
	}

	public void setHaltOnError(boolean haltOnError) {
		this.haltOnError = haltOnError;
	}

	public void run() throws IOException {
		errors = false;
		ExecutorService exe = Executors.newFixedThreadPool(threads);
		try {
			processFile(pathToCommandsList, exe);
			exe.shutdown();
			try {
				while (!exe.isTerminated() && (!errors || !haltOnError)) {
					Thread.sleep(1000);
				}
				if (errors) {
					exe.shutdownNow();
				}
				//probably not need anymore
				exe.awaitTermination(timeout * 60, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (errors) {
				throw new IOException("Errors in test.");
			}
		}
	}
	
	private void processFile(File path, ExecutorService exe) throws FileNotFoundException, IOException {
		String line;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
			while ((line = in.readLine()) != null && (!errors || !haltOnError)) {
				final String line2 = line;
				if (line2.trim().equals("")) {
					// ignore
				} else if (line2.trim().startsWith("#")) {
					logger.info("Ignoring line: " + line2);
				} else if (line2.trim().startsWith("<include>")) {
					File newPath = new File(path.getParentFile(), line2.trim().substring(9).trim());
					logger.info("Processing included file: " + newPath.getAbsolutePath());
					processFile(newPath, exe);
				} else {
					String[] allArgs = line2.split(argsSeparator);
					Map<String, String> optionalArgs = new HashMap<>();
					for (String s : opts) {
						optionalArgs.put(getArgumentKey(s), s);
					}
					if (allArgs.length>2) {
						// override with values from file
						for (String s : Arrays.copyOfRange(allArgs, 2, allArgs.length)) {
							optionalArgs.put(getArgumentKey(s), s);
						}
					}
					exe.execute(new DotifyRegressionTester(this,
							new File(path.getParentFile(), allArgs[0]),
							new File(path.getParentFile(), allArgs[1]),
							table, 
							optionalArgs.values()));
				}
			}
		}
	}

	static String getArgumentKey(String a) {
		String regex = "=";
		return a.split(regex)[0];
	}

	@Override
	public ProcessStarter requestStarter() {
		try {
			return pool.remove(0);
		} catch (IndexOutOfBoundsException e) {
			return new ProcessStarter();
		}
	}

	@Override
	public void returnStarter(ProcessStarter starter) {
		pool.add(starter);
	}

	@Override
	public String getPathToCLI() {
		return pathToDotifyCli;
	}

	@Override
	public File testOutputFolder() {
		return pathToOutput;
	}

	@Override
	public void reportError() {
		errors = true;
	}
}
