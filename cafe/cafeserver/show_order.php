<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách đơn hàng</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
    </style>
</head>
<body>
    <h1>Danh sách đơn hàng</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên khách hàng</th>
                <th>Tên sản phẩm</th>
                <th>Ngày giờ đặt hàng</th>
                <th>Số lượng</th>
            </tr>
        </thead>
        <tbody id="orderTableBody">
            <!-- Dữ liệu sẽ được thêm vào đây thông qua JavaScript -->
        </tbody>
    </table>

    <script>
        // Gửi yêu cầu AJAX lấy dữ liệu đơn hàng
        fetch('get_order_data.php')
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    let orders = data.data;
                    let tableBody = document.getElementById('orderTableBody');

                    orders.forEach(order => {
                        let row = document.createElement('tr');

                        row.innerHTML = `
                            <td>${order.id}</td>
                            <td>${order['customer name']}</td>
                            <td>${order['product name']}</td>
                            <td>${order.date_time}</td>
                            <td>${order.quantity}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                }
            })
            .catch(error => {
                console.error('Error fetching orders:', error);
            });
    </script>
</body>
</html>
