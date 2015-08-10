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
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sowmya on 7/10/15.
 */
public class ViewCategoryTypeActivity extends ListActivity implements View.OnClickListener{

    private ArrayList<HashMap<String,String>> list;
    ArrayList<CategoryTypeModel> categoryList;

    Button buttonAddCategory;
    public ImageButton buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcategorytype);

        buttonAddCategory = (Button) findViewById(R.id.buttonAddCategory);
        buttonHome = (ImageButton) findViewById(R.id.buttonHome);

        buttonAddCategory.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        getAllCategories();


        if(categoryList.size()>0)
        {

            SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.activity_categorydetail,new String[]{"CategoryTypeID","CategoryTypeValue"},new int[] {R.id.hdnCategory_ID,R.id.textCategoryDetail});
            setListAdapter(adapter);

        }
        else {
            String message = getResources().getString(R.string.message_no_category);
            ((TextView) findViewById(R.id.textMessage)).setText(message);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonAddCategory:
                Intent addCategoryIntent = new Intent(ViewCategoryTypeActivity.this,AddCategoryTypeActivity.class);
                startActivity(addCategoryIntent);
                break;

            case R.id.buttonHome:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            case R.id.buttonCancel:
            default:
                finish();
                break;
        }
    }

    private void getAllCategories()
    {
        DataBaseHelper db = new DataBaseHelper(this);
        try
        {
            categoryList = db.getAllCategories();
            for(int i=0;i<categoryList.size();i++)
            {
                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put("CategoryTypeID",Integer.toString(categoryList.get(i).getCategoryTypeID()));
                hashMap.put("CategoryTypeValue",categoryList.get(i).getCategoryTypeValue());

                list.add(hashMap);
            }
        }
        catch (Exception ex)
        {

        }
        finally {
            db.close();
        }
    }

    public void EditButtonClickHandler(View v)
    {
        RelativeLayout currentLayout = (RelativeLayout)v.getParent();
        Intent objEditIntent = new Intent(getApplicationContext(),AddCategoryTypeActivity.class);
        objEditIntent.putExtra("editFlag", true);
        objEditIntent.putExtra("categoryTypeID",((TextView)currentLayout.getChildAt(0)).getText().toString());
        objEditIntent.putExtra("categoryTypeValue", (((TextView)currentLayout.getChildAt(1)).getText().toString()));
        startActivity(objEditIntent);

    }
    protected void onListItemClick(ListView listview, View view, int position, long id) {
        super.onListItemClick(listview, view, position, id);
        Intent intentViewCategory = new Intent(ViewCategoryTypeActivity.this,ViewCategoryTypeActivity.class );
        //intentViewCategory.putExtra("view_by", VIEW_BY_CAT_ID);
        intentViewCategory.putExtra("categoryID",categoryList.get(position).toString());
        startActivity(intentViewCategory);
    }
}
