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

      // Create snaking paths (2-3 paths)
      for (int p = 0; p < 2 + rand.nextInt(2); p++) {
          createPath(map, rand);
      }

      // Add ruins scattered around the map
      addRuins(map, rand);

      return map;
  }


    // Function to create a lake and surround it with sand
    public static void createLake(int[][] map, Random rand) {
      int x = 5 + rand.nextInt(MAP_SIZE - 10);
      int y = 5 + rand.nextInt(MAP_SIZE - 10);
      int lakeSize = 15 + rand.nextInt(10);  // Set the lake size (number of tiles)

      // Generate a random blob-like shape for the lake
      for (int i = 0; i < lakeSize; i++) {
          int dx = rand.nextInt(3) - 1;  // Random -1, 0, or 1 (shift x)
          int dy = rand.nextInt(3) - 1;  // Random -1, 0, or 1 (shift y)

          // Make sure the position stays within bounds and is placed on grass
          if (x > 1 && x < MAP_SIZE - 1 && y > 1 && y < MAP_SIZE - 1 && map[x + dx][y + dy] == GRASS) {
              x += dx;  // Move lake tile in random direction
              y += dy;
              map[x][y] = WATER;
          }
      }

      // Surround the lake with sand (beach)
      for (int i = 1; i < MAP_SIZE - 1; i++) {
          for (int j = 1; j < MAP_SIZE - 1; j++) {
              if (map[i][j] == WATER) {
                  // Check adjacent tiles and place sand around water
                  for (int di = -1; di <= 1; di++) {
                      for (int dj = -1; dj <= 1; dj++) {
                          int adjX = i + di;
                          int adjY = j + dj;
                          if (adjX >= 1 && adjX < MAP_SIZE - 1 && adjY >= 1 && adjY < MAP_SIZE - 1) {
                              if (map[adjX][adjY] == GRASS) {
                                  map[adjX][adjY] = SAND;  // Place beach around the lake
                              }
                          }
                      }
                  }
              }
          }
      }
  }



    // Function to create a forest
    public static void createForest(int[][] map, Random rand) {
      int x = 5 + rand.nextInt(MAP_SIZE - 10);
      int y = 5 + rand.nextInt(MAP_SIZE - 10);
      int radius = 4 + rand.nextInt(4);  // Bigger forest with radius between 4 and 7

      // Place trees, but introduce randomness to avoid perfectly square shapes
      for (int i = x - radius; i <= x + radius; i++) {
          for (int j = y - radius; j <= y + radius; j++) {
              if (i > 0 && i < MAP_SIZE - 1 && j > 0 && j < MAP_SIZE - 1 && map[i][j] == GRASS) {
                  // Randomly decide if a tree should be placed (for organic shape)
                  if (rand.nextInt(3) != 0) {  // About 2/3 chance to place a tree
                      map[i][j] = TREE;
                  }
              }
          }
      }
    }


    public static void createPath(int[][] map, Random rand) {
      int x = rand.nextInt(MAP_SIZE);
      int y = rand.nextInt(MAP_SIZE);
      int direction = rand.nextInt(4); // 0: right, 1: down, 2: left, 3: up
      int length = 40 + rand.nextInt(20);  // Longer paths (40-60 tiles long)

      for (int i = 0; i < length; i++) {
          // Check if we can place a path (it shouldn't go over water)
          if (x > 0 && x < MAP_SIZE - 1 && y > 0 && y < MAP_SIZE - 1 && map[x][y] != WATER) {
              map[x][y] = EARTH;  // Place path over grass or forest
          }

          // Randomly change direction more frequently (for more natural paths)
          if (rand.nextInt(4) == 0) {  // About 25% chance to change direction each step
              direction = rand.nextInt(4);  // Randomly choose a new direction
          }

          // Move in the current direction
          if (direction == 0) x++;        // Move right
          else if (direction == 1) y++;   // Move down
          else if (direction == 2) x--;   // Move left
          else if (direction == 3) y--;   // Move up

          // Keep path inside bounds
          if (x < 1) x = 1;
          if (x >= MAP_SIZE - 1) x = MAP_SIZE - 2;
          if (y < 1) y = 1;
          if (y >= MAP_SIZE - 1) y = MAP_SIZE - 2;
      }
  }


  public static void addRuins(int[][] map, Random rand) {
    int numRuins = 10 + rand.nextInt(10); // Place between 10 and 20 ruins

    for (int i = 0; i < numRuins; i++) {
        int x = rand.nextInt(MAP_SIZE);
        int y = rand.nextInt(MAP_SIZE);

        // Ensure the ruin is placed on grass, earth, or forest, but not water
        if (map[x][y] != WATER) {
            map[x][y] = WALL;
        }
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
