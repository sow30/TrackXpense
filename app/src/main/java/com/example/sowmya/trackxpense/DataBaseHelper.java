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

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.JarEntry;

/**
 * Created by Sowmya on 7/8/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final String TABLE_EXPENSE = "Expense";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "expenseCategory";
    public static final String COLUMN_DATE = "expenseDate";
    public static final String COLUMN_AMOUNT = "expenseAmount";
    public static final String COLUMN_DESCRIPTION = "expenseDescription";

    private static final String TABLE_CATEGORYTYPE = "CategoryType";
    public static final String COLUMN_CATEGORYTYPE = "CategoryType";
    public static final String COLUMN_CATEGORYTYPEID = "CategoryTypeID";

    public static final String TABLE_INCOME = "Income";
    public static final String COLUMN_INCOMEID = "incomeId";
    public static final String COLUMN_INCOMECATEGORY = "incomeCategory";
    public static final String COLUMN_INCOMEDATE = "incomeDate";
    public static final String COLUMN_INCOMEAMOUNT = "incomeAmount";
    public static final String COLUMN_INCOMEDESCRIPTION = "incomeDescription";

    Context c;
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Query = create table ExpenseTable(_id INTEGER PRIMARY KEY,
            *           expenseCategory TEXT,
            *           expenseDate INTEGER,
            *           expenseAmount REAL,
            *           expenseDescription Text)
        */
        String createExpenseTableQuery = "CREATE TABLE " + TABLE_EXPENSE + "(" +COLUMN_ID+ " INTEGER PRIMARY KEY,"
                            + COLUMN_CATEGORY + " TEXT,"
                            + COLUMN_DATE + " INTEGER,"
                            + COLUMN_AMOUNT + " REAL NOT NULL,"
                            + COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(createExpenseTableQuery);

        String createCategoryTypeTable = "CREATE TABLE " + TABLE_CATEGORYTYPE + "(CategoryTypeID INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COLUMN_CATEGORYTYPE + " TEXT);";
        db.execSQL(createCategoryTypeTable);

        String createIncomeTableQuery = "CREATE TABLE " + TABLE_INCOME + " (" + COLUMN_INCOMEID +" INTEGER PRIMARY KEY,"
                            + COLUMN_INCOMECATEGORY+" TEXT NOT NULL,"
                            + COLUMN_INCOMEDATE+" INTEGER NOT NULL,"
                            + COLUMN_INCOMEAMOUNT+" REAL NOT NULL,"
                            + COLUMN_INCOMEDESCRIPTION+" TEXT);";
        db.execSQL(createIncomeTableQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORYTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS ExpenseTable");
        db.execSQL("DROP TABLE IF EXISTS CategoryTypeTable");
        onCreate(db);
    }

    /*Save an Expense activity to the Database*/
    //parameters: objExpense - Expense object
    //mode - Save mode - Add/Edit/Delete.
    public long SaveExpenseActivity(ExpenseActivity objExpense,String mode) {

        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CATEGORY, objExpense.get_category());
            contentValues.put(COLUMN_DATE, objExpense.get_date());
            contentValues.put(COLUMN_AMOUNT, objExpense.get_amount());
            contentValues.put(COLUMN_DESCRIPTION, objExpense.get_description());

            if (objExpense.get_ID() > 0) {
                //Edit/Delete values in db
                //contentValues.put(COLUMN_ID,objExpense.get_ID());
                if(mode.toLowerCase()=="delete") {
                    //DELETE RECORD
                    result = db.delete(TABLE_EXPENSE, COLUMN_ID + "=" + objExpense._id, null);
                }
                else {
                    //MODIFY RECORD
                    result = db.update(TABLE_EXPENSE, contentValues, COLUMN_ID + "=" + objExpense._id, null);
                }
            } else {
                //ADD RECORD
                result =  db.insert(TABLE_EXPENSE, null, contentValues);
            }

        }
        catch (Exception e)
        {
            String msg = e.getMessage();
        }
        finally {
            db.close();
            return result;
        }
    }


    public ArrayList<ExpenseActivity> getExpenseByInterval(String interval,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ExpenseActivity> expenseList1 = new ArrayList<ExpenseActivity>();
        String query="";
        try
        {
            switch (interval.toLowerCase())
            {
                case "yearly":
                    query = "SELECT * FROM " + TABLE_EXPENSE+ " WHERE expenseDate like " + "'%/"+ value +"'";
                    break;
                case "monthly":
                    query = "SELECT * FROM " + TABLE_EXPENSE+ " WHERE expenseDate like " + "'"+ value +"/%'";
                    break;
                case "weekly":

                    Date startDate,endDate;
                    String startDateString,endDateString;
                    //result should be like july 20-26 (from Monday to Sunday)
                    //Code to get start of week

                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(df.parse(value));
                    while (cal.get(Calendar.DAY_OF_WEEK)!=2)//2 is monday - start of week
                    {
                        cal.add(Calendar.DATE,-1);
                    }
                    startDate = (cal.getTime());

                    startDateString = df.format(startDate);

                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(df.parse(value));
                    //Code to get end of week
                    while (cal2.get(Calendar.DAY_OF_WEEK)!=1)//1 is sunday. end of week
                    {
                        cal2.add(Calendar.DATE,1);
                    }
                    endDate = cal2.getTime();
                    endDateString = df.format(endDate);
                    query = "SELECT * FROM " + TABLE_EXPENSE+" WHERE expenseDate between '" + startDateString+ "' and '"+ endDateString+"'";
                    break;


            }

            Cursor cursor = db.rawQuery(query,null);

            if(cursor.getCount()!=0){
                if (cursor.moveToFirst()) {
                    do {
                        ExpenseActivity expense = new ExpenseActivity();
                        expense._id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                        expense._amount = cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT));
                        expense._category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
                        expense._date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                        expense._description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                        expenseList1.add(expense);
                    }while(cursor.moveToNext());
                }
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();

        }
        finally {
            db.close();
            return expenseList1;
        }
    }

    ArrayList<CategoryTypeModel> categoryList = new ArrayList<CategoryTypeModel>();
    /*Get All Category types*/
    public ArrayList<CategoryTypeModel> getAllCategories(){

        categoryList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_CATEGORYTYPEID + "," + COLUMN_CATEGORYTYPE +" FROM " + TABLE_CATEGORYTYPE;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()!=0){
            if (cursor.moveToFirst()) {
                do {
                    CategoryTypeModel category = new CategoryTypeModel();
                    category.CategoryTypeID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORYTYPEID)));
                    category.CategoryTypeValue = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORYTYPE));
                    categoryList.add(category);
                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        db.close();
        return categoryList;

    }
    /*Add New Category type*/
    public long SaveCategoryType(CategoryTypeModel categoryType,String mode){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CATEGORYTYPE,categoryType.getCategoryTypeValue());
            if(categoryType.getCategoryTypeID()>0)
            {
                if(mode.toLowerCase().equals("delete"))
                    //db.delete(TABLE_CATEGORYTYPE,COLUMN_CATEGORYTYPEID+" = ?",new String[]{String.valueOf(categoryType.getCategoryTypeID())});
                    result = db.delete(TABLE_CATEGORYTYPE,COLUMN_CATEGORYTYPEID+" = " + categoryType.getCategoryTypeID(),null);
                else
                    result = db.update(TABLE_CATEGORYTYPE,contentValues,COLUMN_CATEGORYTYPEID + " = " + categoryType.getCategoryTypeID(),null);
            }
            else
                result = db.insert(TABLE_CATEGORYTYPE, null, contentValues);
        }
        catch (Exception ex)
        {
            String message = ex.getMessage();
        }
        finally {
            db.close();
            return result;
        }

    }

    /*Get the category value by category id*/
    public String getCategorybyID(int CategoryID)
    {
        String categoryValue ="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT CATEGORYTYPE FROM " +TABLE_CATEGORYTYPE+" WHERE CATEGORYTYPEID = "+CategoryID,null);
        if(cursor.moveToFirst())
            categoryValue = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORYTYPE));

        cursor.close();
        db.close();
        return categoryValue;

    }

    private ArrayList<ExpenseActivity> expenseList = new ArrayList<ExpenseActivity>();
    /*Get all the expense values from the Database */
    public ArrayList<ExpenseActivity> getAllExpenses(){
        expenseList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " ORDER BY expenseDate ", null);

        if(cursor.getCount()!=0){
            if (cursor.moveToFirst()) {
                do {
                    ExpenseActivity expense = new ExpenseActivity();
                    expense._id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    expense._amount = cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT));
                    expense._category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
                    expense._date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                    expense._description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    expenseList.add(expense);
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    public long SaveIncome(IncomeModel objIncome,String mode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_INCOMECATEGORY,objIncome.getIncomeCategory());
            contentValues.put(COLUMN_INCOMEAMOUNT,objIncome.getIncomeAmount());
            contentValues.put(COLUMN_INCOMEDATE,objIncome.getIncomeDate());
            contentValues.put(COLUMN_INCOMEDESCRIPTION, objIncome.getIncomeDescription());

            if(objIncome.getIncomeID() >0)
            {
                //Modify Income record - edit/delete based on mode
                if(mode.toLowerCase() =="delete")
                {
                    result = db.delete(TABLE_INCOME,COLUMN_INCOMEID + "=" + objIncome.getIncomeID(),null);
                }
                else
                {
                    result = db.update(TABLE_INCOME,contentValues,COLUMN_INCOMEID + "=" + objIncome.getIncomeID(),null);
                }


            }
            else
            {
                //Add new income record to DB
                result = db.insert(TABLE_INCOME,null,contentValues);
            }


        }
        catch (Exception e)
        {
            String msg = e.getMessage();
        }
        finally {
            db.close();
            return  result;
        }
    }

    public IncomeModel getIncomeByID(int incomeID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        IncomeModel result = new IncomeModel();
        String query = "SELECT * FROM " + TABLE_INCOME + " WHERE "+ COLUMN_INCOMEID + " = " + incomeID + " ORDER BY " + COLUMN_INCOMEDATE+" DESC LIMIT 1";
        try {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.getCount()!=0) {
                if (cursor.moveToFirst()) {
                    result.incomeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INCOMEID));
                    result.incomeCategory = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMECATEGORY));
                    result.incomeDate = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMEDATE));
                    result.incomeAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_INCOMEAMOUNT));
                    result.incomeDescription = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMEDESCRIPTION));
                }
            }
            cursor.close();
        }
        catch (Exception e) {
            String msg = e.getMessage();
        }
        finally {
            db.close();
            return result;
        }
    }

    //get All income values
    public ArrayList<IncomeModel> getAllIncome()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<IncomeModel> incomeList = new ArrayList<>();
        try
        {

            String query = "SELECT * FROM "+ TABLE_INCOME+" ORDER BY " + COLUMN_INCOMEDATE + " DESC ";
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.getCount()!=0)
            {
                if(cursor.moveToFirst())
                {
                    do{
                        IncomeModel objIncome = new IncomeModel();
                        objIncome.incomeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INCOMEID));
                        objIncome.incomeCategory = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMECATEGORY));
                        objIncome.incomeDate = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMEDATE));
                        objIncome.incomeAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_INCOMEAMOUNT));
                        objIncome.incomeDescription = cursor.getString(cursor.getColumnIndex(COLUMN_INCOMEDESCRIPTION));
                        incomeList.add(objIncome);
                    }while(cursor.moveToNext());
                }
            }
            cursor.close();

        }
        catch (Exception ex)
        {

        }
        finally {
            db.close();
            return incomeList;
        }
    }
}
