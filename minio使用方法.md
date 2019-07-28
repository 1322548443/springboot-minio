# Spring Boot 2 整合Minio

## 整合方式

1. **在项目根目录的pom.xml文件中导入minio的依赖包**
   ```xml
     <dependency>
      <groupId>io.minio</groupId>
      <artifactId>minio</artifactId>
      <version>3.0.10</version>
     </dependency>
   ```
2. **添加minio的配置参数**
- 在application.propeties配置文件中
     ```java
       spring.minio.uri=http://192.168.1.109:9000
       spring.minio.accessKey=WORKDAYANDNIGHT
       spring.minio.secretKey=WORKDAYBYDAY
    ```
    
 - 在application.yaml配置文件中
    
    ```yaml
     spring: 
       minio:
         - uri: http://192.168.1.109:9000
         - accessKey=WORKDAYANDNIGHT
         - secretKey=WORKDAYBYDAY
   ```

3. **编写配置类MinioConfig.java**
   ```java
    @Configuration
    public class MinioConfig{
        //从配置文件中加载Minio的相关配置
        @Value("${spring.minio.uri}")
        private  String  minioUrl;
        
        @Value("${spring.minio.accessKey}")
        private  String  access;
        
        @Value("${spring.minio.secretKey}")
        private  String  secret;
        
        // 连接Minio服务器，将连接成功后的对象交于框架管理
        @Bean
        public MinioClient minioClient(){
            try {
                // 最后一个false表示不使用SSL连接
                return  new  MinioClient(minioUrl,  access,  secret,  false);
            }catch  (InvalidEndpointException  |  InvalidPortException  e)  {
                e.printStackTrace();
            }
            return null;
        }
    }
   ```

4. **编写静态工具类MinioUtils.java**
   ```java
     @Component
     public class MinioUtils {
     
         // 注入上一步生成的连接对象
         @Autowired
         private MiniClient miniClient;
         
         // 创建一个静态工具类对象
         private static MinioUtils minioUtils;
         
         // 写一个初始化函数，将连接对象赋值为静态工具类对象的一个属性
         @PostConstruct
         public void init(){
             minioUtils = this;
             minioUtils.minioClient = this.minioClient;
         }
         // 经过以上步骤，我们已经可以使用Minio自带的函数
         // 我们可以将原生的函数再封装成一个静态函数，方便在其他地方调用
         // 举个栗子
         public static List<Bucket> getBucketList() {
             try {
                 // listBuckets函数的功能为获取所有的bucket并返回一个列表
                 return minioUtils.minioClient.listBuckets();
             }catch(Exception e) {
                 e.printStackTrace();
             }
             return null;
         }
         ...
     }
   ```

## Minio原生函数接口文档（[文档地址](https://docs.min.io/cn/java-client-api-reference.html)）
### 1. 储存桶操作

**==public void makeBucket(String bucetkName)==**
创建一个新的存储桶

| 参数      | 类型    |  描述 |
| :--------- | :-------- | :----- |
| BucketName    | String  | 储存桶的名称 |

| 返回值类型   |  描述 |
| :--------- | :-----: |
| 无   | 该函数无返回值 |

**==public List<Bucket> listBuckets()==**
列出所有存储桶

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
| 无| void| 该函数无参数 |

| 返回值类型   |  描述 |
| :--------- | :-----: |
| List<Bucket> | 返回Bucket类型的列表，列表中为数据库中所有存储桶的名字 |

**==boolean bucketExists(String bucketName)==**
检查存储桶是否存在

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
| bucketName| String| 存储桶的名字 |

| 返回值类型   |  描述 |
| :--------- | :-----: |
| boolean | 该存储桶存在返回true，不存在返回false |

**==void removeBucket(String bucketName)==**
删除一个存储桶
_注意：使用该方法不会删除存储桶的存储的对象，你需要通过removeObject函数删除其中的对象_

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
| bucketName| String| 存储桶的名字 |

| 返回值类型   |  描述 |
| :--------- | :-----: |
| void | 该函数无返回值 |

### 2. 对象操作
**==Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive, boolean useVersion1)==**
列出某个存储桶中的所有对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
| bucketName| String| 存储桶的名字 |
|prefix|String|可选参数，表示对象名称的前缀|
|recursive|boolean|可选参数，true使用递归查找，false模拟文件夹结构查找|
|useVersion1|boolean|可选参数，true使用版本1的REST API|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| Iterable<Result<Item>>| 返回Item类型的结果项迭代器 |

