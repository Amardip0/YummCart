package com.cs7035.yummcart;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.Uri;

public class OrdersProvider extends ContentProvider {
    public static final String AUTH = "com.cs7035.yummcart.orders";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTH + "/orders");
    Db helper;

    public boolean onCreate() {
        helper = new Db(getContext());
        return true;
    }

    public Cursor query(Uri u, String[] p, String s, String[] a, String o) {
        return helper.getReadableDatabase().query("orders", p, s, a, null, null, o == null ? "_id DESC" : o);
    }

    public String getType(Uri u) {
        return "vnd.android.cursor.dir/vnd.unilife.order";
    }

    public Uri insert(Uri u, ContentValues v) {
        long id = helper.getWritableDatabase().insert("orders", null, v);
        Uri out = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(out, null);
        return out;
    }

    public int delete(Uri u, String s, String[] a) {
        int n = helper.getWritableDatabase().delete("orders", s, a);
        getContext().getContentResolver().notifyChange(u, null);
        return n;
    }

    public int update(Uri u, ContentValues v, String s, String[] a) {
        int n = helper.getWritableDatabase().update("orders", v, s, a);
        getContext().getContentResolver().notifyChange(u, null);
        return n;
    }
}
