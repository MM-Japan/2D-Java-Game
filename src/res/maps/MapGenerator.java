package res.maps;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MapGenerator {

    static final int WALL = 1;
    static final int GRASS = 0;
    static final int WATER = 2;
    static final int EARTH = 3;
    static final int TREE = 4;
    static final int SAND = 5;

    static final int MAP_SIZE = 50;

    public static void main(String[] args) {
        int[][] map = generateMap(MAP_SIZE);
        writeMapToFile(map, "/home/max/code/MM-Japan/2D-Java-Game/src/res/maps/generated_map.txt");
    }

    // Function to generate the map with lakes, forests, paths, and beaches
    public static int[][] generateMap(int size) {
        int[][] map = new int[size][size];
        Random rand = new Random();

        // Place walls around the edges
        for (int i = 0; i < size; i++) {
            map[i][0] = WALL;
            map[i][size - 1] = WALL;
            map[0][i] = WALL;
            map[size - 1][i] = WALL;
        }

        // Fill the rest with grass
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                map[i][j] = GRASS;
            }
        }

        // Generate lakes and surround them with sand (up to 2-3 lakes)
        for (int l = 0; l < 2 + rand.nextInt(2); l++) {
            createLake(map, rand);
        }

        // Create forests (4-5 clusters of trees)
        for (int f = 0; f < 4 + rand.nextInt(2); f++) {
            createForest(map, rand);
        }

        // Create paths (2-3 paths)
        for (int p = 0; p < 2 + rand.nextInt(2); p++) {
            createPath(map, rand);
        }

        return map;
    }

    // Function to create a lake and surround it with sand
    public static void createLake(int[][] map, Random rand) {
        int x = 5 + rand.nextInt(MAP_SIZE - 10);
        int y = 5 + rand.nextInt(MAP_SIZE - 10);
        int radius = 3 + rand.nextInt(3);

        // Place water
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                if (i > 0 && i < MAP_SIZE - 1 && j > 0 && j < MAP_SIZE - 1) {
                    map[i][j] = WATER;
                }
            }
        }

        // Surround with sand
        for (int i = x - radius - 1; i <= x + radius + 1; i++) {
            for (int j = y - radius - 1; j <= y + radius + 1; j++) {
                if (i > 0 && i < MAP_SIZE - 1 && j > 0 && j < MAP_SIZE - 1 && map[i][j] == GRASS) {
                    map[i][j] = SAND;
                }
            }
        }
    }

    // Function to create a forest
    public static void createForest(int[][] map, Random rand) {
        int x = 5 + rand.nextInt(MAP_SIZE - 10);
        int y = 5 + rand.nextInt(MAP_SIZE - 10);
        int radius = 2 + rand.nextInt(3);

        // Place trees
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                if (i > 0 && i < MAP_SIZE - 1 && j > 0 && j < MAP_SIZE - 1 && map[i][j] == GRASS) {
                    map[i][j] = TREE;
                }
            }
        }
    }

    // Function to create a path
    public static void createPath(int[][] map, Random rand) {
        int x = rand.nextInt(MAP_SIZE);
        int y = rand.nextInt(MAP_SIZE);
        int direction = rand.nextInt(4); // 0: right, 1: down, 2: left, 3: up
        int length = 20 + rand.nextInt(20);

        for (int i = 0; i < length; i++) {
            if (x > 0 && x < MAP_SIZE - 1 && y > 0 && y < MAP_SIZE - 1) {
                map[x][y] = EARTH;
            }

            // Move in the chosen direction
            if (direction == 0) x++;
            else if (direction == 1) y++;
            else if (direction == 2) x--;
            else if (direction == 3) y--;

            // Keep path inside bounds
            if (x < 1) x = 1;
            if (x >= MAP_SIZE - 1) x = MAP_SIZE - 2;
            if (y < 1) y = 1;
            if (y >= MAP_SIZE - 1) y = MAP_SIZE - 2;
        }
    }

    // Function to write the generated map to a text file
    public static void writeMapToFile(int[][] map, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int[] row : map) {
                for (int tile : row) {
                    writer.write(tile + " ");
                }
                writer.newLine();
            }
            System.out.println("Map written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
