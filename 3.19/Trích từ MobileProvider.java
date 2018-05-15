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

/**
 * Update pets in the database with the given content values. Apply the changes to the rows
 * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
 * Return the number of rows that were successfully updated.
 */
private int updateMobile(Uri uri, ContentValues values, String selection, String[] selectionArgs) {  
    // TODO: Cập nhật thiết bị đã chọn trong bảng cơ sở dữ liệu với ContentValues đã cho

    // TODO: Trả lại số hàng được chỉnh sửa
    return 0;
}