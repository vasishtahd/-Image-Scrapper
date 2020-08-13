package appengine.parser.repository;

import appengine.parser.objects.AccessToken;
import appengine.parser.objects.UserCredentials;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anand.kurapati on 12/12/17.
 */
public class PagesAggregatorRepository extends BaseRepository {

    private final String MANIT_AGGREGATOR_ID="376916969427259";
    private final String MANIT_CHIMERA_ID="779389722147162";
    private final String BEING_MANITIAN_ID="100013706256386";

    private final String default_access_token = "1926066927645816|RPu4zuvmPe3PDTs123WVm2jy664";

    private final AccessToken[] access_tokens = new AccessToken[]{
       new AccessToken("EAAbXv2cJNHgBAAUZAZAosZAVv8wIiN7pmWcvYaAgvmBdv6jQX0bhrMjiinpD3Ap0RdNdaAeII2EDpf2K6G4JPC",
               AccessToken.ID_TYPE.PAGE,MANIT_AGGREGATOR_ID)

    };

    private final String[] third_party_page_ids = new String[]{

            MANIT_CHIMERA_ID,
            BEING_MANITIAN_ID
    };

    private final Map<String, String[]> pageofSameCategoryMap = new HashMap<String, String[]>() {
        {
            put(BEING_MANITIAN_ID, new String[]{MANIT_AGGREGATOR_ID});
            put(MANIT_CHIMERA_ID,new String[]{MANIT_AGGREGATOR_ID});
        }
    };


    @Override
    public UserCredentials[] getUserCredentials() {
        return null;
    }

    @Override
    public String getDefaultAccessToken() {
        return default_access_token;
    }

    @Override
    public AccessToken[] getAccessTokens() {
        return access_tokens;
    }

    @Override
    public String[] getThirdPartyPages() {
        return third_party_page_ids;
    }

    @Override
    public String[] getOwnPages() {
        return null;
    }

    @Override
    public Map<String, String[]> getPageofSameCategoryMap() {
        return pageofSameCategoryMap;
    }
}
