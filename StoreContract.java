public final class StoreContract {

    public static abstract class HeadphoneEntry implements BaseColumns {
      
        public static final String TABLE_NAME = "headphones";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_STYLE = "style";
        public static final String COLUMN_IN_STOCK = "in_stock";
        public static final String COLUMN_DESCRIPTION = "description";
    
        /**
         * Possible values for the style of the headphone.
         */
        public static final int STYLE_EAR_BUD = 0;
        public static final int STYLE_ON_EAR = 1;
        public static final int STYLE_OVER_EAR = 2;
        public static final int STYLE_BONE_CONDUCTION = 3;

    }
}
