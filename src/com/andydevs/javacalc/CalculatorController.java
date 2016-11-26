/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.0.0
 */
package com.andydevs.javacalc;

/**
 * Controls calculator operations
 * 
 * @author Anshul Kharbanda
 * @since  0.0.0
 */
public class CalculatorController
{
	/**
	 * The model being operated on
	 */
	private CalculatorModel model;

	/** 
	 * Creates a CalculatorController
	 */
	public CalculatorController()
	{
		model = new CalculatorModel();
	}

	/** 
	 * Processes an input stream to a double value
	 *
	 * @param input the input to process
	 *
	 * @return double value representing the input
	 *
	 * @throw Exception upon error parsing input
	 */ 
	public double process(String input) throws Exception
	{
		return model.process(input);
	}
}