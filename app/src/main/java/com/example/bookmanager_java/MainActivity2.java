package com.example.bookmanager_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    String TAG = MainActivity2.class.getSimpleName()+"My";

    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    private boolean  flag = false;

    SQLiteDataBaseHelper mDBHelper;



    //所有資料
    ArrayList<HashMap<String, String>> bookList = new ArrayList<>();
    //被選中的資料
    ArrayList<HashMap<String, String>> selectedBookList = new ArrayList<>();

    EditText ed_bookName, ed_author, ed_press, ed_counter,ed_search;
    Button btn_Create, btn_Modify,btn_clear,btn_search;
    MyAdapter myAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Stetho.initializeWithDefaults(this);

        mDBHelper = new SQLiteDataBaseHelper(this, DB_NAME
                , null, DB_VERSION, TABLE_NAME);//初始化資料庫


        mDBHelper.chickTable();//確認是否存在資料表，沒有則新增
        bookList = mDBHelper.showAll();//撈取資料表內所有資料


        //連接所有元件
        btn_Create = findViewById(R.id.btn_Create);
        btn_Modify = findViewById(R.id.btn_Modify);
        btn_clear = findViewById(R.id.btn_Clear);
        btn_search = findViewById(R.id.btn_search);

        ed_bookName = findViewById(R.id.ed_bookName);
        ed_author = findViewById(R.id.ed_author);
        ed_press = findViewById(R.id.ed_press);
        ed_counter = findViewById(R.id.ed_counter);
        ed_search = findViewById(R.id.ed_search);

        //btn_Clear功能，清除輸入欄位的資料
        btn_clear.setOnClickListener(v->{
            clearAll();
        });

        //btn_Create功能，創建新的資料
        btn_Create.setOnClickListener(v->{
            if(ed_bookName.getText().toString().length()<=0 || ed_author.getText().toString().length()<=0
             || ed_press.getText().toString().length()<=0 || ed_counter.getText().toString().length()<=0){
                Toast.makeText(MainActivity2.this, "資料為填齊!", Toast.LENGTH_SHORT).show();
            }
            else{
                mDBHelper.addData(ed_bookName.getText().toString()
                        ,ed_author.getText().toString()
                        ,ed_press.getText().toString()
                        ,ed_counter.getText().toString());
                if(flag){
                    bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                }
                else{
                    bookList = mDBHelper.showAll();
                }
                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity2.this,"新增完成",Toast.LENGTH_SHORT).show();
            }
        });

        //btn_Modify功能，將修改後的資料存入資料庫
        btn_Modify.setOnClickListener(v->{
            if(ed_bookName.getText().toString().length()<=0 || ed_author.getText().toString().length()<=0
                    || ed_press.getText().toString().length()<=0 || ed_counter.getText().toString().length()<=0){
                Toast.makeText(MainActivity2.this, "資料未填齊!", Toast.LENGTH_SHORT).show();
            }
            else{
                mDBHelper.modify(selectedBookList.get(0).get("id")
                ,ed_bookName.getText().toString()
                ,ed_author.getText().toString()
                ,ed_press.getText().toString()
                ,ed_counter.getText().toString());
                if(flag){
                    bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                }
                else{
                    bookList = mDBHelper.showAll();
                }
                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity2.this,"修改完成",Toast.LENGTH_SHORT);
            }
        });

        //btn_search功能，判斷是否有搜尋
        btn_search.setOnClickListener(v->{
            if(ed_search.getText().toString().length()>0){
                bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                flag = true;
            }
            else{
                bookList = mDBHelper.showAll();
                flag = false;
            }
            myAdapter.notifyDataSetChanged();
        });

        //設置資料顯示欄
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        setRecyclerFunction(recyclerView);
    }

    private void clearAll(){
        ed_bookName.setText("");
        ed_author.setText("");
        ed_press.setText("");
        ed_counter.setText("");
        ed_search.setText("");
        selectedBookList.clear();
    }



    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {//設置Adapter
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.tvTitle.setText("書名:"+ bookList.get(position).get("bookName")+
                        "\t作者:"+ bookList.get(position).get("author")+
                        "\t出版社:"+ bookList.get(position).get("press")+
                        "\t本數:"+ bookList.get(position).get("counter"));
                holder.itemView.setOnClickListener((v) -> {

                    selectedBookList.clear();
                    selectedBookList = mDBHelper.searchById(bookList.get(position).get("id"));
                    try {
                        ed_bookName.setText(selectedBookList.get(0).get("bookName"));
                        ed_author.setText(selectedBookList.get(0).get("author"));
                        ed_press.setText(selectedBookList.get(0).get("press"));
                        ed_counter.setText(selectedBookList.get(0).get("counter"));
                    } catch (Exception e) {
                        Log.d(TAG, "onBindViewHolder: " + e.getMessage());
                    }
                });


        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    private void setRecyclerFunction(RecyclerView recyclerView){
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {//設置RecyclerView手勢功能
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);//左滑或右滑刪除資料
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                    case ItemTouchHelper.RIGHT:
                        mDBHelper.deleteById(bookList.get(position).get("id"));
                        bookList.remove(position);
                        if(flag){
                            bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                        }
                        else{
                            bookList = mDBHelper.showAll();
                        }
                        myAdapter.notifyItemRemoved(position);


                        break;

                }
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }
}