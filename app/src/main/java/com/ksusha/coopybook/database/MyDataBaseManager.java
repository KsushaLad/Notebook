package com.ksusha.coopybook.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ksusha.coopybook.OnDataReceived;
import com.ksusha.coopybook.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDataBaseManager {
    private Context context;
    private MyDataBaseHelper myDataBaseHelper;
    private SQLiteDatabase dataBase;

    public MyDataBaseManager(Context context) {
        this.context = context;
        myDataBaseHelper = new MyDataBaseHelper(context);
    }

    public void openDataBase() { //открытие БД
        dataBase = myDataBaseHelper.getWritableDatabase(); //открытие БД для записи
    }

    public void insertToDataBase(String title, String discription, String link) { //вставка в БД
        ContentValues cv = new ContentValues();
        cv.put(MyConstans.TITLE, title);
        cv.put(MyConstans.DISCRIPTION, discription);
        cv.put(MyConstans.LINK, link);
        dataBase.insert(MyConstans.TABLE_NAME, null, cv); //вставка данных в таблицу
    }

    public void updateItem(String title, String discription, String link, int id) { //обновление БД
        String selection = MyConstans._ID + "=" + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstans.TITLE, title);
        contentValues.put(MyConstans.DISCRIPTION, discription);
        contentValues.put(MyConstans.LINK, link);
        dataBase.update(MyConstans.TABLE_NAME, contentValues, selection, null);
    }

    public void delete(int id) { //удаление с БД
        String selection = MyConstans._ID + "=" + id; //String запроса к SQL для поиска ID
        dataBase.delete(MyConstans.TABLE_NAME, selection, null);
    }

    public void getFromDataBase(String searchText, OnDataReceived onDataReceived) { //получение из БД и поиск по заголовку
        final List<ListItem> tempList = new ArrayList<>(); //создание списка для вывода элементов
        String selection = MyConstans.TITLE + " like ?"; //колона в БД в которой будем искать
        final Cursor cursor = dataBase.query(MyConstans.TABLE_NAME, null, selection,  //получение определенных элементов из БД
                new String[]{"%" + searchText + "%"}, null, null, null);
        while (cursor.moveToNext()) {
            ListItem item = new ListItem();
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MyConstans.TITLE));        //считывание данных из курсора
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(MyConstans.DISCRIPTION));
            @SuppressLint("Range") String linkToPhoto = cursor.getString(cursor.getColumnIndex(MyConstans.LINK));
            @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex(MyConstans._ID));
            item.setTitle(title);
            item.setDescription(description);
            item.setLinkToPhoto(linkToPhoto);
            item.setId(_id);
            tempList.add(item);

        }
        cursor.close();
        onDataReceived.onReceived(tempList); //запуск интерфейса
    }

    public void closeDataBase() { //закрытие БД
        myDataBaseHelper.close();
    }
}