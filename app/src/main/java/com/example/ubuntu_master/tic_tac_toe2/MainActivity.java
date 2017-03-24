package com.example.ubuntu_master.tic_tac_toe2;

import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int boardSize = 4;
    private boolean whiteMove = true;
    private int moveCount = 0;
    private enum State{Blank, X, O};
    private State[][] board = new State[boardSize][boardSize];
    private int gamesWonO = 0;
    private int gamesWonX = 0;
    private ArrayList<Integer> idOfBusyFields = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBoard();
        createBoard();

        AutoResizeTextView aa = (AutoResizeTextView)findViewById(R.id.portrait_mode_result);
        aa.setText(gamesWonO + " : " + gamesWonX);
        String s = String.valueOf(getResources().getConfiguration().orientation);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        1 - portrait
//        2 - landscape


    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        LinearLayout l = (LinearLayout)findViewById(R.id.landscape_mode_result_info);
//        LinearLayout ll = (LinearLayout)findViewById(R.id.portrait_mode_result_info);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            l.setVisibility(View.VISIBLE);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//            ll.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//            l.setVisibility(View.GONE);
//            ll.setVisibility(View.VISIBLE);
        }
    }

    private void createBoard(){
        GridLayout gridLayout = (GridLayout)findViewById(R.id.board_grid);
        gridLayout.removeAllViews();

        gridLayout.setColumnCount(boardSize);
        gridLayout.setRowCount(boardSize);
        for(int i =0, c = 0, r = 0; i < boardSize * boardSize; i++, c++) {
            if(c == boardSize){
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(this);
            oImageView.setTag(r * boardSize + c);
            oImageView.setOnClickListener(
                    new ImageView.OnClickListener(){

                        @Override
                        public void onClick(View v) {
//                            ImageView img = (ImageView)findViewById(v.getId());
//                            System.out.println(getFieldCoordinate(Integer.parseInt(v.getTag().toString())));
                            handleSingleField(v);
                        }
                    }
            );
//            System.out.println(r * boardSize + c);
//            Toast.makeText(this, "Draw " + (r * boardSize + c), Toast.LENGTH_SHORT).show();
            oImageView.setImageResource(R.drawable.empty4);
            oImageView.setId(r * boardSize + c);
            oImageView.setLayoutParams (getFieldParam(c, r));
            gridLayout.addView(oImageView);
        }
    }
    private GridLayout.LayoutParams getFieldParam(int columnSpec, int rowSpec){
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        Point fieldSize = getScreenSize();// fieldSize.x = screenWidth, fieldSize.y = screenHeight



//        RelativeLayout r = (RelativeLayout)findViewById(R.id.activity_main);
//        LinearLayout l = (LinearLayout)findViewById(R.id.portrait_mode_result_info);
//        r.setVisibility(View.GONE);
//        l.setVisibility(View.GONE);
//        Toast.makeText(this, String.valueOf(l.getHeight()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(r.getHeight()), Toast.LENGTH_SHORT).show();

        param.height = (int)Math.floor(fieldSize.x / boardSize);
        param.width = (int)Math.floor(fieldSize.x / boardSize);
        param.columnSpec = GridLayout.spec(columnSpec);
        param.rowSpec = GridLayout.spec(rowSpec);
        return param;
    }

    private Point getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }

    public void handleSingleField(View view){
        ImageView img = (ImageView)findViewById(view.getId());
//        System.out.println("ala ma  ktoa");
        int x = getFieldCoordinate(Integer.parseInt(view.getTag().toString())).x;
        int y = getFieldCoordinate(Integer.parseInt(view.getTag().toString())).y;
//        System.out.println(x + " " + y + " field id = " + view.getId());
        if(validMove(x, y)){
            if(whiteMove){
                move(x, y, State.O);

                img.setImageResource(R.drawable.circle4);
//                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            else{
                move(x, y, State.X);
                img.setImageResource(R.drawable.cross4);
//                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            idOfBusyFields.add(view.getId());
            whiteMove = !whiteMove;

        }
        else{
            Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
        }

    }

    private void move(int x, int y, State s){
        board[x][y] = s;
        moveCount++;

        checkCol(x, y, s);
        checkRow(x, y, s);
        checkDiagonal(x, y, s);
        checkAntiDiagonal(x, y, s);
        checkDraw();
    }

    private void checkCol(int x, int y, State s){
        for(int i = 0; i < boardSize; i++){
            if(board[x][i] != s)
                break;
            if(i == boardSize - 1){
                handleWinner(s);
                Toast.makeText(this, "Winner! - " + s.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void checkRow(int x, int y, State s){
        for(int i = 0; i < boardSize; i++){
            if(board[i][y] != s)
                break;
            if(i == boardSize - 1){
                handleWinner(s);
                Toast.makeText(this, "Winner! - " + s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkDiagonal(int x, int y, State s){
        if(x == y){
            for(int i = 0; i < boardSize; i++){
                if(board[i][i] != s)
                    break;
                if(i == boardSize - 1){
                    handleWinner(s);
                    Toast.makeText(this, "Winner! - " + s.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkAntiDiagonal(int x, int y, State s){
        if(x + y == boardSize - 1){
            for(int i = 0;i < boardSize;i++){
                if(board[i][(boardSize - 1)-i] != s)
                    break;
                if(i == boardSize - 1){
                    handleWinner(s);
                    Toast.makeText(this, "Winner! - " + s.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkDraw(){
        if(moveCount == (int)(Math.pow(boardSize, 2))){
            //report draw
//            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            handleWinner(State.Blank);
        }
    }


    private boolean validMove(int x, int y){
        return (board[x][y] == State.Blank);
    }

    private Point getFieldCoordinate(int tag){ // getFieldCoordinate(int tag).x = row   getFieldCoordinate(int tag).y = column
        return new Point((int) tag / boardSize, tag % boardSize);
    }

    private void initBoard(){
        for(int i=0; i < board.length; i++){
            Arrays.fill( board[i], State.Blank );
        }
    }

    private void handleWinner(State s){
        GridLayout l = (GridLayout) findViewById(R.id.board_grid);
        LinearLayout ll = (LinearLayout)findViewById(R.id.quit_play_again_layout);
//        TextView tv = (TextView)findViewById(R.id.landscape_mode_result);
        TextView tv2 = (TextView)findViewById(R.id.portrait_mode_result);
        TextView lll = (TextView) findViewById(R.id.win_info);
        lll.setVisibility(View.VISIBLE);
        l.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
        if(s == State.O){
            gamesWonO++;
            Toast.makeText(this, s + "  won ! ! !", Toast.LENGTH_SHORT).show();
            lll.setText(R.string.circle_won);
        }
        else if(s == State.X){
            gamesWonX++;
            lll.setText(R.string.cross_won);
            Toast.makeText(this, s + "  won ! ! !", Toast.LENGTH_SHORT).show();
        }
        else {
            lll.setText(R.string.draw);
            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
        }

//        tv.setText(gamesWonO + " : " + gamesWonX);
        tv2.setText(gamesWonO + " : " + gamesWonX);
    }
    public void handleQuitButton(View view){
        System.exit(1);
    }

    public void handlePlayAgainButton(View view){
        GridLayout l = (GridLayout)findViewById(R.id.board_grid);
        LinearLayout ll = (LinearLayout)findViewById(R.id.quit_play_again_layout);
        TextView lll = (TextView) findViewById(R.id.win_info);
        lll.setVisibility(View.GONE);
        initBoard();
        whiteMove = true;
        l.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
        clearBoard();
        moveCount = 0;
    }

    private void clearBoard(){
        ImageView field;
        for(Integer id : idOfBusyFields ){
            field = (ImageView)findViewById(id);
            field.setImageResource(R.drawable.empty4);
//            field.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
