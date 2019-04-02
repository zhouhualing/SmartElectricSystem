/**
 * 
 */
package com.harmazing.spms.base.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014-07-11
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /* (non-Javadoc)
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
 	return DataSourceContextHolder.getCustomerType();
    }

}
