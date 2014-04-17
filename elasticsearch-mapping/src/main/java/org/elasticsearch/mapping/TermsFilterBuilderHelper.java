package org.elasticsearch.mapping;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Build a term filter.
 * 
 * @author luc boutier
 */
public class TermsFilterBuilderHelper extends AbstractFilterBuilderHelper implements IFilterBuilderHelper {

    /**
     * Initialize the helper to build term filters.
     * 
     * @param nestedPath The path to the nested object if any.
     * @param filterPath The path to the field to filter.
     */
    public TermsFilterBuilderHelper(final String nestedPath, final String filterPath) {
        super(nestedPath, filterPath);
    }

    @Override
    public String getEsFieldName() {
        return getFullPath();
    }

    @Override
    public FilterBuilder buildFilter(final String key, final String[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Filter values cannot be null or empty");
        }
        if (getNestedPath() == null) {
            return buildTermFilter(key, values);
        }
        return FilterBuilders.nestedFilter(getNestedPath(), buildTermFilter(getFilterPath(), values));
    }

    private FilterBuilder buildTermFilter(final String key, final String[] values) {
        if (values.length == 1) {
            return FilterBuilders.termFilter(key, values[0]);
        } else {
            return FilterBuilders.inFilter(key, values);
        }
    }

    @Override
    public QueryBuilder buildQuery(String key, String[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Filter values cannot be null or empty");
        }
        if (getNestedPath() == null) {
            return buildTermQuery(key, values);
        }
        return QueryBuilders.nestedQuery(getNestedPath(), buildTermQuery(getFilterPath(), values));
    }

    private QueryBuilder buildTermQuery(final String key, final String[] values) {
        if (values.length == 1) {
            return QueryBuilders.termQuery(key, values[0]);
        } else {
            return QueryBuilders.inQuery(key, values);
        }
    }
}