package com.example.android.mobilecare.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

/**
 * {@link ContentProvider} cho ứng dụng MobileCare.
 */
public class MobileProvider extends ContentProvider {

    /** URI matcher code cho content URI để dùng trong bảng mobilecare */
    private static final int MOBILE = 100;

    /** URI matcher code cho content URI để dùng cho một thiết bị đơn lẻ trong bảng */
    private static final int MOBILE_ID = 101;

    /**
     * Đối tượng UriMatcher để match một content URI với code tương ứng.
     * Input truyền vào constructor đại diện cho code để trả về root URI.
     * Thường thì ta sẽ sử dụng NO_MATCH làm input trong trường hợp này.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Khởi tạo static, được chạy lần đầu tiên khi bất cứ thứ gì được gọi từ lớp này.
    static {
        // Các lời gọi đến addURI() đặt ở đây, cho tất cả mẫu content URI mà provider cần nhận diện.
        // Tất cả các đường dẫn được thêm vào UriMatcher có một code tương ứng
        // để trả về khi tìm thấy match.

        // Hoàn thành: Thêm 2 content URI vào URI matcher
        // URI matcher này dùng để cấp quyền truy cập đến NHIỀU hàng trong bảng.
        sUriMatcher.addURI(MobileContract.CONTENT_AUTHORITY, MobileContract.PATH_MOBILE, MOBILE);
        // URI matcher này dùng để cấp quyền truy cập đến một hàng đơn lẻ trong bảng.
        sUriMatcher.addURI(MobileContract.CONTENT_AUTHORITY, MobileContract.PATH_MOBILE + "/#", MOBILE_ID);
    }

    private MobileDbHelper mDbHelper;

    /** Tag của log messages */
    public static final String LOG_TAG = MobileProvider.class.getSimpleName();

    /**
     * Khởi tạo provider và đối tượng trợ giúp cơ sở dữ liệu.
     */
    @Override
    public boolean onCreate() {
        // Hoàn thành: Tạo đối tượng MobileDbHelper để lấy quyền truy cập cơ sở dữ liệu.
        // Biến phải là toàn cục để nó có thể được tham chiếu từ các phương thức ContentProvider khác.
        // ContentProvider methods.
        mDbHelper = new MobileDbHelper(getContext());
        return true;
    }

    /**
     * Thực hiện truy vấn với URI đã cho.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Lấy cơ sở dữ liệu để đọc
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Con trỏ này giữ kết quả truy vấn
        Cursor cursor;

        // Kiểm tra xem URI matcher có thể match URI với code được hay không
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOBILE:
                // Với MOBILE code, truy vấn bảng mobile trực tiếp với projection, selection,
                // selection arguments, và sort order đã cho. Con trỏ có thể chứa nhiều hàng của
                // bảng.
                cursor = database.query(MobileContract.MobileEntry.TABLE_NAME, projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case MOBILE_ID:
                // Với MOBILE_ID code, trích xuất ID từ URI.
                // VD: với URI là "content://com.example.android.mobilecare/mobile/3",
                // selection sẽ là "_id=?" và selection argument là một chuỗi String chứa ID 3.

                // Với mỗi dấu "?" trong selection, ta cần có một element trong selection arguments
                // để điền vào dấu "?". Vì ta có 1 dấu "?" ở selection, ta sẽ có 1 String trong
                // chuỗi String của selection arguments.
                selection = MobileContract.MobileEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                // Code sau sẽ thực hiện truy vấn trong bảng mobile nơi _id = 3 để trả về một con
                // trỏ chứa hàng đó trong bảng.
                cursor = database.query(MobileEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Không thể truy vấn " + uri);
        }

        // Đặt URI thông báo trên con trỏ để biết con trỏ được tạo ra cho content URI nào.
        // Nếu dữ liệu tại URI này thay đổi, thì ta cần phải cập nhật Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Chèn dữ liệu mới vào provider với ContentValues đã cho.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOBILE:
                return insertMobile(uri, contentValues);
            default:
                throw new IllegalArgumentException("Không thể chèn " + uri);
        }
    }

    /**
     * Chèn một thiết bị mới vào cơ sở dữ liệu với các giá trị đã cho. Trả về content URI
     * cho hàng đó trong cơ sở dữ liệu.
     */
    private Uri insertMobile(Uri uri, ContentValues values) {
        // Kiểm tra tên hợp lệ
        String name = values.getAsString(MobileEntry.COLUMN_MOBILE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Hãy nhập tên thiết bị");
        }

        // Kiểm tra hệ điều hành hợp lệ
        Integer os = values.getAsInteger(MobileEntry.COLUMN_MOBILE_OS);
        if (os == null || !MobileEntry.isValidOs(os)) {
            throw new IllegalArgumentException("Hãy nhập hệ điều hành");
        }

        // Kiểm tra tình trạng thiết bị hợp lệ
        String status = values.getAsString(MobileEntry.COLUMN_MOBILE_STATUS);
        if (status == null) {
            throw new IllegalArgumentException("Hãy nhập tình trạng thiết bị");
        }

        // Không cần kiểm tra tên hãng, chấp nhận giá trị null.

        // Lấy cơ sở dữ liệu
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Chèn thiết bị mới với giá trị cho trước
        long id = database.insert(MobileEntry.TABLE_NAME, null, values);
        // Nếu ID = -1 thì xảy ra lỗi khi chèn. Log ra lỗi và trả về null.
        if (id == -1) {
            Log.e(LOG_TAG, "Không thể chèn hàng cho URI " + uri);
            return null;
        }

        // Thông báo cho tất cả listener rằng dữ liệu đã thay đổi cho content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Khi biết ID của hàng mới trong bảng, trả về URI với ID ở cuối
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Cập nhật dữ liệu với ContentValues mới.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOBILE:
                return updateMobile(uri, contentValues, selection, selectionArgs);
            case MOBILE_ID:
                // Đối với code MOBILE_ID, ta lấy ra ID từ URI, để biết được dòng nào cần cập nhật.
                // Selection sẽ là "_id =?" và selection arguments sẽ là một mảng String chứa ID thực.
                selection = MobileEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMobile(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Không thể cập nhật " + uri);
        }
    }

