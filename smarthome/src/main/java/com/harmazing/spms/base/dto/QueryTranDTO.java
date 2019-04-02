package com.harmazing.spms.base.dto;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.common.dto.IDTO;
import com.harmazing.spms.common.entity.IEntity;

public class QueryTranDTO implements IDTO {

    /**
     * queryId
     */
    private String queryId;

    /**
     * 要查询的class全名
     */
    private String baseClass = "";

    /**
     * 总数量
     */
    private Long allCount = 0L;

    /**
     * 开始的编号
     */
    private Integer beginNum = 0;

    /**
     * 每页最大数量
     */
    private Integer pageMaxCount = 0;

    /**
     * 当前的页数
     */
    private Integer nowPage = 0;

    /**
     * 获得Spring Data DAO
     */
    private SpringDataDAO<IEntity> springDataDAO = null;

    /**
     * QueryDTO 对应缓存中的dto
     */
    private QueryDTO queryDTO = new QueryDTO();

    /**
     * 执行查询的条件
     */
    private Pageable pageable;

    /**
     * 取得的结果集
     */
    private Page<IEntity> page;

    /**
     * resultMaps
     */
    private List<Map<String, Object>> customDatas;

    /**
     * 查询过滤filter
     */
    private List<SearchFilter> searchFilters;

    private List<Map<String, Object>> customerSearchFilter;

    /**
     * 显示的列
     */
    private List<ColumnDTO> showColumnDTOs;

    /**
     * 实体id
     */
    private String entityId;

    /**
     * 字典dto
     */
    private Map<String, List<DictDTO>> dictMap;

    /**
     * columnNames
     */
    private List<String> columnNames;

    private String countSql;

    private String sourceSql;

    private Map<String, Object> returnInfos;

    public Long getAllCount() {
        return allCount;
    }

    public QueryDTO getQueryDTO() {
        return queryDTO;
    }

    public String getCountSql() {
        return countSql;
    }

    public List<Map<String, Object>> getCustomerSearchFilter() {
        return customerSearchFilter;
    }

    public void setCustomerSearchFilter(
            List<Map<String, Object>> customerSearchFilter) {
        this.customerSearchFilter = customerSearchFilter;
    }

    public Map<String, Object> getReturnInfos() {
        return returnInfos;
    }

    public void setReturnInfos(Map<String, Object> returnInfos) {
        this.returnInfos = returnInfos;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setQueryDTO(QueryDTO queryDTO) {
        this.queryDTO = queryDTO;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public Map<String, List<DictDTO>> getDictMap() {
        return dictMap;
    }

    public void setDictMap(Map<String, List<DictDTO>> dictMap) {
        this.dictMap = dictMap;
    }

    public Page<IEntity> getPage() {
        return page;
    }

    public void setPage(Page<IEntity> page) {
        this.page = page;
    }

    public List<ColumnDTO> getShowColumnDTOs() {
        return showColumnDTOs;
    }

    public List<Map<String, Object>> getCustomDatas() {
        return customDatas;
    }

    public void setCustomDatas(List<Map<String, Object>> customDatas) {
        this.customDatas = customDatas;
    }

    public void setShowColumnDTOs(List<ColumnDTO> showColumnDTOs) {
        this.showColumnDTOs = showColumnDTOs;
    }

    public Integer getBeginNum() {
        return beginNum;
    }

    public void setBeginNum(Integer beginNum) {
        this.beginNum = beginNum;
    }

    public Integer getPageMaxCount() {
        return pageMaxCount;
    }

    public void setPageMaxCount(Integer pageMaxCount) {
        this.pageMaxCount = pageMaxCount;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public SpringDataDAO<IEntity> getSpringDataDAO() {
        return springDataDAO;
    }

    public void setSpringDataDAO(SpringDataDAO<IEntity> springDataDAO) {
        this.springDataDAO = springDataDAO;
    }

    public String getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(List<SearchFilter> searchFilters) {
        this.searchFilters = searchFilters;
    }


}