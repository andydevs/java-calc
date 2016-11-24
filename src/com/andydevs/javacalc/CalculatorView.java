/**
 * A calculator written in Java (which follows the MVC design patterns)
 *
 * @author  Anshul Kharbanda
 * @version 0.0.0
 */
package com.andydevs.javacalc;

// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Handles the user interface of the calculator
 *
 * @author Anshul Kharbanda
 * @since  0.0.0
 */
public class CalculatorView extends JFrame
{
	// ----------------------------- Constants ------------------------------

	/**
	 * The output line key
	 */
	public static final String OUTKEY = ">>>";

	// -------------------------- Swing Components --------------------------

	/**
	 * Panel the holds the output
	 */
	private JScrollPane outputPane;

	/**
	 * The output
	 */
	private JLabel output;

	/**
	 * The input
	 */
	private JTextField input;

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
		outputPane = new JScrollPane(output,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outputPane.setPreferredSize(new Dimension(783,530));
		outputPane.setBackground(Color.WHITE);
		outputPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Input Component
		input = new JTextField(71);
		input.addKeyListener(new InputChangeListener());
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Add Components
		add(outputPane);
		add(input);

		// Set Visibility
		setVisible(true);
	}

	// ---------------------------- Operations ------------------------------

	/**
	 * Outputs the given line to the view
	 *
	 * @param line the line to output
	 */
	public void output(String line)
	{
		output.setText(output.getText() + OUTKEY + " " + line + "<br>");
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
			System.exit(0);
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
	    	// If enter key is pressed (and input is not empty)
	        if (event.getKeyCode() == KeyEvent.VK_ENTER && !input().equals(""))
	        {
	        	// Handle enter key
	        	output(input());
	        	clearInput();
	        }
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