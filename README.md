## Spring boot 2 整合minio

## 整合方式

- **使用工具类实现**
  -  pom.xml文件中导入Minio的jar包
  ```xml
   <dependency>
      <groupId>io.minio</groupId>
      <artifactId>minio</artifactId>
      <version>3.0.10</version>
    </dependency>
   ```
   - 在application.properties中添加Minio的相关参数
   ```text
      spring.minio.uri=http://IP:PORT
      spring.minio.accessKey=YOURACCESSKEY
      spring.minio.secretKey=YOURSECRETKEY
   ```
   - 编写MinioConfig.java，注入上一步配置的参数并连接Minio服务器，将连接成功的对象交于spring管理
   ```java
    @Bean
    public MinioClient minioClient() {
        try {
            return new MinioClient(minioUrl, access, secret, false);
        } catch (InvalidEndpointException | InvalidPortException e) {
            e.printStackTrace();
        }
        return null;
    }
  ```
  - 编写MinioService.java，在其中新建四个接口
  ```java
    @Service
    public interface MinioService {

    // 创建一个存储桶
    Map<String, Boolean> createBucket(String bucketName);

    // 删除一个存储桶
    Map<String, Boolean> removeBucket(String bucketName);

    // 返回存储桶列表
    Map<String, List<Bucket>> ListBucket();

    // 返回某个储存桶中的所有对象
    Map<String, List<Item>> ListObjects(String bucketName);
    }
  ```
  - 编写MinioServiceImpl实现MinioService中定义的接口，具体的业务实现逻辑在该文件中编写
  ```java
    @Override
    public Map<String, List<Bucket>> ListBucket() {
        try {
            return new HashMap<String, List<Bucket>>(){{
                put("success", minioClient.listBuckets());
            }};
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Bucket>>(){{
            put("failed", null);
        }};
    }
  ```
  
 - **使用工具类实现**
   - 导入java包，编写MinioConfig.java文件，该步骤同上
   - 编写工具类MinioUtils.java，在其中注入minioClient并新建一个静态对象，并写一个初始化函数进行绑定
   ```java
    @Autowired
    private MinioClient minioClient;

    private static MinioUtils minioUtils;

    @PostConstruct
    public void ini() {
        minioUtils = this;
        minioUtils.minioClient = this.minioClient;
    }
   ```
   - 在工具类中编写静态方法，实现业务逻辑
   ```java
   public static Map<String, List<Bucket>> ListBucket() {
        try {
            return new HashMap<String, List<Bucket>>(){{
                put("correct", minioUtils.minioClient.listBuckets());
            }};
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Bucket>>(){{
            put("wrong", null);
        }};
    }
    ```
    - 编写MinioUtilService.java，在其中调用工具类中的方法
    ```java
    @Service
    public class MinioUtilService {

        // 创建一个存储桶
        public Map<String, Boolean> createBucket(String bucketName){
            return MinioUtils.createBucket(bucketName);
        }
    
        // 删除一个存储桶
        public Map<String, Boolean> removeBucket(String bucketName){
            return MinioUtils.removeBucket(bucketName);
        }
    
        // 返回存储桶列表
        public Map<String, List<Bucket>> ListBucket(){
            return MinioUtils.ListBucket();
        }
    
        // 返回某个储存桶中的所有对象
        public Map<String, List<Item>> ListObjects(String bucketName){
            return MinioUtils.ListObjects(bucketName);
        }
    }
    ```
    
