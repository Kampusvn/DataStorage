public class CatalogActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        ...

        mDbHelper = new PetDbHelper(this);
        displayDatabaseInfo();
    }
    
    ...
    
   /**
     * Phương thức trợ giúp để chèn dữ liệu thiết bị được mã hóa vào cơ sở dữ liệu. Chỉ dành cho mục đích debug.
     */
    private void insertPet() {
        // TODO: Chèn một thiết bị vào cơ sở dữ liệu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Người dùng bấm vào một lựa chọn trên menu trong app bar
        switch (item.getItemId()) {
			// Phản hồi cho thao tác bấm vào lựa chọn "Thêm dữ liệu giả"
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
         
         ...
    }
}