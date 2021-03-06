/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.1.0
 */
package com.andydevs.javacalc;

/**
 * Controls calculator operations
 * 
 * @author Anshul Kharbanda
 * @since  0.1.0
 */
public class CalculatorController
{
	/**
	 * The command to clear the input
	 */
	public static final String CLEAR_COMMAND = "clear";

	/**
	 * The exit command
	 */
	public static final String EXIT_COMMAND = "exit";

	/**
	 * Parses expressions
	 */
	private CalculatorExpressionParser parser;

	/**
	 * The view being controlled
	 */
	private CalculatorView view;

	/** 
	 * Creates a CalculatorController with the given view
	 *
	 * @param v the view being controlled
	 */
	public CalculatorController(CalculatorView v)
	{
		view = v;
		parser = new CalculatorExpressionParser();
	}

	/** 
	 * Processes view inputs
	 */ 
	public void process()
	{
		// Get input
		String input = view.input();

		// Handle System commands
		if (input.equals(CLEAR_COMMAND)) view.clearOutput();
		else if (input.equals(EXIT_COMMAND)) exit();
		else 
		{
			// Process expression
	    	try {
	    		view.output(parser.parse(input).toString());
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		view.output(e.getClass().getSimpleName() + ": " + e.getMessage(), CalculatorView.OutLevel.ERROR);
	    	}
		}

    	// Clear Input
    	view.clearInput();
	}

	/**
	 * Exits the program
	 */
	public void exit() { System.exit(0); }
}