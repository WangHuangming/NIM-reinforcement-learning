package com.paul.nimgame;

public class LearningTable {
    int[][] table;

    public LearningTable(int row, int column) {
        table = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                table[i][j] = 0;
            }
        }
    }
    public int getValue(int row, int column) {
        return table[row][column];
    }

    public void setValue(int row, int column, int value) {
        table[row][column] = value;
    }

    public int optimalIndex(int col) {
        int optimalIndex =-1;
        int optimalValue = Integer.MIN_VALUE;
        for (int i=0; i < table.length; i++) {
            if (table[i][col] > optimalValue) {
                optimalValue = table[i][col];
                optimalIndex = i;
            }
        }
        return optimalIndex;
    }
}
