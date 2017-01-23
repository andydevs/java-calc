/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.1.0
 */
package com.andydevs.javacalc;

// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * Handles the user interface of the calculator
 *
 * @author Anshul Kharbanda
 * @since  0.1.0
 */
public class CalculatorView extends JFrame
{
	// ----------------------------- Out Level ------------------------------

	/**
	 * Represents the different output levels in the CalculatorView
	 *
	 * @author Anshul Kharbanda
	 * @since  0.0.0
	 */
	public enum OutLevel
	{
		// Out levels
		NORMAL("black"),
		ERROR("red");

		/**
		 * The color of the level
		 */
		private String color;

		/**
		 * Creates the new out level with the given color
		 *
		 * @param c the color of the level
		 */
		private OutLevel(String c) { color = c; }

		/**
		 * Returns the color of the level
		 *
		 * @return the color of the level
		 */
		public String getColor() { return color; }
	}

	// ----------------------------- Constants ------------------------------

	/**
	 * The output line key
	 */
	public static final String OUTKEY = ">>>";

	// -------------------------- Swing Components --------------------------

	/**
	 * The output
	 */
	private JLabel output;

	/**
	 * Panel that holds the output
	 */
	private JScrollPane outputPane;

	/**
	 * The input
	 */
	private JTextField input;

	// ---------------------------- Controller ------------------------------

	/**
	 * The controller being called
	 */
	private CalculatorController controller;

	// ---------------------------- Constructor -----------------------------

	/**
	 * Construct CalculatorView
	 */
	public CalculatorView()
	{
		// Configure View
		super("Java Calculator");
		setSize(800, 600);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setResizable(false);
		addWindowListener(new CloseWindowAdapter());

		// Output Component
		output = new JLabel("<html>");
		output.setVerticalAlignment(SwingConstants.TOP);
		output.setOpaque(true);
		output.setBackground(Color.WHITE);
		output.setBorder(new EmptyBorder(1,5,5,5));
		outputPane = new JScrollPane(output,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outputPane.setPreferredSize(new Dimension(783,530));
		outputPane.setBackground(Color.WHITE);
		outputPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Input Component
		input = new JTextField(70);
		input.addKeyListener(new InputChangeListener());
		input.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.BLACK),
			BorderFactory.createEmptyBorder(1,6,1,6)
		));

		// Add Components
		add(outputPane);
		add(input);

		// Set Visibility
		setVisible(true);

		// Set controller
		controller = new CalculatorController(this);
	}

	// ---------------------------- Operations ------------------------------

	/**
	 * Outputs the given line to the view
	 *
	 * @param line the line to output
	 */
	public void output(String line)
	{
		output.setText(output.getText() + "<p>" + OUTKEY + " " + line + "</p>");
	}

	/**
	 * Outputs the given line to the view at the given level
	 *
	 * @param line  the line to output
	 * @param level the level to output at
	 */
	public void output(String line, OutLevel level)
	{
		String cline = "<p color=\"" + level.getColor() + "\">" + OUTKEY + " " + line + "</p>";
		output.setText(output.getText() + cline);
	}

	/**
	 * Returns the input from the view
	 *
	 * @return the input from the view
	 */
	public String input()
	{
		return input.getText();
	}

	/**
	 * Clears the view output
	 */
	public void clearOutput()
	{
		output.setText("<html>");
	}

	/**
	 * Clears the view input
	 */
	public void clearInput()
	{
		input.setText("");
	}

	// ----------------------------- Formatter ------------------------------

	/**
	 * Formats the numeric value
	 *
	 * @param number the number to format
	 *
	 * @return string of formatted number
	 */
	private String formatNumber(double number)
	{
		if (Math.floor(number) == number && !Double.isInfinite(number))
			return String.valueOf((int)number);
		else 
			return String.valueOf(number);
	}

	// ------------------------------ Events --------------------------------

	/**
	 * Handles closing windows
	 *
	 * @author Anshul Kharbanda
	 * @since  0.0.0
	 */
	private class CloseWindowAdapter extends WindowAdapter
	{
		/**
		 * Closes program when window is closed
		 *
		 * @param event the event that was triggered
		 */
		public void windowClosing(WindowEvent event)
		{
			controller.exit();
		}
	}

	/**
	 * Handles change in inputs
	 *
	 * @author Anshul Kharbanda
	 * @since  0.0.0
	 */
	private class InputChangeListener implements KeyListener
	{
		/**
	     * Handles key press
	     *
	     * @param event the event triggered
	     */
	    public void keyPressed(KeyEvent event)
	    {
	    	// Process code if enter key is pressed (and input is not empty)
	        if (event.getKeyCode() == KeyEvent.VK_ENTER && !input().equals(""))
	        	controller.process();
	    }

		/**
		 * Does nothing on key type
		 *
		 * @param event the event triggered
		 */
		public void keyTyped(KeyEvent event) {}

	    /**
	     * Does nothing on key release
	     *
	     * @param event the event triggered
	     */
	    public void keyReleased(KeyEvent event) {}
	}
}