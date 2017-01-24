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
import java.util.Map;
import java.util.HashMap;

/**
 * Controls calculations and data storage
 * 
 * @author Anshul Kharbanda
 * @since  0.2.0
 */
public class CalculatorModel
{
	// --------------------------------- Vartable ---------------------------------

	/**
	 * The name of the last answer
	 */
	public static final String LAST = "ans";

	/**
	 * Maps a variable name to a value
	 */
	private Map<String, Double> vartable;

	// ---------------------------------- Value -----------------------------------

	/**
	 * Represents a CalculatorValue which could be an identifier
	 *
	 * @author  Anshul Kharbanda
	 * @since   0.2.0
	 */
	private class Value
	{
		/**
		 * Variable identifier value
		 */
		private String identifier;

		/**
		 * Raw value
		 */
		private double value;

		/**
		 * Creates a value with the given identifier
		 *
		 * @param id the identifier of the value
		 */
		public Value(String id)
		{
			identifier = id;
			value = isAssigned() ? vartable.get(identifier) : 0;
		}

		/**
		 * Creates a value with the given raw value
		 *
		 * @param id the raw value
		 */
		public Value(double val)
		{
			identifier = null;
			value = val;
		}

		/**
		 * Puts the value into the vartable at the set identifier
		 *
		 * @throws Exception if value cannot be assigned
		 */
		private void assign() throws Exception
		{
			if (isAssignable()) vartable.put(identifier, new Double(value));
			else throw new Exception("Value " + toString() + " cannot be assigned!");
		}

		/**
		 * Returns the string representation of the Value
		 *
		 * @return the string representation of the Value
		 */
		public String toString()
		{
			if (isAssignable() && isAssigned()) {
				return "("+identifier+" = "+value+")";
			} else if (isAssignable()) {
				return "("+identifier+")";
			} else {
				return "("+value+")";
			}
		}

		/**
		 * Returns true if the value is assignable
		 *
		 * @return true if the value is assignable
		 */
		public boolean isAssignable()
		{
			return identifier != null;
		}

		/**
		 * Returns true if the value has been assigned
		 *
		 * @return true if the value has been assigned
		 */
		public boolean isAssigned()
		{
			return vartable.containsKey(identifier);
		}

		/**
		 * Returns the identifier
		 *
		 * @return the identifier
		 */
		public String getIdentifier()
		{
			return identifier;
		}

		/**
		 * Sets the identifier to the new value
		 *
		 * @param id the new value of the identifier
		 *
		 * @throws Exception if value cannot be assigned
		 */
		public void setIdentifier(String id) throws Exception
		{
			identifier = id;
			assign();	
		}

		/**
		 * Returns the raw value
		 *
		 * @return the raw value
		 */
		public double getValue()
		{
			return value;
		}

		/**
		 * Sets the Value to the given new raw value
		 *
		 * @param val the raw value to set
		 *
		 * @throws Exception if value cannot be assigned
		 */
		public void setValue(double val) throws Exception
		{
			value = val;
			assign();
		}

		/**
		 * Assigns this value to another value
		 *
		 * @param other the other value to assign
		 *
		 * @throws Exception if value cannot be assigned
		 */
		public void setValue(Value other) throws Exception
		{
			setValue(other.getValue());
		}

		/**
		 * Adds another value to this value
		 *
		 * @param other the other value to add
		 */
		public void add(Value other)
		{
			value += other.getValue();
			identifier = null;
		}

		/**
		 * Subtract another value from this value
		 *
		 * @param other the other value to subtract
		 */
		public void sub(Value other)
		{
			value -= other.getValue();
			identifier = null;
		}

		/**
		 * Multiplies another value by this value
		 *
		 * @param other the other value to multiply
		 */
		public void mul(Value other)
		{
			value *= other.getValue();
			identifier = null;
		}

		/**
		 * Divides another value from this value
		 *
		 * @param other the other value to divides
		 */
		public void div(Value other)
		{
			value /= other.getValue();
			identifier = null;
		}

		/**
		 * Raises this value to the power of another value
		 *
		 * @param other the other value to raise to the power of
		 */
		public void pow(Value other)
		{
			value = Math.pow(value, other.getValue());
			identifier = null;
		}
	}

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
	private Value result;

	// -------------------------------- Constructor -------------------------------

	/**
	 * Creates a new CalculatorModel
	 */
	public CalculatorModel()
	{
		vartable = new HashMap<String, Double>();
	}

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
		if (location == input.length()) return result.getValue();
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
	private Value assign() throws Exception
	{
		// Start term
		Value assign = sum();

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
		else assign.setIdentifier(LAST);

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
	private Value sum() throws Exception
	{
		// Start term
		Value sumv = term();

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
	private Value term() throws Exception
	{
		// Start factor
		Value termv = factor();

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
	private Value factor() throws Exception
	{
		// Start number
		Value factorv = value();

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
	private Value value() throws Exception
	{
		// Parse subexpression if lparen is found
		if (lparenMatcher.find(location))
		{
			// Set location
			location = lparenMatcher.end();

			// Skip space
			space();

			// Get sum value
			Value value = sum();

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
			return new Value(identifierMatcher.group());
		}
		// Else return double if found
		else if (numericMatcher.find(location))
		{
			// Set location
			location = numericMatcher.end();

			// Return number
			return new Value(Double.parseDouble(numericMatcher.group()));
		}
		// Else throw exception
		else throw new Exception("Unexpected \"" + input.charAt(location) + "\". exepected double value or lparen");
	}
}