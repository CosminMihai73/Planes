import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serverul rulează pe portul " + PORT);

            Socket clientSocket = serverSocket.accept();
            System.out.println("S-a conectat un client: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            playGame(in, out);

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(BufferedReader in, PrintWriter out) throws IOException {
        char[][] board = new char[3][3];
        initializeBoard(board);

        char currentPlayer = 'X';

        while (true) {
            sendBoardState(out, board);
            out.println("TURN:" + currentPlayer);
            String move = in.readLine();
            int row = Integer.parseInt(move.split(",")[0].trim());
            int col = Integer.parseInt(move.split(",")[1].trim());

            if (isValidMove(board, row, col)) {
                board[row][col] = currentPlayer;
                if (checkWinner(board, currentPlayer)) {
                    sendBoardState(out, board);
                    out.println("WINNER:" + currentPlayer);
                    break;
                } else if (isBoardFull(board)) {
                    sendBoardState(out, board);
                    out.println("DRAW");
                    break;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            } else {
                out.println("INVALID_MOVE");
            }
        }
    }

    private static void initializeBoard(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    private static void sendBoardState(PrintWriter out, char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                out.print(board[row][col]);
                if (col < 2) {
                    out.print(",");
                }
            }
            out.println();
        }
    }

    private static boolean isValidMove(char[][] board, int row, int col) {
        return (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ');
    }

    private static boolean checkWinner(char[][] board, char player) {
        // Verificăm rândurile și coloanele
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player ||
                    board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        // Verificăm diagonalele
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player ||
                board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private static boolean isBoardFull(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
