import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            playGame(in, out);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(BufferedReader in, PrintWriter out) throws IOException {
        char[][] board = new char[3][3];
        initializeBoard(board);

        while (true) {
            printBoard(board);
            String response = in.readLine();

            if (response.startsWith("TURN:")) {
                char currentPlayer = response.charAt(5);
                System.out.println("Este rândul jucătorului " + currentPlayer);

                int row = readInteger("Introdu rândul dorit: ");
                int col = readInteger("Introdu coloana dorită: ");
                out.println(row + "," + col);
            } else if (response.startsWith("WINNER:")) {
                char winner = response.charAt(7);
                printBoard(board);
                System.out.println("Jucătorul " + winner + " a câștigat!");
                break;
            } else if (response.equals("DRAW")) {
                printBoard(board);
                System.out.println("Remiză!");
                break;
            } else if (response.equals("INVALID_MOVE")) {
                System.out.println("Mișcare invalidă. Încearcă din nou.");
            } else {
                String[] rows = response.split("\n");
                for (int row = 0; row < 3; row++) {
                    String[] cells = rows[row].split(",");
                    for (int col = 0; col < 3; col++) {
                        board[row][col] = cells[col].charAt(0);
                    }
                }
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

    private static void printBoard(char[][] board) {
        System.out.println("   1   2   3");
        for (int row = 0; row < 3; row++) {
            System.out.print((row + 1) + "  ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col]);
                if (col < 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (row < 2) {
                System.out.println("  ---+---+---");
            }
        }
    }

    private static int readInteger(String message) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        return Integer.parseInt(reader.readLine().trim());
    }
}
