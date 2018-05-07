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

        // TODO: Chèn một thiết bị mới vào bảng cơ sở dữ liệu với ContentValues đã cho

        // Khi biết ID của hàng mới trong bảng, trả về URI với ID ở cuối
        return ContentUris.withAppendedId(uri, id);
    }