# Datagather

Datagather 是一个较全面的Android设备信息收集库，可以帮助开发者快速集成设备信息收集功能。



# 怎么使用

* Step1：Gradle中添加依赖：

```
dependencies {
    compile 'com.ciba:datagather:0.1.6'
}
```

* Step2：在Application中进行初始化

```
DataGatherManager.getInstance().init(this);
```

* Step3：设备信息获取

```
// 获取APP版本号等
String versionName = PackageUtil.getVersionName();
```

