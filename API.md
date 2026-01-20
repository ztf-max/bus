# 巴士拼车系统 API 接口文档

## 📋 目录

- [1. 用户模块](#1-用户模块)
  - [1.1 微信小程序登录](#11-微信小程序登录)
  - [1.2 账户密码登录](#12-账户密码登录)
- [2. 位置模块](#2-位置模块)
  - [2.1 位置上报](#21-位置上报)
- [3. 地图模块](#3-地图模块)
  - [3.1 获取地图位置信息](#31-获取地图位置信息)

---

## 🌐 基础信息

### 服务器地址
- 开发环境：`https://springboot-hl68-215177-7-1394804405.sh.run.tcloudbase.com`
- 生产环境：`https://your-domain.com`

### 通用响应格式

所有接口都返回统一的响应格式：

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "traceId": "trace-id-xxxx",
  "total": null,
  "data": {}
}
```

**响应字段说明：**

| 字段 | 类型 | 说明 |
|------|------|------|
| isSucceed | Boolean | 是否成功 |
| code | Integer | 状态码（200-成功，其他-失败） |
| msg | String | 返回消息 |
| traceId | String | 请求追踪ID |
| total | Long | 总数（分页时使用） |
| data | Object | 业务数据 |

### 通用错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录或token失效 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |

---

## 1. 用户模块

### 1.1 微信小程序登录

**接口描述：** 微信小程序一键授权登录，支持乘客端和司机端

**接口地址：** `POST /user/wx-login`

**是否需要登录：** 否

#### 请求参数

**Content-Type：** `application/json`

**Body 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| code | String | 是 | 微信登录凭证（wx.login获取） |
| nickname | String | 否 | 用户昵称 |
| avatarUrl | String | 否 | 用户头像URL |
| platform | String | 否 | 平台类型：`user`-乘客端, `driver`-司机端（默认：user） |

**请求示例：**

```json
{
  "code": "093tKU0w3KO9vR2xxx",
  "nickname": "张三",
  "avatarUrl": "https://thirdwx.qlogo.cn/xxx.jpg",
  "platform": "user"
}
```

#### 响应结果

**成功响应：**

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "data": {
    "userId": 1001,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdC1idXMiLCJpYXQiOjE3MDQwNjcyMDAsImV4cCI6MTcwNjc0NTYwMCwidXNlcl9pZCI6MTAwMSwibmlja19uYW1lIjoi5byg5LiJIiwidXNlcl90eXBlIjoidXNlciJ9.xxx",
    "nickname": "张三",
    "avatarUrl": "https://thirdwx.qlogo.cn/xxx.jpg",
    "isNewUser": true
  }
}
```

**响应字段说明：**

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户ID |
| token | String | JWT Token（有效期30天） |
| nickname | String | 用户昵称 |
| avatarUrl | String | 用户头像URL |
| isNewUser | Boolean | 是否是新用户 |

**失败响应：**

```json
{
  "isSucceed": false,
  "code": 500,
  "msg": "微信登录失败：invalid code",
  "data": null
}
```

#### 注意事项

1. `code` 是微信小程序通过 `wx.login()` 获取的临时凭证，有效期5分钟
2. 首次登录会自动创建用户记录
3. 乘客和司机共用同一个用户表，通过 `user_type` 字段区分
4. Token 会自动保存到数据库，支持多端互踢

---

### 1.2 账户密码登录

**接口描述：** 使用手机号和密码登录，支持乘客端和司机端

**接口地址：** `POST /user/password-login`

**是否需要登录：** 否

#### 请求参数

**Content-Type：** `application/json`

**Body 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| account | String | 是 | 手机号或用户名 |
| password | String | 是 | 密码 |
| platform | String | 否 | 平台类型：`user`-乘客端, `driver`-司机端（默认：user） |

**请求示例：**

```json
{
  "account": "13800138000",
  "password": "123456",
  "platform": "user"
}
```

#### 响应结果

**成功响应：**

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "data": {
    "userId": 1001,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdC1idXMiLCJpYXQiOjE3MDQwNjcyMDAsImV4cCI6MTcwNjc0NTYwMCwidXNlcl9pZCI6MTAwMSwibmlja19uYW1lIjoi5byg5LiJIiwidXNlcl90eXBlIjoidXNlciJ9.xxx",
    "nickname": "张三",
    "avatarUrl": "https://thirdwx.qlogo.cn/xxx.jpg",
    "isNewUser": false
  }
}
```

**响应字段说明：**

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户ID |
| token | String | JWT Token（有效期30天） |
| nickname | String | 用户昵称 |
| avatarUrl | String | 用户头像URL |
| isNewUser | Boolean | 是否是新用户（账户密码登录始终为 false） |

**失败响应：**

**账户或密码错误：**

```json
{
  "isSucceed": false,
  "code": 401,
  "msg": "账户或密码错误",
  "data": null
}
```

**用户未设置密码：**

```json
{
  "isSucceed": false,
  "code": 401,
  "msg": "该账户未设置密码，请使用其他登录方式",
  "data": null
}
```

**参数错误：**

```json
{
  "isSucceed": false,
  "code": 400,
  "msg": "账户不能为空",
  "data": null
}
```

#### 业务逻辑

1. **参数校验**：验证账户和密码不能为空
2. **用户查询**：根据手机号和用户类型（platform）查询用户
3. **密码验证**：验证用户是否已设置密码，并验证密码是否正确
4. **Token生成**：登录成功后生成JWT Token（有效期30天）
5. **用户类型**：根据 `platform` 参数区分乘客端和司机端，默认为乘客端

#### 注意事项

1. `account` 字段支持手机号或用户名，系统会根据手机号查询用户
2. 密码验证使用 BCrypt 加密算法（当前密码验证逻辑可能被注释，需要确认）
3. 如果用户未设置密码，会返回错误提示，需要使用其他登录方式（如微信登录）
4. `platform` 参数用于区分乘客端和司机端，如果不传则默认为 `user`（乘客端）
5. 账户密码登录不会创建新用户，只支持已注册用户的登录
6. Token 会自动保存到数据库，支持多端互踢
7. 同一手机号可能同时存在乘客和司机两种类型的账户，需要通过 `platform` 参数指定登录类型

---

## 2. 位置模块

### 2.1 位置上报

**接口描述：** 上报用户当前位置，乘客端和司机端共用

**接口地址：** `POST /location/report`

**是否需要登录：** 是

#### 请求参数

**Header 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| token | String | 是 | 登录时获取的JWT Token |

**Content-Type：** `application/json`

**Body 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| latitude | BigDecimal | 是 | 纬度（如：31.230416） |
| longitude | BigDecimal | 是 | 经度（如：121.473701） |
| heading | Float | 否 | 车头朝向，0-360度（仅司机端需要） |
| speed | Float | 否 | 速度，单位km/h（仅司机端需要） |

**乘客端请求示例：**

```json
{
  "latitude": 31.230416,
  "longitude": 121.473701
}
```

**司机端请求示例：**

```json
{
  "latitude": 31.230416,
  "longitude": 121.473701,
  "heading": 90.5,
  "speed": 45.2
}
```

#### 响应结果

**成功响应：**

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "data": true
}
```

**失败响应：**

```json
{
  "isSucceed": false,
  "code": 401,
  "msg": "用户未登录",
  "data": null
}
```

#### 注意事项

1. 每个用户只保留一条最新位置记录（根据 `user_id` 唯一索引）
2. 系统会自动更新 `gmt_modified` 字段，用于判断用户在线状态
3. 司机端需要额外上报 `heading` 和 `speed` 信息
4. 乘客端的 `heading` 和 `speed` 可以不传或传 null

---

## 3. 地图模块

### 3.1 获取地图位置信息

**接口描述：** 获取地图上的所有位置信息，根据用户类型返回不同数据

**接口地址：** `POST /map/locations`

**是否需要登录：** 是

**权限说明：**
- **司机端**：返回所有司机位置 + 所有乘客位置
- **乘客端**：返回所有司机位置 + 自己的位置

#### 请求参数

**Header 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| token | String | 是 | 登录时获取的JWT Token |

**Content-Type：** `application/json`

**Body 参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| latitude | BigDecimal | 是 | 当前用户纬度 |
| longitude | BigDecimal | 是 | 当前用户经度 |
| heading | Float | 否 | 车头朝向（仅司机端） |
| speed | Float | 否 | 速度（仅司机端） |

**请求示例：**

```json
{
  "latitude": 31.230416,
  "longitude": 121.473701,
  "heading": 90.5,
  "speed": 45.2
}
```

#### 响应结果

**司机端成功响应：**

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "data": {
    "drivers": [
      {
        "driverId": 2001,
        "name": "李师傅",
        "plateNumber": "京A12345",
        "latitude": 31.230416,
        "longitude": 121.473701,
        "heading": 90.5,
        "speed": 45.2,
        "status": 1
      },
      {
        "driverId": 2002,
        "name": "王师傅",
        "plateNumber": "京B67890",
        "latitude": 31.240000,
        "longitude": 121.480000,
        "heading": 180.0,
        "speed": 30.5,
        "status": 2
      }
    ],
    "users": [
      {
        "userId": 1001,
        "nickname": "张三",
        "avatarUrl": "https://xxx.jpg",
        "latitude": 31.235000,
        "longitude": 121.475000
      },
      {
        "userId": 1002,
        "nickname": "李四",
        "avatarUrl": "https://yyy.jpg",
        "latitude": 31.238000,
        "longitude": 121.478000
      }
    ],
    "userType": "driver"
  }
}
```

**乘客端成功响应：**

```json
{
  "isSucceed": true,
  "code": 200,
  "msg": "操作成功!",
  "data": {
    "drivers": [
      {
        "driverId": 2001,
        "name": "李师傅",
        "plateNumber": "京A12345",
        "latitude": 31.230416,
        "longitude": 121.473701,
        "heading": 90.5,
        "speed": 45.2,
        "status": 1
      }
    ],
    "users": [
      {
        "userId": 1001,
        "nickname": "张三",
        "avatarUrl": "https://xxx.jpg",
        "latitude": 31.235000,
        "longitude": 121.475000
      }
    ],
    "userType": "user"
  }
}
```

**响应字段说明：**

**MapLocationResponse：**

| 字段 | 类型 | 说明 |
|------|------|------|
| drivers | Array\<DriverLocationVO\> | 司机位置列表 |
| users | Array\<UserLocationVO\> | 乘客位置列表 |
| userType | String | 当前用户类型（user/driver） |

**DriverLocationVO：**

| 字段 | 类型 | 说明 |
|------|------|------|
| driverId | Long | 司机ID |
| name | String | 司机姓名 |
| plateNumber | String | 车牌号 |
| latitude | BigDecimal | 纬度 |
| longitude | BigDecimal | 经度 |
| heading | Float | 车头朝向（0-360度） |
| speed | Float | 速度（km/h） |
| status | Integer | 状态：0-收车, 1-听单中, 2-服务中 |

**UserLocationVO：**

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 乘客ID |
| nickname | String | 乘客昵称 |
| avatarUrl | String | 头像URL |
| latitude | BigDecimal | 纬度 |
| longitude | BigDecimal | 经度 |

**失败响应：**

```json
{
  "isSucceed": false,
  "code": 401,
  "msg": "用户未登录",
  "data": null
}
```

#### 业务逻辑

1. **自动更新位置**：调用此接口时会先更新当前用户的位置
2. **权限隔离**：
   - 司机端：可以看到所有司机和所有乘客的位置
   - 乘客端：可以看到所有司机的位置，但只能看到自己的位置
3. **实时性**：建议前端每3-5秒调用一次，保持位置数据实时更新

#### 注意事项

1. 必须携带有效的 token
2. 请求参数中的位置信息会先更新到数据库
3. 返回的数据已按 `gmt_modified` 降序排列
4. 只返回有位置信息的用户（未上报过位置的用户不会出现在列表中）

---

## 📱 微信小程序调用示例

### 1. 登录

```javascript
// 小程序端代码
wx.login({
  success: res => {
    if (res.code) {
      wx.request({
        url: 'https://your-domain.com/user/wx-login',
        method: 'POST',
        header: {
          'content-type': 'application/json'
        },
        data: {
          code: res.code,
          nickname: '张三',
          avatarUrl: 'https://xxx.jpg',
          platform: 'user' // 或 'driver'
        },
        success: function(response) {
          if (response.data.isSucceed) {
            const { token, userId } = response.data.data;
            // 保存token到本地
            wx.setStorageSync('token', token);
            wx.setStorageSync('userId', userId);
            console.log('登录成功');
          }
        }
      });
    }
  }
});
```

### 2. 位置上报

```javascript
// 上报位置
function reportLocation() {
  wx.getLocation({
    type: 'gcj02',
    success: function(res) {
      wx.request({
        url: 'https://your-domain.com/location/report',
        method: 'POST',
        header: {
          'token': wx.getStorageSync('token'),
          'content-type': 'application/json'
        },
        data: {
          latitude: res.latitude,
          longitude: res.longitude,
          heading: 90.5,  // 仅司机端
          speed: 45.2     // 仅司机端
        },
        success: function(response) {
          console.log('位置上报成功');
        }
      });
    }
  });
}

// 定时上报（每5秒）
setInterval(reportLocation, 5000);
```

### 3. 获取地图位置

```javascript
// 获取地图数据
function getMapLocations() {
  wx.getLocation({
    type: 'gcj02',
    success: function(res) {
      wx.request({
        url: 'https://your-domain.com/map/locations',
        method: 'POST',
        header: {
          'token': wx.getStorageSync('token'),
          'content-type': 'application/json'
        },
        data: {
          latitude: res.latitude,
          longitude: res.longitude,
          heading: 90.5,  // 仅司机端
          speed: 45.2     // 仅司机端
        },
        success: function(response) {
          if (response.data.isSucceed) {
            const { drivers, users, userType } = response.data.data;
            
            // 渲染地图标记
            renderMapMarkers(drivers, users, userType);
          }
        }
      });
    }
  });
}

// 渲染地图标记
function renderMapMarkers(drivers, users, userType) {
  let markers = [];
  
  // 添加司机标记
  drivers.forEach((driver, index) => {
    markers.push({
      id: index,
      latitude: driver.latitude,
      longitude: driver.longitude,
      iconPath: '/images/driver-icon.png',
      width: 30,
      height: 30,
      callout: {
        content: `${driver.name} - ${driver.plateNumber}`,
        display: 'ALWAYS'
      },
      customCallout: {
        display: 'ALWAYS'
      }
    });
  });
  
  // 添加乘客标记
  users.forEach((user, index) => {
    markers.push({
      id: drivers.length + index,
      latitude: user.latitude,
      longitude: user.longitude,
      iconPath: userType === 'user' ? '/images/my-icon.png' : '/images/user-icon.png',
      width: 25,
      height: 25,
      callout: {
        content: user.nickname,
        display: 'BYCLICK'
      }
    });
  });
  
  // 更新地图
  this.setData({ markers: markers });
}

// 定时刷新（每3秒）
setInterval(getMapLocations, 3000);
```

---

## 🔐 Token 使用说明

### Token 获取
通过 `/user/wx-login` 接口登录后，在响应的 `data.token` 字段获取

### Token 使用
在需要登录的接口请求头中添加：

```
token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Token 有效期
- **有效期：** 30天
- **自动续期：** 无（过期需重新登录）
- **多端策略：** 同一用户在新设备登录，旧设备token会失效

### Token 失效处理

```javascript
wx.request({
  url: 'https://your-domain.com/location/report',
  header: {
    'token': wx.getStorageSync('token')
  },
  fail: function(error) {
    if (error.code === 401) {
      // Token失效，重新登录
      wx.redirectTo({
        url: '/pages/login/login'
      });
    }
  }
});
```

---

## 📊 数据库表结构

### users 表（用户/司机综合表）

```sql
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `openid` varchar(64) NOT NULL COMMENT '微信OpenID',
  `user_type` varchar(20) NOT NULL DEFAULT 'user' COMMENT '用户类型: user-乘客, driver-司机',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码（BCrypt加密）',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `plate_number` varchar(20) DEFAULT NULL COMMENT '车牌号（司机专用）',
  `vehicle_desc` varchar(100) DEFAULT NULL COMMENT '车辆描述（司机专用）',
  `work_status` tinyint(1) DEFAULT 0 COMMENT '司机状态: 0-收车, 1-听单中, 2-服务中',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) DEFAULT '' COMMENT '修改人',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_user_type` (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户/司机综合表';
```

### user_locations 表（用户位置表）

```sql
CREATE TABLE `user_locations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '关联用户表ID',
  `latitude` decimal(10, 7) NOT NULL COMMENT '纬度',
  `longitude` decimal(10, 7) NOT NULL COMMENT '经度',
  `heading` float DEFAULT 0 COMMENT '车头朝向(0-360)',
  `speed` float DEFAULT 0 COMMENT '速度',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) DEFAULT '' COMMENT '修改人',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户实时位置';
```

### token 表（Token存储表）

```sql
CREATE TABLE `token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `client_type` varchar(20) NOT NULL DEFAULT 'WEB' COMMENT '客户端类型(WEB/TEAM)',
  `token` varchar(2048) NOT NULL COMMENT 'Token字符串',
  `issued_at` datetime NOT NULL COMMENT 'Token签发时间',
  `expire_time` datetime NOT NULL COMMENT 'Token过期时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) DEFAULT '' COMMENT '修改人',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_client` (`user_id`, `client_type`) USING BTREE,
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户Token在线状态表';
```

---

## ⚠️ 注意事项

### 1. 坐标系统
- 使用国测局坐标系（GCJ-02），也称为火星坐标系
- 微信小程序 `wx.getLocation` 的 type 设置为 `gcj02`

### 2. 权限要求
- 需要在小程序管理后台配置服务器域名（request合法域名）
- 需要用户授权位置权限

### 3. 性能优化
- 位置上报建议间隔：3-5秒
- 地图刷新建议间隔：3-5秒
- 已使用批量查询优化，支持大量用户同时在线

### 4. 安全性
- 所有接口都有完整的异常处理
- Token 存储在数据库，支持失效控制
- 位置数据有权限隔离（乘客看不到其他乘客）

---

## 📞 技术支持

如有问题，请联系开发团队。

**文档版本：** v1.0  
**更新时间：** 2026-01-10
