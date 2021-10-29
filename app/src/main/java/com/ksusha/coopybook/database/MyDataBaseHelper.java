package com.ksusha.coopybook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, MyConstans.DB_NAME, null, MyConstans.DB_VERSION); //создание БД
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstans.TABLE_STRUCTURE); //создание таблицы внутри БД
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstans.DROP_TABLE); //удаление старой БД
        onCreate(db); //создание новой БД с новыми данными
    }
}
