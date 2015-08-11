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

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sowmya on 7/21/15.
 */
public class ViewExpenseActivity extends ListActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    ArrayList<HashMap<String,String>> list =
            new ArrayList<>();
    Button buttonCancel ;
    Spinner reportInterval;
    String selectedInterval;
    ImageButton previousButton,nextButton,editButton,homeButton;
    Calendar modifiedDate;
    DateFormat monthlyFormat = new SimpleDateFormat("MMMM");
    DateFormat yearlyFormat = new SimpleDateFormat("yyyy");
    DateFormat dateFormat = new SimpleDateFormat("dd");
    DateFormat simpledateFormat = new SimpleDateFormat("MM/dd/yyyy");
    //Format weeklyformat = getWeeklyFormat();
    TextView selectedIntervalTextView;
    String strInterval,value;


    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewexpense);

        list = new ArrayList<>();
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        reportInterval = (Spinner)findViewById(R.id.spinnerReportInterval);
        selectedIntervalTextView = (TextView)findViewById(R.id.selectedInterval);

        InvokeButtonClick();

        TrackXpenseHelper helper = new TrackXpenseHelper();
        List<String> listReportInterval = helper.getListInterval();//  new ArrayList<>();
        //listReportInterval.add("Weekly");
        //listReportInterval.add("Monthly");
        //listReportInterval.add("Yearly");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listReportInterval);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportInterval.setAdapter(spinnerAdapter);
        reportInterval.setOnItemSelectedListener(this);

        adapter = new SimpleAdapter(this, list, R.layout.activity_expensedetail, new String[]{"_id","_category", "_date", "_amount","_description"},
                new int[] {R.id.hdnExpenseID,R.id.CategoryColumn,R.id.DateColumn,R.id.AmountColumn,R.id.hdnDescription});

        //getAllExpenses(strInterval,value);

        setListAdapter(adapter);

        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonPrevious:
                getPrevious();
                break;

            case R.id.buttonNext:
                getNext();
                break;

            case R.id.buttonHome:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.buttonCancel:
            default:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        reportInterval.setSelection(position);
        selectedInterval = reportInterval.getSelectedItem().toString();
        try {
            setSelectedIntervalValue(selectedInterval);
            adapter.notifyDataSetChanged();
        }
        catch (ParseException e)
        {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onListItemClick(ListView listview, View view, int position, long id) {
        super.onListItemClick(listview, view, position, id);

        Intent objEditIntent = new Intent(getApplicationContext(),AddExpenseActivity.class);
        objEditIntent.putExtra("editFlag",true);

        objEditIntent.putExtra("_id",Integer.parseInt(list.get(position).get("_id").toString()));
        objEditIntent.putExtra("_date", list.get(position).get("_date"));
        objEditIntent.putExtra("_category", list.get(position).get("_category"));
        objEditIntent.putExtra("_amount", list.get(position).get("_amount"));
        objEditIntent.putExtra("_description", list.get(position).get("_description"));

        startActivity(objEditIntent);
    }

    private void getAllExpenses(String interval,String value) {
        DataBaseHelper db;
        list.clear();
        db = new DataBaseHelper(this);
        ArrayList<ExpenseActivity> expenseList;
        if( interval =="" || value=="" || interval == null || value == null) {

            expenseList = db.getAllExpenses();
        }
        else
        {
            expenseList = db.getExpenseByInterval(interval,value);
        }

        for(int i =0;i<expenseList.size();i++){

            HashMap<String,String> hashmap = new HashMap<>();
            hashmap.put("_id",Integer.toString(expenseList.get(i).get_ID()));
            hashmap.put("_date",expenseList.get(i).get_date().toString());
            hashmap.put("_category",expenseList.get(i).get_category().toString());
            hashmap.put("_amount", Float.toString(expenseList.get(i).get_amount()));
            hashmap.put("_description", expenseList.get(i).get_description().toString());
            hashmap.put("select","");
            list.add(hashmap);
        }

        db.close();
    }

    //private method to set the selected interval based on drop down value
    //parameter interval - monthly/yearly/weekly
    private void setSelectedIntervalValue (String interval) throws ParseException
    {

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        modifiedDate=currentDate;
        String result="";
        switch (interval.toLowerCase())
        {
            case "weekly":
                result = getWeeklyFormat(modifiedDate);
                strInterval="weekly";
                value = simpledateFormat.format(currentDate.getTime());
                getAllExpenses(strInterval,value);

                break;
            case "monthly":

                result = monthlyFormat.format(currentDate.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(monthlyFormat.parse(result));
                int monthInt = cal.get(Calendar.MONTH) + 1;
                strInterval = "monthly";
                value = monthInt<10 ? "0"+Integer.toString(monthInt) :Integer.toString(monthInt);
                getAllExpenses(strInterval,value);
                break;

            case "yearly":

                result = yearlyFormat.format(currentDate.getTime());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(yearlyFormat.parse(result));
                int yearInt = cal2.get(Calendar.YEAR);
                strInterval = "yearly";
                value = Integer.toString(yearInt);
                getAllExpenses(strInterval,value);
                DataBaseHelper db2 = new DataBaseHelper(getApplicationContext());
                db2.getExpenseByInterval("yearly",Integer.toString(yearInt));

                //Intent viewExpenseIntent1 = new Intent(ViewExpenseActivity.this,ViewExpenseActivity.class);
                //startActivity(viewExpenseIntent1);
                break;


        }

        selectedIntervalTextView.setText(result);
    }


    private String getWeeklyFormat(Calendar dateValue) {

        String startDate,endDate,month,result;
        //result should be like july 20-26 (from Monday to Sunday)
        //Code to get start of week
        Calendar cal = dateValue;
        while (cal.get(Calendar.DAY_OF_WEEK)!=2)//2 is monday - start of week
        {
            cal.add(Calendar.DATE,-1);
        }
        startDate = dateFormat.format(cal.getTime());
        month = monthlyFormat.format(cal.getTime());

        Calendar cal2 = dateValue;
        //Code to get end of week
        while (cal2.get(Calendar.DAY_OF_WEEK)!=1)//1 is sunday. end of week
        {
            cal2.add(Calendar.DATE,1);
        }
        endDate = dateFormat.format(cal2.getTime());

        result = month.substring(0,3)+" "+startDate+" - "+ endDate;
        return result;
        //cal.getTime();        //Mon Jul 20 23:13:16 PDT 2015

    }
    private void InvokeButtonClick()
    {
        previousButton = (ImageButton) findViewById(R.id.buttonPrevious);
        nextButton = (ImageButton) findViewById(R.id.buttonNext);
        editButton = (ImageButton)findViewById(R.id.EditColumn);
        homeButton = (ImageButton)findViewById(R.id.buttonHome);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);

    }

    public void EditButtonClickHandler(View v)
    {
        RelativeLayout currentLayout = (RelativeLayout) v.getParent();
        Intent objEditIntent = new Intent(getApplicationContext(),AddExpenseActivity.class);
        objEditIntent.putExtra("editFlag", true);


        objEditIntent.putExtra("_id",((TextView)currentLayout.getChildAt(0)).getText().toString());
        objEditIntent.putExtra("_category", (((TextView)currentLayout.getChildAt(1)).getText().toString()));
        objEditIntent.putExtra("_amount", (((TextView)currentLayout.getChildAt(2)).getText().toString()));
        objEditIntent.putExtra("_date", (((TextView)currentLayout.getChildAt(3)).getText().toString()));
        objEditIntent.putExtra("_description", (((TextView) currentLayout.getChildAt(5)).getText().toString()));
        startActivity(objEditIntent);

    }
    private void getPrevious()
    {
        try {


            String result = "";
            switch (selectedInterval.toLowerCase()) {
                case "weekly":
                    modifiedDate.add(Calendar.DAY_OF_YEAR,-7);
                    result=getWeeklyFormat(modifiedDate);
                    setIntervalAndValue("weekly",simpledateFormat.format(modifiedDate.getTime()));
                    break;

                case "monthly":
                    modifiedDate.add(Calendar.MONTH, -1);
                    result = monthlyFormat.format(modifiedDate.getTime());
                    setIntervalAndValue("monthly", result);
                    break;

                case "yearly":
                    modifiedDate.add(Calendar.YEAR, -1);
                    result = yearlyFormat.format(modifiedDate.getTime());
                    setIntervalAndValue("yearly", result);
                    break;
            }
            selectedIntervalTextView.setText(result);
            adapter.notifyDataSetChanged();
        }
        catch (ParseException ex)
        {

        }
    }

    private void getNext(){
        try {
            String result = "";
            switch (selectedInterval.toLowerCase()) {
                case "weekly":
                    modifiedDate.add(Calendar.DAY_OF_YEAR,7);
                    result=getWeeklyFormat(modifiedDate);
                    setIntervalAndValue("weekly",simpledateFormat.format(modifiedDate.getTime()));
                    break;

                case "monthly":
                    modifiedDate.add(Calendar.MONTH, 1);
                    result = monthlyFormat.format(modifiedDate.getTime());
                    setIntervalAndValue("monthly", result);
                    break;

                case "yearly":
                    modifiedDate.add(Calendar.YEAR, 1);
                    result = yearlyFormat.format(modifiedDate.getTime());
                    setIntervalAndValue("yearly", result);
                    break;
            }
            selectedIntervalTextView.setText(result);
            adapter.notifyDataSetChanged();
        }
        catch (ParseException e) {

        }
    }

    private void setIntervalAndValue(String i,String v) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        switch (i.toLowerCase())
        {
            case "monthly":
                cal.setTime(monthlyFormat.parse(v));
                int monthInt = cal.get(Calendar.MONTH) + 1;
                v = monthInt<10 ? "0"+Integer.toString(monthInt) :Integer.toString(monthInt);
                break;
            case "yearly":
            case "weekly":
            default:
                break;

        }

        //DataBaseHelper db1 = new DataBaseHelper(getApplicationContext());
        //db1.getExpenseByInterval("monthly", monthInt<10 ? "0"+Integer.toString(monthInt) :Integer.toString(monthInt) );
        //db1.close();
        getAllExpenses(i, v);
    }


}
