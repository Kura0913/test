package com.example.bookmanager_java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {
    String TAG = MainActivity3.class.getSimpleName()+"My";

    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    private boolean  flag = false;

    SQLiteDataBaseHelper mDBHelper;



    //所有資料
    ArrayList<HashMap<String, String>> bookList = new ArrayList<>();
    //被選中的資料
    ArrayList<HashMap<String, String>> selectedBookList = new ArrayList<>();

    TextView tv_bookName, tv_author, tv_press, tv_counter;
    EditText ed_search;
    Button btn_Create, btn_bookBorrow,btn_bookReturn,btn_clear,btn_search;
    MainActivity3.MyAdapter myAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Stetho.initializeWithDefaults(this);

        Button btn_back = findViewById(R.id.btn_back);

        mDBHelper = new SQLiteDataBaseHelper(this, DB_NAME
                , null, DB_VERSION, TABLE_NAME);//初始化資料庫


        mDBHelper.chickTable();//確認是否存在資料表，沒有則新增
        bookList = mDBHelper.showAll();//撈取資料表內所有資料


        //連接所有元件
        btn_Create = findViewById(R.id.btn_Create);
        btn_bookBorrow = findViewById(R.id.btn_bookBorrow);
        btn_bookReturn = findViewById(R.id.btn_bookReturn);
        btn_clear = findViewById(R.id.btn_Clear);
        btn_search = findViewById(R.id.btn_search);

        tv_bookName = findViewById(R.id.tv_bookName);
        tv_author = findViewById(R.id.tv_author);
        tv_press = findViewById(R.id.tv_press);
        tv_counter = findViewById(R.id.tv_counter);
        ed_search = findViewById(R.id.ed_search);

        //btn_Clear功能，清除輸入欄位的資料
        btn_clear.setOnClickListener(v->{
            clearAll();
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this,MyService3.class);
                startService(intent);
                finish();
            }
        });



        //btn_bookBorrow功能
        btn_bookBorrow.setOnClickListener(v->{
            if(tv_bookName.getText().toString().length()<=0 || tv_author.getText().toString().length()<=0
                    || tv_press.getText().toString().length()<=0 || tv_counter.getText().toString().length()<=0){
                Toast.makeText(MainActivity3.this, "資料為填齊!", Toast.LENGTH_SHORT).show();
            }
            else{

                int book_counter = Integer.parseInt(tv_counter.getText().toString())-1;
                if(book_counter<0)
                {
                    Toast.makeText(MainActivity3.this, "數量不足無法借閱!", Toast.LENGTH_SHORT).show();
                }else
                {
                    mDBHelper.ChangeCounter(selectedBookList.get(0).get("id")
                            ,tv_bookName.getText().toString()
                            ,tv_author.getText().toString()
                            ,tv_press.getText().toString()
                            ,book_counter);
                    if(flag){
                        bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                    }
                    else{
                        bookList = mDBHelper.showAll();
                    }
                    myAdapter.notifyDataSetChanged();
                    clearAll();
                    Toast.makeText(MainActivity3.this,"借書完成",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //btn_bookReturn功能
        btn_bookReturn.setOnClickListener(v->{
            if(tv_bookName.getText().toString().length()<=0 || tv_author.getText().toString().length()<=0
                    || tv_press.getText().toString().length()<=0 || tv_counter.getText().toString().length()<=0){
                Toast.makeText(MainActivity3.this, "資料為填齊!", Toast.LENGTH_SHORT).show();
            }
            else{

                mDBHelper.ChangeCounter(selectedBookList.get(0).get("id")
                        ,tv_bookName.getText().toString()
                        ,tv_author.getText().toString()
                        ,tv_press.getText().toString()
                        ,Integer.parseInt(tv_counter.getText().toString())+1);
                if(flag){
                    bookList = mDBHelper.searchByBookName(ed_search.getText().toString());
                }
                else{
                    bookList = mDBHelper.showAll();
                }

                myAdapter.notifyDataSetChanged();
                clearAll();
                Toast.makeText(MainActivity3.this,"還書完成",Toast.LENGTH_SHORT).show();
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

    }

    private void clearAll(){
        tv_bookName.setText("");
        tv_author.setText("");
        tv_press.setText("");
        tv_counter.setText("");
        ed_search.setText("");
        selectedBookList.clear();
    }



    private class MyAdapter extends RecyclerView.Adapter<MainActivity3.MyAdapter.ViewHolder> {//設置Adapter
        @NonNull
        @Override
        public MainActivity3.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, null);
            return new MainActivity3.MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity3.MyAdapter.ViewHolder holder, int position) {
            holder.tvTitle.setText("書名:"+ bookList.get(position).get("bookName")+
                    "\t作者:"+ bookList.get(position).get("author")+
                    "\t出版社:"+ bookList.get(position).get("press")+
                    "\t本數:"+ bookList.get(position).get("counter"));
            holder.itemView.setOnClickListener((v) -> {

                selectedBookList.clear();
                selectedBookList = mDBHelper.searchById(bookList.get(position).get("id"));
                try {
                    tv_bookName.setText(selectedBookList.get(0).get("bookName"));
                    tv_author.setText(selectedBookList.get(0).get("author"));
                    tv_press.setText(selectedBookList.get(0).get("press"));
                    tv_counter.setText(selectedBookList.get(0).get("counter"));
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



}
