package com.ksusha.coopybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.ksusha.coopybook.adapter.ListItem;
import com.ksusha.coopybook.adapter.MainAdapter;
import com.ksusha.coopybook.database.AppExecuter;
import com.ksusha.coopybook.database.MyDataBaseManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceived {
    private MyDataBaseManager myDataBaseManager;
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //создание меню
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //поиск введенного текста в SearchView
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) { //обновление текста при вводе
                readFromDataBase(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initialization() {
        myDataBaseManager = new MyDataBaseManager(this);
        RecyclerView rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this)); //расположение элементов по вертикали
        getItemTouchHelper().attachToRecyclerView(rcView); //связывание слушателя свайпа и списка элементов
        rcView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDataBaseManager.openDataBase();
        readFromDataBase(""); //считывание данных с БД
    }

    public void onClickAdd(View view) {
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDataBaseManager.closeDataBase();
    }

    private ItemTouchHelper getItemTouchHelper() { //слушатель свайпа по элементу RecyclerView
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { //возможность свайпа влево и вправо
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDataBaseManager); //удаление элемента по свайпу
            }
        });
    }

    private void readFromDataBase(final String text) { //чтение с БД с другого потока
        AppExecuter.getInstance().getSubIO().execute(new Runnable() { //запуск второстепенного потока
            @Override
            public void run() {
                myDataBaseManager.getFromDataBase(text, MainActivity.this); //получение информации с БД на другом потоке
            }
        });

    }

    @Override
    public void onReceived(final List<ListItem> list) { //обновление адаптера
        AppExecuter.getInstance().getMainIO().execute(new Runnable() { //запуск основного потока
            @Override
            public void run() {
                mainAdapter.updateAdapter(list); //обновление списка данных
            }
        });
    }
}