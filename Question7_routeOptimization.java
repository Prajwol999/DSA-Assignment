import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Question7_routeOptimization extends JFrame {

    private Map<String, Point> cityPositions;
    private Map<String, Integer> cityIndexMap;
    private int[][] distances;

    private String startCity;
    private String endCity;

    private List<Integer> shortestPath;

    public Question7_routeOptimization() {
        setTitle("Route Optimization for Delivery Service");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize cities and distances
        initializeCitiesAndDistances();

        // Main layout
        setLayout(new BorderLayout(15, 15));

        // Draw panel for graph visualization
        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(graphPanel, BorderLayout.CENTER);

        // Panel for displaying shortest path
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pathPanel.setBackground(new Color(245, 245, 245));  // Soft gray background

        JLabel pathLabel = new JLabel("Shortest Path will be displayed here");
        pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pathLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
        pathLabel.setForeground(new Color(0, 102, 204));  // Dark blue color
        pathPanel.add(pathLabel, BorderLayout.CENTER);
        add(pathPanel, BorderLayout.EAST);

        // Input panel for selecting start and end points
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(230, 230, 250));  // Light lavender color

        JLabel startLabel = new JLabel("Start City:");
        startLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        startLabel.setForeground(new Color(51, 51, 51));  // Dark gray color
        inputPanel.add(startLabel);

        JComboBox<String> startComboBox = new JComboBox<>(cityIndexMap.keySet().toArray(new String[0]));
        startComboBox.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        startComboBox.addActionListener(e -> {
            startCity = (String) startComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        startComboBox.setToolTipText("Select the starting city");
        inputPanel.add(startComboBox);

        JLabel endLabel = new JLabel("End City:");
        endLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        endLabel.setForeground(new Color(51, 51, 51));  // Dark gray color
        inputPanel.add(endLabel);

        JComboBox<String> endComboBox = new JComboBox<>(cityIndexMap.keySet().toArray(new String[0]));
        endComboBox.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        endComboBox.addActionListener(e -> {
            endCity = (String) endComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        endComboBox.setToolTipText("Select the ending city");
        inputPanel.add(endComboBox);

        JButton optimizeButton = new JButton("Optimize Route");
        optimizeButton.setFont(new Font("Sans Serif", Font.BOLD, 14));
        optimizeButton.setBackground(new Color(0, 153, 76));  // Green color
        optimizeButton.setForeground(Color.WHITE);
        optimizeButton.setFocusPainted(false);
        optimizeButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 51)));  // Darker green border
        optimizeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optimizeButton.addActionListener(e -> {
            if (startCity != null && endCity != null && !startCity.equals(endCity)) {
                findShortestPath(startCity, endCity);
                pathLabel.setText("Shortest Path: " + shortestPathToString());
                graphPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select different start and end cities.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        optimizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                optimizeButton.setBackground(new Color(0, 204, 102));  // Lighter green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                optimizeButton.setBackground(new Color(0, 153, 76));  // Original green
            }
        });
        inputPanel.add(optimizeButton);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeCitiesAndDistances() {
        // Initialize city positions (for graphical representation)
        cityPositions = new HashMap<>();
        cityPositions.put("CityA", new Point(200, 100));
        cityPositions.put("CityB", new Point(100, 300));
        cityPositions.put("CityC", new Point(400, 150));
        cityPositions.put("CityD", new Point(300, 400));
        cityPositions.put("CityE", new Point(500, 300));
        cityPositions.put("CityF", new Point(600, 200));

        // Initialize cities and their indices
        String[] cities = {"CityA", "CityB", "CityC", "CityD", "CityE", "CityF"};
        cityIndexMap = new HashMap<>();
        for (int i = 0; i < cities.length; i++) {
            cityIndexMap.put(cities[i], i);
        }

        // Initialize distances (adjacency matrix)
        distances = new int[cities.length][cities.length];
        for (int i = 0; i < cities.length; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
            distances[i][i] = 0;
        }

        // Add connections between cities with updated names
        addConnection("CityA", "CityB", 200);
        addConnection("CityB", "CityD", 50);
        addConnection("CityD", "CityF", 150);
        addConnection("CityF", "CityE", 50);
        addConnection("CityC", "CityF", 55);
        addConnection("CityC", "CityB", 200);
    }

    private void addConnection(String city1, String city2, int distance) {
        int index1 = cityIndexMap.get(city1);
        int index2 = cityIndexMap.get(city2);
        distances[index1][index2] = distance;
        distances[index2][index1] = distance;
    }

    private void findShortestPath(String startCity, String endCity) {
        int startIndex = cityIndexMap.get(startCity);
        int endIndex = cityIndexMap.get(endCity);
        shortestPath = dijkstra(startIndex, endIndex);
    }

    private List<Integer> dijkstra(int start, int end) {
        int numCities = distances.length;
        int[] minDistances = new int[numCities];
        boolean[] visited = new boolean[numCities];
        int[] previous = new int[numCities];

        Arrays.fill(minDistances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        minDistances[start] = 0;

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(i -> minDistances[i]));
        queue.add(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            visited[u] = true;

            for (int v = 0; v < numCities; v++) {
                if (!visited[v] && distances[u][v] != Integer.MAX_VALUE) {
                    int alt = minDistances[u] + distances[u][v];
                    if (alt < minDistances[v]) {
                        minDistances[v] = alt;
                        previous[v] = u;
                        queue.add(v);
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int current = end;
        while (current != -1) {
            path.add(current);
            current = previous[current];
        }
        Collections.reverse(path);
        return path;
    }

    private String shortestPathToString() {
        if (shortestPath == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            sb.append(getCityName(shortestPath.get(i)));
            if (i < shortestPath.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    private class GraphPanel extends JPanel {

        private static final int NODE_RADIUS = 20;
        private static final Color NODE_COLOR = new Color(0, 102, 204);  // Blue color
        private static final Color EDGE_COLOR = new Color(102, 102, 102);  // Gray color
        private static final Color PATH_COLOR = new Color(204, 0, 0);  // Red color
        private static final Font EDGE_FONT = new Font("Sans Serif", Font.PLAIN, 12);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw edges
            g2d.setColor(EDGE_COLOR);
            for (int i = 0; i < distances.length; i++) {
                for (int j = i + 1; j < distances[i].length; j++) {
                    if (distances[i][j] != Integer.MAX_VALUE) {
                        Point p1 = cityPositions.get(getCityName(i));
                        Point p2 = cityPositions.get(getCityName(j));
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                        g2d.setFont(EDGE_FONT);
                        g2d.drawString(String.valueOf(distances[i][j]), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
                    }
                }
            }

            // Draw nodes
            g2d.setColor(NODE_COLOR);
            for (Map.Entry<String, Point> entry : cityPositions.entrySet()) {
                String cityName = entry.getKey();
                Point position = entry.getValue();
                g2d.fillOval(position.x - NODE_RADIUS, position.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2d.setColor(Color.WHITE);
                g2d.drawString(cityName, position.x - g2d.getFontMetrics().stringWidth(cityName) / 2, position.y + 5);
                g2d.setColor(NODE_COLOR);
            }

            // Draw shortest path
            if (shortestPath != null && shortestPath.size() > 1) {
                g2d.setColor(PATH_COLOR);
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    Point p1 = cityPositions.get(getCityName(shortestPath.get(i)));
                    Point p2 = cityPositions.get(getCityName(shortestPath.get(i + 1)));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        private String getCityName(int index) {
            for (Map.Entry<String, Integer> entry : cityIndexMap.entrySet()) {
                if (entry.getValue().equals(index)) {
                    return entry.getKey();
                }
            }
            return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question7_routeOptimization());
    }
    private String getCityName(Integer index) {
        for (Map.Entry<String, Integer> entry : cityIndexMap.entrySet()) {
            if (entry.getValue().equals(index)) {
                return entry.getKey();
            }
        }
        return null;
    }

}