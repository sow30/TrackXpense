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
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddCategoryTypeActivity extends Activity implements OnClickListener {

    EditText categoryValue,categoryID;
    public Button buttonSave, buttonCancel,buttonDelete;
    public ImageButton buttonHome;
    public static boolean editFlag = false;
    int hdnCategoryTypeID;
    TextView labelAddCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcategorytype);
        labelAddCategory = (TextView) findViewById(R.id.label_AddCategory);
        categoryValue = (EditText) findViewById(R.id.txtCategory);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonHome = (ImageButton)findViewById(R.id.buttonHome);
        categoryID = (EditText)findViewById(R.id.hdnCategoryID);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editFlag = getIntent().getBooleanExtra("editFlag",false);
        if(editFlag)
        {
            PopulateCategoryByID();

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                if(ValidateInput()) {
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    db.getWritableDatabase();
                    db.SaveCategoryType(ParseCategoryType(), "edit");
                    db.close();
                    Intent viewCategoryIntent = new Intent(AddCategoryTypeActivity.this, ViewCategoryTypeActivity.class);
                    startActivity(viewCategoryIntent);
                }
                else
                    OpenAlertDialog("validate");

                break;
            case R.id.buttonDelete:
                OpenAlertDialog("delete");
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

    private CategoryTypeModel ParseCategoryType()
    {
        CategoryTypeModel category = new CategoryTypeModel();
        category.setCategoryTypeID(editFlag ? Integer.parseInt(categoryID.getText().toString()) : 0);
        category.setCategoryTypeValue(categoryValue.getText().toString());
        return category;
    }
    private boolean ValidateInput()
    {
        if(categoryValue.getText().equals(""))
            return false;
        else
            return true;
    }
    public void OpenAlertDialog(String mode)
    {
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(this);
        if(mode=="delete") {
            dialogBuilder.setMessage("Confirm Delete?");
            dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
                    dbHelper.getWritableDatabase();
                    dbHelper.SaveCategoryType(ParseCategoryType(), "delete");
                    dbHelper.close();
                    Intent viewCategoryIntent1 = new Intent(AddCategoryTypeActivity.this, ViewCategoryTypeActivity.class);
                    startActivity(viewCategoryIntent1);
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        else if(mode=="validate") {
            dialogBuilder.setMessage("Please enter all fields");
            dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                }
        );
    }
    public void PopulateCategoryByID()
    {
        hdnCategoryTypeID = Integer.parseInt(getIntent().getStringExtra("categoryTypeID"));
        categoryID.setText(getIntent().getStringExtra("categoryTypeID"));
        labelAddCategory.setText("EDIT CATEGORY DETAILS");

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        categoryValue.setText(db.getCategorybyID(hdnCategoryTypeID));
        buttonDelete.setVisibility(View.VISIBLE);
    }

}
