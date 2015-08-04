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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonAddExpense,buttonViewExpense,buttonAddIncome,buttonViewIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddExpense = (Button) findViewById(R.id.buttonAddExpense);
        buttonViewExpense = (Button) findViewById(R.id.buttonViewExpense);
        buttonAddIncome = (Button) findViewById(R.id.buttonAddIncome);
        buttonViewIncome = (Button) findViewById(R.id.buttonViewIncome);

        buttonAddExpense.setOnClickListener(this);
        buttonViewExpense.setOnClickListener(this);
        buttonAddIncome.setOnClickListener(this);
        buttonViewIncome.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.buttonAddExpense:
                Intent addExpenseIntent = new Intent(MainActivity.this,AddExpenseActivity.class);
                startActivity(addExpenseIntent);
                break;

            case R.id.buttonViewExpense:
                Intent viewExpenseIntent = new Intent(MainActivity.this,ViewExpenseActivity.class);
                startActivity(viewExpenseIntent);
                break;
            case R.id.buttonAddIncome:
                Intent addIncomeIntent = new Intent(MainActivity.this,AddIncomeActivity.class);
                startActivity(addIncomeIntent);
                break;

            case R.id.buttonViewIncome:
                Intent viewIncomeIntent = new Intent(MainActivity.this,ViewIncomeActivity.class);
                startActivity(viewIncomeIntent);
                break;

            default:
                break;
        }

    }
}
