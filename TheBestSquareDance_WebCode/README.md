## 参考资料
1.[Build a CRUD Web App With Python and Flask - Part One](https://scotch.io/tutorials/build-a-crud-web-app-with-python-and-flask-part-one)

2.[Build a CRUD Web App With Python and Flask - Part Two](https://scotch.io/tutorials/build-a-crud-web-app-with-python-and-flask-part-two)

## 启动方式
如果是Linux/Mac系统，打开bash按如下操作
```
$export FLASK_CONFIG=development
$export FLASK_APP=run.py
$ flask run
 * Serving Flask app "run"
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
```

如果是Windows系统，cmd进入Python安装的Scripts目录，输入activate;然后，切换到项目根目录依次输入
```
> set FLASK_CONFIG=development
> set FLASK_APP=run.py
> flask run
 * Serving Flask app "run"
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
```

## 创建数据库
查看项目目录instance中config.py文件
```
# instance/config.py

SECRET_KEY = 'p9Bv<3Eid9%$i01'
SQLALCHEMY_DATABASE_URI = 'mysql://root:root@localhost/book'
```
其中第一个root是数据库登陆用户名，第2个root是数据库登陆密码，book是数据库名称，这个book数据库要提前在MySQL数据库中创建好，否则创建数据库表时会报错。
进入之前cmd打开flask run服务的窗口，按下CTRL+C停止服务，输入如下命令：
```
>flask db init
```
然后输入：
```
>flask db migrate
```
最后输入：
```
>flask db upgrade
```

## 添加管理员账户
进入上面的cmd窗口，输入如下命令：
```
>flask shell
>>> from app.models import Employee
>>> from app import db
>>> admin = Employee(email="admin@admin.com",username="admin",password="admin2016",is_admin=True)
>>> db.session.add(admin)
>>> db.session.commit()
```
表示，插入一个用户邮箱为admin@admin.com，用户名为admin，用户密码为admin2016的管理员账户。
