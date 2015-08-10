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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sowmya on 7/28/15.
 */
public class AddIncomeActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private TextView labelAddIncome;
    private EditText incomeDate,incomeAmount,incomeDescription;
    Button buttonSave,buttonCancel,buttonDelete;
    public ImageButton buttonCalendarImage,buttonHome;
    private Spinner incomeCategory;
    private List<String> list = new ArrayList<>();
    DateFormat simpleDateFormat;
    DatePickerDialog datePickerDialog;
    private Boolean editFlag = false;
    private int hdnIncomeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addincome);

        setCalendarDialog();
        findViewsAndSetClickHandler();
        loadCategoryValues();

        editFlag = getIntent().getBooleanExtra("editFlag",false);
        if(editFlag)
        {//populate view with saved values fetched from database
            populateIncomeByIncomeID();
        }

    }

    private void findViewsAndSetClickHandler() {
        //Find Views
        labelAddIncome = (TextView) findViewById(R.id.label_AddIncome);
        incomeDate = (EditText)findViewById(R.id.date);
        incomeAmount = (EditText)findViewById(R.id.amount);
        incomeDescription = (EditText)findViewById(R.id.description);
        incomeCategory = (Spinner)findViewById(R.id.spinnerCategory);

        buttonCalendarImage = (ImageButton) findViewById(R.id.calendaricon);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonHome = (ImageButton) findViewById(R.id.buttonHome);

        //Set button click handler

        buttonCalendarImage.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonHome.setOnClickListener(this);

    }

    //private method to populate income category drop down values
    private void loadCategoryValues()
    {
        TrackXpenseHelper helper = new TrackXpenseHelper();
        list = helper.getIncomeCategory();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeCategory.setAdapter(categoryAdapter);
        incomeCategory.setOnItemSelectedListener(this);
    }

    private void setCalendarDialog()
    {
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                incomeDate.setText(simpleDateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        long result;
        Intent viewIncomeIntent = new Intent(AddIncomeActivity.this,ViewIncomeActivity.class);
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        switch (v.getId())
        {

            case R.id.calendaricon:
                datePickerDialog.show();
                break;

            case R.id.buttonSave:
                if(!ValidateInput())
                    DisplayAlertDialog("validate");

                else {
                    result = db.SaveIncome(parseIncomeModel(), "save");
                    startActivity(viewIncomeIntent);
                }
                break;

            case R.id.buttonDelete:
                DisplayAlertDialog("delete");
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
        incomeCategory.setSelection(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private IncomeModel parseIncomeModel()
    {
        IncomeModel income = new IncomeModel();

        income.incomeID = editFlag ? hdnIncomeID : 0;
        income.incomeCategory = incomeCategory.getSelectedItem().toString();
        income.incomeAmount = Float.parseFloat(incomeAmount.getText().toString());
        income.incomeDate = incomeDate.getText().toString();
        income.incomeDescription = incomeDescription.getText().toString();

        return income;
    }

    //populate the income object on click of edit button
    private void populateIncomeByIncomeID()
    {
        hdnIncomeID = Integer.parseInt(getIntent().getStringExtra("incomeID"));
        labelAddIncome.setText("EDIT INCOME DETAILS");

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        IncomeModel income = db.getIncomeByID(hdnIncomeID);


        incomeAmount.setText(Float.toString(income.getIncomeAmount()));
        incomeCategory.setSelection(list.indexOf(income.getIncomeCategory()));
        incomeDate.setText(income.getIncomeDate());
        incomeDescription.setText(income.getIncomeDescription());

        buttonDelete.setVisibility(View.VISIBLE);

    }


    public void DisplayAlertDialog(String mode)
    {
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(this);
        if(mode=="delete") {
            dialogBuilder.setMessage("Confirm Delete?");
            dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
                    dbHelper.getWritableDatabase();
                    dbHelper.SaveIncome(parseIncomeModel(), "delete");
                    dbHelper.close();
                    Intent viewIncomeIntent1 = new Intent(AddIncomeActivity.this, ViewIncomeActivity.class);
                    startActivity(viewIncomeIntent1);
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

        }
        else if(mode == "validate")
        {
            dialogBuilder.setMessage("Please enter all fields");
            dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );
    }

    private boolean ValidateInput()
    {
        if(incomeAmount.getText().toString().equals("") ||
                incomeDate.getText().toString().equals("") || incomeDescription.getText().toString().equals(""))
        return false;
        if(incomeCategory.getSelectedItem().equals(""))
            return false;
        return true;
    }
}
