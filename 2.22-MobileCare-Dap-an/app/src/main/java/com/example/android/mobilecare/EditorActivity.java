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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;
import com.example.android.mobilecare.data.MobileDbHelper;

/**
 * Cho phép người dùng thêm mới hoặc chỉnh sửa một thiết bị
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText cho tên thiết bị */
    private EditText mNameEditText;

    /** EditText cho hãng sản xuất */
    private EditText mBrandEditText;

    /** EditText cho tình trạng thiết bị */
    private EditText mStatusEditText;

    /** EditText cho hệ điều hành */
    private Spinner mOSSpinner;

    /**
     * Hệ điều hành của thiết bị. Các giá trị hợp lệ có thể có trong tệp MobileContract.java:
     * {@link MobileEntry#OS_ANDROID}, {@link MobileEntry#OS_IOS}, hoặc
     * {@link MobileEntry#OS_OTHER}.
     */
    private int mOS = MobileEntry.OS_ANDROID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Tìm tất cả các View có liên quan để đọc từ input của người dùng
        mNameEditText = (EditText) findViewById(R.id.edit_mobile_name);
        mBrandEditText = (EditText) findViewById(R.id.edit_mobile_brand);
        mStatusEditText = (EditText) findViewById(R.id.edit_mobile_status);
        mOSSpinner = (Spinner) findViewById(R.id.spinner_os);

        setupSpinner();
    }

    /**
     * Thiết lập lựa chọn spinner cho hệ điều hành để người dùng có thể chọn từ menu xổ xuống
     */
    private void setupSpinner() {
        // Tạo adapter cho spinner
        // Các tùy chọn lấy từ các mảng String, và spinner sẽ sử dụng layout mặc định
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_os_options, android.R.layout.simple_spinner_item);

        // Kiểu layout xổ xuống - list view đơn giản với mỗi mục trên một dòng
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Áp dụng adapter vào spinner
        mOSSpinner.setAdapter(genderSpinnerAdapter);

        // Đặt số nguyên mSelected thành các giá trị hằng số
        mOSSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.os_android))) {
                        mOS = MobileEntry.OS_ANDROID;
                    } else if (selection.equals(getString(R.string.os_ios))) {
                        mOS = MobileEntry.OS_IOS;
                    } else {
                        mOS = MobileEntry.OS_OTHER;
                    }
                }
            }

            // Vì AdapterView là lớp trừu tượng nên onNothingSelected phải được định nghĩa
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mOS = MobileEntry.OS_ANDROID;
            }
        });
    }

    /**
     * Lấy thông tin từ người dùng để nhập vào cơ sở dữ liệu.
     */
    private void insertMobile() {
        // Đọc từ các trường input
        // Loại bỏ các khoảng trắng ở đầu hoặc cuối chuỗi kí tự
        String nameString = mNameEditText.getText().toString().trim();
        String brandString = mBrandEditText.getText().toString().trim();
        String statusString = mStatusEditText.getText().toString().trim();

        // Tạo helper
        MobileDbHelper mDbHelper = new MobileDbHelper(this);

        // Lấy cơ sở dữ liệu ở chế độ ghi
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues với các tên cột là các khóa, và các thuộc tính
        // từ trình soạn thảo là các giá trị.
        ContentValues values = new ContentValues();
        values.put(MobileEntry.COLUMN_MOBILE_NAME, nameString);
        values.put(MobileEntry.COLUMN_MOBILE_BRAND, brandString);
        values.put(MobileEntry.COLUMN_MOBILE_OS, mOS);
        values.put(MobileEntry.COLUMN_MOBILE_STATUS, statusString);

        // Chèn một hàng mới trong cơ sở dữ liệu và trả lại ID của hàng mới đó.
        long newRowId = db.insert(MobileEntry.TABLE_NAME, null, values);

        // Hiển thị toast khi lưu thành công hoặc thất bại
        if (newRowId == -1) {
            // Nếu hàng có _ID là -1 thì thất bại.
            Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu không thì chèn thành công và ta xuất ra số _ID.
            Toast.makeText(this, "Thiết bị đã được lưu với id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate các lựa chọn menu từ file res/menu/menu_editor.xml
        // Việc này sẽ thêm các menu item vào app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Người dùng bấm vào một lựa chọn từ menu xổ xuống trên app bar
        switch (item.getItemId()) {
            // Phản hồi khi người dùng bấm "Lưu"
            case R.id.action_save:
                // Lưu thiết bị
                insertMobile();
                // Thoát activity
                finish();
                return true;
            // Phản hồi khi người dùng bấm "Xóa"
            case R.id.action_delete:
                // Chưa làm gì lúc này
                return true;
            // Phản hồi khi người dùng bấm nút Up
            case android.R.id.home:
                // Quay về activity cha (là CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}