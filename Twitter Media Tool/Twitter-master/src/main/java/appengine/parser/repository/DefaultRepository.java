package appengine.parser.repository;

import appengine.parser.facebook.AcceptFriendRequests;
import appengine.parser.objects.AccessToken;
import appengine.parser.objects.UserCredentials;
import appengine.parser.repository.BaseRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anand.kurapati on 11/12/17.
 */
public class DefaultRepository extends BaseRepository {

    private final String HUMORLY_YOURS_PAGE_ID = "1899435196944780";
    private final String SARCASM_PAGE_ID = "1515871602074952";
    private final String AWESOME_THINGS_IN_INDIA = "1541529642777088";

    private final String FOURG_GIRL_PAGE_ID = "1755050324732167";
    private final String AWESOME_INDIAN_THINGS = "1714453775260964";

    private final String PRIYA_DUBE_ID = "100023324076991";

    private final String PAYALY_YADAV_ID = "100018675767618";

    private final String SWETHA_VERMA_ID = "100018589601343";

    private final String RICHA_PATEL_ID = "100018660498186";

    private final String KIRAN_SHINDE_ID ="100009362654286";

    private final UserCredentials[] userCredentials = new UserCredentials[]{
            new UserCredentials("anandparser@yahoo.com", "parsingparsing"),
            new UserCredentials("payalyadav90566@gmail.com", "dfkler&67"),
            new UserCredentials("swetav9856@gmail.com", "gfghjlk567"),
            new UserCredentials("richapatel475839@gmail.com", "dsd5kl34"),
            new UserCredentials("mazisangli@rediffmail.com","asdasdsdas")
           /* new UserCredentials("aartidube588@gmail.com", "sdfertfg234"),
            // new UserCredentials("Nehasharma4379@gmail.com","sdfertfgtr67"),
            new UserCredentials("poojaverma4356@gmail.com", "fkdsljf893"),
            new UserCredentials("payalsharma9985@gmail.com", "ewewe45fg"),
            new UserCredentials("rupaliagrwal93@outlook.com", "gfghjlk567"),
            new UserCredentials("swetav9856@gmail.com", "gfghjlk567")*/
    };

    private final String default_access_token = "1698410660455774|d47xxWu7HyFQt5G3MerQE-QIgwo";

