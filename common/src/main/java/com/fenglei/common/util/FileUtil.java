package com.fenglei.common.util;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: yzy
 * Date: 2019/11/4 0004
 * Time: 11:01
 * Description: No Description
 */
@Slf4j
public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * MultipartFile转File
     *
     * @param multipartFile
     * @return
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除
     *
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     *
     * @param size
     * @return
     */
    public static String getSize(int size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * 文件合并
     *
     * @param targetFile
     * @param folder
     */
    public static Boolean merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static List<File> getFileList(String strPath) {
        LinkedList<File> fileList = new LinkedList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileList(file.getAbsolutePath());
                } else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * 获取图片宽度
     *
     * @param file 图片文件
     * @return 宽度
     */
    public static int getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null); // 得到源图宽
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     *
     * @param file 图片文件
     * @return 高度
     */
    public static int getImgHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Map uploadFile(MultipartFile file, HttpServletRequest request, String folderPath, String img_upload_url)
            throws Exception {
        if (file == null) {
            return null;
        }
        String absolutePath = null;
        // 1-修改上传的文件名，避免重复
        // 获取上传的文件的名称
        String uploadFileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
        String fileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
        // 生成uuid作为文件名称
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 修改后完整的文件名称
        String newFileName = uuid + "." + suffix;

        // 保存到图片服务器
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String address = folderPath + sdf.format(date);

        String httpAddress;
        if (suffix.contains("jpg") || suffix.contains("jpeg") || suffix.contains("png")
                || suffix.contains("JPG") || suffix.contains("JPEG") || suffix.contains("PNG")) {
            httpAddress = img_upload_url + "/" + sdf.format(date);
        } else {
            httpAddress = img_upload_url + "/files/" + sdf.format(date);
        }

        // 生成保存图片的文件夹
        File dirs = new File(address);
        if (!dirs.exists()) { // 创建文件夹
            dirs.mkdirs();
        }
        // 3-上传文件
        // 文件保存路径
        File targetFile = new File(address + "/" + newFileName);
        // 上传
        try {
            file.transferTo(targetFile);
        } catch (IllegalStateException e1) {
            throw e1;
        } catch (IOException e1) {
            throw e1;
        }
        // 4-返回图片的绝对路径
        absolutePath = httpAddress + "/" + newFileName;
        Map map = new HashMap();
        map.put("url", absolutePath);
        map.put("file", targetFile);
        map.put("newFileName", newFileName);
        map.put("oldFileName", uploadFileName);
        return map;
    }

    public static String uploadFile2(MultipartFile file, HttpServletRequest request, String folderPath, String upload_url) {
        if (file == null) {
            return null;
        }
        String absolutePath = null;
        // 1-修改上传的文件名，避免重复
        // 获取上传的文件的名称
        String uploadFileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
        // 生成uuid作为文件名称
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 修改后完整的文件名称
        String newFileName = uuid + "." + suffix;

        // 保存到图片服务器
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // String folderPath = "upload/images/sign/";
        String address = "";
        String httpAddress = "";

        address += folderPath + sdf.format(date);
        httpAddress += upload_url + sdf.format(date);

        // 生成保存图片的文件夹
        File dirs = new File(address);
        if (!dirs.exists()) { // 创建文件夹
            dirs.mkdirs();
        }
        // 3-上传文件
        // 文件保存路径
        File targetFile = new File(address + "/" + newFileName);
        // 上传
        try {
            file.transferTo(targetFile);
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // 4-返回图片的绝对路径
        absolutePath = httpAddress + "/" + newFileName;
        return absolutePath;
    }

    /**
     * 通过网络url读取文件
     *
     * @param netUrl  网络url
     * @param outPath 输出路径
     * @throws Exception
     */
    public static void readFileUrl(String netUrl, String outPath) throws Exception {
        //new一个URL对象
        URL url = new URL(netUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        //new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(outPath);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    /**
     * 根据属性名获取属性元素，包括各种安全范围和所有父类（考虑父类继承过来的属性，包括四类访问权限，private，protect，default，public）
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Field getFieldByClasss(String fieldName, Object object) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                // 这里什么都不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会进入
            }
        }
        return field;
    }


    /**
     * 根据属性名获取属性值，包括各种安全范围和所有父类（考虑父类继承过来的属性，包括四类访问权限，private，protect，default，public）
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getFieldValueByClass(String fieldName, Object object) {
        Field field = getFieldByClasss(fieldName, object);
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return null;
        }
    }


    public static File zipBase64(List<Map<String, String>> resources) throws IOException {

        String path = FileUtil.class.getResource("/").getPath();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowStr = sdf.format(now);

        BASE64Decoder decoder = new BASE64Decoder();
        Integer index = 0;
        List<String> filePaths = new ArrayList<>();
        for (Map<String, String> res : resources) {
            OutputStream out = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                // 生成pdf文件
                if (res.containsKey("pdf")) {
                    String pdf = res.get("pdf");

                    byte[] bytes = decoder.decodeBuffer(pdf);//base64编码内容转换为字节数组
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
                    bis = new BufferedInputStream(byteInputStream);
                    File file = new File(path + nowStr + ++index + ".pdf");
                    filePaths.add(file.getPath());
                    File pathTemp = file.getParentFile();
                    if (!pathTemp.exists()) {
                        pathTemp.mkdirs();
                    }
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);

                    byte[] buffer = new byte[1024];
                    int length = bis.read(buffer);
                    while (length != -1) {
                        bos.write(buffer, 0, length);
                        length = bis.read(buffer);
                    }
                }

                // 生成图片文件
                if (res.containsKey("pic")) {
                    String pic = res.get("pic");
                    out = new FileOutputStream(path + nowStr + ++index + ".png");
                    filePaths.add(path + nowStr + index + ".png");
                    // Base64解码
                    byte[] b = decoder.decodeBuffer(pic);
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {// 调整异常数据
                            b[i] += 256;
                        }
                    }
                    out.write(b);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (null != bos) {
                    bos.flush();
                    bos.close();
                    fos.close();
                    bis.close();
                }
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        }

        //定义压缩文件夹的名称和相关的位置
        String name = nowStr + ".zip";
        File zipFile = new File(path + name);
        InputStream input = null;
        //定义压缩输出流
        ZipOutputStream zipOut = null;
        //实例化压缩输出流  并定制压缩文件的输出路径
        zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        for (String o : filePaths) {
            File file = new File(o);
            //定义输入文件流
            input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            //设置注释
            zipOut.setComment("风雷科技");
            int temp = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
            }
            input.close();
            file.delete();
        }
        zipOut.close();
        return zipFile;
    }
}
