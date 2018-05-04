TextView displayView = (TextView) findViewById(R.id.text_view_pet);

try {
    // Tạo header trong Text View như sau:
    //
    // Bảng thiết bị chứa <số hàng trong con trỏ> thiết bị
    // _id - name - breed - gender - weight
    //
	// Trong vòng lặp dưới đây, lặp qua từng hàng của con trỏ và hiển thị thông tin
	// từ mỗi cột theo thứ tự này.
    displayView.setText("Bảng chứa " + cursor.getCount() + " thiết bị.\n\n");
    displayView.append(PetEntry._ID + " - " +
            PetEntry.COLUMN_PET_NAME + "\n");

    // Lấy chỉ số của mỗi cột
    int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
    int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);

    // Lặp qua các hàng được trả về trong con trỏ
    while (cursor.moveToNext()) {
		// Sử dụng chỉ số đó để trích xuất giá trị String hoặc Int của thiết bị
		// tại hàng hiện tại mà con trỏ đang trỏ tới.
        int currentID = cursor.getInt(idColumnIndex);
        String currentName = cursor.getString(nameColumnIndex);
		// Hiển thị giá trị từ mỗi cột của dòng hiện tại trong con trỏ ở TextView
        displayView.append(("\n" + currentID + " - " +
                currentName));
    }
} finally {
    // Always close the cursor when you're done reading from it. This releases all its
    // resources and makes it invalid.
	// Luôn đóng con trỏ sau khi đọc để giải phóng tài nguyên.
    cursor.close();
}