    private final AccessToken[] access_tokens = new AccessToken[]{
            //4g girl
            new AccessToken("EAAYIsgwZAZAV4BAMsKBZCpsVWM3KhXHzw6QFgA5oZCttbvMkZB6umo3IcOM0xnDdZB7qtbZAblkRx1GjPf3AZCo1K0Qu7M4LSgbuboSksn6JQnmPCqTvdAlKTLPWJh7PnACpZB4OiO3UbGqB2klFStwoQRqQ7n4GjG2FxzOS6iWR4uBII3gf7bSZAu",
                    AccessToken.ID_TYPE.PAGE, FOURG_GIRL_PAGE_ID),
            new AccessToken("EAAYIsgwZAZAV4BAKj3jhGnho5F4TkOYPiNrPZBUi3E3UZB3naJYhKOvJ3dNZA1VbNWK1p7yceLCgbI7ah7n47zVjuTgZCx7HPx4TMaKhS768eZAUtW8GS1kNp91S64T5bqRazz8Ur7BWyCaE8j9gxLZC11gNEA05ZAbE1gz92b9KrUAZDZD",
                    AccessToken.ID_TYPE.PAGE, AWESOME_INDIAN_THINGS),

            //priya dube
            new AccessToken("EAAYIsgwZAZAV4BAAZBu1maenkTPRfGZC7w3pa9ZBnrFQUiivuQqf6P89Ifcmj9ADgpri8u8LeQQLzAEHFAZATZBezZCcJRD5PnHXSiRDiyEosxOsEzQNPTOAfXPJzrnWm8NZBFLVLMmJJ2A81pqbUXolqXsSHtPXFEwqLdZAHeGf95AZAchRsT4GzE7",
                    AccessToken.ID_TYPE.USER, PRIYA_DUBE_ID),

            new AccessToken("EAAYIsgwZAZAV4BAIxJz53G0gnQnJ7oivqaGQbAhREqvT5CNvwW23PrUtuYA7ZCVH5cms5u0A5iaNxN2LgQvDhb7CNZC0yAZC47KjfLW37w7zZBZAB8XhrybQXMhOj3WRrW6CK9iAaVzGCEsGV7M4Jv1Ays4FRKp8py5hvUIddYSwAZDZD",
                    AccessToken.ID_TYPE.USER, PAYALY_YADAV_ID),

            new AccessToken("EAAYIsgwZAZAV4BAATqOd4HOClZCnjUKDHZCAVcK01bgdruyxoGkY0PeTZAZAsRiHoKZA5cINVHuAJZAmvxWoftpv8Im5oFZB6TmSZBqObdZARU095FljHyb4uPuPcZAfqiG4D7oda4ZB21AdQ4ceh5m8hGajhQEywOwdyACVHWyA1GG3rqmZAr1IwOCB7B",
                    AccessToken.ID_TYPE.USER, SWETHA_VERMA_ID),

            new AccessToken("EAAYIsgwZAZAV4BAPGN58k1iApmZAvA5hH2XyikyIrOOWJ7q7aN6PSNcFLtKY83Co8LxhqO6VGPgnD2T8ENDlWSbaOKa872LmqaVMZAuFLOneccy6ZCPjdmZArSmBjmWvACERmTUbl8GkOipAhxpSsJtF9DT5ZAX709DQGJShM5xFvAgfZBQEhYY8",
                    AccessToken.ID_TYPE.USER, RICHA_PATEL_ID),
            //feb 2018
            new AccessToken("EAAbBb5ZAEIYIBAHjsz9mTyDoi2yRgj6aeNl5ApINPp9TdwwSZCrZCrdVB87VB4qSGDCCqvZAxrZB3DZCss2TA8fwfZAuYmdSZAhF4sbfTGHpeYtdo0gyC5I8R0LXpHrvusDHXbvy1KhB2lMZCrtReCn009DTHP4zVWgYNtI7d3nMbQQZDZD",
                    AccessToken.ID_TYPE.USER,KIRAN_SHINDE_ID)
            /*
            //Great Models
            new AccessToken("EAAYIsgwZAZAV4BAOCoGi19fxS9iYTq34rSZBBDUoSn9CUznElRZBUrKmxe2viIp8lIZAiZCcUtG0ZAgNcjvpGlRgZCkJZB7L00Y3ZCgZC1uX9zfDkrKa2UfUZCONWZCUaD0MaKFZBn1WjrZBVJKGXXzXNKCNgEz3CvjoZAQNa6qjIYlG5KJ3hf6D0CVDoZA5d",
                    AccessToken.ID_TYPE.PAGE)*/
    };

    private final String[] third_party_page_ids = new String[]{
            //Humourly yours
            "1899435196944780",
            //Sarcasm
            "1515871602074952",
            //Awesome things in India
            "1541529642777088"
    };

    private final String[] own_page_ids = new String[]{
            //4g girl
            "1755050324732167",
            //Awesome Indian Things
            "1714453775260964"
    };

    private final String to_be_promoted_page = own_page_ids[0];

    private final Map<String, String[]> pageofSameCategoryMap = new HashMap<String, String[]>() {
        {
            put(HUMORLY_YOURS_PAGE_ID, new String[]{FOURG_GIRL_PAGE_ID});
            put(SARCASM_PAGE_ID, new String[]{FOURG_GIRL_PAGE_ID});
            put(AWESOME_THINGS_IN_INDIA, new String[]{AWESOME_INDIAN_THINGS});
        }
    };


    @Override
    public UserCredentials[] getUserCredentials() {
        return userCredentials;
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
        return own_page_ids;
    }

    @Override
    public Map<String, String[]> getPageofSameCategoryMap() {
        return pageofSameCategoryMap;
    }
}
