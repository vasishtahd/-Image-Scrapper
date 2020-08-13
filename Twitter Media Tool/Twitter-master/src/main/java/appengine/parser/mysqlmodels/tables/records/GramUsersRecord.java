/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.GramUsers;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


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
public class GramUsersRecord extends TableRecordImpl<GramUsersRecord> implements Record6<String, String, Integer, Integer, Integer, Integer> {

    private static final long serialVersionUID = -844818918;

    /**
     * Setter for <code>6txKRsiwk3.gram_users.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>6txKRsiwk3.gram_users.username</code>.
     */
    public void setUsername(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.username</code>.
     */
    public String getUsername() {
        return (String) get(1);
    }

    /**
     * Setter for <code>6txKRsiwk3.gram_users.mediacount</code>.
     */
    public void setMediacount(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.mediacount</code>.
     */
    public Integer getMediacount() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>6txKRsiwk3.gram_users.followercount</code>.
     */
    public void setFollowercount(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.followercount</code>.
     */
    public Integer getFollowercount() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>6txKRsiwk3.gram_users.followingcount</code>.
     */
    public void setFollowingcount(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.followingcount</code>.
     */
    public Integer getFollowingcount() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>6txKRsiwk3.gram_users.credittake</code>.
     */
    public void setCredittake(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.gram_users.credittake</code>.
     */
    public Integer getCredittake() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, String, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, String, Integer, Integer, Integer, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return GramUsers.GRAM_USERS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return GramUsers.GRAM_USERS.USERNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return GramUsers.GRAM_USERS.MEDIACOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return GramUsers.GRAM_USERS.FOLLOWERCOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return GramUsers.GRAM_USERS.FOLLOWINGCOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return GramUsers.GRAM_USERS.CREDITTAKE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getMediacount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getFollowercount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getFollowingcount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getCredittake();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value1(String value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value2(String value) {
        setUsername(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value3(Integer value) {
        setMediacount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value4(Integer value) {
        setFollowercount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value5(Integer value) {
        setFollowingcount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord value6(Integer value) {
        setCredittake(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GramUsersRecord values(String value1, String value2, Integer value3, Integer value4, Integer value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GramUsersRecord
     */
    public GramUsersRecord() {
        super(GramUsers.GRAM_USERS);
    }

    /**
     * Create a detached, initialised GramUsersRecord
     */
    public GramUsersRecord(String id, String username, Integer mediacount, Integer followercount, Integer followingcount, Integer credittake) {
        super(GramUsers.GRAM_USERS);

        set(0, id);
        set(1, username);
        set(2, mediacount);
        set(3, followercount);
        set(4, followingcount);
        set(5, credittake);
    }
}
