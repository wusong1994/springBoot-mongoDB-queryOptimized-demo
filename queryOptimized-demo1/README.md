# springboot Demo
1. springboot整合通用mapper插件、XML以及注解方式，实现查询分页等

2. 自定义日志输出

3. 多配置环境切换，maven打包动态指定配置环境

4. 接口(无论异常)统一格式输出

5. 整合mongoDB，采用非传统方式（skip + limit）进行分页查询设计，优化查询方式

## mongoDB查询优化设计描述
1. 大数据量的情况下采用 skip + limit 的方式进行查询，越往后，需要跳过越多的页，查询也会越来越慢；可以采用合理设计主键的方式，不用额外创建索引，分页查询后放回下一页的查询起点，这样边查询边缩小查询范围，越往后查询效率反而更理想。
2. 主键设计规则： _id = 设备ID + "." + 上报时间（时间戳整数，秒为单位）。
注意事项：根据实际场景，如果时间跨度很大，秒的位数不一样，则注意前面补0，毕竟查询的时候是按字符串比较的方式；否则会出现查询时间比较不对（前面的时间大于后面的 例如： "12" < "5"）。
3. 上述是根据物联设备上报不规则、不确定的属性信息，而采用的通用mongoDB文档存储方式。
4. 除了主键设计规则外，项目也可以改造成通过额外创建索引的方式进行设计，即通过 nodeId(设备ID)、上报时间字段 创建组合索引的方式。 代码中的具体逻辑只有小调整（将根据主键，改为根据设备ID、上报时间字段为主，按上报时间排序，不断缩小上报时间的查询范围）即可，依然可以根据上报时间对某个设备ID上报的数据进行上述分页查询。
5. 具体代码可以看controller中的通用接口编写调用即可。

 ## 接口编写（调用详见：/doc/images下 postman测试）
 1. 通用接口：根据dcp节点、集合获取最新上报记录 /dcpNodeReportedInfo/latest
 2. 通用接口：根据dcp节点、集合分页获取节点上报 /dcpNodeReportedInfo/page
 3. 通用聚合接口：根据dcp节点、集合获取属性最高、最低、均值 /dcpNodeReportedInfo/aggregateData
 4. 上报存储方法，service服务层： putMapObj(Map<String, Object> saveMap, String collectionName);
 (即 存储Map对象到mongoDB集合中： _id = nodeId + "." + 时间（秒）)

 ## 搭建步骤
 1. 执行document/db下的sql脚本，建立mysql数据库(支持mysql5.7以上)、安装mongoDB文档数据库4.4+  （本项目当时使用到的是4.4.3）
 2. 执行test目录下的测试单元
 
 ## 根据不同环境进行打包
例如 测试环境：
mvn clean package -Ptest -Dmaven.test.skip=true
