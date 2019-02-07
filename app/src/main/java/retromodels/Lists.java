
package retromodels;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Lists {

    @SerializedName("data")
    private java.util.List<String> mData;
    @SerializedName("result")
    private String mResult;

    public java.util.List<String> getData() {
        return mData;
    }

    public void setData(java.util.List<String> data) {
        mData = data;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

}
