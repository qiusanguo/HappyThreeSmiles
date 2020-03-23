# 一、 项目介绍
由于开发出来的APP很容易被逆向，修改代码逻、加入广告、病毒等二次打包后发布，对开发者和用户造成一定的损失。因此我们的APP运行过程中需要进行签名校验，以及使用加解密算法对数据进行处理，从而保证访问服务端的请求是我们信任的APP。
# 包括以下内容
## I、App Library，客户端签名校验和数据加解密
1. 签名自动读取APP的证书进行签名校验
2. 与服务端的数据交互使用AES加解密
3. 结合MD5、DES生成动态AES的加密key

## II、Server Library
服务端使用，完成数据的加解密

## III、Key Library
主要是生成APP和服务端使用的秘钥文件

# 二、 使用方法
## 第一步：Key Library 生成秘钥文件
运行中 Main.class的main方法（），需要传入我们APP 签名证书的sha1值以及包名，完成后会在LibKey目录下生成htsSec和htsServer,其中htsSec客户端使用，htsServer服务端使用。参考如下：

```
public static void main(String[] args){
    String jskSha1 = "D0030147F0841E0DBBD8A861A69AD5CDB8A4C69E";
    String pkgName = "com.hts.security.app";
    KeyUtil.generate(jskSha1,pkgName);
}
```

## 第二步：APP集成
1. 项目中导入 libRealse中的htssecrity-xxx.aar包
2. 项目的assets 目录导入第一步生成的htsSec文件
3. 代码中接入：

```
//Application onCreate()中初始化
@Override
public void onCreate() {
    super.onCreate();
    SecurityUtil.init();
}

//app合适的地方调用加密和解密方法
String ecryptData = SecurityUtil.encrypt(data);
Strign decryptData = SecurityUtil.decrypt(data);

```

## 第三步：Server端集成
1，项目中导入 libRealse中的libServer-xxx.jar包
2，打开第一步生成的文件htsServer，获取其中的值，调用HtsSecurityUtit进行初始化，参考如下：
```
Strign htsServerFileData = "CD0E98545EB6ACAF4CADF31C0EB01CC7";
HtsSecurityUtil.init(htsServerFileData);
//合适的地方调用加解密方法
String enryptData = HtsSecurityUtil.encrypt(data);
String decryptData = HtsSecurityUtil.decrypt(data);

```


# 三、 相关博客介绍
[APP安全(一)-防二次打包（C、C++签名校验）](https://blog.csdn.net/mrRuby/article/details/104046640)

[APP安全(二)-c、c++AES实现加解密](https://blog.csdn.net/mrRuby/article/details/104064606)

[APP安全(三)-结合MD5、DES生成一个AES Secret Key](https://blog.csdn.net/mrRuby/article/details/104765421)

# 四、 建议
因为是开源的安全方案，所以在一定程度上来说就不安全了，所以最要不要直接使用该组件编译好的组件，我们需要将项目clone下来，修改相关的key以及实现算法，从新编译后在使用，从而增强安全性。


# 五、 如果对你项目有帮助，可鼓励一下或者给个star

![image](https://github.com/qiusanguo/HappyThreeSmiles/blob/master/img/alpay.png)


