import java.io.*;
import java.net.*;


class Client {
    private static void applyMove(String move, int[][] board) {
        move = move.toUpperCase().replaceAll("\\s+", "");

        int fromCol = move.charAt(0) - 'A';
        int fromRow = 8 - Character.getNumericValue(move.charAt(1));
        int toCol = move.charAt(3) - 'A';
        int toRow = 8 - Character.getNumericValue(move.charAt(4));

        int piece = board[fromRow][fromCol];
        board[fromRow][fromCol] = Game.EMPTY;
        board[toRow][toCol] = piece;
    }

    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];

        try {
            MyClient = new Socket("localhost", 8888);

            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Game game = null;
            AI ai = null;

            while(1 == 1){
                char cmd = 0;

                cmd = (char)input.read();
                System.out.println(cmd);
                // Debut de la partie en joueur blanc
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        y++;
                        if(y == 8){
                            y = 0;
                            x++;
                        }
                    }
                    game = new Game(board);

                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    ai = new AI(Game.RED);

                    Move bestMove = ai.getBestMove(game);
                    String move = bestMove.toString();
                    System.out.println("AI joue : " + move);
                    applyMove(move, board);

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    ai = new AI(Game.BLACK);

                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        y++;
                        if(y == 8){
                            y = 0;
                            x++;
                        }
                    }
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :"+ s);

                    applyMove(s, board);

                    game = new Game(board);

                    System.out.println("Entrez votre coup : ");

                    Move bestMove = ai.getBestMove(game);
                    String move = bestMove.toString();
                    System.out.println("AI joue : " + move);
                    applyMove(move, board);

                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // La partie est terminée
                if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}


