package com.g


import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyTask implements Plugin<Project> {
    void apply(Project project) {

        println "================Hi this is micky's plugin=========2"
        //AppExtension就是build.gradle中android{...}这一块
        project.extensions.create("xiao", AutoRegisterConfig)
        def android = project.extensions.getByType(AppExtension)
        def transform = new MyClassTransform(project)
        android.registerTransform(transform)

       /* project.task('myTask') << {
            println "================Hi this is micky's plugin========="
        }*/
    }

}

class AutoRegisterConfig {

    /** 宿主包名,默认null */
    def hostApplicationId = null
}