import java.util.Arrays;

public class BusService {

    // This method rearranges the passengers in groups of 'k'
    public static int[] rearrangePassengers(int[] passengers, int k) {
        int n = passengers.length; // Number of passengers
        int[] result = new int[n]; // Result array to hold rearranged passengers
        int index = 0; // Index to track the position in the result array

        // Loop through the passengers array in chunks of 'k'
        for (int i = 0; i < n; i += k) {
            // Calculate the end of the current chunk
            int e = Math.min(i + k, n); // Ensure we don't go out of bounds

            // If the current chunk has exactly 'k' elements, reverse it
            if (e - i == k) {
                for (int j = e - 1; j >= i; j--) {
                    result[index++] = passengers[j]; // Add elements in reverse order
                }
            } else {
                // If the chunk has fewer than 'k' elements (last chunk), keep the order
                for (int j = i; j < e; j++) {
                    result[index++] = passengers[j]; // Add elements in normal order
                }
            }
        }

        return result; // Return the rearranged array
    }

    public static void main(String[] args) {
        
        // Call rearrangePassengers with a sample input and print the result
        int[] passengers = rearrangePassengers(new int[]{1, 2, 3, 4, 5}, 2);
        System.out.println(Arrays.toString(passengers)); // Output: [2, 1, 4, 3, 5]
    }
}
