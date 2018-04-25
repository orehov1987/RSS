package ru.orehovai.rss_news;
/*
* Активность для отображения подробностей новости
* */
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RssDetails extends AppCompatActivity  {

    //private static final String TAG = "MyLog";

    public static Rss selectedRSSItem = null;
    TextView tvDetails;
    Button brouzerBtn;
    Button bookmarkBtn;
    ImageView ivDetImage;
    int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIndex = getIntent().getIntExtra("mIndex", 0);//и индекс выбранного пункта в архиве
        setContentView(R.layout.rss_details);
        tvDetails = findViewById(R.id.tvDetails);
        brouzerBtn = findViewById(R.id.brouserBtn);
        bookmarkBtn = findViewById(R.id.bookmarkButton);
        ivDetImage = findViewById(R.id.ivDetImage);

        selectedRSSItem = Main.getNews().get(mIndex);//передаем пункт списка


        tvDetails.setText(selectedRSSItem.getTitle());//уствнваливаем текст
        if (selectedRSSItem.getImage() != null) {
            ivDetImage.setImageBitmap(selectedRSSItem.getBitmap());
        } else ivDetImage.setImageResource(R.drawable.prphoto);//устанавливаем картинку или заглушку

        //обработчик кнопки закладки
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(selectedRSSItem.isBookmarked())) selectedRSSItem.setBookmarked(true);//если не закладка сделать закладкой
                else selectedRSSItem.setBookmarked(false);//если уже заклдка отменить
                finish();
            }
        });

        brouzerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedRSSItem.getLink())));//открываем браузер с полным описнием новости
            }
        });
    }
}
