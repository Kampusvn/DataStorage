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

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

/**
 * Cho phép người dùng thêm mới hoặc chỉnh sửa một thiết bị
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Bộ nhận dạng cho trình nạp dữ liệu thiết bị */
    private static final int EXISTING_MOBILE_LOADER = 0;

    /** Content URI cho thiết bị hiện tại (null nếu đó là thiết bị mới) */
    private Uri mCurrentMobileUri;

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

    /** Cờ boolean theo dõi xem thiết bị có được chỉnh sửa hay không */
    private boolean mMobileHasChanged = false;

    /**
     * OnTouchListener lắng nghe sự kiện người dùng chạm bấm vào View, tức là họ đang sửa View,
     * khi đó ta chuyển giá trị của boolean mMobileHasChanged thành true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMobileHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Kiểm tra intent được sử dụng để khởi động activity này, để biết liệu chúng ta đang
        // tạo thiết bị mới hay chỉnh sửa một thiết bị hiện có.
        Intent intent = getIntent();
        mCurrentMobileUri = intent.getData();

        // Nếu intent KHÔNG chứa content URI, tức là ta đang tạo một thiết bị mới.
        if(mCurrentMobileUri == null) {
            // Vì đang nhập thiết bị mới nên app bar sẽ hiển thị "Thêm thiết bị"
            setTitle(getString(R.string.editor_activity_title_new_mobile));

            // Hủy bỏ menu tùy chọn, để tùy chọn "Xoá" có thể được ẩn.
            // (Người dùng không thể xóa nếu chưa có thiết bị.)
            invalidateOptionsMenu();
        } else {
            // Nếu không phải thêm mới, thì app bar sẽ hiển thị "Sửa thiết bị"
            setTitle(getString(R.string.editor_activity_title_edit_mobile));

            // Khởi tạo loader để đọc dữ liệu từ cơ sở dữ liệu và hiển thị các giá trị hiện tại
            // trong trình edit
            getLoaderManager().initLoader(EXISTING_MOBILE_LOADER, null, this);
        }

        // Tìm tất cả các View có liên quan để đọc từ input của người dùng
        mNameEditText = (EditText) findViewById(R.id.edit_mobile_name);
        mBrandEditText = (EditText) findViewById(R.id.edit_mobile_brand);
        mStatusEditText = (EditText) findViewById(R.id.edit_mobile_status);
        mOSSpinner = (Spinner) findViewById(R.id.spinner_os);

        // Thiết lập OnTouchListeners trên tất cả các trường nhập, vì vậy ta có thể
        // xác định khi nào người dùng đã bấm vào hay thay đổi chúng. Việc này sẽ cho ta biết
        // người dùng đã lưu nội dung hay chưa khi họ rời khỏi editor.
        mNameEditText.setOnTouchListener(mTouchListener);
        mBrandEditText.setOnTouchListener(mTouchListener);
        mStatusEditText.setOnTouchListener(mTouchListener);
        mOSSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    /**
     * Thiết lập lựa chọn spinner cho hệ điều hành để người dùng có thể chọn từ menu xổ xuống
     */
    private void setupSpinner() {
        // Tạo adapter cho spinner
        // Các tùy chọn lấy từ các mảng String, và spinner sẽ sử dụng layout mặc định
        ArrayAdapter osSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_os_options, android.R.layout.simple_spinner_item);

        // Kiểu layout xổ xuống - list view đơn giản với mỗi mục trên một dòng
        osSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Áp dụng adapter vào spinner
        mOSSpinner.setAdapter(osSpinnerAdapter);

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
     * Lấy thông tin từ người dùng và lưu vào cơ sở dữ liệu.
     */
    private void saveMobile() {
        // Đọc từ các trường input
        // Loại bỏ các khoảng trắng ở đầu hoặc cuối chuỗi kí tự
        String nameString = mNameEditText.getText().toString().trim();
        String brandString = mBrandEditText.getText().toString().trim();
        String statusString = mStatusEditText.getText().toString().trim();


        // Tạo một đối tượng ContentValues với các tên cột là các khóa, và các thuộc tính
        // từ trình soạn thảo là các giá trị.
        ContentValues values = new ContentValues();
        values.put(MobileEntry.COLUMN_MOBILE_NAME, nameString);
        values.put(MobileEntry.COLUMN_MOBILE_BRAND, brandString);
        values.put(MobileEntry.COLUMN_MOBILE_OS, mOS);
        values.put(MobileEntry.COLUMN_MOBILE_STATUS, statusString);

        // Xác định xem đây là thiết bị mới hoặc đã nhập bằng cách kiểm tra xem mCurrentMobileUri
        // có giá trị hay không
        if (mCurrentMobileUri == null) {
            // Trường hợp này là thiết bị mới, vì vậy ta thêm nó vào provider
            // và trả về content URI cho thiết bị.
            Uri newUri = getContentResolver().insert(MobileEntry.CONTENT_URI, values);
            // Hiển thị tin nhắn toast khi chèn thành công hoặc thất bại.
            if (newUri == null) {
                // Khi chèn thất bại
                Toast.makeText(this, getString(R.string.editor_insert_mobile_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Trường hợp thành công
                Toast.makeText(this, getString(R.string.editor_insert_mobile_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu không, đây là thiết bị đã có, vì vậy ta cập nhật thiết bị với content URI:
            // mCurrentMobileUri và truyền vào ContentValues mới.
            // Truyền null cho selection và selection args bởi vì mCurrentMobileUri đã xác định
            // đúng hàng trong cơ sở dữ liệu mà chúng ta muốn sửa đổi.
            int rowsAffected = getContentResolver().update(mCurrentMobileUri, values,
                    null, null);
            // Hiển thị tin nhắn toast khi cập nhật thành công hoặc thất bại.
            if (rowsAffected == 0) {
                // Khi cập nhật thất bại
                Toast.makeText(this, getString(R.string.editor_update_mobile_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Khi cập nhật thành công
                Toast.makeText(this, getString(R.string.editor_update_mobile_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate các lựa chọn menu từ file res/menu/menu_editor.xml
        // Việc này sẽ thêm các menu item vào app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * Phương thức này được gọi đến sau invalidateOptionsMenu(), để menu được cập nhật
     * (một vài menu item có thể được ẩn đi hoặc hiển thị).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Nếu đây là một thiết bị mới, ta ẩn menu item "Xóa".
        if (mCurrentMobileUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Người dùng bấm vào một lựa chọn từ menu xổ xuống trên app bar
        switch (item.getItemId()) {
            // Phản hồi khi người dùng bấm "Lưu"
            case R.id.action_save:
                // Lưu thiết bị
                saveMobile();
                // Thoát activity
                finish();
                return true;
            // Phản hồi khi người dùng bấm "Xóa"
            case R.id.action_delete:
                // Chưa làm gì lúc này
                return true;
            // Phản hồi khi người dùng bấm nút Up
            case android.R.id.home:
                // Nếu nội dung không thay đổi, ta quay về parent activity,
                // tức là {@link CatalogActivity}
                if (!mMobileHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Nếu có những thay đổi chưa được lưu, ta thiết lập một hộp thoại để
                // cảnh báo người dùng.
                // Tạo một click listener để người dùng xác nhận hủy các thay đổi.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Người dùng bấm "Hủy và thoát" để quay về parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Hiển thị hộp thoại báo cho người dùng biết đang có các thay đổi chưa được lưu
                showUnsavedChangesDialog(discardButtonClickListener);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Phương thức này được gọi khi người dùng bấm back
     */
    @Override
    public void onBackPressed() {
        // Nếu thiết bị chưa được thay đổi, ta xử lý khi người dùng bấm nút back
        if (!mMobileHasChanged) {
            super.onBackPressed();
            return;
        }

        // Nếu có những thay đổi chưa được lưu, thiết lập một hộp thoại để cảnh báo người dùng.
        // Tạo click listener để người dùng xác nhận loại bỏ các thay đổi.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Người dùng bấm "Hủy và thoát" để đóng activity hiện tại.
                        finish();
                    }
                };

        // Hiển thị hộp thoại thông báo đang có các thay đổi chưa được lưu
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Vì editor hiển thị tất cả thuộc tính của thiết bị, hãy xác định một projection có chứa
        // tất cả các cột từ bảng
        String[] projection = {
                MobileEntry._ID,
                MobileEntry.COLUMN_MOBILE_NAME,
                MobileEntry.COLUMN_MOBILE_BRAND,
                MobileEntry.COLUMN_MOBILE_OS,
                MobileEntry.COLUMN_MOBILE_STATUS };

        // Loader này sẽ thực hiện phương thức truy vấn ContentProvider trên background thread
        return new CursorLoader(this,   // Activity cha
                mCurrentMobileUri,         // Truy vấn content URI cho thiết bị hiện tại
                projection,             // Các cột trong con trỏ kết quả
                null,                   // Không có mệnh đề selection
                null,                   // Không có đối số selection
                null);                  // Thứ tự sắp xếp mặc định
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Trả về nếu con trỏ không hợp lệ hoặc có ít hơn 1 hàng trong con trỏ
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Tiếp tục di chuyển đến dòng đầu tiên của con trỏ và đọc dữ liệu từ nó
        // (Đây sẽ là hàng duy nhất trong con trỏ)
        if (cursor.moveToFirst()) {
            // Tìm các cột thuộc tính thiết bị mà ta cần
            int nameColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_NAME);
            int brandColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_BRAND);
            int osColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_OS);
            int statusColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_STATUS);

            // Trích xuất giá trị từ con trỏ cho chỉ mục cột đã cho
            String name = cursor.getString(nameColumnIndex);
            String brand = cursor.getString(brandColumnIndex);
            int os = cursor.getInt(osColumnIndex);
            String status = cursor.getString(statusColumnIndex);

            // Cập nhật view trên màn hình với giá trị từ cơ sở dữ liệu
            mNameEditText.setText(name);
            mBrandEditText.setText(brand);
            mStatusEditText.setText(status);

            // Hệ điều hành là một spinner, do đó ta ánh xạ giá trị hằng từ cơ sở dữ liệu vào một
            // trong các lựa chọn thả xuống (1 là Android, 2 là iOS, 3 là Khác).
            // Sau đó ta gọi setSelection() để tùy chọn đó được hiển thị trên màn hình.
            switch (os) {
                case MobileEntry.OS_ANDROID:
                    mOSSpinner.setSelection(0);
                    break;
                case MobileEntry.OS_IOS:
                    mOSSpinner.setSelection(1);
                    break;
                default:
                    mOSSpinner.setSelection(2);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nếu loader không hợp lệ, ta xóa tất cả dữ liệu ở các trường nhập
        mNameEditText.setText("");
        mBrandEditText.setText("");
        mStatusEditText.setText("");
        mOSSpinner.setSelection(0);
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     * Hiển thị một hộp thoại cảnh báo người dùng có những thay đổi chưa được lưu sẽ mất nếu họ
     * tiếp tục rời khỏi editor.
     *
     * @param discardButtonClickListener click listener quyết định phải làm gì khi
     *                                  người dùng xác nhận họ muốn hủy thay đổi.
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Tạo một AlertDialog.Builder và thiết lập thông báo,
        // và click listeners cho các nút trên hộp thoại.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Người dùng bấm "Tiếp tục", hủy bỏ hộp thoại
                // và tiếp tục chỉnh sửa.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Tạo và hiển thị AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}