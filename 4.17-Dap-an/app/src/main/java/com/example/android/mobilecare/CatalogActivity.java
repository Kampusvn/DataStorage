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

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

import java.net.URI;

/**
 * Hiển thị danh sách các thiết bị được nhập.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOBILE_LOADER = 0;

    MobileCursorAdapter mCursorAdapter;

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

        // Tìm ListView gắn với dữ liệu
        ListView mobileListView = (ListView) findViewById(R.id.list);

        // Tìm và thiết lập view rỗng trên ListView để hiển thị khi danh sách không có item nào.
        View emptyView = findViewById(R.id.empty_view);
        mobileListView.setEmptyView(emptyView);

        // Thiết lập một adapter để tạo một list item cho mỗi hàng dữ liệu trong con trỏ
        // Chưa có dữ liệu thiết bị (cho đến khi hoàn thành loader) nên ta chuyền null vào vị trí con trỏ
        mCursorAdapter = new MobileCursorAdapter(this, null);
        mobileListView.setAdapter(mCursorAdapter);

        // Thiết lập item click listener
        mobileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Tạo intent mới để đưa vào {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Tạo content URI đại diện cho thiết bị cụ thể được bấm vào, bằng cách thêm "id"
                // (được truyền như đầu vào cho phương thức này) vào {@link MobileEntry # CONTENT_URI}.
                // Ví dụ: URI sẽ là "content: //com.example.android.mobilecare/mobile/2" nếu bạn
                // bấm vào thiết bị có ID = 2.
                Uri currentMobileUri = ContentUris.withAppendedId(MobileEntry.CONTENT_URI, id);

                // Đặt URI trên trường dữ liệu của intent
                intent.setData(currentMobileUri);

                // Khởi chạy {@link EditorActivity} để hiển thị dữ liệu cho thiết bị hiện tại.
                startActivity(intent);
            }
        });

        // Khởi động loader
        getLoaderManager().initLoader(MOBILE_LOADER, null, this);
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

    /**
     * Phương thức trợ giúp xóa hết thiết bị trong cơ sở dữ liệu.
     */
    private void deleteAllMobiles() {
        int rowsDeleted = getContentResolver().delete(MobileEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " thiết bị đã xóa khỏi cơ sở dữ liệu");
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
                return true;
            // Phản hồi khi người dùng bấm "Xóa toàn bộ các thiết bị"
            case R.id.action_delete_all_entries:
                deleteAllMobiles();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Định nghĩa một phép chiếu xác định các cột từ bảng mà chúng ta quan tâm
        String[] projection = {
                MobileEntry._ID,
                MobileEntry.COLUMN_MOBILE_NAME,
                MobileEntry.COLUMN_MOBILE_BRAND
        };

        // Loader này sẽ thực thi phương thức truy vấn của ContentProvider trong luồng background
        return new CursorLoader(this,
                MobileEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Cập nhật {@link MobileCursorAdapter} với con trỏ mới chứa dữ liệu thiết bị đã cập nhật
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback được gọi khi cần xóa dữ liệu
        mCursorAdapter.swapCursor(null);
    }
}