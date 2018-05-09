private void showDeleteConfirmationDialog() {
    // Tạo AlertDialog.Builder và thiết lập thông báo cùng click listener
	// cho các nút trên hộp thoại.
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.delete_dialog_msg);
    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // Người dùng nhấn nút "Xóa" để xác nhận xóa thiết bị.
            deleteMobile();
        }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // Người dùng nhấn nút "Không" và tắt hộp thoại
            // để tiếp tục chỉnh sửa thông tin thiết bị
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    });

    // Tạo và hiển thị hộp thoại AlertDialog
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
}

/**
 * Thực hiện xóa thiết bị trong cơ sở dữ liệu
 */
private void deleteMobile() {
    // TODO: Hoàn thành phương thức này
}