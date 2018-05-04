package com.example.android.mobilecare.data;

import android.provider.BaseColumns;

/**
 * API Contract của ứng dụng MobileCare.
 */
public final class MobileContract {

    // Để tránh vô tình khởi tạo lớp contract, ta cho nó một constructor rỗng
    private MobileContract() {}

    /**
     * Lớp bên trong định nghĩa các hằng giá trị không đổi cho bảng cơ sở dữ liệu.
     * Mỗi mục nhập trong bảng đại diện cho một thiết bị.
     */
    public static final class MobileEntry implements BaseColumns {

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