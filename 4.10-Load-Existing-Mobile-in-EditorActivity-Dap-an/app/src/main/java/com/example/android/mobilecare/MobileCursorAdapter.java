package com.example.android.mobilecare;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.mobilecare.data.MobileContract.MobileEntry;

/**
 * {@link MobileCursorAdapter} là một adapter cho list hoặc grid view
 * sử dụng con trỏ cho dữ liệu các thiết bị để làm nguồn dữ liệu. Adapter này
 * có thể tạo list items cho mỗi hàng dữ liệu thiết bị trong con trỏ.
 */
public class MobileCursorAdapter extends CursorAdapter {

    /**
     * Xây dựng {@link MobileCursorAdapter}.
     *
     * @param context context
     * @param c       con trỏ để lấy dữ liệu.
     */
    public MobileCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Tạo list item view trống. Chưa có dữ liệu hoặc dữ liệu chưa được gắn vào view.
     *
     * @param context Context
     * @param cursor  Con trỏ đã được đưa vào vị trí.
     * @param parent  View cha
     * @return  list item view mới tạo.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate một list item view dùng layout từ list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Phương thức này ràng buộc dữ liệu (ở hàng đang được con trỏ trỏ vào) với list item layout
     * đã cho. VD, tên của thiết bị hiện tại có thể được đặt vào TextView name của list item layout.
     *
     * @param view    view được trả về từ phương thức newView()
     * @param context context
     * @param cursor  con trỏ để lấy dữ liệu.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Tìm các view muốn sửa đổi trong list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        // Tìm các cột thuộc tính của thiết bị mà ta cần
        int nameColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_NAME);
        int brandColumnIndex = cursor.getColumnIndex(MobileEntry.COLUMN_MOBILE_BRAND);

        // Đọc các thuộc tính của thiết bị hiện tại từ con trỏ
        String mobileName = cursor.getString(nameColumnIndex);
        String mobileBrand= cursor.getString(brandColumnIndex);

        // Cập nhật TextViews với các thuộc tính của thiết bị hiện tại
        nameTextView.setText(mobileName);
        summaryTextView.setText(mobileBrand);
    }
}