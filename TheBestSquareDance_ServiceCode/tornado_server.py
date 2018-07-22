#coding=utf-8
#tornado_server.py
from tornado.wsgi import WSGIContainer
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop

from mongo import app


http_server = HTTPServer(WSGIContainer(app))
# http_server.listen(9000)  #此时，开启端口9000，默认本机127.0.0.1的IP地址
http_server.bind(80, "172.18.252.20")# 开启端口为9000，172.18.252.20为内网ip地址,也可以设置为0.0.0.0，
http_server.start(1)
IOLoop.instance().start()
