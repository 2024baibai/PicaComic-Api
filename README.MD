# PicaComic-Api
哔咔漫画的Api，基本实现了大部分常用接口

-----
## 已实现的接口
- 用户信息
    - 登陆 
    - 获取个人用户信息
        - 注册邮箱/性别/名称/生日
        - 登陆IP/注册时间/激活时间
        - 头衔/个人签名/经验值/等级
        - 其他信息...
    - 个人中心签到
- 获取本子信息
    - 获取我收藏的本子 
    - 搜索本子
    - 获取本子详细信息
        - 点赞数/喜欢数/评论数
        - 作者/汉化组/上传时间
        - 标签/分类
        - 章节/图片资源地址
        - 其他信息...

-----

## 目录

- [使用教程](#使用教程)
    - [用户登陆(PicaLogin)](#用户登陆)
        - [构造一个PicaLogin对象](#构造一个PicaLogin对象)    
        - [获取Authorization字段](#获取Authorization字段)
    - [PicaHeader(哔咔标准请求头) 和 PicaResult(哔咔请求结果)](#PicaHeader和PicaResult) 
        - [构建一个Pica标准请求头](#构建一个Pica标准请求头)
        - [模拟发起请求并获取请求结果](#模拟发起请求并获取请求结果)
        - [解析请求结果](#解析请求结果)
    - [用户Api(PicaUserApi)](#用户Api(PicaUserApi))
        - [获取我的个人资料](#获取我的个人资料)
        - [获取其他用户的个人资料](#获取其他用户的个人资料)
        - [个人签到](#个人签到)
    - [本子Api(PicaBookApi)](#本子Api(PicaBookApi))
        - [获取我收藏的本子](#获取我收藏的本子)
        - [搜索本子](#搜索本子)
        - [BookSimpleInfo(本子缩略信息)](#BookSimpleInfo(本子缩略信息))
        - [BookDetailInfo(本子详细信息)](#BookDetailInfo(本子详细信息))
        - [获取章节信息](#获取章节信息)
        - [获取本子图片页信息](#获取本子图片页信息)
- [更新日志](#更新日志)
- [注意事项](#注意事项)

 
-----
 <span id="使用教程"></span>
# 使用教程

本章节主要内容为如何构造请求并获取请求结果
<span id="用户登录"></span>
## 用户登陆 
<span id="构造一个PicaLogin对象"></span>
### 构造一个PicaLogin对象 

首先构造一个``PicaLogin``对象
```java
String username = "123456789@qq.com";
String password = "456789123";
PicaLogin loginApi = new PicaLogin(username, password);
```
<span id="获取Authorization字段"></span>
### 获取Authorization字段 

上一个章节构造了一个``PicaLogin``对象，这个对象提供了一个``login()``方法，返回一个``String``字段。
```java
String authStr = loginApi.login();
```
如果登陆失败，将会抛出异常。
这个字段可以缓存，有效期大约为1~2周。
此字段为之后绝大部分请求都需要的字段。

<span id="PicaHeader和PicaResult"></span>
## PicaHeader和PicaResult 
PicaHeader(哔咔标准请求头) 和 PicaResult(哔咔请求结果) 
哔咔的请求头数量极多，``PicaHeader``为哔咔请求头的封装类，以下称为**哔咔标准请求头**
各个Api已经封装了此请求头，基本无需自行构建。
由于篇幅关系，仅说明几个重要的请求头

1. **authorization** 为登陆后返回的身份验证字段，也是各个API的构造函数参数，几乎所有请求都需要此字段。
2. **time** 请求的时间戳，单位秒，与北京时间相差不得大于300秒，否则会提示时间戳过期。
3. **signature** 签名，算法请参照PicaHeader类，每次请求都要对部分请求参数签名获得此字段。
4. **url** 请求的目标URL。
5. **user-agent** 部分请求需要此字段，否则会返回400.
6. **host** 获取本子需要此字段，否则会返回403.


<span id="构建一个Pica标准请求头"></span>
### 构建一个Pica标准请求头

手动构造一个Pica标准请求头的代码如下:

```java
PicaHeader header = new PicaHeader();
header.setAuthorization(authrization);//设置身份验证字段
header.setUrl("https://picaapi.picacomic.com/comics/" + bookID);//设置请求的目标URL
```

<span id="模拟发起请求并获取请求结果"></span>
### 模拟发起请求并获取请求结果

发起请求可以使用NetUtil包中的方法，也可以使用PicaResult中的静态方法``getPicaResult(PicaHeader header)``获取
这里以PicaResult的方法为例:
```java
PicaResult result = PicaResult.getPicaResult(header);
```
此方法会返回一个result对象，即哔咔服务器返回的json，请求成功后哔咔服务器返回的内容格式如下（省略部分内容）:
```json
{
	"code": 200,
	"message": "success",
	"data": {
			"user": {
			"_id": "xxxxxxxxxxxxxxxxx",
			"birthday": "1990-01-01T00:00:00.000Z",
			"email": "123456789@qq.com",
			"gender": "m",
			"name": "我的ID",
			"slogan": "个人签名",
		}
	}
}
```
如果接口请求失败，则会返回错误码，可以使用``hasError()``方法判断是否出现错误。
如果由于其他原因（网络异常，json解析失败等）则会抛出异常。

<span id="解析请求结果"></span>
### 解析请求结果

result对象包含以下几个数据：
```
int getCode();//返回http请求结果
boolean hasError();//本次请求是否出错（哔咔请求不正确，不是网络波动等错误）
int getErrorCode();//获取错误码，仅当请求出错时才可获取到。
String getMessage();//获取信息，成功返回success，请求失败则返回提示文本，例如invalid username or password
JSONObject getData();//返回接口数据，一个json对象也是构造请求结果的重要参数。
```

------

<span id="用户Api(PicaUserApi)"></span>
## 用户Api(PicaUserApi)

此API集成了以下功能:

- 获取指定用户信息
- 签到
- 获取我的个人信息
- 获取我喜欢的本子

构造方法如下:

```java
//使用登录API返回的authorization字段构造
PicaUserApi uApi = new PicaUserApi(authorization);
```

<span id="获取我的个人资料"></span>
### 获取我的个人资料

注意：哔咔的个人资料获取有两种渠道，一种是个人中心接口，返回自己的资料，另一种是从用户详情访问（即访问其他用户），后者返回的资料比个人中心更加丰富，如需获取更多信息，请参阅下一节**获取其他用户的个人资料**


```java
PicaUserApi uApi = new PicaUserApi(authorization);
PicaUserProfile myProfile =  uApi.getMyProfile();//从我的个人中心获取资料
```

哔咔接口会返回以下内容:

```json
{
	"code": 200,
	"message": "success",
	"data": {
			"user": {
			"_id": "xxxxxxxxxxxxxxxxx",
			"birthday": "1990-01-01T00:00:00.000Z",
			"email": "123456789@qq.com",
			"gender": "m",
			"name": "我的ID",
			"slogan": "个人签名",
			"title": "萌新",
			"verified": false,
			"exp": 1234,
			"level": 4,
			"characters": [],
			"created_at": "2016-01-01T01:01:01.000Z",
			"avatar": {
				"originalName": "avatar.jpg",
				"path": "xxxxxxxx-xxxx-aaaa-ssss-xxxxxxxxxxxx.jpg",
				"fileServer": "https://storage1.picacomic.com"
			},
			"isPunched": false
		}
	}
}
```

注意这里的``_id``字段，代表了每个用户的唯一id
仅个人中心请求方式才会返回``isPunched``(是否签到)字段。
具体的接口方法和字段含义请自行参阅方法注释，这里不再赘述。

<span id="获取其他用户的个人资料"></span>
### 获取其他用户的个人资料

相比个人中心接口，此接口返回的信息非常丰富，但缺少``isPunched``(是否签到)字段，如不需要检查是否签到，推荐使用此接口进行获取。

获取方式有以下两种

```java
PicaUserApi uApi = new PicaUserApi(authorization);
PicaUserProfile profile ；
//第一种方法，将otherView参数设为true
profile = uApi.getMyProfile(true) ;//以其他用户视角
//第二种方法，直接指定目标用户ID(也可指定为自己的ID)
profile = uApi.getUserProfile(String targetID);//使用指定的用户ID获取
```
以请求某用户数据为例(数据已脱敏):
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"user": {
			"_id": "582995xxxxxx43585xxxxxxx",
			"birthday": "1999-01-01T00:00:00.000Z",
			"email": "123456789@qq.com",
			"gender": "bot",
			"name": "xx汉化组ww",
			"password": "$2a$08$wA44er0afQjeRInkxxxxxxxxxxxxxxxxJYgLjppzK8j/tAyY2",
			"activation_code": "asdfqwef-e5bb-43db-af07-zxcvbnmkjhgf",
			"activation_date": "2016-11-10T00:00:00.000Z",
			"last_login_date": "2019-01-27T00:00:01.123Z",
			"slogan": "欢迎各位~\n希望各位能多多评论呀！",
			"ip": "123.45.67.89",  
			"title": "简介写的棒棒的大佬",
			"verified": false,
			"exp": 80000,
			"level": 28,
			"updated_at": "2019-01-27T14:21:36.891Z",
			"created_at": "2016-11-11T10:45:40.000Z",
			"avatar": {
				"originalName": "avatar.jpg",
				"path": "qwerasdf-d0ef-4db7-afbe-qwertyuiolkj.jpg",
				"fileServer": "https://storage1.picacomic.com"
			},
			"resendCount": 0,
			"forgotCount": 0,
			"character": "https://www.picacomic.com/characters/frame_knight_500_999.png?r=3"
		}
	}
}
```
注意：所有字段并非一定存在，具体请参阅各方法的注释文档。

<span id="个人签到"></span>
### 个人签到

```java
PicaUserApi uApi = new PicaUserApi();
uApi.punch_in();//签到，固定返回签到成功
```

注意：无论是否已经签到，服务器都会返回签到成功，返回样例(已封装为``PicaResult``):

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"res": {
			"status": "ok",
			"punchInLastDay": "2019-01-28"
		}
	}
}
```

<span id="本子Api(PicaBookApi)"></span>
## 本子Api(PicaBookApi)
此API集成了以下功能:

- 搜索本子
- 获取本子详细信息
- 获取本子章节信息
- 获取本子页

构造方法如下:

```java
//使用登录API返回的authorization字段构造
PicaBookApi buApi = new PicaBookApi(authorization);
```

<span id="获取我收藏的本子"></span>
### 获取我收藏的本子

一个简单的遍历收藏的本子样例如下:

```java
//获取用户API
PicaUserApi puApi = new PicaUserApi(authorization);
//获取第一页（总页码包含在返回结果中）
PicaBookResult result = puApi.getMyFavorite(1);
//获取总页码（至少为1）
int page = result.getPages();
//遍历每一页
for (int i = 1; i <=page ; i++) {
    result = puApi.getMyFavorite(i);
    //遍历本页所有的本子(如果不存在任何本子返回空数组)
    for (BookSimpleInfo info : result.getBooks()) {
        //打印本子标题
        System.out.println(info.getTitle());
    }
}
```


<span id="搜索本子"></span>
### 搜索本子

使用PicaBookApi中的``search()``方法，``search()``方法有两个重载
```java
//固定返回第一页结果
PicaBookResult search(String keyword);
//可手动指定页码
PicaBookResult search(int page, String keyword);
```
当页码超出范围时，结果中将不包含任何本子，当请求的页码非法（例如-1），``PicaResult``则返回错误。
遍历与上个例子相同，不再赘述。

<span id="BookSimpleInfo(本子缩略信息)"></span>
### BookSimpleInfo(本子缩略信息)
当请求本子列表时，本子的信息为BookSimpleInfo,如要获取更详细的信息，请参阅下一章:``BookDetailInfo(本子详细信息)``章节

``BookSimpleInfo``结构:

```json
{
	"_id": "qwertye12345678f93poiuyt",
	"title": "[xx汉化组]xxxxxxxxxxxxxxxx",
	"author": "sadsadsadsads",
	"pagesCount": 24,
	"epsCount": 1,
	"finished": true,
	"categories": [
		"同人",
		"短篇",
		"純愛"
	],
	"thumb": {
		"originalName": "cover.jpg",
		"path": "qwertyui-606a-4a0d-8015-asdfghjklpoi.jpg",
		"fileServer": "https://storage1.picacomic.com"
	},
	"likesCount": 115
}
```

此对象一般存在于每个列表的返回结果中，例如搜索结果列表，喜欢的本子列表。

<span id="BookDetailInfo(本子详细信息)"></span>
### BookDetailInfo(本子详细信息)

当请求指定ID的本子时（点击查看本子），返回的信息为``BookDetailInfo``，此类**继承了**``BookSimpleInfo``。

获取样例:
```java
PicaBookApi buApi = new PicaBookApi(authorization);
//以上一个搜索结果例子为例，遍历搜索结果
for (BookSimpleInfo info : result.getBooks()) {
    //获取本子ID
    String bookID = info.get_ID();
    //获取本子详细信息
    detail = buApi.getBookDetail(bookID);
}

```

对象结构: **(已省略BookSimpleInfo包含的内容)**

```json
{
	"_creator": {
		"_id": "qwertyu433232b7aezxcvbnm",
		"gender": "m",
		"name": "迷迭迷迭paryi桑",
		"slogan": "手Oキ",
		"title": "狗牌",
		"verified": false,
		"exp": 54404,
		"level": 23,
		"characters": [
			"knight"
		],
		"avatar": {
			"fileServer": "https://storage1.picacomic.com",
			"path": "qwertyui-3e35-49d3-9ab4-zxcvbnmlkjhg.jpg",
			"originalName": "avatar.jpg"
		},
		"character": "https://www.picacomic.com/characters/frame_knight_250_499.png?r=3"
	},
	"description": "屑女仆 神楽めあ的pixiv杂图",
	"chineseTeam": "meaqua贴贴制糖组",
	"tags": [
		"萌",
		"全彩"
	],
	"updated_at": "2019-01-10T00:00:00.001Z",
	"created_at": "2019-01-10T00:00:00.000Z",
	"viewsCount": 35658,
	"isFavourite": false,
	"isLiked": false,
	"commentsCount": 56
}
```

可以看到主要是包括了上传者信息，描述，汉化组，上传时间，观看数量以及是否点赞。

<span id="获取章节信息"></span>
### 获取章节信息

获取章节信息需要本子的ID和章节页码
每40个章节为1页，页码可在返回的``EpisodeInfo``对象中获取。
目前(2019/02/14)尚未发现有超过一页章节的本子。
页码至少为1

```java
String bookID = "qwertyu433232b7aezxcvbnm";
PicaBookApi buApi = new PicaBookApi(authorization);
//获取章节第一页
EpisodeInfo epi = buApi.getEpisodeInfo(bookID, 1);
//重载的方法(使用BookSimpleInfo对象)
//buApi.getEpisodeInfo(BookSimpleInfo info, int page);
```

EpisodeInfo结构:
```json
{
	"docs": [{
		"_id": "5a6954608d7d24559d8a42c0",
		"title": "第2集",
		"order": 2,
		"updated_at": "2018-01-24T13:49:13.771Z"
	}, {
		"_id": "5a6954608d7d24559d8a42bf",
		"title": "第1集",
		"order": 1,
		"updated_at": "2018-01-24T13:46:52.658Z"
	}],
	"total": 2,
	"limit": 40,
	"page": 1,
	"pages": 1
}
```

注意每个章节都有一个``order``字段，获取本子内容图片时需要此字段确认此本子的哪一个章节。

<span id="获取本子图片页信息"></span>
### 获取本子图片页信息

请求样例:

```java
String bookID = "qwertyu433232b7aezxcvbnm";
PicaBookApi buApi = new PicaBookApi(authorization);
//章节ID
int order = 1;
//获取到本子页面对象
BookPage page = buApi.getBookPage(bookid,order,1);
```

BookPage结构 **（省略了部分图片）**:
```json
{
	"docs": [{
		"_id": "5bfa0ffb150a0924fb46a79b",
		"media": {
			"originalName": "01.jpg",
			"path": "79faa4fb-a70c-49bd-beb6-0bb6c0f38261.jpg",
			"fileServer": "https://storage1.picacomic.com"
		}
	}, {
		"_id": "5bfa0ffb150a0924fb46a79c",
		"media": {
			"originalName": "02.jpg",
			"path": "550b0f48-2d5d-496e-becf-c2836f8b606e.jpg",
			"fileServer": "https://storage1.picacomic.com"
		}
	}],
	"total": 17,
	"limit": 40,
	"page": 1,
	"pages": 1
}
```

此对象包含了当前页面，总页面，单页限制，图片总数等，其中图片的地址包含在Media对象中

获取Media并遍历图片的样例:
```java
//遍历每个图片资源文件
for(Media md :page.getPics()){
    //获取图片URL
    String url = md.getURL();
    //下载
    PicaHeader header = new PicaHeader();
    //设置图片地址
    header.setUrl(url);
    //必须要添加host，否则访问一定次数后会403
    header.setHost("s3.picacomic.com");
    //获取图片数据
    byte[] data = NetUtil.getFile(header);
    //省略写入文件等操作
}
```

注意，请求虽然不需要带上完整的哔咔请求头(例如authorization等)，但是必须要带上host字段，建议使用哔咔请求头进行获取。

------


## 更新日志
>### 2019-02-14 (V1.0.1)
>       1. 更新了README排版，增加使用教程和部分样例
>       2. 移动了部分类到其他包
>### 2019-01-31 (V1.0.0)
>       初始化仓库

-----

## 注意事项
本项目仅供学习使用，请勿用于非法用途。如有任何问题，欢迎联系: [Shirosaki@flannep.com][1]
如果您喜欢本项目，欢迎Star/Fork~ QwQ




  [1]: mailto:shirosaki@flannep.com