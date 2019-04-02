package com.harmazing.spms.portalmodules.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.portalmodules.dao.SpmsPortalModulesDAO;
import com.harmazing.spms.portalmodules.manager.SpmsPoratlModulesManager;

@Service
public class SpmsPortalModulesManagerImpl implements SpmsPoratlModulesManager{
	@Autowired
	private SpmsPortalModulesDAO spmsPortalModulesDAO;
}
