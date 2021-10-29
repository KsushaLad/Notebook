package com.ksusha.coopybook.database;

public class MyConstans {
    public static final String LIST_ITEM_INTENT = "list_item_intent"; //ключ для Intent
    public static final String EDIT_STATE = "edit_state"; //ключ для Intent для создания или редактирования заметки
    public static final String TABLE_NAME = "my_table";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DISCRIPTION = "discription";
    public static final String LINK = "uri";
    public static final String DB_NAME = "my_db.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + LINK + " TEXT," +  //запрос на создание таблицы определенной структуры
            DISCRIPTION + " TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME; //запрос на удаление таблицы

}
