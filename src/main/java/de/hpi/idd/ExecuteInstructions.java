package de.hpi.idd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public interface ExecuteInstructions {

	/**
	 * Reading each instruction line by line, executing 'executeInstruction'
	 * method, outputting the results to the fileOut.
	 *
	 * @param fileIn:
	 *            input instructions file
	 * @param fileOut:
	 *            output results file
	 * @param parameters
	 */
	public void executeFile(File fileIn, File fileOut, HashMap<String, String> parameters);

	/**
	 * Execute any of the instructions:
	 *
	 * buildIndex insert (remove) getDuplicates destroyIndex
	 *
	 * @param instruction
	 */
	public void executeInstruction(ArrayList<String> instruction);

}
