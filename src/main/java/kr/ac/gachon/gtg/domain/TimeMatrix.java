package kr.ac.gachon.gtg.domain;

public class TimeMatrix {
    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI, NULL
    }

    private int[][] matrix;

    public TimeMatrix() {
        this.matrix = new int[5][28];
    }

    public TimeMatrix(int row, int col) {
        if (row <= 0) {
            row = 0;
        }

        if (col <= 0) {
            col = 0;
        }

        this.matrix = new int[row][col];
    }

    public TimeMatrix(TimeMatrix timeMatrix) {
        this.matrix = new int[timeMatrix.getRowSize()][timeMatrix.getColSize()];

        for (int i = 0; i < timeMatrix.getRowSize(); i++) {
            for (int j = 0; j < timeMatrix.getColSize(); j++) {
                this.matrix[i][j] = timeMatrix.getMatrixValue(i, j);
            }
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getRowSize() {
        return this.matrix.length;
    }

    public int getColSize() {
        return this.matrix[0].length;
    }

    public int getMatrixValue(int row, int col) {
        return this.matrix[row][col];
    }

    public void clearMatrix() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                this.matrix[i][j] = 0;
            }
        }
    }

    public void parseTimeString(String s) {
        if (s == null || s.length() == 0) {
            return;
        }

        String[] array = s.replaceAll(" ", "").split("(\\s*,)");

        for (String test : array) {
            int row = -1;
            int col = -1;
            int range;

            switch (test.charAt(0)) {
                case '월':
                    row = 0;
                    break;
                case '화':
                    row = 1;
                    break;
                case '수':
                    row = 2;
                    break;
                case '목':
                    row = 3;
                    break;
                case '금':
                    row = 4;
                    break;
            }

            if (test.matches(".*[ABCDE]")) {
                switch (test.substring(1)) {
                    case "A":
                        col = 1;
                        break;
                    case "B":
                        col = 4;
                        break;
                    case "C":
                        col = 7;
                        break;
                    case "D":
                        col = 10;
                        break;
                    case "E":
                        col = 13;
                        break;
                }
                range = 3;
            } else {
                col = (Integer.parseInt(test.substring(1)) * 2) - 2;
                range = 2;
            }

            for (int i = col; i < col + range; i++) {
                this.matrix[row][i]++;
            }
        }
    }

    private String replaceAlphabetTime(String s) {
        char[] alphabetList = {'A', 'B', 'C', 'D', 'E'};

        for (char alphabet : alphabetList) {
            char number = '0';

            switch (alphabet) {
                case 'A':
                    number = '2';
                    break;
                case 'B':
                    number = '3';
                    break;
                case 'C':
                    number = '4';
                    break;
                case 'D':
                    number = '6';
                    break;
                case 'E':
                    number = '8';
                    break;
                default:
                    break;
            }

            s = s.replace(alphabet, number);
        }

        return s;
    }

    public int countDuplicate() {
        int count = 0;

        for (int i = 0; i < getRowSize(); i++) {
            for (int j = 0; j < getColSize(); j++) {
                if (matrix[i][j] > 1) {
                    count += matrix[i][j] - 1;
                }
            }
        }

        return count;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("TimeMatrix:\n");

        for (int i = 0; i < getRowSize(); i++) {
            for (int j = 0; j < getColSize(); j++) {
                stringBuilder.append(matrix[i][j])
                        .append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