    private int updateMobile(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Nếu {@link MobileEntry#COLUMN_MOBILE_NAME} tồn tại,
        // ta kiểm tra xem nó có null không.
        if (values.containsKey(MobileEntry.COLUMN_MOBILE_NAME)) {
            String name = values.getAsString(MobileEntry.COLUMN_MOBILE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Cần nhập tên thiết bị");
            }
        }

        // Nếu {@link MobileEntry#COLUMN_MOBILE_OS} tồn tại, kiểm tra xem nó có hợp lệ hay không.
        if (values.containsKey(MobileEntry.COLUMN_MOBILE_OS)) {
            Integer os = values.getAsInteger(MobileEntry.COLUMN_MOBILE_OS);
            if (os == null || !MobileEntry.isValidOs(os)) {
                throw new IllegalArgumentException("Hệ điều hành không hợp lệ");
            }
        }

        // Nếu {@link MobileEntry#COLUMN_MOBILE_STATUS} tồn tại, kiểm tra xem nó có null hay không.
        if (values.containsKey(MobileEntry.COLUMN_MOBILE_STATUS)) {
            String status = values.getAsString(MobileEntry.COLUMN_MOBILE_STATUS);
            if (status == null) {
                throw new IllegalArgumentException("Cần nhập trạng thái thiết bị");
            }
        }

        // Không cần kiểm tra hãng sản xuất, mọi giá trị đều được chấp nhận.

        // Nếu không có giá trị nào cần cập nhật, ta không update gì trong cơ sở dữ liệu.
        if (values.size() == 0) {
            return 0;
        }

        // Nếu có dữ liệu cần cập nhật thì ta lấy cơ sở dữ liệu để ghi
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Thực hiện cập nhật trên cơ sở dữ liệu và lấy số hàng bị ảnh hưởng
        int rowsUpdated = database.update(MobileEntry.TABLE_NAME, values, selection, selectionArgs);

        // Nếu đã cập nhật 1 hoặc nhiều hàng, ta thông báo cho tất cả listener rằng
        // dữ liệu tại URI được cho đã thay đổi
                if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Trả về số hàng được cập nhật
        return rowsUpdated;
    }

    /**
     * Xóa dữ liệu tại các trường dữ liệu.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Lấy cơ sở dữ liệu để ghi
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Theo dõi số hàng bị xóa
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOBILE:
                // Xóa tất cả các hàng phù hợp với selection và selection args
                rowsDeleted = database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOBILE_ID:
                // Xóa một hàng với ID cho trước trong URI
                selection = MobileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Không thể xóa " + uri);
        }

        // Nếu 1 hay nhiều hàng bị xóa, ta thông báo cho tất cả listener rằng
        // dữ liệu tại URI được cho đã thay đổi
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Trả về số hàng bị xóa
        return rowsDeleted;
    }

    /**
     * Trả về MIME type cho content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOBILE:
                return MobileEntry.CONTENT_LIST_TYPE;
            case MOBILE_ID:
                return MobileEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI không xác định " + uri + " và match " + match);
        }
    }
}