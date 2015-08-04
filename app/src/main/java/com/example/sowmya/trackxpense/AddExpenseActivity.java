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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.text.InputType;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Sowmya on 7/15/15.
 */
public class AddExpenseActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private EditText dateValue,amountValue,description;
    private Button buttonSave,buttonCancel,buttonDelete;
    private Spinner categorySpinner;
    private ImageButton buttonImage;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    public static Boolean editFlag = false;
    private List<String> list = new ArrayList<>();
    TextView labelAddExpense;
    int hdnExpenseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpense);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        findViewsById();
        setDateTimeField();

        editFlag = getIntent().getBooleanExtra("editFlag",false);
        if(editFlag)
        {//populate view with saved values fetched from database
            hdnExpenseID = Integer.parseInt(getIntent().getStringExtra("_id"));
            labelAddExpense.setText("EDIT EXPENSE DETAILS");
            amountValue.setText(getIntent().getStringExtra("_amount"));
            categorySpinner.setSelection(list.indexOf(getIntent().getStringExtra("_category")));
            dateValue.setText(getIntent().getStringExtra("_date"));
            description.setText(getIntent().getStringExtra("_description"));

            buttonDelete.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySpinner.setSelection(position);
        int sel = position;
        if(list.size()>0 && position == list.size()-1)
        {
            Intent viewCategoryIntent = new Intent(AddExpenseActivity.this,ViewCategoryTypeActivity.class);
            startActivity(viewCategoryIntent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void findViewsById() {
        dateValue = (EditText) findViewById(R.id.editText1);
        amountValue = (EditText)findViewById(R.id.amount);
        description = (EditText)findViewById(R.id.description);
        labelAddExpense = (TextView)findViewById(R.id.label_AddExpense);

        buttonSave = (Button)findViewById(R.id.buttonSave);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        buttonImage = (ImageButton) findViewById(R.id.calendaricon);
        buttonDelete = (Button)findViewById(R.id.buttonDelete);

        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonImage.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        ArrayList<CategoryTypeModel> categoryList = new ArrayList<CategoryTypeModel>();
        categoryList = dataBaseHelper.getAllCategories();
        dataBaseHelper.close();

        for(int i=0;i< categoryList.size();i++)
        {
            CategoryTypeModel obj = categoryList.get(i);
            list.add(obj.CategoryTypeValue);
        }
        list.add("Add/Edit Category");

        categorySpinner = (Spinner)findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter_state);
        categorySpinner.setOnItemSelectedListener(this);

    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateValue.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        long result;
        switch (view.getId()) {
            case R.id.calendaricon:datePickerDialog.show();
                break;
            case R.id.buttonSave:
                DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                db.getWritableDatabase();
                result = db.SaveExpenseActivity(ParseExpenseObject(),"save");
                db.close();
                Intent viewExpenseIntent = new Intent(AddExpenseActivity.this,ViewExpenseActivity.class);
                startActivity(viewExpenseIntent);
                break;
            case R.id.buttonDelete:
                DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
                dbHelper.getWritableDatabase();
                result = dbHelper.SaveExpenseActivity(ParseExpenseObject(), "delete");
                dbHelper.close();
                Intent viewExpenseIntent1 = new Intent(AddExpenseActivity.this,ViewExpenseActivity.class);
                startActivity(viewExpenseIntent1);
                break;
            case R.id.buttonCancel:
            default:
                finish();
                break;
        }
    }

    private ExpenseActivity ParseExpenseObject() {

        ExpenseActivity expense = new ExpenseActivity();

        expense._id = editFlag ? hdnExpenseID : 0;
        expense._date = dateValue.getText().toString();
        expense._amount = Float.parseFloat(amountValue.getText().toString());
        expense._description = description.getText().toString();
        expense._category = categorySpinner.getSelectedItem().toString();

        return expense;
    }

    public void HomeButtonClickHandler(View v)
    {
        startActivity(new Intent(AddExpenseActivity.this,MainActivity.class));
    }
}
