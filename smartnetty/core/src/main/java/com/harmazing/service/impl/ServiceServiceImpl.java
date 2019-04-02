package com.harmazing.service.impl;

import com.harmazing.entity.Device;
import com.harmazing.mapper.ServiceMapper;
import com.harmazing.service.DeviceService;
import com.harmazing.service.ServiceService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-5.
 */
public class ServiceServiceImpl extends ServiceImpl implements ServiceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceServiceImpl.class);

    @Override
    public List<Map> getUserAirconService(String userId) {
        List<String> list = getAirconServiceIdByUserId(userId);
        List<Map> mapList = null;
        if(list != null && list.size() > 0) {
            mapList = getAirconServiceByServiceIds(userId, list.toArray(new String[list.size()]));
        }
        return mapList;
    }

    @Override
    public List<String> getAirconServiceIdByUserId(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            ServiceMapper serviceMapper = session.getMapper(ServiceMapper.class);
            list = serviceMapper.selectAirconServiceIdByUserId(userId);
        } catch (Exception e) {
            LOGGER.info("", e);
        } finally {
            session.close();
            return list;
        }
    }

    @Override
    public List<String> getDeviceIdByAirconServiceId(String userId, String serviceId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            ServiceMapper serviceMapper = session.getMapper(ServiceMapper.class);
            list = serviceMapper.selectDeviceIdByAirconServiceId(userId, serviceId);
        } catch (Exception e) {
            LOGGER.info("", e);
        } finally {
            session.close();
            return list;
        }
    }

    @Override
    public Map getAirconServiceByServiceId(String userId, String serviceId) {
        SqlSession session = sqlSessionFactory.openSession();
        Map map = null;
        try {
            ServiceMapper serviceMapper = session.getMapper(ServiceMapper.class);
            map = serviceMapper.selectAirconServiceById(serviceId);
            List<String> deviceIds = serviceMapper.selectDeviceIdByAirconServiceId(userId, serviceId);
            map.put("deviceIds", deviceIds);
        } catch (Exception e) {
            LOGGER.info("", e);
        } finally {
            session.close();
            return map;
        }
    }

    @Override
    public List<Map> getAirconServiceByServiceIds(String userId, String[] ids) {
        SqlSession session = sqlSessionFactory.openSession();
        List<Map> mapList = null;
        try {
            ServiceMapper serviceMapper = session.getMapper(ServiceMapper.class);
            mapList = serviceMapper.selectAirconServiceByIds(ids);
            Iterator iterator = mapList.iterator();
            while (iterator.hasNext()) {
                Map map = (Map) iterator.next();
                List<String> deviceIds = serviceMapper.selectDeviceIdByAirconServiceId(userId, (String) map.get("id"));
                map.put("deviceIds", deviceIds);
                List<Device> devices = new ArrayList<Device>();
                Iterator<String> deviceIterator = deviceIds.iterator();
                DeviceService deviceService = new DeviceServiceImpl();
                while (deviceIterator.hasNext()) {
                    devices.add(deviceService.getDeviceById(deviceIterator.next()));
                }
                map.put("devices", devices);
            }
        } catch (Exception e) {
            LOGGER.info("", e);
        } finally {
            session.close();
            return mapList;
        }
    }

}
