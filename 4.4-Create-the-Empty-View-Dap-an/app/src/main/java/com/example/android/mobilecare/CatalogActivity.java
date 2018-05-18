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

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Phương thức trợ giúp tạm thời để hiển thị thông tin trong TextView trên màn hình về
     * trạng thái của cơ sở dữ liệu mobilecare
     */
    private void displayDatabaseInfo() {

        // Khai báo một phép chiếu xác định ta sẽ truy vấn từ cột nào trong cơ sở dữ liệu.
        String[] projection = {
                MobileEntry._ID,
                MobileEntry.COLUMN_MOBILE_NAME,
                MobileEntry.COLUMN_MOBILE_BRAND,
                MobileEntry.COLUMN_MOBILE_OS,
                MobileEntry.COLUMN_MOBILE_STATUS };

        // Thực hiện truy vấn từ bảng
        Cursor cursor = getContentResolver().query(
                MobileEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        // Tìm ListView sẽ chứa dữ liệu thiết bị
        ListView mobileListView = (ListView) findViewById(R.id.list);

        // Thiết lập một Adapter để tạo ra một list item cho mỗi hàng dữ liệu thiết bị trong con trỏ.
        MobileCursorAdapter adapter = new MobileCursorAdapter(this, cursor);

        // Gắn adapter vào ListView.
        mobileListView.setAdapter(adapter);
    }

    /**
     * Phương thức trợ giúp để chèn dữ liệu giả vào cơ sở dữ liệu.
     */
    private void insertMobile() {
        // Lấy cơ sở dữ liệu ở chế độ ghi

        // Tạo đối tượng ContentValues nơi tên cột là các khóa,
        // và các thuộc tính của thiết bị là các giá trị.
        ContentValues values = new ContentValues();
        values.put(MobileEntry.COLUMN_MOBILE_NAME, "A910S");
        values.put(MobileEntry.COLUMN_MOBILE_BRAND, "Vega");
        values.put(MobileEntry.COLUMN_MOBILE_OS, MobileEntry.OS_ANDROID);
        values.put(MobileEntry.COLUMN_MOBILE_STATUS, "Chua kiem tra");

        // Chèn hàng mới cho thiết bị, và trả về ID của hàng đó.
        // Đối số đầu tiên của db.insert() là tên bảng.
        // Đối số thứ 2 là tên cột, có thể được framework chèn NULL nếu ContentValue rỗng
        // (nếu đối số này đặt là NULL thì framework sẽ không chèn thêm hàng mà không có dữ liệu)
        // Đối số thứ ba là đối tượng ContentValues có chứa thông tin cho thiết bị.
        Uri newUri = getContentResolver().insert(MobileEntry.CONTENT_URI, values);
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
                insertMobile();
                displayDatabaseInfo();
                return true;
            // Phản hồi khi người dùng bấm "Xóa toàn bộ các thiết bị"
            case R.id.action_delete_all_entries:
                // Chưa làm gì lúc này
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}