**==Iterable<Result<Upload>> listIncompleteUploads(String bucketName, String prefix, boolean recursive)==**
列出某储存桶中还未完全上传的对象(对象太大，需要多次上传时)

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
| bucketName| String| 存储桶的名字 |
|prefix|String|可选参数，表示对象名称的前缀|
|recursive|boolean|可选参数，true使用递归查找，false模拟文件夹结构查找|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| Iterable<Result<Upload>>| 返回Upload类型的结果项迭代器 |

**==InputStream getObject(String bucketName, String objectName, long offset)==**
以流的形式，从指定的偏移量开始下载一个对象(记得要关闭流)

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|offset|long|可选参数，数据偏移量|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| InputStream| 包含对象数据的输入流 |

**==InputStream getObject(String bucketName, String objectName, long offset, Long length)==**
以流的形式，从指定的偏移量开始下载指定长度的一段数据(断点续传功能)

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|offset|long|可选参数，数据偏移量|
|length|long|可选参数，读取数据的长度，无值默认读到文件尾|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| InputStream| 包含对象数据的输入流 |

**==void getObject(String bucketName, String objectName, String fileName)==**
以流的形式，从指定的偏移量开始下载指定长度的一段数据(断点续传功能)

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|fileName|String|下载后的文件名|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| void | 该函数无返回类型 |

**==void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType)==**
通过InputStream上传对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|stream|InputStream|要上传的流，即你要将上传的数据转换为流|
|size|long|要上传的流的长度|
|contentType|String|HTTP中的Content-Type|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| void | 该函数无返回类型 |

**==void putObject(String bucketName, String objectName, String fileName)==**
通过InputStream上传对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|fileName|String|要上传的文件，如 “/opt/a.jpg”|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| void | 该函数无返回类型 |

**==ObjectStat statObject(String bucketName, String objectName)==**
获取对象的元数据
| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| ObjectStat| 对象的元数据信息 |

**==void removeObject(String bucketName, String objectName)==**
删除某个储存桶中的一个对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| void| 该函数无返回值 |

**==Iterable<Result<DeleteError>> removeObject(String bucketName, Iterable<String> objectNames)==**
删除某个存储桶中的多个对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName|String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|objectNames|Iterable<String>|含有多个要删除对象的名称的迭代器|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| Iterable<Result<DeleteError>>| 返回DeleteError类型的结果项的迭代器 |

**==void removeIncompleteUpload(String bucketName, String objectName)==**
删除一个未完整上传的对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName|String| 存储桶的名称 |
|objectName|String|存储桶里对象的名称|

| 返回值类型   |  描述 |
| :--------- | :-----: |
|void|该函数无返回值|

**==void copyObject(String bucketName, String objectName, String destBucketName, String destObjectName, CopyConditions cpConds, Map<String, String> metadata)==**
将源对象复制到所提供的destbucket中具有所提供名称的destObject中，也可以依据cpConds中提供的键值对尝试复制对象

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String|源存储桶的名称 |
|objectName|String|源存储桶里对象的名称|
|destBucketName|String|目标存储桶的名称|
|destObjectName|String|要创建的目标对象名称,如果为空，默认为源对象名称|
|cpConds|CopyConditions|拷贝操作的一些条件Map|
|metadata|Map<String, String>|给目标对象的元数据Map|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| ObjectStat| 对象的元数据信息 |


### 3. Presigned操作

**==String presignedGetObject(String bucketName, String objectName, Integer expires)==**
生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String|存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|expires|Integer|过期时间，以秒为单位，默认7天，不大于7天|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| String| 用于下载对象的URL |

**==String presignedPutObject(String bucketName, String objectName, Integer expires)==**
生成一个给HTTP PUT请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|bucketName| String|存储桶的名称 |
|objectName|String|存储桶里对象的名称|
|expires|Integer|过期时间，以秒为单位，默认7天，不大于7天|

| 返回值类型   |  描述 |
| :--------- | :-----: |
| String| 用于下载对象的URL |

**==Map<String,String> presignedPostPolicy(PostPolicy policy==**
允许给POST请求的presigned URL设置策略，比如接收对象上传的存储桶名称的策略，key名称前缀，过期策略

| 参数      | 类型    |  描述 |
| --------- | -------- | :----- |
|policy|PostPolicy|对象的post策略|

| 返回值类型   |  描述 |
| :--------- | :-----: |
|Map<String,String>| 用于构造表单数据的字符串映射 |

