rm -rf build
javac -d build --module-source-path . $(find . -name '*.java')


java --module-path build -m moduleA/net.teaho.demo.java9.modular.a.Invoker


#package
bash ./build_module.sh
mkdir build/jar
jar --create --file=build/jar/moduleB.jar --module-version=1.0 -C build/moduleB .
jar --create --file=build/jar/moduleA.jar --module-version=1.0 --main-class=net.teaho.demo.java9.modular.a.Invoker -C build/moduleA .
mkdir build/jmod
jmod create --class-path build/moduleB build/jmod/moduleB.jmod
jmod create --class-path build/moduleA build/jmod/moduleA.jmod

#上面命令分别打了jar包和jmod包。分别打了如下包：
#
#├── build
#│   ├── jar
#│   │   ├── moduleA.jar
#│   │   └── moduleB.jar
#│   ├── jmod
#│   │   ├── moduleA.jmod
#│   │   └── moduleB.jmod
#jmod包的特点：
#新的jmod文件格式是在jar文件格式之上，囊括上native代码，配置文件和其他数据文件等一些不适合放在先有JAR文件格式的资源。
#jmod文件可用在编译期、链接期，但不能用于运行时

#链接
jlink --verbose --module-path build --add-modules moduleB,java.base --output build/moduleApp

