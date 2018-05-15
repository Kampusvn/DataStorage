@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Lấy cơ sở dữ liệu để ghi
    SQLiteDatabase database = mDbHelper.getWritableDatabase();

    final int match = sUriMatcher.match(uri);
    switch (match) {
        case MOBILE:
            // Xóa tất cả các hàng phù hợp với selection và selection args
            return database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
        case MOBILE_ID:
             // Xóa một hàng với ID cho trước trong URI
            selection = MobileEntry._ID + "=?";
            selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
            return database.delete(MobileEntry.TABLE_NAME, selection, selectionArgs);
        default:
            throw new IllegalArgumentException("Không thể xóa " + uri);
    }
}