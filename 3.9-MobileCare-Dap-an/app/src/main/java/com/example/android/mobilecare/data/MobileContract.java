package com.example.android.mobilecare.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract của ứng dụng MobileCare.
 */
public final class MobileContract {

    // Để tránh vô tình khởi tạo lớp contract, ta cho nó một constructor rỗng
    private MobileContract() {}

    /**
     * "Content authority" là tên của content provider, tương tự như mối quan hệ giữa tên miền và
     * trang web của nó. Một chuỗi thuận tiện để sử dụng cho content provider là tên gói của ứng
     * dụng, được bảo đảm là duy nhất trên thiết bị.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.mobilecare";

    /**
     * Sử dụng CONTENT_AUTHORITY để tạo ra cơ sở của tất cả các URI mà ứng dụng sẽ sử dụng
     * để liên hệ với Content Provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Đường dẫn khả thi (được thêm vào nội dung cơ bản của URI).
     * Ví dụ:
     *
     * content://com.example.android.mobilecare/mobilecare/ là một đường dẫn hợp lệ để xem
     * dữ liệu các thiết bị.
     *
     * content: //com.example.android.mobilecare/staff/ sẽ thất bại, vì ContentProvider chưa được
     * cung cấp bất kỳ thông tin nào về việc phải làm gì với "staff".
     */
    public static final String PATH_MOBILE = "mobile";

    /**
     * Lớp bên trong định nghĩa các giá trị không đổi cho bảng cơ sở dữ liệu các thiết bị.
     * Mỗi entry trong bảng đại diện cho một thiết bị.
     */
    public static final class MobileEntry implements BaseColumns {

        /** Content URI để truy cập dữ liệu trong provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOBILE);

        /** Tên bảng cơ sở dữ liệu */
        public final static String TABLE_NAME = "mobile";

        /**
         * ID duy nhất cho các thiết bị (chỉ dùng trong bảng cơ sở dữ liệu).
         *
         * Loại: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Tên thiết bị.
         *
         * Loại: TEXT
         */
        public final static String COLUMN_MOBILE_NAME ="Name";

        /**
         * Hãng sản xuất.
         *
         * Loại: TEXT
         */
        public final static String COLUMN_MOBILE_BRAND = "Brand";

        /**
         * Hệ điều hành.
         *
         * Các giá trị có thể đặt là {@link #OS_ANDROID}, {@link #OS_IOS},
         * hoặc {@link #OS_OTHER}.
         *
         * Loại: INTEGER
         */
        public final static String COLUMN_MOBILE_OS = "OS";

        /**
         * Tình trạng thiết bị.
         *
         * Loại: TEXT
         */
        public final static String COLUMN_MOBILE_STATUS = "Status";

        /**
         * Các giá trị của hệ điều hành.
         */
        public static final int OS_ANDROID = 1;
        public static final int OS_IOS = 2;
        public static final int OS_OTHER = 3;
    }

}