# 1 setXIncludeAware is not supported on this JAXP implementation

https://github.com/spring-projects/spring-boot/issues/5035

原因: 类路径中有xerces的旧版本, 移除旧版本, 使用hadoop提供的版本.
