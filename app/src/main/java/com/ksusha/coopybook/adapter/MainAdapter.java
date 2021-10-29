package com.ksusha.coopybook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksusha.coopybook.EditActivity;
import com.ksusha.coopybook.R;
import com.ksusha.coopybook.database.MyConstans;
import com.ksusha.coopybook.database.MyDataBaseManager;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private final Context context;
    private final List<ListItem> mainArray;


    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //отображение в RecyclerView элементов
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false); //отрисовка элемента, заполнение шаблона данными
        return new MyViewHolder(view, context, mainArray); //создание нового элемента по количеству элементов в массиве
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { //заполнение элементами в каждом Item
        holder.setData(mainArray.get(position).getTitle()); //взятие заголовка с массива и заполнение ним элементов
    }

    @Override
    public int getItemCount() {
        return mainArray.size(); //количество элементов в списке
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final Context context;
        private final List<ListItem> mainArray;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this); //весь Item будет реагировать на нажатие
        }

        public void setData(String title) {
            tvTitle.setText(title); //присваивание заголовка для каждого Item
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra(MyConstans.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition())); //получение позиции какой Item был нажат
            i.putExtra(MyConstans.EDIT_STATE, false); //открытие для создания или просмотра заметки
            context.startActivity(i);
        }
    }

    public void updateAdapter(List<ListItem> newList) { //обновление адаптера
        mainArray.clear(); //очищение старого списка
        mainArray.addAll(newList); //добавление новых элементов списка
        notifyDataSetChanged(); //сообщение адаптеру об обновлении
    }

    public void removeItem(int position, MyDataBaseManager myDataBaseManager) { //смещение элемента и удаление из БД
        myDataBaseManager.delete(mainArray.get(position).getId()); //удаление с БД по ID
        mainArray.remove(position);
        notifyItemRangeChanged(0, mainArray.size()); //указание адаптеру, что количество элементов изменилось, необходимо сдвинуть оставшиеся элементы вверх
        notifyItemRemoved(position);
    }
}