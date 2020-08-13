/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.Instagramfollowers;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record15;
import org.jooq.Row15;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InstagramfollowersRecord extends UpdatableRecordImpl<InstagramfollowersRecord> implements Record15<String, String, String, Byte, Byte, Byte, String, Integer, Timestamp, Byte, Integer, Integer, Integer, String, Byte> {

    private static final long serialVersionUID = 382111223;

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.pagename</code>.
     */
    public void setPagename(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.pagename</code>.
     */
    public String getPagename() {
        return (String) get(0);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.user_id</code>.
     */
    public void setUserId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.user_id</code>.
     */
    public String getUserId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.user_name</code>.
     */
    public void setUserName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.user_name</code>.
     */
    public String getUserName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.is_verified</code>.
     */
    public void setIsVerified(Byte value) {
        set(3, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.is_verified</code>.
     */
    public Byte getIsVerified() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.followed_by_viewer</code>.
     */
    public void setFollowedByViewer(Byte value) {
        set(4, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.followed_by_viewer</code>.
     */
    public Byte getFollowedByViewer() {
        return (Byte) get(4);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.requested_by_viewer</code>.
     */
    public void setRequestedByViewer(Byte value) {
        set(5, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.requested_by_viewer</code>.
     */
    public Byte getRequestedByViewer() {
        return (Byte) get(5);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.from_user_name</code>.
     */
    public void setFromUserName(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.from_user_name</code>.
     */
    public String getFromUserName() {
        return (String) get(6);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.id</code>.
     */
    public void setId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.id</code>.
     */
    public Integer getId() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.requested_on</code>.
     */
    public void setRequestedOn(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.requested_on</code>.
     */
    public Timestamp getRequestedOn() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.is_private</code>.
     */
    public void setIsPrivate(Byte value) {
        set(9, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.is_private</code>.
     */
    public Byte getIsPrivate() {
        return (Byte) get(9);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.follower_count</code>.
     */
    public void setFollowerCount(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.follower_count</code>.
     */
    public Integer getFollowerCount() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.following_count</code>.
     */
    public void setFollowingCount(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.following_count</code>.
     */
    public Integer getFollowingCount() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.media_count</code>.
     */
    public void setMediaCount(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.media_count</code>.
     */
    public Integer getMediaCount() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.city_name</code>.
     */
    public void setCityName(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.city_name</code>.
     */
    public String getCityName() {
        return (String) get(13);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramfollowers.is_updated</code>.
     */
    public void setIsUpdated(Byte value) {
        set(14, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramfollowers.is_updated</code>.
     */
    public Byte getIsUpdated() {
        return (Byte) get(14);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record15 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<String, String, String, Byte, Byte, Byte, String, Integer, Timestamp, Byte, Integer, Integer, Integer, String, Byte> fieldsRow() {
        return (Row15) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<String, String, String, Byte, Byte, Byte, String, Integer, Timestamp, Byte, Integer, Integer, Integer, String, Byte> valuesRow() {
        return (Row15) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.PAGENAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.USER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field4() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.IS_VERIFIED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field5() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.FOLLOWED_BY_VIEWER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field6() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.REQUESTED_BY_VIEWER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.FROM_USER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.REQUESTED_ON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field10() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.IS_PRIVATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.FOLLOWER_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.FOLLOWING_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.MEDIA_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.CITY_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field15() {
        return Instagramfollowers.INSTAGRAMFOLLOWERS.IS_UPDATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getPagename();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value4() {
        return getIsVerified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value5() {
        return getFollowedByViewer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value6() {
        return getRequestedByViewer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getFromUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getRequestedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value10() {
        return getIsPrivate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getFollowerCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getFollowingCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getMediaCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getCityName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value15() {
        return getIsUpdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value1(String value) {
        setPagename(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value2(String value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value3(String value) {
        setUserName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value4(Byte value) {
        setIsVerified(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value5(Byte value) {
        setFollowedByViewer(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value6(Byte value) {
        setRequestedByViewer(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value7(String value) {
        setFromUserName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value8(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value9(Timestamp value) {
        setRequestedOn(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value10(Byte value) {
        setIsPrivate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value11(Integer value) {
        setFollowerCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value12(Integer value) {
        setFollowingCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value13(Integer value) {
        setMediaCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value14(String value) {
        setCityName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord value15(Byte value) {
        setIsUpdated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramfollowersRecord values(String value1, String value2, String value3, Byte value4, Byte value5, Byte value6, String value7, Integer value8, Timestamp value9, Byte value10, Integer value11, Integer value12, Integer value13, String value14, Byte value15) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InstagramfollowersRecord
     */
    public InstagramfollowersRecord() {
        super(Instagramfollowers.INSTAGRAMFOLLOWERS);
    }

    /**
     * Create a detached, initialised InstagramfollowersRecord
     */
    public InstagramfollowersRecord(String pagename, String userId, String userName, Byte isVerified, Byte followedByViewer, Byte requestedByViewer, String fromUserName, Integer id, Timestamp requestedOn, Byte isPrivate, Integer followerCount, Integer followingCount, Integer mediaCount, String cityName, Byte isUpdated) {
        super(Instagramfollowers.INSTAGRAMFOLLOWERS);

        set(0, pagename);
        set(1, userId);
        set(2, userName);
        set(3, isVerified);
        set(4, followedByViewer);
        set(5, requestedByViewer);
        set(6, fromUserName);
        set(7, id);
        set(8, requestedOn);
        set(9, isPrivate);
        set(10, followerCount);
        set(11, followingCount);
        set(12, mediaCount);
        set(13, cityName);
        set(14, isUpdated);
    }
}