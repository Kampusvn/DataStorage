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
                cursor = database.query(MobileContract.MobileEntry.TABLE_NAME, projection,selection,
                        selectionArgs,null,null,sortOrder);
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
        return cursor;
    }