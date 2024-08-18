import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class BasicCalculatorGUI extends JFrame {
    private JTextField inputField;
    private JLabel resultLabel;

    public BasicCalculatorGUI() {
        // Set up the main frame
        setTitle("Basic Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(3, 1));

        // Create and add the input field
        inputField = new JTextField();
        add(inputField);

        // Create and add the calculate button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateResult();
            }
        });
        add(calculateButton);

        // Create and add the result label
        resultLabel = new JLabel("Result: ");
        add(resultLabel);

        // Make the frame visible
        setVisible(true);
    }

    private void calculateResult() {
        // Get the expression from the input field
        String expression = inputField.getText().trim();
        try {
            // Evaluate the expression and display the result
            int result = evaluateExpression(expression);
            resultLabel.setText("Result: " + result);
        } catch (Exception e) {
            // Display an error message if the expression is invalid
            resultLabel.setText("Error: Invalid expression");
        }
    }

    private int evaluateExpression(String s) {
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int num = 0;
        char lastOperator = '+';

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // If the character is a digit, build the number
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }

            // If the character is an operator or it's the last character
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                // Apply the last operator to the current number
                if (lastOperator == '+') {
                    numbers.push(num);
                } else if (lastOperator == '-') {
                    numbers.push(-num);
                } else if (lastOperator == '*') {
                    numbers.push(numbers.pop() * num);
                } else if (lastOperator == '/') {
                    numbers.push(numbers.pop() / num);
                }

                // Reset the number and update the last operator
                num = 0;
                lastOperator = c;

                // Handle parentheses and operators
                if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    // Evaluate everything inside the parentheses
                    while (operators.peek() != '(') {
                        numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.pop(); // Remove the '('
                } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                    // Apply operators with higher or equal precedence
                    while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                        numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.push(c);
                }
            }
        }

        // Apply any remaining operators
        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        // The final result is the top of the numbers stack
        return numbers.pop();
    }

    // Helper method to determine operator precedence
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    // Helper method to apply an operator to two operands
    private int applyOperator(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        // Create and show the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasicCalculatorGUI();
            }
        });
    }
}