<?php
header('Content-Type: application/json');

// Kết nối tới MySQL
$conn = new mysqli("localhost", "root", "", "cafedb");

// Kiểm tra kết nối
if ($conn->connect_error) {
    echo json_encode([
        "status" => "error",
        "message" => "Connection failed: " . $conn->connect_error
    ]);
    exit();
}

// Truy vấn dữ liệu từ bảng orders
$sql = "SELECT id, `customer name`, `product name`, `date_time`, `quantity` FROM orders";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $data = array();
    while ($row = $result->fetch_assoc()) {
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

$conn->close();
?>
