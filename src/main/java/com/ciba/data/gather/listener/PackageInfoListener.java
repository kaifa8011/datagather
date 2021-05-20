package com.ciba.data.gather.listener;


import com.ciba.data.synchronize.entity.CustomPackageInfo;

import java.util.List;

/**
 * @author songzi
 * @date 2021/4/30
 */
public interface PackageInfoListener {
     void onPackgeInfoFinished(List<CustomPackageInfo> packageInfos);
}
