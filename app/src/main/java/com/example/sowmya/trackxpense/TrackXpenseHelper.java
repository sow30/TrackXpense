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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sowmya on 7/28/15.
 */
public class TrackXpenseHelper {

    public List<String> getExpenseMode() {

        List<String> expenseModeList = new ArrayList<>();
        expenseModeList.add("Cash");
        expenseModeList.add("Credit Card");
        expenseModeList.add("Debit Card");
        expenseModeList.add("Cheque");
        expenseModeList.add("Online");
        expenseModeList.add("Others");

        return expenseModeList;
    }

    public List<String> getListInterval() {

        List<String> intervalList = new ArrayList<>();
        intervalList.add("Yearly");
        intervalList.add("Monthly");
        intervalList.add("Weekly");
        return intervalList;
    }

    public List<String> getIncomeCategory()
    {
        List<String> incomeCategoryList = new ArrayList<>();
        incomeCategoryList.add("Salary");
        incomeCategoryList.add("Rent");
        return incomeCategoryList;
    }
}
