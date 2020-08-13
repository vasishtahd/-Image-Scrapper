/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables;


import appengine.parser.mysqlmodels._6txkrsiwk3;
import appengine.parser.mysqlmodels.tables.records.OptimalRecord;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
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
public class Optimal extends TableImpl<OptimalRecord> {

    private static final long serialVersionUID = -1309336659;

    /**
     * The reference instance of <code>6txKRsiwk3.optimal</code>
     */
    public static final Optimal OPTIMAL = new Optimal();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OptimalRecord> getRecordType() {
        return OptimalRecord.class;
    }

    /**
     * The column <code>6txKRsiwk3.optimal.time</code>.
     */
    public final TableField<OptimalRecord, Timestamp> TIME = createField("time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.coin</code>.
     */
    public final TableField<OptimalRecord, String> COIN = createField("coin", org.jooq.impl.SQLDataType.VARCHAR.length(10), this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.buy_from</code>.
     */
    public final TableField<OptimalRecord, String> BUY_FROM = createField("buy_from", org.jooq.impl.SQLDataType.VARCHAR.length(20), this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.buy_for</code>.
     */
    public final TableField<OptimalRecord, Double> BUY_FOR = createField("buy_for", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.sell_at</code>.
     */
    public final TableField<OptimalRecord, String> SELL_AT = createField("sell_at", org.jooq.impl.SQLDataType.VARCHAR.length(10), this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.sell_for</code>.
     */
    public final TableField<OptimalRecord, Double> SELL_FOR = createField("sell_for", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>6txKRsiwk3.optimal.profit</code>.
     */
    public final TableField<OptimalRecord, Double> PROFIT = createField("profit", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * Create a <code>6txKRsiwk3.optimal</code> table reference
     */
    public Optimal() {
        this("optimal", null);
    }

    /**
     * Create an aliased <code>6txKRsiwk3.optimal</code> table reference
     */
    public Optimal(String alias) {
        this(alias, OPTIMAL);
    }

    private Optimal(String alias, Table<OptimalRecord> aliased) {
        this(alias, aliased, null);
    }

    private Optimal(String alias, Table<OptimalRecord> aliased, Field<?>[] parameters) {
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
    public Optimal as(String alias) {
        return new Optimal(alias, this);
    }

    /**
     * Rename this table
     */
    public Optimal rename(String name) {
        return new Optimal(name, null);
    }
}
