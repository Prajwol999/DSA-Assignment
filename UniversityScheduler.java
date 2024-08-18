import java.util.*;

public class UniversityScheduler {

    public static int mostUsedClassroom(int n, int[][] classes) {
        // Step 1: Sort classes by start time, and by size (number of students) in descending order if start times are the same.
        Arrays.sort(classes, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            } else {
                return Integer.compare(b[1], a[1]);
            }
        });

        // Step 2: Initialize a priority queue to manage classrooms based on their availability.
        PriorityQueue<Integer> availableRooms = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            availableRooms.add(i);
        }

        // Step 3: Initialize a priority queue to manage the end times of currently used classrooms.
        PriorityQueue<int[]> ongoingClasses = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Step 4: Track the number of classes each room held.
        int[] roomUsage = new int[n];

        // Step 5: Process each class in the sorted order.
        for (int[] cls : classes) {
            int start = cls[0];
            int end = cls[1];

            // Step 6: Release any rooms that have become available before the current class starts.
            while (!ongoingClasses.isEmpty() && ongoingClasses.peek()[0] <= start) {
                int[] finishedClass = ongoingClasses.poll();
                availableRooms.add(finishedClass[1]);
            }

            // Step 7: If there's an available room, assign it to the current class.
            if (!availableRooms.isEmpty()) {
                int room = availableRooms.poll();
                roomUsage[room]++;
                ongoingClasses.add(new int[]{end, room});
            } else {
                // Step 8: If no room is available, delay the class until the next room is free.
                int[] earliestFinish = ongoingClasses.poll();
                int room = earliestFinish[1];
                roomUsage[room]++;
                ongoingClasses.add(new int[]{earliestFinish[0] + (end - start), room});
            }
        }

        // Step 9: Find the room that held the most classes. If there's a tie, return the smallest room number.
        int maxClasses = 0;
        int mostUsedRoom = 0;
        for (int i = 0; i < n; i++) {
            if (roomUsage[i] > maxClasses) {
                maxClasses = roomUsage[i];
                mostUsedRoom = i;
            }
        }

        return mostUsedRoom;
    }

    public static void main(String[] args) {
        // Example 1
        int n1 = 2;
        int[][] classes1 = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        System.out.println(mostUsedClassroom(n1, classes1)); // Output: 0

        // Example 2
        int n2 = 3;
        int[][] classes2 = {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};
        System.out.println(mostUsedClassroom(n2, classes2)); // Output: 1
    }
}
