package vn.edu.tnut.k215480106120_appgetapi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private DataFetcher dataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.status_text);

        // Khởi tạo DataFetcher
        dataFetcher = new DataFetcher(this);

        // Khởi tạo nút và sự kiện khi bấm
        Button getLastIdButton = findViewById(R.id.get_lastid);
        getLastIdButton.setOnClickListener(v -> dataFetcher.fetchDataFromApi(new DataFetcher.DataFetcherCallback() {
            @Override
            public void onDataFetched(String data) {
                resultTextView.setText(data);

                // Hiển thị thông báo
                Toast.makeText(MainActivity.this, "Last ID: " + data, Toast.LENGTH_SHORT).show();
            }
        }));

        // Bắt đầu việc lấy dữ liệu mỗi 30 giây
        dataFetcher.startFetchingData(new DataFetcher.DataFetcherCallback() {
            @Override
            public void onDataFetched(String data) {
                resultTextView.setText(data);

                // Hiển thị thông báo
                Toast.makeText(MainActivity.this, "Last ID: " + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng việc lấy dữ liệu khi Activity bị hủy
        dataFetcher.stopFetchingData();
    }
}
