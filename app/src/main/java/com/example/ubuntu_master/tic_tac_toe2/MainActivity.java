package com.example.ubuntu_master.tic_tac_toe2;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = (GridLayout)findViewById(R.id.board_grid);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Toast.makeText(this, "high = " + height + "  width = " + width, Toast.LENGTH_SHORT).show();




        gridLayout.removeAllViews();

        int total = 16;
        int column = 4;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for(int i =0, c = 0, r = 0; i < total; i++, c++)
        {
            if(c == column)
            {
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(this);
            oImageView.setImageResource(R.drawable.cross3);
            GridLayout.LayoutParams param =new GridLayout.LayoutParams();
            param.height = (int)Math.floor(width / column);
            param.width = (int)Math.floor(width / column);
//            param.height = 0;
//            param.width = 0;
//            param.height = ActionBar.LayoutParams.MATCH_PARENT;
//            param.width = ActionBar.LayoutParams.MATCH_PARENT;
//            param.rightMargin = 5;
//            param.topMargin = 5;
//            grid:layout_columnWeight="1"
//            grid:layout_rowWeight="1"
//            GridLayout.LayoutParams params = (GridLayout.LayoutParams) child.getLayoutParams();
//            params.width = (parent.getWidth()/parent.getColumnCount()) -params.rightMargin - params.leftMargin;
//            child.setLayoutParams(params);
//            param.setGravity(Gravity.FILL_HORIZONTAL);
//            param.setGravity(Gravity.FILL);
//            param.setGravity(Gravity.FILL_HORIZONTAL);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            oImageView.setLayoutParams (param);
            gridLayout.addView(oImageView);
        }
    }
}
