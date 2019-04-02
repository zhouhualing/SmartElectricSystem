///**
// * 
// */
//package com.harmazing.spms.product.manager;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.Map;
//
//import com.harmazing.spms.base.dto.QueryTranDTO;
//import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
//import org.springframework.transaction.annotation.Transactional;
//import com.harmazing.spms.common.manager.IManager;
//import com.harmazing.spms.device.dto.SpmsDeviceDTO;
//import com.harmazing.spms.product.dto.SpmsProductDTO;
//import com.harmazing.spms.product.entity.SpmsProduct;
//
///**
// * describe:
// * @author TanFan
// * 产品 服务接口类
// * since 2014年12月31日
// */
//@Transactional
//public interface SpmsProductManager extends IManager {
//    
//    /**
//     * save Product
//     * @param SpmsProductDTO
//     * @return SpmsProductDTO
//     */
//	
//    public SpmsProductDTO doSave(SpmsProductDTO spmsProductDTO);
//    /**
//     * query Product
//     * @param id
//     * @return SpmsProductDTO
//     */
//	public SpmsProductDTO doQuery(String s) ;
//	//删除方法
//	public Boolean doDelete(String string);
//	//修改方法
//	public SpmsProductDTO doSaveDL(SpmsProductDTO spmsProductDTO);
//	//批量删除方法
//	public Boolean doDeleteAll(int[] data1);
//	//根据用户ID获取所有产品
//	public List<SpmsProductDTO> findProductByUserId(String userId);
//	//根据产品获取所有设备
//	public List<SpmsDeviceDTO> findDeviceByProductId();
//	
//	public Map<String,Object> getAllProducttype();
//	
//	public Map<String, Object> getCountAmount(String type, String start,String end);
//	
//	public Map<String, Object> getCostEarnings(String type, String start,String end)  throws ParseException;
//	
//	public Map<String,Object> getOldEarnings(String type, String start,String end)  throws ParseException;
//
//	/**
//	 * 根据工单信息获取产品信息
//	 * @param info
//	 * @return List <SpmsProductDTO>
//	 */
//	public Map<String, Object> getProductInfoByWorkOrder(Map<String, Object> info);
//
//	/**
//	 * 给订户绑定网关
//	 * 给指定产品绑定设备
//	 * @param spmsProductDTO
//	 * @return SpmsProductDTO
//	 * @throws Exception 
//	 * @throws IOException 
//	 */
//	public SpmsProductDTO bindDeviceToProduct(SpmsProductDTO spmsProductDTO) throws Exception ;
//	
//	
//	public List<SpmsDeviceDTO> getProductDeviceList(String id);
//
//	/**
//	 * 获取统计列表
//	 * @param queryTranDTO
//	 * @return
//	 */
//	public QueryTranDTO getProductCont(QueryTranDTO queryTranDTO) throws ParseException;
//	
//	/**
//	 * 检查设备、网关
//	 * @param spmsProductDTO
//	 * @return
//	 */
//	public Map<String, Object> checkGWandDevice(SpmsProductDTO spmsProductDTO);
//	
//	/**
//	 * 获取产品列表
//	 * @param spmsProductDTO
//	 * @return
//	 */
//	public List<SpmsProduct> getProductList(SpmsProductDTO spmsProductDTO);
//}
