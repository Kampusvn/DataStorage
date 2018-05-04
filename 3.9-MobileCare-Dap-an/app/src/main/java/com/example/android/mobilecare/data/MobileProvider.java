package com.example.android.mobilecare.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * {@link ContentProvider} cho ứng dụng MobileCare.
 */
public class MobileProvider extends ContentProvider {

    /** Tag của log messages */
    public static final String LOG_TAG = MobileProvider.class.getSimpleName();

    /**
     * Khởi tạo provider và đối tượng trợ giúp cơ sở dữ liệu.
     */
    @Override
    public boolean onCreate() {
        // TODO: Tạo đối tượng MobileDbHelper để lấy quyền truy cập cơ sở dữ liệu.
        // Biến phải là toàn cục để nó có thể được tham chiếu từ các phương thức ContentProvider khác.
        // ContentProvider methods.
        return true;
    }

    /**
     * Thực hiện truy vấn với URI đã cho. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    /**
     * Chèn dữ liệu mới vào provider với ContentValues đã cho.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * Cập nhật dữ liệu với ContentValues mới.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Xóa dữ liệu tại các trường dữ liệu.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Trả về MIME type cho content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}