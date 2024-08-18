public class SecretDecoder {

    public static String decodeMessage(String s, int[][] shifts) {
        // Convert the string to a character array for easy manipulation
        char[] message = s.toCharArray();

        // Step 1: Apply each shift instruction in the order given
        for (int[] shift : shifts) {
            int startDisc = shift[0];
            int endDisc = shift[1];
            int direction = shift[2];

            // Step 2: Apply the rotation for the specified range of discs
            for (int i = startDisc; i <= endDisc; i++) {
                if (direction == 1) {
                    // Clockwise rotation (shift forward by one position)
                    message[i] = shiftForward(message[i]);
                } else {
                    // Counter-clockwise rotation (shift backward by one position)
                    message[i] = shiftBackward(message[i]);
                }
            }
        }

        // Step 3: Convert the character array back to a string and return the result
        return new String(message);
    }

    // Helper function to shift a character forward by one position in the alphabet
    private static char shiftForward(char c) {
        if (c == 'z') {
            return 'a'; // Wrap around from 'z' to 'a'
        } else {
            return (char) (c + 1);
        }
    }

    // Helper function to shift a character backward by one position in the alphabet
    private static char shiftBackward(char c) {
        if (c == 'a') {
            return 'z'; // Wrap around from 'a' to 'z'
        } else {
            return (char) (c - 1);
        }
    }

    public static void main(String[] args) {
        // Example
        String s = "hello";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};
        String decodedMessage = decodeMessage(s, shifts);
        System.out.println(decodedMessage); // Output: jglko
    }
}
