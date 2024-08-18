// TreeNode class representing a node in the binary tree
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

public class TreasureHunt {

    // Class to store the information about a subtree
    static class SubtreeInfo {
        boolean isBST; // Whether the subtree is a BST
        int sum;       // Sum of the subtree coins
        int min;       // Minimum value in the subtree
        int max;       // Maximum value in the subtree

        SubtreeInfo(boolean isBST, int sum, int min, int max) {
            this.isBST = isBST;
            this.sum = sum;
            this.min = min;
            this.max = max;
        }
    }

    // Global variable to keep track of the maximum sum of a valid BST subtree
    private static int maxSum = 0;

    // Function to find the largest magical grove (maximum BST subtree) in the binary tree
    public static int findLargestMagicalGrove(TreeNode root) {
        // Start the post-order traversal to find the maximum BST subtree
        postOrderTraversal(root);
        return maxSum; // Return the maximum sum found
    }

    // Helper function to perform post-order traversal and gather information about the subtree
    private static SubtreeInfo postOrderTraversal(TreeNode node) {
        // Base case: If the node is null, return a valid BST with sum 0 and extreme min/max values
        if (node == null) {
            return new SubtreeInfo(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        // Recursively process the left and right subtrees
        SubtreeInfo leftInfo = postOrderTraversal(node.left);
        SubtreeInfo rightInfo = postOrderTraversal(node.right);

        // Check if the current node can form a valid BST with its left and right subtrees
        if (leftInfo.isBST && rightInfo.isBST && node.val > leftInfo.max && node.val < rightInfo.min) {
            // Current subtree is a valid BST
            int currentSum = leftInfo.sum + rightInfo.sum + node.val;
            int currentMin = Math.min(node.val, leftInfo.min);
            int currentMax = Math.max(node.val, rightInfo.max);

            // Update the global maximum sum
            maxSum = Math.max(maxSum, currentSum);

            // Return updated information for the parent node
            return new SubtreeInfo(true, currentSum, currentMin, currentMax);
        } else {
            // If the current subtree is not a BST, return invalid BST information
            return new SubtreeInfo(false, 0, 0, 0);
        }
    }

    public static void main(String[] args) {
        // Example tree: [1, 4, 3, 2, 4, 2, 5, null, null, null, null, null, null, 4, 6]
        // Constructing the tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        // Find the largest magical grove (maximum BST subtree)
        int result = findLargestMagicalGrove(root);

        // Output the result
        System.out.println("Largest Magical Grove Sum: " + result); // Output should be 20
    }
}
