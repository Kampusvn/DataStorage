/** URI matcher code cho content URI để dùng trong bảng mobilecare */
private static final int MOBILE = 100;

/** URI matcher code cho content URI để dùng cho một thiết bị đơn lẻ trong bảng */
private static final int MOBILE_ID = 101;

/**
 * Đối tượng UriMatcher để match một content URI với code tương ứng.
 * Input truyền vào constructor đại diện cho code để trả về root URI.
 * Thường thì ta sẽ sử dụng NO_MATCH làm input trong trường hợp này.
 */
private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

// Khởi tạo static, được chạy lần đầu tiên khi bất cứ thứ gì được gọi từ lớp này.
static {
	// Các lời gọi đến addURI() đặt ở đây, cho tất cả mẫu content URI mà provider cần nhận diện.
	// Tất cả các đường dẫn được thêm vào UriMatcher có một code tương ứng
	// để trả về khi tìm thấy match.

    // TODO: Thêm 2 content URI vào URI matcher
}