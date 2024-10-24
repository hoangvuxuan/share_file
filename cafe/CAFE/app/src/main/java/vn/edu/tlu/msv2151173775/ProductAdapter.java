package vn.edu.tlu.msv2151173775;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private ApiService apiService;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

        // Lấy instance của ApiService
        apiService = ApiClient.getApiService();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice() + " VND");

        // Sử dụng Glide để load hình ảnh
        Glide.with(context)
                .load(product.getPicture())
                .into(holder.pictureImageView);

        // Xử lý sự kiện khi nhấn nút "Đặt hàng"
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thời gian hiện tại
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Gửi yêu cầu POST đến server
                Call<Void> call = apiService.addOrder(product.getName(), currentDateTime);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Hiển thị thông báo khi đặt hàng thành công
                            Toast.makeText(context, "Đã đặt hàng: " + product.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi đặt hàng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        ImageView pictureImageView;
        Button orderButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            priceTextView = itemView.findViewById(R.id.textViewPrice);
            pictureImageView = itemView.findViewById(R.id.imageViewPicture);
            orderButton = itemView.findViewById(R.id.buttonOrder);
        }
    }
}
