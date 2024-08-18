import java.util.*;

public class TSP {

    // Number of cities
    private int numCities;
    // Distance matrix between cities
    private int[][] distances;
    // Random object for generating random numbers
    private Random rand = new Random();

    // Constructor
    public TSP(int numCities, int[][] distances) {
        this.numCities = numCities;
        this.distances = distances;
    }

    // Function to calculate the total distance of the current tour
    private int calculateTourDistance(int[] tour) {
        int totalDistance = 0;
        for (int i = 0; i < numCities - 1; i++) {
            totalDistance += distances[tour[i]][tour[i + 1]];
        }
        // Return to the starting city
        totalDistance += distances[tour[numCities - 1]][tour[0]];
        return totalDistance;
    }

    // Function to perform the Hill Climbing algorithm
    public int[] hillClimbing() {
        // Generate an initial random tour
        int[] currentTour = generateRandomTour();
        int currentDistance = calculateTourDistance(currentTour);

        boolean foundBetter = true;

        // Continue until no better solution is found
        while (foundBetter) {
            foundBetter = false;

            // Try all pairs of cities and swap them
            for (int i = 0; i < numCities - 1; i++) {
                for (int j = i + 1; j < numCities; j++) {
                    // Swap two cities
                    int[] newTour = swapCities(currentTour, i, j);
                    int newDistance = calculateTourDistance(newTour);

                    // If the new tour is better, update the current tour
                    if (newDistance < currentDistance) {
                        currentTour = newTour;
                        currentDistance = newDistance;
                        foundBetter = true;
                    }
                }
            }
        }

        return currentTour;
    }

    // Helper function to generate a random tour
    private int[] generateRandomTour() {
        int[] tour = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            tour[i] = i;
        }
        // Shuffle the cities to create a random tour
        for (int i = numCities - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swap(tour, i, j);
        }
        return tour;
    }

    // Helper function to swap two cities in the tour
    private int[] swapCities(int[] tour, int i, int j) {
        int[] newTour = tour.clone();
        swap(newTour, i, j);
        return newTour;
    }

    // Helper function to swap two elements in an array
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        // Example distance matrix (symmetric TSP)
        int[][] distances = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        int numCities = distances.length;

        // Initialize the TSP solver
        TSP tsp = new TSP(numCities, distances);

        // Perform Hill Climbing to solve the TSP
        int[] bestTour = tsp.hillClimbing();

        // Print the best tour and its total distance
        System.out.println("Best Tour: " + Arrays.toString(bestTour));
        System.out.println("Best Tour Distance: " + tsp.calculateTourDistance(bestTour));
    }
}
