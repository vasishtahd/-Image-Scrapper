package appengine.parser.instagram;

import appengine.parser.utils.DataBaseConnector;
import appengine.parser.utils.OtherUtils;
import com.sun.xml.internal.xsom.XSWildcard;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.jooq.DSLContext;
import org.jooq.Record1;

import java.io.IOException;

import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERS;

public class FetchUserInfo {


    public String fetch(){

        Instagram4j instagram4j =  login();
        for(int i=0;i<100;i++) {
            try {
                String user_name = getNotUpdatedUserName();
                getUserInformation(instagram4j, user_name);
                Thread.sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return "success";
    }

    private Instagram4j login(){
        Instagram4j instagram = Instagram4j.builder().username("beautifully_indian").password("jumpingjapong").build();
        instagram.setup();
        try {
            instagram.login();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instagram;
    }

    private void getUserInformation(Instagram4j instagram,String userName){
        InstagramSearchUsernameResult userResult = null;
        try {
            userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(userName));
            if(userResult.getUser()==null){
                skipAsUpdated(userName);
            } else {
                updateDB(userResult.getUser());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNotUpdatedUserName(){
        String followPageName = "h4ppyboys";
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<String> lastcursorRecord = dslContext.select(INSTAGRAMFOLLOWERS.USER_NAME).from(INSTAGRAMFOLLOWERS)
                .where(INSTAGRAMFOLLOWERS.PAGENAME.eq(followPageName).and(
                        INSTAGRAMFOLLOWERS.IS_UPDATED.eq(OtherUtils.boolToByte(false))
                )).fetchAny();

        return lastcursorRecord.value1();
    }

    private void updateDB(InstagramUser user){
        try {
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            dslContext.update(INSTAGRAMFOLLOWERS).
                    set(INSTAGRAMFOLLOWERS.FOLLOWER_COUNT, user.follower_count)
                    .set(INSTAGRAMFOLLOWERS.FOLLOWING_COUNT, user.following_count)
                    .set(INSTAGRAMFOLLOWERS.MEDIA_COUNT, user.media_count)
                    .set(INSTAGRAMFOLLOWERS.CITY_NAME, user.city_name)
                    .set(INSTAGRAMFOLLOWERS.IS_UPDATED,OtherUtils.boolToByte(true))
                    .set(INSTAGRAMFOLLOWERS.IS_PRIVATE, OtherUtils.boolToByte(user.is_private))
                    .where(INSTAGRAMFOLLOWERS.USER_NAME.eq(user.username))
                    .execute();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void skipAsUpdated(String userName){
        try {
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            dslContext.update(INSTAGRAMFOLLOWERS)
                    .set(INSTAGRAMFOLLOWERS.IS_UPDATED,OtherUtils.boolToByte(true))
                    .where(INSTAGRAMFOLLOWERS.USER_NAME.eq(userName))
                    .execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}


