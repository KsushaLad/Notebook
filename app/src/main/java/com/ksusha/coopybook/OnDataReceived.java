package com.ksusha.coopybook;

import com.ksusha.coopybook.adapter.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}
