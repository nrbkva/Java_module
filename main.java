import java.util.Scanner;

class Ship {
    private int size;
    private int hits;

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
    }

    public int getSize() {
        return size;
    }

    public void hit() {
        hits++;
    }

    public boolean isSunk() {
        return hits == size;
    }
}

class Board {
    private char[][] grid;
    private Ship[] ships;

    public Board() {
        grid = new char[5][5];
        ships = new Ship[3];
        initializeGrid();
        placeShips();
    }

    private void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '-';
            }
        }
    }

    private void placeShips() {
        ships[0] = new Ship(1);
        ships[1] = new Ship(2);
        ships[2] = new Ship(2);

        // Размещение однопалубного корабля
        placeShipRandomly(ships[0]);

        // Размещение двухпалубных кораблей
        placeShipRandomly(ships[1]);
        placeShipRandomly(ships[2]);
    }

    private void placeShipRandomly(Ship ship) {
        int size = ship.getSize();
        boolean isHorizontal = Math.random() < 0.5;
        int row, col;

        do {
            row = (int) (Math.random() * grid.length);
            col = (int) (Math.random() * grid.length);
        } while (!canPlaceShip(ship, row, col, size, isHorizontal));

        for (int i = 0; i < size; i++) {
            if (isHorizontal) {
                grid[row][col + i] = 'S';
            } else {
                grid[row + i][col] = 'S';
            }
        }
    }

    private boolean canPlaceShip(Ship ship, int row, int col, int size, boolean isHorizontal) {
        if (isHorizontal) {
            if (col + size > grid.length) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] == 'S') {
                    return false;
                }
            }
        } else {
            if (row + size > grid.length) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] == 'S') {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard(boolean revealShips) {
        System.out.println("  0 1 2 3 4");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < grid[i].length; j++) {
                char cell = grid[i][j];
                if (cell == 'S' && !revealShips) {
                    System.out.print("- ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public boolean fire(int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            System.out.println("Некорректные координаты. Попробуйте снова.");
            return false;
        }

        char cell = grid[row][col];
        if (cell == 'X' || cell == '*') {
            System.out.println("Вы уже стреляли в эту ячейку. Попробуйте снова.");
            return false;
        }

        if (cell == '-') {
            System.out.println("Промах!");
            grid[row][col] = '*';
            return true;
        }

        if (cell == 'S') {
            System.out.println("Попадание!");
            grid[row][col] = 'X';
            return true;
        }

        return false;
    }
}

public class BattleShipGame {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в игру Морской Бой!");

        while (!board.isGameOver()) {
            board.printBoard(false);

            System.out.print("Введите номер строки (0-4): ");
            int row = scanner.nextInt();

            System.out.print("Введите номер столбца (0-4): ");
            int col = scanner.nextInt();

            boolean validShot = board.fire(row, col);
            if (!validShot) {
                continue;
            }

            if (board.isGameOver()) {
                System.out.println("Поздравляем! Вы потопили все корабли!");
            }
        }

        scanner.close();
    }
}