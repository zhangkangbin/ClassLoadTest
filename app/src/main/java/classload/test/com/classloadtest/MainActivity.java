package classload.test.com.classloadtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  PathClassLoader pathClassLoader=new PathClassLoader();

     /*   dexPath：dex文件路径列表，多个路径使用”:”分隔
        dexOutputDir：经过优化的dex文件（odex）文件输出目录, 是dex解压缩后存放的目录
        libPath：动态库路径（将被添加到app动态库搜索路径列表中）
        parent：这是一个ClassLoader，这个参数的主要作用是保留java中ClassLoader的委托机制（优先父类加载器加载classes，由上而下的加载机制，防止重复加载类字节码）*/
        DexClassLoader loader=new DexClassLoader("",null,"",getClassLoader());
    }
}
