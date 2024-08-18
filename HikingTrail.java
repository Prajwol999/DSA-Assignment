public class HikingTrail {

    // Function to find the longest hike within the elevation gain limit
    public static int longestHike(int[] trail, int k) {
        int maxLength = 0; // To store the maximum length of the hike
        int start = 0; // Start of the current window

        for (int end = 1; end < trail.length; end++) {
            // Check if the elevation gain from the previous point is within the limit
            if (trail[end] - trail[end - 1] > k || trail[end] <= trail[end - 1]) {
                // If not, move the start to the current point
                start = end;
            }

            // Update the maximum length of the hike
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        // Example input
        int[] trail = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k = 3;

        // Find and print the longest hike
        int longestHikeLength = longestHike(trail, k);
        System.out.println("The longest hike length is: " + longestHikeLength);
    }
}
