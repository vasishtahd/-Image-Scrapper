/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.Swiggyevents;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class SwiggyeventsRecord extends UpdatableRecordImpl<SwiggyeventsRecord> implements Record3<Integer, String, String> {

    private static final long serialVersionUID = -927192612;

    /**
     * Setter for <code>6txKRsiwk3.swiggyevents.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.swiggyevents.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>6txKRsiwk3.swiggyevents.json</code>.
     */
    public void setJson(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.swiggyevents.json</code>.
     */
    public String getJson() {
        return (String) get(1);
    }

    /**
     * Setter for <code>6txKRsiwk3.swiggyevents.device_id</code>.
     */
    public void setDeviceId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>6txKRsiwk3.swiggyevents.device_id</code>.
     */
    public String getDeviceId() {
        return (String) get(2);
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
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Swiggyevents.SWIGGYEVENTS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Swiggyevents.SWIGGYEVENTS.JSON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Swiggyevents.SWIGGYEVENTS.DEVICE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getDeviceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SwiggyeventsRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SwiggyeventsRecord value2(String value) {
        setJson(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SwiggyeventsRecord value3(String value) {
        setDeviceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SwiggyeventsRecord values(Integer value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SwiggyeventsRecord
     */
    public SwiggyeventsRecord() {
        super(Swiggyevents.SWIGGYEVENTS);
    }

    /**
     * Create a detached, initialised SwiggyeventsRecord
     */
    public SwiggyeventsRecord(Integer id, String json, String deviceId) {
        super(Swiggyevents.SWIGGYEVENTS);

        set(0, id);
        set(1, json);
        set(2, deviceId);
    }
}