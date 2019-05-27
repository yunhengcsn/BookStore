# BookStore

### 后台功能

- 用户模块   
   + *注册*
   + *激活*
   + *登录*
   + *退出*
- 分类模块
   + *查询所有分类*
- 图书模块
   + *查询所有*
   + *按分类查*
   + *查询详情*
- 购物车模块
   + *添加*
   + *清空*
   + *删除*
   + *查询所有*
- 订单模块
   + *查询所有*
   + *生成订单*
   + *加载详情*
   + *确认收货*
- 分类管理（管理员权限）
   + *查询所有*
   + *增删改查*
- 图书管理（管理员权限）
   + *查询所有*
   + *加载详情*
   + *增删改查*
---
### 数据库结构

##### *admin*

| Field     | Type        | Null | Key | Default | Extra |
:--:|:--:|:--:|:--:|:--:|:--:|
| adminId   | char(32)    | NO   | PRI | NULL    |       |
| adminname | varchar(50) | YES  |     | NULL    |       |
| adminpwd  | varchar(50) | YES  |     | NULL    |       |

##### *book*

| Field       | Type         | Null | Key | Default | Extra          |
:--:|:--:|:--:|:--:|:--:|:--:|
| bid         | char(32)     | NO   | PRI | NULL    |                |
| bname       | varchar(200) | YES  |     | NULL    |                |
| author      | varchar(50)  | YES  |     | NULL    |                |
| price       | decimal(8,2) | YES  |     | NULL    |                |
| currPrice   | decimal(8,2) | YES  |     | NULL    |                |
| discount    | decimal(3,1) | YES  |     | NULL    |                |
| press       | varchar(100) | YES  |     | NULL    |                |
| publishtime | char(10)     | YES  |     | NULL    |                |
| edition     | int(11)      | YES  |     | NULL    |                |
| pageNum     | int(11)      | YES  |     | NULL    |                |
| wordNum     | int(11)      | YES  |     | NULL    |                |
| printtime   | char(10)     | YES  |     | NULL    |                |
| booksize    | int(11)      | YES  |     | NULL    |                |
| paper       | varchar(50)  | YES  |     | NULL    |                |
| cid         | char(32)     | YES  | MUL | NULL    |                |
| image_w     | varchar(100) | YES  |     | NULL    |                |
| image_b     | varchar(100) | YES  |     | NULL    |                |
| orderBy     | int(11)      | NO   | MUL | NULL    | auto_increment |

##### *cartitem*

| Field      | Type     | Null | Key | Default | Extra          |
:--:|:--:|:--:|:--:|:--:|:--:|
| cartItemId | char(32) | NO   | PRI | NULL    |                |
| quantity   | int(11)  | YES  |     | NULL    |                |
| bid        | char(32) | YES  | MUL | NULL    |                |
| uid        | char(32) | YES  | MUL | NULL    |                |
| orderBy    | int(11)  | NO   | MUL | NULL    | auto_increment |

##### *category*

| Field   | Type         | Null | Key | Default | Extra          |
:--:|:--:|:--:|:--:|:--:|:--:|
| cid     | char(32)     | NO   | PRI | NULL    |                |
| cname   | varchar(50)  | YES  | UNI | NULL    |                |
| pid     | char(32)     | YES  | MUL | NULL    |                |
| desc    | varchar(100) | YES  |     | NULL    |                |
| orderBy | int(11)      | NO   | MUL | NULL    | auto_increment |

##### *orderitem*

| Field       | Type         | Null | Key | Default | Extra |
:--:|:--:|:--:|:--:|:--:|:--:|
| orderItemId | char(32)     | NO   | PRI | NULL    |       |
| quantity    | int(11)      | YES  |     | NULL    |       |
| subtotal    | decimal(8,2) | YES  |     | NULL    |       |
| bid         | char(32)     | YES  |     | NULL    |       |
| bname       | varchar(200) | YES  |     | NULL    |       |
| currPrice   | decimal(8,2) | YES  |     | NULL    |       |
| image_b     | varchar(100) | YES  |     | NULL    |       |
| oid         | char(32)     | YES  | MUL | NULL    |       |

##### *orders*

| Field     | Type          | Null | Key | Default | Extra |
:--:|:--:|:--:|:--:|:--:|:--:|
| oid       | char(32)      | NO   | PRI | NULL    |       |
| ordertime | char(19)      | YES  |     | NULL    |       |
| total     | decimal(10,2) | YES  |     | NULL    |       |
| status    | int(11)       | YES  |     | NULL    |       |
| address   | varchar(1000) | YES  |     | NULL    |       |
| uid       | char(32)      | YES  | MUL | NULL    |       |

##### *user*

| Field          | Type        | Null | Key | Default | Extra |
:--:|:--:|:--:|:--:|:--:|:--:|
| uid            | char(32)    | NO   | PRI | NULL    |       |
| loginname      | varchar(50) | YES  | UNI | NULL    |       |
| loginpass      | varchar(50) | YES  |     | NULL    |       |
| email          | varchar(50) | YES  |     | NULL    |       |
| status         | tinyint(1)  | YES  |     | NULL    |       |
| activationCode | char(64)    | YES  |     | NULL    |       |
