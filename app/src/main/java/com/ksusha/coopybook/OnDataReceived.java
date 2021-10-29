package com.ksusha.coopybook;

import com.ksusha.coopybook.adapter.ListItem;

import java.util.List;

public interface OnDataReceived { //считывание с БД данных и отправка List на MainActivity
    void onReceived(List<ListItem> list);
}
