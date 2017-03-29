package ru.alexandrkutashov.translatetestapp.model.translation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.alexandrkutashov.translatetestapp.R;

/**
 * Created by Alexandr on 29.03.2017.
 */

public class LanguageUtils {

    public static Map<String, String> parseLanguageFile(Context context) {
        HashMap<String, String> map = new HashMap<>();

        XmlResourceParser parser = context.getResources().getXml(R.xml.languages);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public static Observable<String> observeSpinner(Spinner spinner) {
        final PublishSubject<String> selectSubject = PublishSubject.create();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                selectSubject.onNext(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return selectSubject;
    }
}
