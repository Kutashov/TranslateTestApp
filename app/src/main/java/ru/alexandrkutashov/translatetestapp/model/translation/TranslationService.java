package ru.alexandrkutashov.translatetestapp.model.translation;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.alexandrkutashov.translatetestapp.BuildConfig;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationService {

    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String API_KEY = "trnsl.1.1.20170324T145123Z.b502e1294eb34749.56f8e96dd4cdf423515a812ef5ce7bb9406fc355";

    private TranslationApi translationApi;

    public TranslationService() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        translationApi = retrofit.create(TranslationApi.class);
    }

    public TranslationApi getApi() {
        return translationApi;
    }

    public interface TranslationApi {

        @GET("translate?key=" + API_KEY)
        Observable<Translate> translate(@Query("text") String text, @Query("lang") String direction);
    }
}
