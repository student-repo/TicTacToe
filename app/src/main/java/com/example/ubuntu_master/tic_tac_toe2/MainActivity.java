package com.example.ubuntu_master.tic_tac_toe2;

import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private int boardSize = 4;
    private boolean circleMove = true;
    private int moveCount = 0;
    private enum State{Blank, X, O};
    private State[][] board = new State[boardSize][boardSize];
    private int gamesWonO = 0;
    private int gamesWonX = 0;
    private ArrayList<Integer> idOfBusyFields = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initBoard();
        createBoard();
        initResultInfo();
        setParamsToCurrentMode();

    }

    private void setParamsToCurrentMode() {
        if(getResources().getConfiguration().orientation == 1){
            setPortraitModeParams();
        }
        else{
            setLandscapeModeParams();
        }
//        1 - portrait
//        2 - landscape
    }

    private void setLandscapeModeParams() {
        SquareGridLayout gridLayout = (SquareGridLayout)findViewById(R.id.board_grid);
        LinearLayout rill = (LinearLayout)findViewById(R.id.result_info_linear_layout);
        LinearLayout mll = (LinearLayout)findViewById(R.id.main_linear_layout);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) rill.getLayoutParams();

        gridLayout.setLandscapeMode(true);
        mll.setOrientation(LinearLayout.HORIZONTAL);
        param.weight = 1;
        rill.setLayoutParams(param);
    }

    private void setPortraitModeParams() {
        SquareGridLayout gridLayout = (SquareGridLayout)findViewById(R.id.board_grid);
        LinearLayout rill = (LinearLayout)findViewById(R.id.result_info_linear_layout);
        LinearLayout mll = (LinearLayout)findViewById(R.id.main_linear_layout);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) rill.getLayoutParams();

        gridLayout.setLandscapeMode(false);
        mll.setOrientation(LinearLayout.VERTICAL);
        param.weight = 0;
        rill.setLayoutParams(param);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLandscapeModeParams();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setParamsToCurrentMode();
        }
    }

    private void createBoard(){
        SquareGridLayout gridLayout = (SquareGridLayout)findViewById(R.id.board_grid);
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
                            handleSingleField(v);
                        }
                    }
            );
            oImageView.setImageResource(R.drawable.empty6);
            oImageView.setId(r * boardSize + c);
            oImageView.setLayoutParams (getFieldParam(c, r));
            gridLayout.addView(oImageView);
        }
    }
    private GridLayout.LayoutParams getFieldParam(int columnSpec, int rowSpec){
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();

        param.height = 1;
        param.width = 1;
        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        param.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        return param;
    }


    public void handleSingleField(View view){
        ImageView img = (ImageView)findViewById(view.getId());
        int x = getFieldCoordinate(Integer.parseInt(view.getTag().toString())).x;
        int y = getFieldCoordinate(Integer.parseInt(view.getTag().toString())).y;
        if(validMove(x, y)){
            if(circleMove){
                move(x, y, State.O);

                img.setImageResource(R.drawable.circle4);




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                ONLY FOR PLAY WITH BOT
                int randomField1 = ThreadLocalRandom.current().nextInt(0, boardSize);
                int randomField2 = ThreadLocalRandom.current().nextInt(0, boardSize);
                for(int i=0; i<boardSize; i++) {
                    for(int j=0; j<boardSize; j++) {
                        if(board[i][j] == State.X){
                            if(i > 0){
                                if(board[i-1][j] == State.Blank){
                                    randomField1 = i -1;
                                    randomField2 = j;
                                    break;
                                }
                            }
                            if(j > 0){
                                if(board[i][j-1] == State.Blank){
                                    randomField1 = i;
                                    randomField2 = j - 1;
                                    break;
                                }
                            }
                            if(j > 0 && i > 0){
                                if(board[i-1][j-1] == State.Blank){
                                    randomField1 = i -1;
                                    randomField2 = j - 1;
                                    break;
                                }
                            }
                        }
                    }
                }
                while(board[randomField1][randomField2] != State.Blank){
                    randomField1 = ThreadLocalRandom.current().nextInt(0, boardSize);
                    randomField2 = ThreadLocalRandom.current().nextInt(0, boardSize);
                }
                ImageView img1 = (ImageView)findViewById(randomField1 * boardSize + randomField2);
                move(randomField1, randomField2, State.X);
                img1.setImageResource(R.drawable.cross4);
                idOfBusyFields.add(randomField1 * boardSize + randomField2);
                circleMove = !circleMove;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            }
            else{
                move(x, y, State.X);
                img.setImageResource(R.drawable.cross4);
            }
            idOfBusyFields.add(view.getId());
            circleMove = !circleMove;

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
        LinearLayout lla = (LinearLayout)findViewById(R.id.main_linear_layout);
//        TextView tv = (TextView)findViewById(R.id.landscape_mode_result);
        TextView tv2 = (TextView)findViewById(R.id.result_text_view);
        TextView lll = (TextView) findViewById(R.id.win_info);
        lll.setVisibility(View.VISIBLE);
        l.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
        lla.setVisibility(View.GONE);
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
        LinearLayout lla = (LinearLayout)findViewById(R.id.main_linear_layout);
        lla.setVisibility(View.VISIBLE);
        lll.setVisibility(View.GONE);
        initBoard();
        circleMove = true;
        l.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
        clearBoard();
        moveCount = 0;
    }

    private void clearBoard(){
        ImageView field;
        for(Integer id : idOfBusyFields ){
            field = (ImageView)findViewById(id);
            field.setImageResource(R.drawable.empty6);
        }
    }
    private void initResultInfo() {
        AutoResizeTextView artv = (AutoResizeTextView)findViewById(R.id.result_text_view);
        artv.setText(gamesWonO + " : " + gamesWonX);
    }
}
