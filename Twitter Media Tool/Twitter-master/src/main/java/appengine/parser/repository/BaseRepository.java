package appengine.parser.repository;

import appengine.parser.objects.AccessToken;
import appengine.parser.objects.UserCredentials;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by anand.kurapati on 11/12/17.
 */
public abstract class BaseRepository {

    public abstract UserCredentials[] getUserCredentials();

    public abstract String getDefaultAccessToken();

    public abstract AccessToken[] getAccessTokens();

    public abstract String[] getThirdPartyPages();

    public abstract String[] getOwnPages();

    public abstract Map<String, String[]> getPageofSameCategoryMap();

    public AccessToken getAccessTokenForId(String id) {

        AccessToken[] accessTokens = getAccessTokens();
        for (AccessToken accessToken : accessTokens) {
            if (accessToken.id.equals(id)) {
                return accessToken;
            }
        }
        return null;
    }

    public ArrayList<AccessToken> getAccessTokensOfSameCategory(String pageId) {

        Map<String, String[]> pageofSameCategoryMap = getPageofSameCategoryMap();
        ArrayList<AccessToken> accessTokens = new ArrayList<>();
        String[] pageIdsOfSameCategory = pageofSameCategoryMap.get(pageId);
        for (String pageIdOfSameCategory : pageIdsOfSameCategory) {
            accessTokens.add(getAccessTokenForId(pageIdOfSameCategory));
        }
        return accessTokens;
    }


}
