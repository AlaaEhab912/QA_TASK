package org.framework.restFramework.constants;

import org.framework.utils.ConfigManager;

public interface ConstantData
{
    String EXCEL_PATH = ConfigManager.getInstance().getString("excelPath");
    String apiBase = ConfigManager.getInstance().getString("apiBaseUrl");
    String CREATE_USER = apiBase+ "/users";
    String UPDATE_USER = apiBase+ "/users/{idNumber}";
    String RETRIEVE_USER = apiBase+ "/users/{idNumber}";
}
