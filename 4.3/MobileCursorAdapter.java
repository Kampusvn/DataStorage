package com.example.android.mobilecare;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

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
     * @param cursor       con trỏ để lấy dữ liệu.
     */
    public MobileCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
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
        // TODO: Hoàn thiện phương thức này và trả về list item view (thay vì null)
        return null;
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
        // TODO: Hoàn thiện phương thức
    }
}