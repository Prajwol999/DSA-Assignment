public class MovieTheater {

    public static boolean canFriendsSitTogether(int[] nums, int indexDiff, int valueDiff) {
        // Loop through the array to check all possible pairs (i, j)
        for (int i = 0; i < nums.length; i++) {
            // Check the possible seating pairs within the indexDiff limit
            for (int j = i + 1; j <= i + indexDiff && j < nums.length; j++) {
                // Check if the seat difference (absolute value) is within the valueDiff limit
                if (Math.abs(nums[i] - nums[j]) <= valueDiff) {
                    return true; // Found a valid pair, return true
                }
            }
        }
        return false; // No valid pair found, return false
    }

    public static void main(String[] args) {
        // Example input
        int[] nums = {2, 3, 5, 4, 9};
        int indexDiff = 2;
        int valueDiff = 1;

        // Check if two friends can sit together
        boolean result = canFriendsSitTogether(nums, indexDiff, valueDiff);

        // Print the result
        System.out.println(result); // Output: true
    }
}
