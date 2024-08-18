import java.util.*;

public class Friendship {

    // Union-Find data structure to track connected components (friendships)
    static class UnionFind {
        int[] parent, rank;

        // Initialize the Union-Find structure
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Each node is its own parent initially
                rank[i] = 1;   // Initial rank is 1 for all nodes
            }
        }

        // Find the representative (root) of the set
        public int find(int x) {
            if (x != parent[x]) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        // Union two sets by rank
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return false; // They are already connected

            // Union by rank to keep the tree flat
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }

        // Check if two nodes are in the same set
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    public static List<String> checkFriendRequests(int n, int[][] restrictions, int[][] requests) {
        // Initialize the Union-Find data structure
        UnionFind uf = new UnionFind(n);

        List<String> result = new ArrayList<>();

        // Process each friend request
        for (int[] request : requests) {
            int house1 = request[0];
            int house2 = request[1];

            boolean canBeFriends = true;

            // Check if the request violates any restrictions
            for (int[] restriction : restrictions) {
                int restricted1 = restriction[0];
                int restricted2 = restriction[1];

                // Check if forming a friendship between house1 and house2
                // would indirectly connect restricted houses
                if (uf.connected(house1, restricted1) && uf.connected(house2, restricted2) ||
                    uf.connected(house1, restricted2) && uf.connected(house2, restricted1)) {
                    canBeFriends = false;
                    break;
                }
            }

            if (canBeFriends) {
                uf.union(house1, house2); // Accept the friend request
                result.add("approved");
            } else {
                result.add("denied"); // Deny the friend request
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Example 1
        int n1 = 3;
        int[][] restrictions1 = {{0, 1}};
        int[][] requests1 = {{0, 2}, {2, 1}};
        System.out.println(checkFriendRequests(n1, restrictions1, requests1)); // Output: [approved, denied]

        // Example 2
        int n2 = 5;
        int[][] restrictions2 = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests2 = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        System.out.println(checkFriendRequests(n2, restrictions2, requests2)); // Output: [approved, denied, approved, denied]
    }
}
