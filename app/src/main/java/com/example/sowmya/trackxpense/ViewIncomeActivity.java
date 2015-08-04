/*
 * Copyright (c) 2015 Sowmya Sathyamurthy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.example.sowmya.trackxpense;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sowmya on 7/28/15.
 */
public class ViewIncomeActivity extends ListActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    ArrayList<HashMap<String,String>> list = new ArrayList<>();
    SimpleAdapter adapter;
    Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewincome);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);

        getAllIncome();

        String[] fromColumnHashmap = new String[]{"incomeID","incomeCategory","incomeDate","incomeAmount"};
        int [] toView = new int[]{R.id.hdnIncomeID,R.id.IncomeCategoryColumn,R.id.IncomeDateColumn,R.id.IncomeAmountColumn};

        adapter = new SimpleAdapter(this,list,R.layout.activity_incomedetail,fromColumnHashmap,toView);
        setListAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonCancel:
            default: finish();
                    break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void EditButtonClickHandler(View v)
    {
        RelativeLayout currentLayout = (RelativeLayout)v.getParent();
        Intent objEditIntent = new Intent(getApplicationContext(),AddIncomeActivity.class);
        objEditIntent.putExtra("editFlag", true);
        objEditIntent.putExtra("incomeID",((TextView)currentLayout.getChildAt(0)).getText().toString());
        objEditIntent.putExtra("incomeCategory", (((TextView)currentLayout.getChildAt(1)).getText().toString()));
        objEditIntent.putExtra("incomeAmount", (((TextView)currentLayout.getChildAt(2)).getText().toString()));
        objEditIntent.putExtra("incomeDate", (((TextView)currentLayout.getChildAt(3)).getText().toString()));
        startActivity(objEditIntent);

    }
    private void getAllIncome() {

        list.clear();
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        ArrayList<IncomeModel> incomeList = new ArrayList<>();

        try
        {

            incomeList = db.getAllIncome();
            if(incomeList.size()<1)
            {
                //handle 0 income code
            }
            else
            {
                for(int i=0;i<incomeList.size();i++)
                {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("incomeID", Integer.toString(incomeList.get(i).getIncomeID()));
                    hashMap.put("incomeCategory",incomeList.get(i).getIncomeCategory());
                    hashMap.put("incomeDate",incomeList.get(i).getIncomeDate());
                    hashMap.put("incomeAmount", Float.toString(incomeList.get(i).getIncomeAmount()));
                    list.add(hashMap);
                }
            }
        }
        catch (Exception ex)
        {

        }
        finally {
            db.close();
        }
    }
}
