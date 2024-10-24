<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Kết nối tới MySQL
    $conn = new mysqli("localhost", "root", "", "cafedb");

    // Kiểm tra kết nối
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Kiểm tra dữ liệu POST
    if (isset($_POST['product_name'])) {
        // Lấy dữ liệu từ POST request
        $product_name = $_POST['product_name'];
        $date_time = isset($_POST['date_time']) ? $_POST['date_time'] : date('Y-m-d H:i:s'); // Lấy thời gian hiện tại nếu không truyền từ client

        // Chuẩn bị và thực thi truy vấn
        $sql = "INSERT INTO orders (`product name`, `date_time`) VALUES (?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $product_name, $date_time);

        if ($stmt->execute()) {
            // Phản hồi thành công về cho ứng dụng
            echo json_encode(["status" => "success", "message" => "Đơn hàng đã được thêm thành công!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Lỗi: " . $stmt->error]);
        }

        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Thiếu thông tin sản phẩm"]);
    }

    $conn->close();
} else {
    echo json_encode(["status" => "error", "message" => "Yêu cầu không hợp lệ"]);
}
?>
