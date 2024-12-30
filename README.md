### 权限认证
* 在user_service和order_service中的需要获取到用户身份的地方，在接口参数中使用@UserId注解获取usreId
* 在/api/**/auth/** 默认为需要鉴权->校验token的路径，没有auth的默认不需要鉴权，此时无需也不能使用@UserId注解
* GateWayFilter是需要配置到每个模块中的过滤器，用来校验请求是否经过了GateWay，通过校验一个长key
* swagger配置完成，有点鸡肋，从每个serivce中生成的接口测试不过网关，测不了
