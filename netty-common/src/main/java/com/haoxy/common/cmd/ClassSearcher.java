package com.haoxy.common.cmd;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassSearcher {
    /**
     * classLoader
     */
    private final ClassLoader loader;
    
    /**
     * 用当前的classLoader
     */
    public ClassSearcher() {
        loader = Thread.currentThread().getContextClassLoader();
    }
    
    /**
     * 使用指定的classLoader
     * @param loader
     */
    public ClassSearcher(ClassLoader loader) {
        this.loader=loader;
    }
    
    public ClassLoader getClassLoader() {
        return loader;
    }
    
    /**
     * @param packageName 包名字
     * @param findChildPackage 是否查询子包
     * @return
     */
    public List<String> getClassNames(String packageName, boolean findChildPackage) {  
        List<String> fileNames = null;  
        String packagePath = packageName.replace(".", "/");  
        URL url = loader.getResource(packagePath);  
        if (url != null) {  
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNamesByFile(packageName, url.getPath(), findChildPackage);  
            }
            if (type.equals("jar")) {
                fileNames = getClassNamesByJar(url.getPath(), findChildPackage);  
            }
        } else {  
            fileNames = getClassNamesByJars(((URLClassLoader) loader).getURLs(), packagePath, findChildPackage);  
        }  
        return fileNames;  
    }
    
    private List<String> getClassNamesByFile(String packageName, String filePath, boolean findChildPackage) {  
        List<String> classNames = new ArrayList<>();  
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null) {
            return classNames;
        }
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                if (findChildPackage) {
                    String childPackageName = packageName+"."+childFile.getName();
                    classNames.addAll(getClassNamesByFile(childPackageName, childFile.getPath(), findChildPackage));  
                }  
            } else {  
                String childFilePath = childFile.getPath();  
                if (childFilePath.endsWith(".class")) {  
                    String name = childFile.getName().replace(".class", "");
                    classNames.add(packageName+"."+name);  
                }  
            }  
        }
        return classNames;  
    }  
    
    private List<String> getClassNamesByJar(String jarPath, boolean findChildPackage) {  
        List<String> classNames = new ArrayList<>();  
        String[] jarInfo = jarPath.split("!");  
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));  
        String packagePath = jarInfo[1].substring(1);
        try(JarFile jarFile = new JarFile(jarFilePath)) {  
            Enumeration<JarEntry> entrys = jarFile.entries();  
            while (entrys.hasMoreElements()) {  
                JarEntry jarEntry = entrys.nextElement();  
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {  
                    if (findChildPackage) {  
                        if (entryName.startsWith(packagePath)) {  
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
                            classNames.add(entryName);
                        }  
                    } else {  
                        int index = entryName.lastIndexOf("/");  
                        String myPackagePath;  
                        if (index != -1) {  
                            myPackagePath = entryName.substring(0, index);  
                        } else {  
                            myPackagePath = entryName;  
                        }
                        if (myPackagePath.equals(packagePath)) {  
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
                            classNames.add(entryName);
                        }  
                    }  
                }  
            } 
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return classNames;  
    }
    
    private List<String> getClassNamesByJars(URL[] urls, String packagePath, boolean findChildPackage) {  
        List<String> classNames = new ArrayList<>();  
        if (urls != null) {
            for(URL url:urls) {  
                String urlPath = url.getPath();  
                // 不必搜索classes文件夹  
                if (urlPath.endsWith("classes/")) {  
                    continue;  
                }  
                String jarPath = urlPath + "!/" + packagePath;
                classNames.addAll(getClassNamesByJar(jarPath, findChildPackage));  
            }  
        }  
        return classNames;  
    }
}
