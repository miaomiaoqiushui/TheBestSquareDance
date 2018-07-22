#coding=utf-8
#mongo.py
from flask import Flask,abort
from flask import jsonify
from flask import request, render_template
from flask_pymongo import PyMongo
import time
import os
import base64


app = Flask(__name__)

app.config['MONGO_DBNAME'] = 'app'
app.config['MONGO_URI'] = 'mongodb://127.0.0.1:27017/app'  #如果部署在本上，其中ip地址可填127.0.0.1

UPLOAD_FOLDER = 'C:\mySoftWare\Apache24\htdocs\upload'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
basedir = os.path.abspath(os.path.dirname(__file__))
ALLOWED_EXTENSIONS = set(['txt', 'png', 'jpg', 'xls', 'JPG', 'PNG', 'xlsx', 'gif', 'GIF','mp4'])

mongo = PyMongo(app)

@app.route('/login', methods=['GET'])
def get_all_users():
  userinfos = mongo.db.userInfo.find()
  output = []
  for s in userinfos:
    output.append({'name' : s['name'], 'passward' : s['passward']})
  return jsonify(output)


@app.route('/register', methods=['POST'])
def add_user():
  userinfos = mongo.db.userInfo
  name = request.json['name']
  pwd = request.json['passward']
  nickname = request.json['nickname']
  star_id = userinfos.insert({'name': name, 'passward': pwd,'nickname': nickname,'tel':"null",'head_pictur':"null",'gender':"null",'region':"null",'signature':"null"})
  new_star = userinfos.find_one({'_id': star_id })
  output = {'name' : new_star['name'], 'passward' : new_star['passward'],'nickname':new_star['nickname']}
  return jsonify(output)

@app.route('/dancegroup_add', methods=['POST'])
def add_dancegroup():
  dancegroup = mongo.db.danceGroup
  dance_name = request.json['dance_name']
  dance_region = request.json['dance_region']
  detailed_address = request.json['detailed_address']
  charge_name = request.json['charge_name']
  charge_tel = request.json['charge_tel']
  star_id = dancegroup.insert({'dance_name': dance_name, 'dance_region': dance_region,'detailed_address': detailed_address,
                               'charge_name':charge_name,'charge_tel':charge_tel,'longitude':"null",'latitude':"null"})
  new_star = dancegroup.find_one({'_id': star_id })
  output = {'dance_name' : new_star['dance_name'], 'dance_region' : new_star['dance_region'],'detailed_address':new_star['detailed_address']}
  return jsonify({'result' : output})

@app.route('/dancegroup_get_all', methods=['GET'])
def get_all_dancegroups():
    dancegroups = mongo.db.danceGroup.find()
    output = []
    for s in dancegroups:
        output.append({'dance_name': s['dance_name'], 'longitude': s['longitude'], 'latitude': s['latitude']})
    return jsonify({'result' : output})

@app.route('/shop_get_goods', methods=['GET'])
def get_all_goods():
  goods = mongo.db.goods.find()
  output = []
  for s in goods:
      output.append({'shop_introduction': s['shop_introduction'], 'goods_name ': s['goods_name '],
                     'goods_weight': s['goods_weight'], 'goods_price': s['goods_price'],
                     'shop_tel': s['shop_tel'], 'goods_pciture': s['goods_pciture']})
  return jsonify({'result' : output})


@app.route('/modify/<string:name>', methods=['PUT'])
def update_user(name):
    user = mongo.db.userInfo.find({"name":name})
    output = []
    for s in user:
      output.append({'name': s['name'], 'pwd': s['pwd']})
    if len(output) == 0:
      abort(404)
    mongo.db.userInfo.update({"name":name},{'$set':{"name":"LZ111"}})
    return jsonify({'result': output})

@app.route('/delete/<string:name>', methods=['DELETE'])
def delete_user(name):
    user = mongo.db.userInfo.find({"name": name})
    output = []
    for s in user:
      output.append({'name': s['name'], 'pwd': s['pwd']})
    if len(output) == 0:
      abort(404)
    mongo.db.userInfo.remove({'name': name})
    return jsonify({'result': True})



# 用于判断文件后缀
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

# 上传文件
@app.route('/api/upload', methods=['POST'], strict_slashes=False)
def api_upload():
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    f = request.files['myfile']  # 从表单的file字段获取文件，myfile为该表单的name值
    if f and allowed_file(f.filename):  # 判断是否是允许上传的文件类型
        fname = f.filename
        print fname
        ext = fname.rsplit('.', 1)[1]  # 获取文件后缀
        unix_time = int(time.time())
        new_filename = str(unix_time) + '.' + ext  # 修改了上传的文件名
        f.save(os.path.join(file_dir, new_filename))  # 保存文件到upload目录
        print new_filename
        token = base64.b64encode(new_filename)
        print token
        return jsonify({"errno": 0, "errmsg": "上传成功", "token": token})
    else:
        return jsonify({"errno": 1001, "errmsg": "上传失败"})


if __name__ == '__main__':
    # app.run(host = '0.0.0.0', port = 80, debug = True)
    app.run()