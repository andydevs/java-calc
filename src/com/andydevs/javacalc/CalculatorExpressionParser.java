/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.1.0
 */
package com.andydevs.javacalc;

// Imports
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorExpressionParser
{
	// ---------------------------------- Regex -----------------------------------

	/**
	 * Reads identifiers
	 */
	private static final Pattern IDENTIFER = Pattern.compile("\\G[a-zA-Z][a-zA-Z0-9_]*");

	/**
	 * Reads numbers
	 */
	private static final Pattern NUMERIC = Pattern.compile("\\G(\\+|\\-)?[0-9]+(\\.[0-9]+)?");

	/**
	 * Reads assign
	 */
	private static final Pattern ASSIGN = Pattern.compile("\\G\\=");

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
	 * Matches identifiers in a given string
	 */
	private Matcher identifierMatcher;

	/**
	 * Matches numbers in a given string
	 */
	private Matcher numericMatcher;

	/**
	 * Matches assign in a given string
	 */
	private Matcher assignMatcher;

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

	// ----------------------------------- Main -----------------------------------

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
	private CalculatorModel result;

	/** 
	 * Parses an input stream to a double value
	 *
	 * @param inp the input to parse
	 *
	 * @return CalculatorModel representing the input
	 *
	 * @throw Exception upon error parsing input
	 */ 
	public CalculatorModel parse(String inp) throws Exception
	{
		// Initialize input and location
		input    = inp;
		location = 0;

		// Set matchers to input
		identifierMatcher = IDENTIFER.matcher(input);
		numericMatcher    = NUMERIC.matcher(input);
		assignMatcher     = ASSIGN.matcher(input);
		addsubMatcher     = ADDSUB.matcher(input);
		muldivMatcher     = MULDIV.matcher(input);
		exponentMatcher   = EXPONENT.matcher(input);
		lparenMatcher     = LPAREN.matcher(input);
		rparenMatcher     = RPAREN.matcher(input);
		wspaceMatcher     = WSPACE.matcher(input);

		// Parse result
		result = assign();

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
	 * Processes an assign
	 *
	 * @return the assignment value parsed
	 *
	 * @throw Exception when parsing assignment
	 */
	private CalculatorModel assign() throws Exception
	{
		// Start term
		CalculatorModel assign = sum();

		// Skip space
		space();

		// If assignmatcher is found
		if (assignMatcher.find(location))
		{
			// Set end to location
			location = assignMatcher.end();

			// Skip space
			space();

			// Right assignment if assignable, else throw Exception
			assign.setValue(sum());
		}
		// Else set to ans
		else assign.setIdentifier(CalculatorModel.LAST);

		// Return assign
		return assign;
	}

	/**
	 * Processes a sum
	 *
	 * @return the sum value parsed
	 *
	 * @throw Exception when parsing sum
	 */
	private CalculatorModel sum() throws Exception
	{
		// Start term
		CalculatorModel sumv = term();

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
			if (addsubMatcher.group().equals("+")) sumv.add(term());
			else if (addsubMatcher.group().equals("-")) sumv.sub(term());

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
	private CalculatorModel term() throws Exception
	{
		// Start factor
		CalculatorModel termv = factor();

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
			if (muldivMatcher.group().equals("*")) termv.mul(factor());
			else if (muldivMatcher.group().equals("/")) termv.div(factor());

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
	private CalculatorModel factor() throws Exception
	{
		// Start number
		CalculatorModel factorv = value();

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
			factorv.pow(value());

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
	private CalculatorModel value() throws Exception
	{
		// Parse subexpression if lparen is found
		if (lparenMatcher.find(location))
		{
			// Set location
			location = lparenMatcher.end();

			// Skip space
			space();

			// Get sum value
			CalculatorModel value = sum();

			// Skip space
			space();

			// Return if paren is closed
			if (rparenMatcher.find(location))
			{	
				// Set location
				location = rparenMatcher.end();

				// Return value
				return value;
			}
			// Else throw error
			else throw new Exception("Umatched lparen");
		}
		// Else return identifier if found
		else if (identifierMatcher.find(location))
		{
			// Set location
			location = identifierMatcher.end();

			// Return identifier
			return new CalculatorModel(identifierMatcher.group());
		}
		// Else return double if found
		else if (numericMatcher.find(location))
		{
			// Set location
			location = numericMatcher.end();

			// Return number
			return new CalculatorModel(Double.parseDouble(numericMatcher.group()));
		}
		// Else throw exception
		else throw new Exception("Unexpected \"" + input.charAt(location) + "\". exepected double value or lparen");
	}	
}