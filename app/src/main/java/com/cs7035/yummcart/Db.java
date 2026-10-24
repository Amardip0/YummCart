package com.cs7035.yummcart;

import android.content.*;
import android.database.sqlite.*;

public class Db extends SQLiteOpenHelper {
    public Db(Context c) {
        super(c, "yummcart.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase d) {
        d.execSQL("CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,email TEXT UNIQUE,password TEXT,address TEXT,contact TEXT,dob TEXT,photo BLOB)");
        d.execSQL("CREATE TABLE foods(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,category TEXT NOT NULL,price REAL NOT NULL)");
        d.execSQL("CREATE TABLE orders(_id INTEGER PRIMARY KEY AUTOINCREMENT,item TEXT NOT NULL,qty INTEGER NOT NULL,price REAL NOT NULL,created_at INTEGER DEFAULT (strftime('%s','now')))");
        d.execSQL("CREATE TABLE cart_items(_id INTEGER PRIMARY KEY AUTOINCREMENT,item TEXT UNIQUE NOT NULL,qty INTEGER NOT NULL DEFAULT 1)");
        d.execSQL("INSERT INTO foods(name,category,price) VALUES('Classic Burger','Burger',149),('Margherita Pizza','Pizza',249),('Pasta Alfredo','Meals',199),('Chocolate Brownie','Dessert',99),('Cold Coffee','Drinks',89),('Paneer Wrap','Meals',169),('Garden Veg Pizza','Pizza',279),('Crispy Corn Burger','Burger',129)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase d, int oldVersion, int newVersion) {
        if (oldVersion < 2) {/* Preserve local data; apply additive migrations here. */}
    }
}
