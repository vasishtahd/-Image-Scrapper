package appengine.parser.optimal.objects;

import java.util.List;
import java.util.Set;

/**
 * Created by anand.kurapati on 09/01/18.
 */
public interface MarketDependentUtil extends MarketUtil{
    void setCoinsToBeFetched(Set<String> coins);
}
