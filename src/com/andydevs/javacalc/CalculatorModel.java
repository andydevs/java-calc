/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.1.0
 */
package com.andydevs.javacalc;

// Imports
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a CalculatorValue which could be an identifier
 *
 * @author Anshul Kharbanda
 * @since  0.2.0
 */
public class CalculatorModel
{
	/**
	 * The name of the last answer
	 */
	public static final String LAST = "ans";

	/**
	 * Maps variable names to a value
	 */
	public static Map<String, Double> vartable = new HashMap<String, Double>();

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
	public CalculatorModel(String id)
	{
		identifier = id;
		value = isAssigned() ? vartable.get(identifier) : 0;
	}

	/**
	 * Creates a value with the given raw value
	 *
	 * @param id the raw value
	 */
	public CalculatorModel(double val)
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
		else throw new Exception("Model \"" + toString() + "\" cannot be assigned!");
	}

	/**
	 * Returns the string representation of the Model
	 *
	 * @return the string representation of the Model
	 */
	public String toString()
	{
		if (isAssignable() && isAssigned()) {
			return identifier+" = "+value;
		} else if (isAssignable()) {
			return identifier;
		} else {
			return String.valueOf(value);
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
	public void setValue(CalculatorModel other) throws Exception
	{
		setValue(other.getValue());
	}

	/**
	 * Adds another value to this value
	 *
	 * @param other the other value to add
	 */
	public void add(CalculatorModel other)
	{
		value += other.getValue();
		identifier = null;
	}

	/**
	 * Subtract another value from this value
	 *
	 * @param other the other value to subtract
	 */
	public void sub(CalculatorModel other)
	{
		value -= other.getValue();
		identifier = null;
	}

	/**
	 * Multiplies another value by this value
	 *
	 * @param other the other value to multiply
	 */
	public void mul(CalculatorModel other)
	{
		value *= other.getValue();
		identifier = null;
	}

	/**
	 * Divides another value from this value
	 *
	 * @param other the other value to divides
	 */
	public void div(CalculatorModel other)
	{
		value /= other.getValue();
		identifier = null;
	}

	/**
	 * Raises this value to the power of another value
	 *
	 * @param other the other value to raise to the power of
	 */
	public void pow(CalculatorModel other)
	{
		value = Math.pow(value, other.getValue());
		identifier = null;
	}
}