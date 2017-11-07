package com.arm.tourist.CurrentTrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arm.tourist.R;

public class CurrentPlanFragment extends Fragment {


    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.current_plan, container, false);

        final Button addDay = myView.findViewById(R.id.btn_current_addDay);
        final TextView myTextViews[] = new TextView[4];
        final EditText myEditTexts[] = new EditText[4];

        init(myEditTexts, myTextViews, myView);

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i<4; i++)
                {

                    if(myTextViews[i].getVisibility() == View.GONE)
                    {
                        myTextViews[i].setVisibility(View.VISIBLE);
                        myEditTexts[i].setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });
        return  myView;

    }

    void init(EditText myEditTexts[], TextView myTextViews[], View myView)
    {
        myEditTexts[0] = myView.findViewById(R.id.et_day2);
        myEditTexts[1] = myView.findViewById(R.id.et_day3);
        myEditTexts[2] = myView.findViewById(R.id.et_day4);
        myEditTexts[3] = myView.findViewById(R.id.et_day5);

        myTextViews[0] = myView.findViewById(R.id.day2_current);
        myTextViews[1] = myView.findViewById(R.id.day3_current);
        myTextViews[2] = myView.findViewById(R.id.day4_current);
        myTextViews[3] = myView.findViewById(R.id.day5_current);

        for(int i = 0; i<4; i++)
        {
            if(myEditTexts[i].getText().toString().equals(""))
            {
                myEditTexts[i].setVisibility(View.GONE);
                myTextViews[i].setVisibility(View.GONE);
            }

        }
    }
}
