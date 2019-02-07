package api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retromodels.Lists;

public interface ApiInterface {
    @GET("API/Value/GetDlrcode")
    Observable<Lists> GetDealerCode();

    @GET("API/Value/GetDlrcode")
    Observable<Lists> GetDealersCode();

    @GET("API/Value/GetDlrcode")
    Call<Lists> GetDealerCodes();
}
