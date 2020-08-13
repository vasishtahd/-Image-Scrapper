
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("iso_language_code")
    @Expose
    private String isoLanguageCode;
    @SerializedName("result_type")
    @Expose
    private String resultType;

    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    public void setIsoLanguageCode(String isoLanguageCode) {
        this.isoLanguageCode = isoLanguageCode;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

}
