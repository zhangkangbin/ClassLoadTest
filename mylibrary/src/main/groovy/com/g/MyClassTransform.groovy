package com.g

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES

class MyClassTransform extends Transform {


    private Project project
    MyClassTransform(Project project){

        this.project=project
    }
    @Override
    String getName() {
        return "__MyClassTransform__"
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException,
            InterruptedException, IOException {
        super.transform(transformInvocation)

        //   println("------------z-------transform class--------------------" + stringJson)

        def  dir=project.getBuildDir().absolutePath+File.separator+FD_INTERMEDIATES+File.separator+"jarConfig.json"
        println("------------z-------dir-------------------" +dir)
        def configFile = new File(dir)

        Map<String, ConfigBean> configMap
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                println("------------z-------create success--------------------")
            } else {
                println("------------z-------create fail--------------------")
            }
            configMap = new HashMap<>()
        } else {
            // println("------------z-------file ready--------------------" + configFile.absolutePath)
            configMap = new Gson().fromJson(configFile.text, new TypeToken<HashMap<String, ConfigBean>>() {
            }.getType())
            println("------------z-------text --------------------" + configMap.size())
            println("------------z-------text --------------------" + configFile.text)

            //   configFile.writeL(stringJson)

        }

        transformInvocation.inputs.each {

            TransformInput input ->

                input.jarInputs.each {

                    JarInput jarInput ->

                        // String fileMd5 = FileUtil.getFileMD5ToString(jarInput.file)
                        String fileMd5 = getFileMd5(jarInput.file)
                        //如果没有这个key就添加到map里面
                        def key=jarInput.file.name+fileMd5
                        if (!configMap.containsKey(key)) {


                            ConfigBean configBean = new ConfigBean()
                            configBean.setPath(jarInput.file.absolutePath)
                            configBean.setMd5(fileMd5)
                            configMap.put(key, configBean)
                            println("---------md523------" + fileMd5)
                        } else {
                            println("---------已存在key23------" + jarInput.file.absolutePath)
                        }

                        /*  println("---------jar------" + jarInput.file.name)
                             println("---------path------" + jarInput.file.absolutePath)
                             println("---------size------" + jarInput.file.size())
                             println("---------hashCode------" + jarInput.file.hashCode())*/
                        /*  jarInput.file.eachFile {
                              File file ->

                                  println("---------jar22------"+file.name)


                          }
  */

                }



                input.directoryInputs.each {

                    DirectoryInput directoryInput ->
/*
                        directoryInput.file.eachFile {File file->

                            println("--------z1-------file name-------------"+file.name)
                        }*/
                        //遍历目录下的每个文件
                        directoryInput.file.eachFileRecurse { File file ->

                            //判断是否有MainActivity.class
                            if (file.name == "MainActivity.class") {
                                println("--------z2-------file name-------------" + file.name)
                            }
                        }


                }

        }

        Gson gson = new Gson()
        String stringJson = gson.toJson(configMap)
        configFile.write(stringJson)
        println("----=====================-----write-==========------------" + stringJson)

    }

    /**
     获取jar的md5
     */
    def static getFileMd5(File jar) {
        def fileInputStream = new FileInputStream(jar)
        def md5 = DigestUtils.md5Hex(fileInputStream)
        fileInputStream.close()
        return md5
    }

/**
 * 需要处理的数据类型，有两种枚举类型
 * CLASSES 和 RESOUCES
 * classes代表处理的java class 文件
 * resources 代表要处理java的资源
 * @return
 */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * //    指Transform要操作内容的范围，官方文档Scope有7种类型：
     //
     //    EXTERNAL_LIBRARIES        只有外部库
     //    PROJECT                       只有项目内容
     //    PROJECT_LOCAL_DEPS            只有项目的本地依赖(本地jar)
     //    PROVIDED_ONLY                 只提供本地或远程依赖项
     //    SUB_PROJECTS              只有子项目。
     //    SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
     //    TESTED_CODE                   由当前变量(包括依赖项)测试的代码
     * @return
     */
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }
}

class ConfigBean {

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }

    String getMd5() {
        return md5
    }

    void setMd5(String md5) {
        this.md5 = md5
    }
    String path;
    String md5;
}