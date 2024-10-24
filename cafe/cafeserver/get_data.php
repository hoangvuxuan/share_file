<?php
header('Content-Type: application/json');

// Kết nối đến MySQL
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "cafedb";

// Tạo kết nối
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    echo json_encode([
        "status" => "error",
        "message" => "Kết nối thất bại: " . $conn->connect_error
    ]);
    exit();
}

// Truy vấn dữ liệu từ bảng Products
$sql = "SELECT id, name, price, picture FROM Products";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $data = array();
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
    echo json_encode([
        "status" => "success",
        "data" => $data
    ]);
} else {
    echo json_encode([
        "status" => "success",
        "data" => []
    ]);
}

// Đóng kết nối
$conn->close();
?>
