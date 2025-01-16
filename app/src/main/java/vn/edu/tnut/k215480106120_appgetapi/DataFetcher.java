package vn.edu.tnut.k215480106120_appgetapi;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataFetcher {

    private static final String API_URL = "https://57kmt.duckdns.org/android/api.aspx?action=last_id";
    private static final int UPDATE_INTERVAL = 30000; // 30 giây (30.000 ms)
    private Handler handler;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Runnable fetchDataRunnable;

    public interface DataFetcherCallback {
        void onDataFetched(String data);
    }

    public DataFetcher(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
        initializeMediaPlayer();
    }

    private void initializeMediaPlayer() {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd("notification.mp3"); // Đảm bảo bạn có tệp âm thanh trong thư mục assets
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startFetchingData(final DataFetcherCallback callback) {
        fetchDataRunnable = new Runnable() {
            @Override
            public void run() {
                fetchDataFromApi(callback);
                handler.postDelayed(this, UPDATE_INTERVAL); // Lặp lại sau mỗi 30 giây
            }
        };
        handler.post(fetchDataRunnable); // Bắt đầu việc lấy dữ liệu ngay khi gọi phương thức này
    }

    void fetchDataFromApi(final DataFetcherCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Gửi yêu cầu GET đến API
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    final String result = response.toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Trả về kết quả và phát âm thanh thông báo
                            callback.onDataFetched(result);

                            // Phát âm thanh thông báo
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopFetchingData() {
        handler.removeCallbacks(fetchDataRunnable);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
