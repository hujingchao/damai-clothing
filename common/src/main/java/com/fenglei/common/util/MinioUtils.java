package com.fenglei.common.util;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Random;
@Component
public class MinioUtils {
    /**
     * minio参数
     */
    private static String VIEW_ENDPOINT;
    private static String ENDPOINT;
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    private static String BUCKET_PARAM;

    @Value("${minio.view-endpoint}")
    public void setViewEndpoint(String viewEndpoint) {
        this.VIEW_ENDPOINT = viewEndpoint;
    }

    @Value("${minio.endpoint}")
    public void setENDPOINT(String endpoint) {
        this.ENDPOINT = endpoint;
    }

    @Value("${minio.access-key}")
    public void setAccessKey(String accessKey) {
        this.ACCESS_KEY = accessKey;
    }

    @Value("${minio.secret-key}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    @Value("${minio.bucket}")
    public void setBucketParam(String bucketParam) {
        this.BUCKET_PARAM = bucketParam;
    }


    /**
     * bucket权限-只读
     */
    private static final String READ_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";
    /**
     * bucket权限-只读
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";
    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    /**
     * 文件url前半段
     *
     * @return 前半段
     */
    public static String getObjectPrefixUrl() {
        return String.format("%s/%s/", ENDPOINT, BUCKET_PARAM);
    }

    /**
     * 创建桶
     *
     * @param bucket 桶
     */
    public static void makeBucket(String bucket) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        // 判断桶是否存在
        boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!isExist) {
            // 新建桶
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 更新桶权限策略
     *
     * @param policy 权限
     */
    public static void setBucketPolicy(String policy) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        switch (policy) {
            case "read-only":
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(BUCKET_PARAM).config(READ_ONLY.replace(BUCKET_PARAM, BUCKET_PARAM)).build());
                break;
            case "write-only":
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(BUCKET_PARAM).config(WRITE_ONLY.replace(BUCKET_PARAM, BUCKET_PARAM)).build());
                break;
            case "read-write":
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(BUCKET_PARAM).config(READ_WRITE.replace(BUCKET_PARAM, BUCKET_PARAM)).build());
                break;
            case "none":
            default:
                break;
        }
    }

    /**
     * 上传本地文件
     *
     * @param objectKey 文件key
     * @param filePath  文件路径
     * @return 文件url
     */
    public static String uploadFile(String objectKey, String filePath) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        client.uploadObject(UploadObjectArgs.builder().bucket(BUCKET_PARAM).object(objectKey).filename(filePath).contentType("image/png").build());
        return getObjectPrefixUrl() + objectKey;
    }

    /**
     * 流式上传文件
     *
     * @param objectKey   文件key
     * @param inputStream 文件输入流
     * @return 文件url
     */
    public static String uploadInputStream(String objectKey, InputStream inputStream) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        // 判断桶是否存在
        boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_PARAM).build());
        if (!isExist) {
            // 新建桶
            client.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_PARAM).build());
        }
        client.putObject(PutObjectArgs.builder().bucket(BUCKET_PARAM).object(objectKey).stream(inputStream, inputStream.available(), -1).contentType("image/png").build());
        return VIEW_ENDPOINT + BUCKET_PARAM +"/" + objectKey;
//        return getObjectPrefixUrl(BUCKET_PARAM) + objectKey;
    }

    /**
     * 下载文件
     *
     * @param objectKey 文件key
     * @return 文件流
     */
    public static InputStream download(String objectKey) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        return client.getObject(GetObjectArgs.builder().bucket(BUCKET_PARAM).object(objectKey).build());
    }

    /**
     * 文件复制
     *
     * @param sourceBucket    源桶
     * @param sourceObjectKey 源文件key
     * @param objectKey       文件key
     * @return 新文件url
     */
    public static String copyFile(String sourceBucket, String sourceObjectKey, String objectKey) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        CopySource source = CopySource.builder().bucket(sourceBucket).object(sourceObjectKey).build();
        client.copyObject(CopyObjectArgs.builder().bucket(BUCKET_PARAM).object(objectKey).source(source).build());
        return getObjectPrefixUrl() + objectKey;
    }

    /**
     * 删除文件
     *
     * @param objectKey 文件key
     */
    public static void deleteFile(String objectKey) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        client.removeObject(RemoveObjectArgs.builder().bucket(BUCKET_PARAM).object(objectKey).build());
    }

    /**
     * 获取文件签名url
     *
     * @param objectKey 文件key
     * @param expires   签名有效时间  单位秒
     * @return 文件签名地址
     */
    public static String getSignedUrl(String objectKey, int expires) throws Exception {
        MinioClient client = MinioClient.builder().endpoint(ENDPOINT).credentials(ACCESS_KEY, SECRET_KEY).build();
        return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(BUCKET_PARAM).object(objectKey).expiry(expires).build());
    }

    /**
     * 获取文件名
     *
     * @param muFile   文件
     * @param isRetain 是否保留源文件名
     * @return 返回文件名，以当前年月日作为前缀路径
     */
    public static String getFilePathName(MultipartFile muFile, boolean isRetain) {
        String fileName = muFile.getOriginalFilename();
        String name = fileName;
        String prefix = "";
        if (fileName.indexOf('.') != -1) {
            name = fileName.substring(0, fileName.indexOf('.'));
            prefix = fileName.substring(fileName.lastIndexOf("."));
        }

        LocalDate date = LocalDate.now();
        StringBuilder filePathName = new StringBuilder("/upload/");
        filePathName.append(date.getYear());
        filePathName.append("/");
        filePathName.append(date.getMonthValue());
        filePathName.append("/");
        filePathName.append(date.getDayOfMonth());
        filePathName.append("/");
        //添加随机后缀
        Random r = new Random();
        int pix = r.ints(1, (100 + 1)).findFirst().getAsInt();
        filePathName.append(System.currentTimeMillis());
        filePathName.append("" + pix);
        //文件名超过32字符则截取
        if (isRetain) {
            filePathName.append("_");
            if (name.length() >= 32) {
                name = name.substring(0, 32);
            }
            filePathName.append(name);
        }
        filePathName.append(prefix);
        return filePathName.toString();
    }

}
