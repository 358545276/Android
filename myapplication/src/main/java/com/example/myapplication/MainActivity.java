package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DataBase dataBase = new DataBase(this, "dict.db", null, 1);
    private Integer id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insert(View v) {
        EditText englishText = (EditText) findViewById(R.id.text1);
        EditText chineseText = (EditText) findViewById(R.id.text2);
        ContentValues contentValues = new ContentValues();
        contentValues.put("english", englishText.getText().toString());
        contentValues.put("chinese", chineseText.getText().toString());
        String information;
        if (id != -1) {
            dataBase.getWritableDatabase().update("dict", contentValues, "id = ?", new String[]{
                    id.toString()
            });
            information = "修改成功";
            id = -1;
        } else {
            dataBase.getWritableDatabase().insert("dict", null, contentValues);
            information = "插入成功";
        }
        Toast.makeText(MainActivity.this, information, Toast.LENGTH_SHORT).show();
        englishText.setText("");
        chineseText.setText("");
    }

    public void search(View v) {
        EditText editText = (EditText) findViewById(R.id.text3);
        Cursor cursor = dataBase.getReadableDatabase().query("dict", null, "english like ? or chinese like ?",
                new String[]{"%" + editText.getText().toString() + "%",
                        "%" + editText.getText().toString() + "%"}, null, null, null
        );
        StringBuffer result = new StringBuffer();
        while (cursor.moveToNext()) {
            result.append("id:" + cursor.getInt(0));
            result.append(";english:" + cursor.getString(1));
            result.append(";中文:" + cursor.getString(2) + "\n");
        }
        String resultStr = new String(result);
        if (resultStr.equals("")) {
            Toast.makeText(MainActivity.this, "未搜索到内容！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
        }

    }

    public void delete(View v) {
        EditText editText = (EditText) findViewById(R.id.text3);
        if (editText.getText().toString().trim().equals("")) {
            Toast.makeText(MainActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
            return;
        }
        dataBase.getWritableDatabase().delete("dict", "english like ? or chinese like ?",
                new String[]{"%" + editText.getText().toString() + "%",
                        "%" + editText.getText().toString() + "%"});
        Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
    }

    public void alter(View v) {
        EditText editText = (EditText) findViewById(R.id.text3);
        if (editText.getText().toString().trim().equals("")) {
            Toast.makeText(MainActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor = dataBase.getReadableDatabase().query("dict", null,
                "english like ? or chinese like ?",
                new String[]{"%" + editText.getText().toString() + "%",
                        "%" + editText.getText().toString() + "%"}, null, null, null);
        EditText englishText = (EditText) findViewById(R.id.text1);
        EditText chineseText = (EditText) findViewById(R.id.text2);
        cursor.moveToFirst();
        if (cursor.getString(1) != "" && cursor.getString(2) != "") {
            englishText.setText(cursor.getString(1));
            chineseText.setText(cursor.getString(2));
            id = cursor.getInt(0);
        } else {
            Toast.makeText(MainActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
        }


    }

}
