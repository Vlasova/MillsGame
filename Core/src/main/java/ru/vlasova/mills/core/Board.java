package ru.vlasova.mills.core;


public class Board {
    private Cell[][][] cells;

    public Board() {
        cells = new Cell[3][3][3];
        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    cells[i][j][k] = new Cell(i, j, k);
        }
        setNotAvailableCells();
    }

    private void setNotAvailableCells() {
        for (int i=0; i<3; i++)
            cells[1][1][i].setStatus(CellStatus.NOTAVAILABLE);
    }

    public Cell[][][] getCells() {
        return cells;
    }

    public void setPiece(Player player, int x, int y, int z) throws RuntimeException{
        if (cells[x][y][z].getStatus().equals(CellStatus.OCCUPIED))
            throw new RuntimeException("Cell is occupied");
        if (cells[x][y][z].getStatus().equals(CellStatus.NOTAVAILABLE))
            throw new RuntimeException("The cell is not available");
        Piece piece = new Piece(player.getColor(), x, y, z);
        player.setPiece(piece);
        cells[x][y][z].setPiece(piece);
    }

    public void moveOneCoord(int color, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) throws RuntimeException{
        if (!isMoveOneCoordPossible(fromX, fromY, fromZ, toX, toY, toZ))
            throw new RuntimeException("the move is impossible");
        if (cells[toX][toY][toZ].getStatus().equals(CellStatus.OCCUPIED))
            throw new RuntimeException("the cell is occupied");
        if (cells[fromX][fromY][fromZ].getPiece().getColor() != color)
            throw new RuntimeException("you can`t move opponent`s piece");
        cells[toX][toY][toZ].setPiece(cells[fromX][fromY][fromZ].removePiece());
    }

    private boolean isMoveOneCoordPossible(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        if (Math.abs(fromX - toX) > 1 || Math.abs(fromY - toY) > 1 || Math.abs(fromZ - toZ) > 1)
            return false;
        if (fromX != toX && fromY != toY)
            return false;
        if (fromX != toX && fromZ != toZ)
            return false;
        if (fromY != toY && fromZ != toZ)
            return false;
        if ((fromX == 0 && toX == 0 && (fromY == 2 || fromY == 0)) && fromZ != toZ)
            return false;
        if ((fromX == 2 && toX ==2 && (fromY == 2 || fromY == 0)) && fromZ != toZ)
            return false;
        return true;
    }

    public void moveAnyCoord(int color, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) throws RuntimeException{
        if (cells[fromX][fromY][fromZ].getPiece().getColor() != color)
            throw new RuntimeException("you can`t move opponent`s piece");
        if (cells[toX][toY][toZ].getStatus().equals(CellStatus.EMPTY))
            cells[toX][toY][toZ].setPiece(cells[fromX][fromY][fromZ].removePiece());
        else
            throw new RuntimeException(("the cell is occupied"));
    }

    public boolean isMill(Player player) {
        if (isMillByX(player, 0) || isMillByX(player, 2) || isMillByY(player, 0)
                || isMillByY(player, 2) || isMillByZHorizontal(player) || isMillByZVertical(player))
            return true;
        else
            return false;
    }

    private boolean isMillByY(Player player, int x) {
        int count = 0;
        Cell[] cellsOfMill = new Cell[3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (cells[x][j][i].getStatus().equals(CellStatus.OCCUPIED) && cells[x][j][i].getPiece().getColor() == player.getColor()) {
                    cellsOfMill[count] = cells[x][j][i];
                    count++;
                }
            }
            if (checkMill(count, player, cellsOfMill))
                return true;
            count = 0;
        }
        return false;
    }

    private boolean isMillByX(Player player, int y) {
        int count = 0;
        Cell[] cellsOfMill = new Cell[3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (cells[j][y][i].getStatus().equals(CellStatus.OCCUPIED) && cells[j][y][i].getPiece().getColor() == player.getColor()) {
                    cellsOfMill[count] = cells[j][y][i];
                    count++;
                }
            }
            if (checkMill(count, player, cellsOfMill))
                return true;
            count = 0;
        }
        return false;
    }

    private boolean isMillByZHorizontal(Player player) {
        int count = 0;
        Cell[] cellsOfMill = new Cell[3];
        for (int i=0; i<2; i++) {
            for (int j=0; j<3; j++) {
                if (cells[i*2][1][j].getStatus().equals(CellStatus.OCCUPIED) && cells[i*2][1][j].getPiece().getColor() == player.getColor()) {
                    cellsOfMill[count] = cells[i*2][1][j];
                    count++;
                }
            }
            if (checkMill(count, player, cellsOfMill))
                return true;
            count = 0;
        }
        return false;
    }

    private boolean isMillByZVertical(Player player) {
        int count = 0;
        Cell[] cellsOfMill = new Cell[3];
        for (int i=0; i<2; i++) {
            for (int j=0; j<3; j++) {
                if (cells[1][i*2][j].getStatus().equals(CellStatus.OCCUPIED) && cells[1][i*2][j].getPiece().getColor() == player.getColor()) {
                    cellsOfMill[count] = cells[1][i*2][j];
                    count++;
                }
            }
            if (checkMill(count, player, cellsOfMill))
                return true;
            count = 0;
        }
        return false;
    }

    private boolean checkMill(int count, Player player, Cell[] cellsOfMill) {
        if (count == 3) {
            for (Cell[] cells : player.getMills()) {
                if (cells[0] == cellsOfMill[0] && cells[1] == cellsOfMill[1] && cells[2] == cellsOfMill[2]) {
                    count = 0;
                    break;
                }
            }
        }
        if (count == 3) {
            player.addMill(cellsOfMill);
            return true;
        }
        else
            return false;
    }


    public Piece removePiece(int color, int x, int y, int z) throws RuntimeException {
        if (cells[x][y][z].getPiece().getColor() == color)
            throw new RuntimeException("you can remove only opponent`s piece");
        if (cells[x][y][z].getStatus().equals(CellStatus.EMPTY))
            throw new RuntimeException("the cell is empty");
        if (cells[x][y][z].getStatus().equals(CellStatus.NOTAVAILABLE))
            throw new RuntimeException("the cell is not available");
        return cells[x][y][z].removePiece();
    }
}
