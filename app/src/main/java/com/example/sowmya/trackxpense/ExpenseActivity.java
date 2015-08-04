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

/**
 * Created by Sowmya on 7/8/15.
 */
public class ExpenseActivity {


    int _id;
    String  _category;
    String  _date;
    float   _amount;
    String  _description;

    public ExpenseActivity() {
    }

    public int get_ID() {
        return _id;
    }

    public void set_ID(int ID) {
        this._id = ID;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String category) {
        this._category = category;
    }

    public String get_date() {
        return _date;
    }

    public void set_time(String time) {
        this._date = time;
    }

    public float get_amount() {
        return _amount;
    }

    public void set_amount(float amount) {
        this._amount = amount;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String description) {
        this._description = description;
    }
}
