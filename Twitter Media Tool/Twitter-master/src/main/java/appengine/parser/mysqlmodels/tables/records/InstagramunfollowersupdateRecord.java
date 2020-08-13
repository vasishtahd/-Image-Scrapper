/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.Instagramunfollowersupdate;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class InstagramunfollowersupdateRecord extends TableRecordImpl<InstagramunfollowersupdateRecord> implements Record3<String, String, Integer> {

    private static final long serialVersionUID = 549252418;

    /**
     * Setter for <code>6txKRsiwk3.instagramunfollowersupdate.pagename</code>.
     */
    public void setPagename(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramunfollowersupdate.pagename</code>.
     */
    public String getPagename() {
        return (String) get(0);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramunfollowersupdate.from_user_name</code>.
     */
    public void setFromUserName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramunfollowersupdate.from_user_name</code>.
     */
    public String getFromUserName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>6txKRsiwk3.instagramunfollowersupdate.followed_till</code>.
     */
    public void setFollowedTill(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.instagramunfollowersupdate.followed_till</code>.
     */
    public Integer getFollowedTill() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<String, String, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<String, String, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Instagramunfollowersupdate.INSTAGRAMUNFOLLOWERSUPDATE.PAGENAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Instagramunfollowersupdate.INSTAGRAMUNFOLLOWERSUPDATE.FROM_USER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return Instagramunfollowersupdate.INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL;
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
        return getFromUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getFollowedTill();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramunfollowersupdateRecord value1(String value) {
        setPagename(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramunfollowersupdateRecord value2(String value) {
        setFromUserName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramunfollowersupdateRecord value3(Integer value) {
        setFollowedTill(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstagramunfollowersupdateRecord values(String value1, String value2, Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InstagramunfollowersupdateRecord
     */
    public InstagramunfollowersupdateRecord() {
        super(Instagramunfollowersupdate.INSTAGRAMUNFOLLOWERSUPDATE);
    }

    /**
     * Create a detached, initialised InstagramunfollowersupdateRecord
     */
    public InstagramunfollowersupdateRecord(String pagename, String fromUserName, Integer followedTill) {
        super(Instagramunfollowersupdate.INSTAGRAMUNFOLLOWERSUPDATE);

        set(0, pagename);
        set(1, fromUserName);
        set(2, followedTill);
    }
}
