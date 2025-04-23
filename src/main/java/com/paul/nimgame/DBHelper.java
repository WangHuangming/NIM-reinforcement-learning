package com.paul.nimgame;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBHelper {
    
    public static final String DB_NAME = "nim.txt";
    private static DBHelper myInstance;
    private Game game;

    private DBHelper(Game game) {
        this.game=game;
    }
    
    public static DBHelper getInstance(Game game) {
        if(myInstance == null) {
            myInstance = new DBHelper(game);
        }
        return myInstance;
    }

    public void createTable() {
        String projectRoot = System.getProperty("user.dir");
        File resFolder = new File(projectRoot + File.separator + "res" + File.separator + "files");
        if (!resFolder.exists()) {
            resFolder.mkdirs();
        }
        File file = new File(resFolder, DB_NAME);

        
        try(FileWriter fileWriter = new FileWriter(file)) {
            for (int i = 0; i < game.getmMaxPick(); ++i) {
                for (int j = 0; j < game.getStartingSticks(); ++j) {
                    fileWriter.write("0 ");
                }
                fileWriter.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LearningTable readTable() {
        String projectRoot = System.getProperty("user.dir");
        File resFolder = new File(projectRoot + File.separator + "res" + File.separator + "files");
        if (!resFolder.exists()) {
            throw new RuntimeException("Resource folder does not exist.");
        }
        File file = new File(resFolder, DB_NAME);
        if(!file.exists()){
            return null;
        }

        LearningTable table = new LearningTable(game.getmMaxPick(),game.getStartingSticks());
        try(Scanner scanner = new Scanner(file)) {
            int row = 0;
            int col = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                
                for(String value : values) {
                    table.setValue(row, col, Integer.parseInt(value));
                    col++;
                }
                row++;
                col = 0;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public void updateTable(LearningTable table, int row, int column, int newValue) {
        String projectRoot = System.getProperty("user.dir");
        File resFolder = new File(projectRoot + File.separator + "res" + File.separator + "files");
        if (!resFolder.exists()) {
            throw new RuntimeException("Resource folder does not exist.");
        }
        File file = new File(resFolder, DB_NAME);

        try {
            List<String> lines = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
            }
            if (row >= lines.size()) {
                throw new IndexOutOfBoundsException("Row index out of bounds.");
            }
            String[] values = lines.get(row).split(" ");
            if (column >= values.length) {
                throw new IndexOutOfBoundsException("Column index out of bounds.");
            }
            values[column] = String.valueOf(newValue);
            lines.set(row, String.join(" ", values));

            try (FileWriter fileWriter = new FileWriter(file)) {
                for (String line : lines) {
                    fileWriter.write(line + System.lineSeparator());
                }
            }

            table.setValue(row, column, newValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
