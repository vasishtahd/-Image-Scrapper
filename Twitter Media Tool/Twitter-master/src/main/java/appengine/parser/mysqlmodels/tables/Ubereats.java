/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables;


import appengine.parser.mysqlmodels.Keys;
import appengine.parser.mysqlmodels._6txkrsiwk3;
import appengine.parser.mysqlmodels.tables.records.UbereatsRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class Ubereats extends TableImpl<UbereatsRecord> {

    private static final long serialVersionUID = -977428072;

    /**
     * The reference instance of <code>6txKRsiwk3.ubereats</code>
     */
    public static final Ubereats UBEREATS = new Ubereats();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UbereatsRecord> getRecordType() {
        return UbereatsRecord.class;
    }

    /**
     * The column <code>6txKRsiwk3.ubereats.uuid</code>.
     */
    public final TableField<UbereatsRecord, String> UUID = createField("uuid", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.title</code>.
     */
    public final TableField<UbereatsRecord, String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.item_description</code>.
     */
    public final TableField<UbereatsRecord, String> ITEM_DESCRIPTION = createField("item_description", org.jooq.impl.SQLDataType.VARCHAR.length(500), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.image_url</code>.
     */
    public final TableField<UbereatsRecord, String> IMAGE_URL = createField("image_url", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.query</code>.
     */
    public final TableField<UbereatsRecord, String> QUERY = createField("query", org.jooq.impl.SQLDataType.VARCHAR.length(20), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.city_id</code>.
     */
    public final TableField<UbereatsRecord, Integer> CITY_ID = createField("city_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.store_uuid</code>.
     */
    public final TableField<UbereatsRecord, String> STORE_UUID = createField("store_uuid", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

    /**
     * The column <code>6txKRsiwk3.ubereats.id</code>.
     */
    public final TableField<UbereatsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>6txKRsiwk3.ubereats</code> table reference
     */
    public Ubereats() {
        this("ubereats", null);
    }

    /**
     * Create an aliased <code>6txKRsiwk3.ubereats</code> table reference
     */
    public Ubereats(String alias) {
        this(alias, UBEREATS);
    }

    private Ubereats(String alias, Table<UbereatsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Ubereats(String alias, Table<UbereatsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return _6txkrsiwk3._6TXKRSIWK3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<UbereatsRecord, Integer> getIdentity() {
        return Keys.IDENTITY_UBEREATS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UbereatsRecord> getPrimaryKey() {
        return Keys.KEY_UBEREATS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UbereatsRecord>> getKeys() {
        return Arrays.<UniqueKey<UbereatsRecord>>asList(Keys.KEY_UBEREATS_UBEREATS_UUID_PK, Keys.KEY_UBEREATS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ubereats as(String alias) {
        return new Ubereats(alias, this);
    }

    /**
     * Rename this table
     */
    public Ubereats rename(String name) {
        return new Ubereats(name, null);
    }
}