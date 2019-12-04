package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Model extends Observable {
    private Cell[][] board;
    private Cell[][] boardTemp;
    private ArrayList<Cell> pathList, pathListTemp;
    private static int BOARDSIZE_X;
    private static int BOARDSIZE_Y;
    private static int NBRSYMBOL = 4;
    private static int NBRPAIRSYMBOL = NBRSYMBOL / 2;


    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);
    }
    /** @brief
     * Notre modèle de jeu utilise des tableaux temporaire, ou tampons, afin de stocker peu à peu l'avancement du Drag & Drop.
     * On stocke l'avancement du plateau de jeu dans une liste, pathListTemp, avant d'y stocker dans pathList une fois le chemin fini.
     *
     */


    /**
     * Fonction permettant la création d'un plateau de jeu vide
     *
     * @param x : Taille du plateau en abscisse
     * @param y : Taille du plateau en ordonnée
     */
    public void CreateEmptyBoard(int x, int y) {
        BOARDSIZE_X = x;
        BOARDSIZE_Y = y;
        board = new Cell[x][y];
        PathCoordinate p = new PathCoordinate(0, 0);
        pathList = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                CellPath cell = new CellPath(i, j, p, p);
                board[i][j] = cell;
                setChanged();
                notifyObservers(cell);
            }
        }
    }

    /**
     * Fonction pemettant de générer un plateau de taille x * y
     *
     * @param x : Taille du plateau en abscisse
     * @param y : Taille du plateau en ordonnée
     * @return Renvoie un tableau de cellules correspondant au tableau de jeu
     */
    public Cell[][] GenerateBoard(int x, int y) {
        CreateEmptyBoard(x, y);
        GenerateRandomSymbol();
        
        return getBoard();
    }

    /**
     * Fonction permettant de générer une position aléatoire pour tous les symboles
     * Utilise les fonctions de vérification pour éviter certaines positions problèmatique
     */
    private void GenerateRandomSymbol() {
        if (BOARDSIZE_X == 3 && BOARDSIZE_Y == 3) {
            CellSymbol cellSymbol1 = new CellSymbol(0, 0, "S1");
            board[0][0] = cellSymbol1;
            setChanged();
            notifyObservers(cellSymbol1);
            CellSymbol cellSymbol2 = new CellSymbol(2, 0, "S2");
            board[2][0] = cellSymbol2;
            setChanged();
            notifyObservers(cellSymbol2);
            CellSymbol cellSymbol3 = new CellSymbol(2, 2, "S2");
            board[2][2] = cellSymbol3;
            setChanged();
            notifyObservers(cellSymbol3);
            CellSymbol cellSymbol4 = new CellSymbol(1, 2, "S1");
            board[1][2] = cellSymbol4;
            setChanged();
            notifyObservers(cellSymbol4);

        } else {
            boolean symbolAdded;
            for (int j = 1; j <= NBRPAIRSYMBOL; j++) {
                for (int i = 0; i < 2; i++) {
                    symbolAdded = false;
                    int random_x = 0, random_y = 0;
                    CellSymbol cell = null;
                    do {
                        Random r = new Random();
                        random_x = r.nextInt(BOARDSIZE_X);
                        r = new Random();
                        random_y = r.nextInt(BOARDSIZE_Y);
                        //System.out.println(random_x + "/" + random_y);
                        cell = new CellSymbol(random_x, random_y, "S" + j);
                        System.out.println(acceptedNeighboor(cell));
                        if (isEmpty(board[random_x][random_y]) && forbidDiagonal(cell) && acceptedNeighboor(cell)) {
                            symbolAdded = true;
                        }
                        //System.out.println(symbolAdded);
                    } while (!symbolAdded);
                    board[random_x][random_y] = cell;
                    setChanged();
                    notifyObservers(cell);

                }
            }
        }
    }

    /**
     * Fonction permettant de stocker la première case cliquée dans la liste temporaire
     *
     * @param x : La position en abscisse de la case courante
     * @param y :La position en ordonnée de la case courante
     */
    public void startDD(int x, int y) {
        boardTemp = new Cell[BOARDSIZE_X][BOARDSIZE_Y];
        for (int i = 0; i < BOARDSIZE_X; i++) {
            System.arraycopy(board[i], 0, boardTemp[i], 0, BOARDSIZE_Y);
        }
        pathListTemp = new ArrayList<>();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol && !CellInsidePathList(cell)) {
            pathListTemp.add(cell);
            System.out.println(cell.toString());
        }
    }

    /**
     * Fonction permettant la mise à jour des tableaux lors de la fin du Drag and Drop
     */
    public void stopDD() {
        int size = pathListTemp.size();
        if (size > 0) {
            Cell cell = pathListTemp.get(size - 1);
            if (cell instanceof CellSymbol) {
                for (int i = 0; i < BOARDSIZE_X; i++) {
                    System.arraycopy(boardTemp[i], 0, board[i], 0, BOARDSIZE_Y);
                }
                pathList.addAll(pathListTemp);
                hasWon();
            } else {
                for (Cell c : pathListTemp) {
                    deleteCellPath(c);
                }
            }
        }
    }

    /**
     * Permet de vérfier si la case passée en paramètre est vide ou non
     *
     * @param c : La cellule à tester
     * @return Retourne vrai si elle est vide, faux sinon
     */
    private boolean isEmpty(Cell c) {
        if (c instanceof CellPath) {
            return (((CellPath) c).getPathEntry().getX()) == 0 & ((CellPath) c).getPathEntry().getY() == 0 & ((CellPath) c).getPathExit().getX() == 0 & ((CellPath) c).getPathExit().getY() == 0;
        }
        return false;
    }

    /**
     * Fonction nous permettant de regarder la cellule sur laquelle la souris est (pendant un Drag) ainsi que déterminer le chemin en utilisant la case précédente
     * On effectue différents tests pour être sûr que la case courante est bien vide, que ce n'est pas un symbole ou déjà un chemin
     *
     * @param x : La position en abscisse de la case courante
     * @param y : La position en ordonnée de la case courante
     */
    public void parcoursDD(int x, int y) {
        //System.out.println(getStringBoard(boardTemp));
        //System.out.println(getStringBoard(board));
        //System.out.println(((CellPath)board[2][0]).getPathEntry().getX());
        //System.out.println(((CellPath)boardTemp[2][0]).getPathEntry().getX());
        //System.out.println(pathListTemp.size());
        CellPath cellPath;
        Cell previousCell;
        Cell currentCell = boardTemp[x][y];
        int listSize = pathListTemp.size();
        if (listSize > 0) {
            previousCell = pathListTemp.get(listSize - 1);
        } else {
            previousCell = null;
        }
        //System.out.println("parcoursDD1 : " + x + "-" + y);
        if (isEmpty(boardTemp[x][y])) {
            System.out.println("Taille de la list : " + listSize);
            if (previousCell == null) {
                System.out.println("Veuillez commencer par un chemin !!!");
            } else if (previousCell instanceof CellSymbol) {
                if (AcceptedJump(previousCell, currentCell) && listSize < 2) {
                    cellPath = GeneratePath(x, y, previousCell);
                    boardTemp[x][y] = cellPath;
                    pathListTemp.add(cellPath);
                    System.out.println(cellPath.toString());
                    setChanged();
                    notifyObservers(cellPath);
                }
            } else {
                // On peux enlever cellsymbolinsidepath et plutot verifier si la list fait plus de 3 !!!
                if (AcceptedJump(previousCell, currentCell) && !CellSymbolInsidePathListTemp(previousCell)) {
                    cellPath = GeneratePath(x, y, previousCell);
                    boardTemp[x][y] = cellPath;
                    pathListTemp.add(cellPath);
                    System.out.println(cellPath.toString());
                    setChanged();
                    notifyObservers(cellPath);
                    ModifyPreviousCell(x, y, (CellPath) previousCell);
                    setChanged();
                    notifyObservers(previousCell);
                }
            }
        } else if (currentCell instanceof CellSymbol && !CellInsidePathList(currentCell) && AcceptedJump(previousCell, currentCell)) {
            //System.out.println(((CellSymbol) pathListTemp.get(0)).getSymbol());
            //System.out.println(((CellSymbol) boardTemp[x][y]).getSymbol());
            if (pathListTemp.size() == 0) {
                pathListTemp.add(currentCell);
                System.out.println(currentCell.toString());
            }
            if (pathListTemp.size() > 1) {
                if (!checkSymbol((CellSymbol) pathListTemp.get(0), (CellSymbol) boardTemp[x][y])) {
                    System.out.println("Veuillez finir par le même symbole !!!");
                } else {
                    //System.out.println("Symbol ajouter pathlisttemp");
                    int listLength = pathListTemp.size();
                    previousCell = pathListTemp.get(listLength - 1);
                    ModifyPreviousCell(x, y, (CellPath) previousCell);
                    pathListTemp.add(currentCell);
                    System.out.println(currentCell.toString());
                    setChanged();
                    notifyObservers(previousCell);
                }
            }

        } else if (currentCell instanceof CellPath && currentCell == pathListTemp.get(listSize - 2) && !(previousCell instanceof CellSymbol)) {

            CellPath lastCell = (CellPath) pathListTemp.get(listSize - 1);
            ((CellPath) currentCell).setPathExit(new PathCoordinate(0, 0));
            pathListTemp.set(listSize - 2, currentCell);

            pathListTemp.remove(pathListTemp.size() - 1);
            removeCellPath(lastCell);

            setChanged();
            notifyObservers(currentCell);
        }
    }

    /**
     * Fonction permettant la vérification entre deux symboles donnés
     *
     * @param c1 : Le premier symbole à tester
     * @param c2 : Le second symbole à tester
     * @return Vrai si les deux symboles sont les mêmes, faux sinon
     */
    private boolean checkSymbol(CellSymbol c1, CellSymbol c2) {
        //System.out.println(c1.getX() + "/" + c2.getX());
        //System.out.println(c1.getY() + "/" + c2.getY());
        //System.out.println(c1.getSymbol() + "/" + c2.getSymbol());
        return ((c1.getX() != c2.getX() || c1.getY() != c2.getY()) && c1.getSymbol().equals(c2.getSymbol()));
    }


    public Cell[][] getBoard() {
        return board;
    }

    /**
     * On vérifie si la direction choisie avec la souris est valide
     * On empêche aussi les sauts de cases : Si le saut saute + d'une case, on retourne Faux, donc la diagonale ou la sortie de fenêtre ne marche pas.
     *
     * @param previousCell : La case d'où vient le click
     * @param currentCell  : La case où se trouve le click actuellement
     * @return Vrai si le saut est de 1 et qu'il s'agit d'une case vide, faux sinon
     */
    private boolean AcceptedJump(Cell previousCell, Cell currentCell) {
        int jumpX = Math.abs(previousCell.getX() - currentCell.getX());
        int jumpY = Math.abs(previousCell.getY() - currentCell.getY());
        int jump = jumpX + jumpY;
        return jump < 2;
    }

    /**
     * Permet la génération du chemin en se basant sur les coordonnées de la case précedente
     *
     * @param x            : La position en abscisse de la case courante
     * @param y            : La position en ordonnée de la case courante
     * @param previousCell : La case précédente
     * @return Le nouveau chemin créée
     */
    private CellPath GeneratePath(int x, int y, Cell previousCell) {
        CellPath cellPath;
        int previousX = previousCell.getX();
        int previousY = previousCell.getY();
        int jumpX = previousX - x;
        int jumpY = y - previousY;
        //System.out.println("Path :" + jumpX + "/" + jumpY);
        PathCoordinate pathX = new PathCoordinate(jumpX, jumpY);
        PathCoordinate pathY = new PathCoordinate(0, 0);
        cellPath = new CellPath(x, y, pathX, pathY);
        return cellPath;
    }

    /**
     * On modifie l'aspect de la case précédente.
     * Exemple : On passe d'un chemin droit à un chemin courbé car on a tourné par rapport à l'endroit où on était avant
     *
     * @param x            : La position en abscisse de la case courante
     * @param y            : La position en ordonnée de la case courante
     * @param previousCell : La case précédente
     */
    private void ModifyPreviousCell(int x, int y, CellPath previousCell) {
        int jumpPreviousX = x - previousCell.getX();
        int jumpPreviousY = previousCell.getY() - y;
        previousCell.getPathExit().setX(jumpPreviousX);
        previousCell.getPathExit().setY(jumpPreviousY);
    }

    /**
     * Fonction permettant de vérifier la présence d'une cellule dans la liste
     *
     * @param cell : La cellule à tester
     * @return Vrai si la cellule est dans la liste, faux sinon
     */
    private boolean CellInsidePathList(Cell cell) {
        for (Cell value : pathList) {
            if (cell.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fonction permettant de vérifier si la cellule en paramètre est dans la liste temporaire.
     * On vérifie aussi qu'il s'agit d'un CellSymbol avant d'effectuer le test.
     *
     * @param cell : La cellule à tester
     * @return Vrai si la cellule est dans la liste temporaire, faux sinon
     */
    private boolean CellSymbolInsidePathListTemp(Cell cell) {
        if (cell instanceof CellSymbol) {
            for (Cell value : pathListTemp) {
                if (cell.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Efface la dernière liste crééee en appuyant sur undo
     */
    public void OnClickUndo() {
        int listSize = pathList.size();
        int compCellSymbol = 0;
        for (int i = listSize - 1; i >= 0; i--) {
            Cell cell = pathList.get(i);
            if (cell instanceof CellSymbol && compCellSymbol < 2) {
                compCellSymbol++;
                pathList.remove(pathList.size() - 1);
            } else if (cell instanceof CellPath) {
                deleteCellPath(cell);
                resetCellPath((CellPath) cell);
                System.out.println(cell.toString());
                board[cell.getX()][cell.getY()] = cell;
                pathList.remove(pathList.size() - 1);
            } else {
                break;
            }
        }
        System.out.println(pathList.size());

    }

    /**
     * Fonction permettant de supprimer la case passée en paramètre
     *
     * @param c : La case à supprimer
     */
    private void deleteCellPath(Cell c) {
        if (c instanceof CellPath) {
            resetCellPath((CellPath) c);
            setChanged();
            notifyObservers(c);
        }
    }

    /**
     * Fonction permettant de supprimer un chemin entier (et le remet à 0 par un appel à resetCellPath)
     *
     * @param cellPath : Le chemin à supprimer
     */
    private void removeCellPath(CellPath cellPath) {
        int x = cellPath.getX();
        int y = cellPath.getY();
        resetCellPath(cellPath);
        board[x][y] = cellPath;
        setChanged();
        notifyObservers(cellPath);
    }

    /**
     * Fonction permettant de remettre toutes les cases d'un chemin à 0 (case vide)
     *
     * @param cell : Le chemin à reset
     */
    private void resetCellPath(CellPath cell) {
        cell.getPathExit().setX(0);
        cell.getPathExit().setY(0);
        cell.getPathEntry().setX(0);
        cell.getPathEntry().setY(0);

    }

    /**
     * Fonction de victoire  : Vérifie si l'utilisateur a gagné ou non
     * On vérifie si le nombre de symboles (nombre définit en global) est égal au nombre de symboles  dans la liste.
     * On vérifie aussi que la taille de la liste est égale à la taile totale du plateau (x * y).
     * Si c'est le case, l'utilisateur a gagné.
     */
    private void hasWon() {
        int listSize = pathList.size();
        int boardSize = BOARDSIZE_X * BOARDSIZE_Y;
        int compCellSymbol = 0;
        for (Cell cell : pathList) {
            if (cell instanceof CellSymbol) {
                compCellSymbol++;
            }
        }
        System.out.println(listSize + "/" + boardSize + "/" + compCellSymbol + "/" + NBRSYMBOL);
        if (listSize == boardSize && compCellSymbol == NBRSYMBOL) {
            setChanged();
            notifyObservers(1);
        }
    }

    /**
     * Fonction utilisée pour interdire la création de symboles proches par la diagonale
     * Ils étaient souvent source de casse-tête irrésolvable
     *
     * @param c : La cellule à tester
     * @return Vrai si la case n'a pas de voisins en diagonale, Faux sinon
     */
    private boolean forbidDiagonal(Cell c) {
        int xCell = c.getX();
        int yCell = c.getY();
        System.out.println(xCell + "/" + yCell);
        if (xCell + 1 < BOARDSIZE_X && yCell + 1 < BOARDSIZE_Y) {
            if (!isEmpty(board[xCell + 1][yCell + 1])) {
                return false;
            }
        }
        if (xCell - 1 >= 0 && yCell - 1 >= 0) {
            if (!isEmpty(board[xCell - 1][yCell - 1])) {
                return false;
            }
        }
        if (xCell + 1 < BOARDSIZE_X && yCell - 1 >= 0) {
            if (!isEmpty(board[xCell + 1][yCell - 1])) {
                return false;
            }
        }
        if (xCell - 1 >= 0 && yCell + 1 < BOARDSIZE_Y) {
            if (!isEmpty(board[xCell - 1][yCell + 1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Permet de verifier si il y a des symbole dans le voisinage de la case en parametre
     *
     * @param cellSymbol
     * @return Vrai si il plus d'une certaine valeur sinon return false
     */
    private boolean acceptedNeighboor(CellSymbol cellSymbol) {
        int[] listMove = {-1, 0, 1};
        int compSymbol = 0;
        int xCell = cellSymbol.getX();
        int yCell = cellSymbol.getY();
        for (int i : listMove) {
            for (int j : listMove) {
                Cell cell = getCellInBoard(xCell + i, yCell + j);
                if (cell instanceof CellSymbol) {
                    compSymbol++;
                }
            }
        }
        return compSymbol <= 0;
    }

    /**
     * Permet de récupérer une case avec sa position
     *
     * @param x : La position en abscisse de la case choisie
     * @param y : La position en ordonnée de la case choisie
     * @return la cellule desiré ou null si elle n'existe pas
     */
    private Cell getCellInBoard(int x, int y) {
        Cell cell;
        try {
            cell = board[x][y];
        } catch (Exception e) {
            cell = null;
        }
        return cell;
    }
}
