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
package com.example.android.mobilecare.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

/**
 * Trình trợ giúp cơ sở dữ liệu cho Ứng dụng. Quản lý việc tạo cơ sở dữ liệu và phiên bản.
 */
public class MobileDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MobileDbHelper.class.getSimpleName();

    /** Tên của file cơ sở dữ liệu */
    private static final String DATABASE_NAME = "mobile.db";

    /**
     * Phiên bản cơ sở dữ liệu. Nếu bạn thay đổi lược đồ cơ sở dữ liệu, bạn phải tăng phiên bản cơ sở dữ liệu.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Xây dựng một thể hiện mới của {@link MobileDbHelper}.
     *
     * @param context của ứng dụng
     */
    public MobileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Được gọi khi tạo cơ sở dữ liệu lần đầu tiên
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo một Chuỗi chứa câu lệnh SQL để tạo bảng mobilecare
        String SQL_CREATE_MOBILES_TABLE =  "CREATE TABLE " + MobileEntry.TABLE_NAME + " ("
                + MobileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MobileEntry.COLUMN_MOBILE_NAME + " TEXT NOT NULL, "
                + MobileEntry.COLUMN_MOBILE_BRAND + " TEXT, "
                + MobileEntry.COLUMN_MOBILE_OS + " INTEGER NOT NULL, "
                + MobileEntry.COLUMN_MOBILE_STATUS + " TEXT NOT NULL DEFAULT 'Chua kiem tra');";

        // Thực thi câu lệnh SQL
        db.execSQL(SQL_CREATE_MOBILES_TABLE);
    }

    /**
     * Được gọi khi cập nhật cơ sở dữ liệu.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cơ sở dữ liệu đang ở version 1, nên chưa làm gì lúc này.
    }
}