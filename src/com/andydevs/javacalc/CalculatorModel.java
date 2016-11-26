/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.0.0
 */
package com.andydevs.javacalc;

// Imports
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Controls calculations and data storage
 * 
 * @author Anshul Kharbanda
 * @since  0.0.0
 */
public class CalculatorModel
{
	// ---------------------------------- Regex -----------------------------------

	/**
	 * Reads numbers
	 */
	private static final Pattern NUMERIC = Pattern.compile("\\G(\\+|\\-)?[0-9]+(\\.[0-9]+)?");

	/**
	 * Reads add and sub operators
	 */
	private static final Pattern ADDSUB = Pattern.compile("\\G(\\+|\\-)");

	/**
	 * Reads mul and div operators
	 */
	private static final Pattern MULDIV = Pattern.compile("\\G(\\*|\\/)");

	/**
	 * Reads exponent operators
	 */
	private static final Pattern EXPONENT = Pattern.compile("\\G\\^");

	/**
	 * Reads lparens
	 */
	private static final Pattern LPAREN = Pattern.compile("\\G\\(");

	/**
	 * Reads rparens
	 */
	private static final Pattern RPAREN = Pattern.compile("\\G\\)");

	/**
	 * Reads whitespaces
	 */
	private static final Pattern WSPACE = Pattern.compile("\\G +");

	// --------------------------------- Matchers ---------------------------------

	/**
	 * Matches numbers in a given string
	 */
	private Matcher numericMatcher;

	/**
	 * Matches addsubs in a given string
	 */
	private Matcher addsubMatcher;

	/**
	 * Matches muldivs in a given string
	 */
	private Matcher muldivMatcher;

	/**
	 * Matches exponents in a given string
	 */
	private Matcher exponentMatcher;

	/**
	 * Matches lparens in a given string
	 */
	private Matcher lparenMatcher;

	/**
	 * Matches rparens in a given string
	 */
	private Matcher rparenMatcher;

	/**
	 * Matches whitespaces in a given string
	 */
	private Matcher wspaceMatcher;

	// -------------------------------- Variables --------------------------------

	/**
	 * The input being processed
	 */
	private String input;

	/**
	 * The location in the input to process
	 */
	private int location;

	/**
	 * The last result being parsed
	 */
	private double result;

	// -------------------------------- Operations --------------------------------

	/** 
	 * Processes an input stream to a double value
	 *
	 * @param inp the input to process
	 *
	 * @return double value representing the input
	 *
	 * @throw Exception upon error parsing input
	 */ 
	public double process(String inp) throws Exception
	{
		// Initialize input and location
		input = inp;
		location = 0;

		// Set matchers to input
		numericMatcher  = NUMERIC.matcher(input);
		addsubMatcher   = ADDSUB.matcher(input);
		muldivMatcher   = MULDIV.matcher(input);
		exponentMatcher = EXPONENT.matcher(input);
		lparenMatcher   = LPAREN.matcher(input);
		rparenMatcher   = RPAREN.matcher(input);
		wspaceMatcher   = WSPACE.matcher(input);

		// Parse result
		result = sum();

		// Return result if all characters are paresed (else throw error)
		if (location == input.length()) return result;
		else throw new Exception("Unexpected character at " + location + ": " + input.charAt(location));
	}

	// ---------------------------- Expression Parsing ----------------------------

	/**
	 * Skips spaces
	 */
	private void space()
	{
		// Skip space
		if (wspaceMatcher.find(location))
			location = wspaceMatcher.end();
	}

	/**
	 * Processes a sum
	 *
	 * @return the sum value parsed
	 *
	 * @throw Exception when parsing sum
	 */
	private double sum() throws Exception
	{
		// Start term
		double sumv = term();

		// Skip space
		space();

		// Keep adding addsubs
		while (addsubMatcher.find(location))
		{
			// Set end to location
			location = addsubMatcher.end();

			// Skip space
			space();

			// Perform operation based on input string
			if (addsubMatcher.group().equals("+"))
				sumv += term();
			else if (addsubMatcher.group().equals("-"))
				sumv -= term();

			// Skip space
			space();
		}

		// Return end value
		return sumv;
	}

	/**
	 * Processes a term
	 *
	 * @return the term value parsed
	 *
	 * @throw Exception when parsing term
	 */
	private double term() throws Exception
	{
		// Start factor
		double termv = factor();

		// Skip space
		space();

		// Keep adding muldivs while muldiv token exists
		while (muldivMatcher.find(location))
		{
			// Set end to location
			location = muldivMatcher.end();

			// Skip space
			space();

			// Perform operation based on input string
			if (muldivMatcher.group().equals("*"))
				termv *= factor();
			else if (muldivMatcher.group().equals("/"))
				termv /= factor();

			// Skip space
			space();
		}

		// Return end value
		return termv;
	}

	/**
	 * Processes a factor
	 *
	 * @return the factor value parsed
	 *
	 * @throw Exception when parsing factor
	 */
	private double factor() throws Exception
	{
		// Start number
		double factorv = value();

		// Skip space
		space();

		// Keep adding exponents while exponent token exists
		while (exponentMatcher.find(location))
		{
			// Set end to location
			location = exponentMatcher.end();

			// Skip space
			space();

			// Perform power
			factorv = Math.pow(factorv, value());

			// Skip space
			space();
		}

		// Return end value
		return factorv;
	}

	/**
	 * Processes a value
	 *
	 * @return the value value parsed
	 *
	 * @throw Exception when parsing a value
	 */
	private double value() throws Exception
	{
		// Return subexpression if lparen is found
		if (lparenMatcher.find(location))
		{
			// Set location
			location = lparenMatcher.end();

			// Skip space
			space();

			// Get sum value
			double value = sum();

			// Skip space
			space();

			// Return if paren is closed (else throw error)
			if (rparenMatcher.find(location))
			{	
				// Set location
				location = rparenMatcher.end();

				// Return value
				return value;
			}
			else throw new Exception("Umatched lparen");
		}
		// Else return double if found
		else if (numericMatcher.find(location))
		{
			// Set location
			location = numericMatcher.end();

			// Return number
			return Double.parseDouble(numericMatcher.group());
		}
		// Else throw exception
		else throw new Exception("Unexpected \"" + input.charAt(location) + "\". exepected double value or lparen");
	}
}