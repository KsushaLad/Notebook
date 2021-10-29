package com.ksusha.coopybook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ksusha.coopybook.adapter.ListItem;
import com.ksusha.coopybook.database.AppExecuter;
import com.ksusha.coopybook.database.MyConstans;
import com.ksusha.coopybook.database.MyDataBaseManager;

public class EditActivity extends AppCompatActivity {

    private final int PICK_IMAGE_CODE = 123;
    private ImageView imNewImage;
    private ConstraintLayout imageContainer;
    private FloatingActionButton fbAddImage;
    private ImageButton imEditImage, imDeleteImage;
    private EditText editTextTitle, editTextDescription;
    private MyDataBaseManager myDbManager;
    private String tempLink = "empty";
    private boolean isEditState = true;
    private ListItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialization();
        getMyIntents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDataBase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //ожидание ответа на запрос к системе
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null) { //проверка есть ли результат и ссылка на картинку
            tempLink = data.getData().toString(); //путь к картинке
            imNewImage.setImageURI(data.getData()); //помещение выбранной картинки в контейнер
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION); //постоянная ссылка на картинку
        }
    }

    private void initialization() {
        editTextTitle = findViewById(R.id.edTitle);
        editTextDescription = findViewById(R.id.edDescription);
        imNewImage = findViewById(R.id.imNewImage);
        fbAddImage = findViewById(R.id.fbAddImaage);
        imageContainer = findViewById(R.id.imageContainer);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
        myDbManager = new MyDataBaseManager(this);
    }

    private void getMyIntents() {
        Intent i = getIntent();
        if (i != null) {
            item = (ListItem) i.getSerializableExtra(MyConstans.LIST_ITEM_INTENT);  //получение информации с первой Activity
            isEditState = i.getBooleanExtra(MyConstans.EDIT_STATE, true); //по умолчанию создание новой заметки
            if (!isEditState) {  //для просмотра заметки
                editTextTitle.setText(item.getTitle());                //показ заголовка
                editTextDescription.setText(item.getDescription());    //показ описания
                if (!item.getLinkToPhoto().equals("empty")) {
                    tempLink = item.getLinkToPhoto(); //сохранение ссылки на картинку
                    imageContainer.setVisibility(View.VISIBLE); //если есть картинка то показываем контейнер с содержимым
                    imNewImage.setImageURI(Uri.parse(item.getLinkToPhoto()));
                    imDeleteImage.setVisibility(View.GONE);
                    imEditImage.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onClickSave(View view) {
        final String title = editTextTitle.getText().toString();
        final String description = editTextDescription.getText().toString();
        if (title.equals("") || description.equals("")) {      //если в одном из полей пусто, то выводить сообщение и не сохранять в БД ничего
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            if (isEditState) {
                AppExecuter.getInstance().getSubIO().execute(new Runnable() { //запись на второстепенном потоке
                    @Override
                    public void run() {
                        myDbManager.insertToDataBase(title, description, tempLink); //добавление данных в БД, если новая заметка
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            } else {
                myDbManager.updateItem(title, description, tempLink, item.getId()); //обновление БД, если просмотр или редактирование
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
            myDbManager.closeDataBase(); //закрытие БД
            finish(); //закрытие активности
        }
    }

    public void onClickDeleteImage(View view) { //удаление фотографии
        imNewImage.setImageResource(R.drawable.gaallery); //картинка по умолчанию
        tempLink = "empty"; //стирание ссылки
        imageContainer.setVisibility(View.GONE); //скрытие контейнера
        fbAddImage.setVisibility(View.VISIBLE); //показ кнопки "Добавить фотографию"
    }

    public void onClickAddImage(View view) { //добавление фотографии
        imageContainer.setVisibility(View.VISIBLE); //показ контейнера
        view.setVisibility(View.GONE); //скрытие кнопки "Добавить фотографию"
    }

    public void onClickChooseImage(View view) {
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT); //сообщение системе для открытия приложения, где хранятся картинки на смартфоне
        chooser.setType("image/*"); //тип файла для поиска - картинка
        startActivityForResult(chooser, PICK_IMAGE_CODE); //отправка запроса и его кода к системе
    }
}