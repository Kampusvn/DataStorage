/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.mobilecare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;
import com.example.android.mobilecare.data.MobileDbHelper;

/**
 * Hiển thị danh sách các thiết bị được nhập.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Cài đặt nút FAB để thêm thiết bị mới
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    /**
     * Phương thức trợ giúp tạm thời để hiển thị thông tin trong TextView trên màn hình về
     * trạng thái của cơ sở dữ liệu mobilecare
     */
    private void displayDatabaseInfo() {
        // Để truy cập cơ sở dữ liệu, chúng ta tạo lớp con của SQLiteOpenHelper và truyền context
        // vào, và context ở đây là activity hiện tại.
        MobileDbHelper mDbHelper = new MobileDbHelper(this);

        // Tạo và/hoặc mở một cơ sở dữ liệu để đọc
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Thực hiện truy vấn SQL thô "SELECT * FROM mobilecare" để có được một Con trỏ chứa
        // tất cả các hàng từ bảng mobilecare
        Cursor cursor = db.rawQuery("SELECT * FROM " + MobileEntry.TABLE_NAME, null);
        try {
            // Hiển thị số hàng trong con trỏ (phản ánh số hàng trong bảng mobilecare trong cơ sở dữ liệu).
            TextView displayView = (TextView) findViewById(R.id.text_view_mobile);
            displayView.setText("Số dòng trong bảng cơ sở dữ liệu mobile là " + cursor.getCount() + "\n\n");
        } finally {
            // Luôn đóng con trỏ khi đọc xong để giải phóng tài nguyên.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu options từ file res/menu/menu_catalog.xml.
        // Việc này sẽ thêm các item cho menu ở thanh trên cùng của ứng dụng
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Khi người dùng bấm vào một tùy chọn
        switch (item.getItemId()) {
            // Phản hồi khi người dùng bấm "Thêm dữ liệu giả"
            case R.id.action_insert_dummy_data:
                // Chưa làm gì lúc này
                return true;
            // Phản hồi khi người dùng bấm "Xóa toàn bộ các thiết bị"
            case R.id.action_delete_all_entries:
                // Chưa làm gì lúc này
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}