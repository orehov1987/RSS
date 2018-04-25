package ru.orehovai.rss_news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RssItems {

    private static final String TAG = "MyLog";


    public ArrayList<Rss> getRSSItems(String feedURL) {
        ArrayList<Rss> rssItems = new ArrayList<>();
        try {
            //открываем URL соединение и делаем GET-запрос на сервер.Забираем XML(RSS данные)
            URL url = new URL(feedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                //для парсинга xml DocumentBuilderFactory,DocumentBuilder
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                //парсим данные и добавляем их в Element
                Document document = db.parse(is);
                Element element = document.getDocumentElement();
                //достаем по тегу item
                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength() > 0) {//заполняем элементы в соответствии с тегами
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element entry = (Element) nodeList.item(i);

                        Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element _descriptuonE = (Element) entry.getElementsByTagName("description").item(0);
                        Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
                        Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
                        //заполняем переменные
                        String _title = _titleE.getFirstChild().getNodeValue();
                        String _description = _descriptuonE.getFirstChild().getNodeValue();
                        Date _pubDate = new Date(_pubDateE.getFirstChild().getNodeValue());
                        String _link = _linkE.getFirstChild().getNodeValue();
                        String imgUrlStr = null;
                        Bitmap _bitmap = null;
                        boolean _isBookmarked = false;
                        if (_description.contains("<img")) {//если если ссылка на картинку добавляем ее в переменнуюб
                            String[] array = _description.split("\"", 3);
                            imgUrlStr = array[1];//делим строку на 3 части кавычками и берем ту часть что между ними - адрес
                        }    else imgUrlStr = null;

//////////////////////////////////////

                       /* public void onClick(View v) {
                            new Thread(new Runnable() {
                                public void run() {
                                    final Bitmap bitmap = loadImageFromNetwork("http://example.com/image.png");
                                    mImageView.post(new Runnable() {
                                        public void run() {
                                            mImageView.setImageBitmap(bitmap);
                                        }
                                    });
                                }
                            }).start();
                        }*/
                        try {
                            URL imgUrl = new URL(imgUrlStr);
                            HttpURLConnection urlConnection = null;
                            if (imgUrl.getProtocol().toLowerCase().equals("https")) {
                                imgUrlStr = imgUrlStr.replace ("https", "http");
                                imgUrl = new URL(imgUrlStr);//меняе в адресе https на http для простоты
                            }
                            urlConnection = (HttpURLConnection) imgUrl.openConnection();
                            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//проверка доступности
                                InputStream inputStream = urlConnection.getInputStream();
                                _bitmap = BitmapFactory.decodeStream(inputStream);//загружаем картинку
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //инициализируем элемент списка и передаем туда переменные
                        Rss getRSS = new Rss(_title, _description, _pubDate, _link, imgUrlStr, _bitmap, _isBookmarked);
                        rssItems.add(getRSS);//добавляем элемени в список
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssItems;//возвращаем список
    }
}
