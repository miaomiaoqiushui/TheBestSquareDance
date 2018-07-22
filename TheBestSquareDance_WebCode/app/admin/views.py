# -*- coding: utf-8 -*-
from flask import abort, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from . import admin
from forms import DepartmentForm, EmployeeAssignForm, RoleForm, UserInfoForm,DanceGroupForm,VideoForm,GoodsForm,CommentForm
from .. import db, app,mongo
from ..models import Department, Employee, Role
from flask_pymongo import PyMongo
from flask import jsonify,json





def check_admin():
    # prevent non-admins from accessing the page
    if not current_user.is_admin:
        abort(403)

# UserInfo Views

@admin.route('/userinfos', methods=['GET', 'POST'])
@login_required
def list_userinfos():
    """
    List all departments
    """
    check_admin()

    userinfos = mongo.db.userInfo.find({})

    return render_template('admin/userinfos/userinfos.html',
                           userinfos=userinfos, title=u"用户信息")

@admin.route('/userinfos/add', methods=['GET', 'POST'])
@login_required
def add_userinfo():
    """
    Add a userinfo to the database
    """
    check_admin()

    add_userinfo = True

    form = UserInfoForm()
    if form.validate_on_submit():
        userinfo = mongo.db.userInfo
        # userinfo = UserInfo(name=form.name.data,
        #                         description=form.description.data)
        try:
            # add department to the database
            userinfo.insert({'name': form.name.data, 'passward': form.passward.data,'nickname':form.nickname.data, 'tel':form.tel.data, 'head_picture':form.head_picture.data,
                             'gender':form.gender.data,'region':form.region.data,'signature':form.signature.data})
            flash(u'您已经成功添加一个用户信息')
        except:
            # in case department name already exists
            flash(u'错误: 该用户信息已存在')

        # redirect to departments page
        return redirect(url_for('admin.list_userinfos'))

    # load department template
    return render_template('admin/userinfos/userinfo.html', action="Add",
                           add_userinfo=add_userinfo, form=form,
                           title=u"添加用户信息")


class UserInfoArray(object):
    def __init__(self, name, passward, nickname, tel, head_picture, gender, region, signature):
        self.name = name
        self.passward = passward
        self.nickname = nickname
        self.tel = tel
        self.head_picture = head_picture
        self.gender = gender
        self.region = region
        self.signature = signature

@admin.route('/userinfos/edit/<string:name>', methods=['GET', 'POST'])
@login_required
def edit_userinfo(name):
    """
    Edit a userinfo
    """
    check_admin()

    add_userinfo = False

    userinfo = mongo.db.userInfo.find({"name":name})
    userinfos = mongo.db.userInfo
    # userinfos.remove({'name': name})
    edit_user = {'name': u"待赋值", 'passward': u"待赋值",'nickname':u"待赋值", 'tel':u"待赋值", 'head_picture':u"待赋值",
                             'gender':u"待赋值",'region':u"待赋值",'signature':u"待赋值"}
    userinfoarray = UserInfoArray(name=u"待赋值", passward=u"待赋值", nickname=u"待赋值", tel=u"待赋值", head_picture=u"待赋值",
                                  gender=u"待赋值", region=u"待赋值", signature=u"待赋值")
    for s in userinfo:
        userinfoarray.name = s['name']
        userinfoarray.passward = s['passward']
        userinfoarray.nickname = s['nickname']
        userinfoarray.tel = s['tel']
        userinfoarray.head_picture = s['head_picture']
        userinfoarray.gender = s['gender']
        userinfoarray.region = s['region']
        userinfoarray.signature = s['signature']

    form = UserInfoForm(obj=userinfoarray)
    userinfos.remove({'name': name})
    if form.validate_on_submit():
        edit_user['name'] = form.name.data
        edit_user['passward'] = form.passward.data
        edit_user['nickname'] = form.nickname.data
        edit_user['tel'] = form.tel.data
        edit_user['head_picture'] = form.head_picture.data
        edit_user['gender'] = form.gender.data
        edit_user['region'] = form.region.data
        edit_user['signature'] = form.signature.data
        # db.session.commit()
        userinfos.insert(edit_user)
        flash(u'您已经成功修改用户信息')

        # redirect to the duserinfos page
        return redirect(url_for('admin.list_userinfos'))
    for s in userinfo:
       form.passward.data = s['passward']
       form.name.data = s['name']
    # form.passward.data = userinfo[0]['passward']
    # form.name.data = userinfo[0]['name']
    return render_template('admin/userinfos/userinfo.html', action="Edit",
                           add_userinfo=add_userinfo, form=form,
                           userinfo=userinfo, title=u"修改用户信息")


@admin.route('/userinfos/delete/<string:name>', methods=['GET', 'POST'])
@login_required
def delete_userinfo(name):
    """
    Delete a userinfo from the database
    """
    check_admin()

    # userinfo = mongo.db.userInfo.find({"name": name})
    userinfos = mongo.db.userInfo
    userinfos.remove({'name': name})

    # department = Department.query.get_or_404(id)
    # db.session.delete(department)
    # db.session.commit()
    flash(u'您已成功删除该用户信息')

    # redirect to the userinfos page
    return redirect(url_for('admin.list_userinfos'))

    # return render_template(title=u"删除用户信息")



# DanceGroup Views

@admin.route('/dancegroups', methods=['GET', 'POST'])
@login_required
def list_dancegroups():
    """
    List all dancegroups
    """
    check_admin()
    dancegroups = mongo.db.danceGroup.find({})
    return render_template('admin/dancegroups/dancegroups.html',
                           dancegroups=dancegroups, title=u"舞群信息")

@admin.route('/dancegroups/add', methods=['GET', 'POST'])
@login_required
def add_dancegroup():
    """
    Add a dancegroup to the database
    """
    check_admin()
    add_dancegroup = True
    form = DanceGroupForm()
    if form.validate_on_submit():
        dancegroup = mongo.db.danceGroup
        try:
            # add department to the database
            dancegroup.insert({'dance_name': form.dance_name.data, 'dance_region': form.dance_region.data,'detailed_address':form.detailed_address.data, 'charge_name':
                form.charge_name.data, 'charge_tel':form.charge_tel.data,
                             'longitude':form.longitude.data,'latitude':form.latitude.data})
            flash(u'您已经成功添加一个舞群信息')
        except:
            # in case department name already exists
            flash(u'错误: 该舞裙信息已被申请')

        # redirect to departments page
        return redirect(url_for('admin.list_dancegroups'))

    # load department template
    return render_template('admin/dancegroups/dancegroup.html', action="Add",
                           add_dancegroup=add_dancegroup, form=form,
                           title=u"添加舞群信息")

class DanceGroupArray(object):
    def __init__(self, dance_name, dance_region, detailed_address, charge_name, charge_tel, longitude, latitude):
        self.dance_name = dance_name
        self.dance_region = dance_region
        self.detailed_address = detailed_address
        self.charge_name = charge_name
        self.charge_tel = charge_tel
        self.longitude = longitude
        self.latitude = latitude

@admin.route('/dancegroups/edit/<string:name>', methods=['GET', 'POST'])
@login_required
def edit_dancegroup(name):
    """
    Edit a dancegroup
    """
    check_admin()
    add_dancegroup = False
    dancegroup = mongo.db.danceGroup.find({"dance_name":name})
    dancegroups = mongo.db.danceGroup
    edit_dancegroup = {'dance_name': u"待赋值", 'dance_region': u"待赋值",'detailed_address':u"待赋值", 'charge_name':u"待赋值", 'charge_tel':u"待赋值",
                             'longitude':u"待赋值",'latitude':u"待赋值"}
    dancegrouparray = DanceGroupArray(dance_name=u"待赋值", dance_region=u"待赋值", detailed_address=u"待赋值", charge_name=u"待赋值", charge_tel=u"待赋值",
                                      longitude=u"待赋值", latitude=u"待赋值")
    for s in dancegroup:
        dancegrouparray.dance_name = s['dance_name']
        dancegrouparray. dance_region = s['dance_region']
        dancegrouparray.detailed_address = s['detailed_address']
        dancegrouparray.charge_name = s['charge_name']
        dancegrouparray.charge_tel = s['charge_tel']
        dancegrouparray.longitude = s['longitude']
        dancegrouparray.latitude = s['latitude']

    form = DanceGroupForm(obj=dancegrouparray)
    dancegroups.remove({'dance_name': name})
    if form.validate_on_submit():
        edit_dancegroup['dance_name'] = form.dance_name.data
        edit_dancegroup['dance_region'] = form.dance_region.data
        edit_dancegroup['detailed_address'] = form.detailed_address.data
        edit_dancegroup['charge_name'] = form.charge_name.data
        edit_dancegroup['charge_tel'] = form.charge_tel.data
        edit_dancegroup['longitude'] = form.longitude.data
        edit_dancegroup['latitude'] = form.latitude.data
        dancegroups.insert(edit_dancegroup)
        flash(u'您已经成功修改舞群信息')

        # redirect to the dancegroups page
        return redirect(url_for('admin.list_dancegroups'))
    for s in dancegroup:
       form.dance_name.data = s['dance_name']
       form.dance_region.data = s['dance_region']

    return render_template('admin/dancegroups/dancegroup.html', action="Edit",
                           add_dancegroup=add_dancegroup, form=form,
                           dancegroup=dancegroup, title=u"修改舞群信息")


@admin.route('/dancegroups/delete/<string:name>', methods=['GET', 'POST'])
@login_required
def delete_dancegroup(name):
    """
    Delete a dancegroup from the database
    """
    check_admin()
    dancegroups = mongo.db.danceGroup
    dancegroups.remove({'dance_name': name})

    flash(u'您已成功删除该舞群信息')

    # redirect to the dancegroups page
    return redirect(url_for('admin.list_dancegroups'))



# Video Views

@admin.route('/videos', methods=['GET', 'POST'])
@login_required
def list_videos():
    """
    List all departments
    """
    check_admin()
    videos = mongo.db.video.find({})
    return render_template('admin/videos/videos.html',
                           videos=videos, title=u"视频信息")

@admin.route('/videos/add', methods=['GET', 'POST'])
@login_required
def add_video():
    """
    Add a video to the database
    """
    check_admin()
    add_video = True
    form = VideoForm()
    if form.validate_on_submit():
        video = mongo.db.video
        try:
            # add department to the database
            video.insert({'video_name': form.video_name.data, 'video_address': form.video_address.data,'video_picture':form.video_picture.data, 'release_name':
                form.release_name.data, 'praise_num':form.praise_num.data,
                             'collection_name':form.collection_name.data,'upload_name':form.upload_name.data,'download_name':form.download_name.data})
            flash(u'您已经成功添加一个视频信息')
        except:
            # in case department name already exists
            flash(u'错误: 该视频信息已存在')

        # redirect to departments page
        return redirect(url_for('admin.list_videos'))

    # load department template
    return render_template('admin/videos/video.html', action="Add",
                           add_video=add_video, form=form,
                           title=u"添加视频信息")

class VideoArray(object):
    def __init__(self, video_name, video_address, video_picture, release_name, praise_num, collection_name, upload_name, download_name):
        self.video_name = video_name
        self.video_address = video_address
        self.video_picture = video_picture
        self.release_name = release_name
        self.praise_num = praise_num
        self.collection_name = collection_name
        self.upload_name = upload_name
        self.download_name = download_name

@admin.route('/videos/edit/<string:name>', methods=['GET', 'POST'])
@login_required
def edit_video(name):
    """
    Edit a video
    """
    check_admin()
    add_video = False
    video = mongo.db.video.find({"video_name":name})
    videos = mongo.db.video
    edit_video = {'video_name': u"待赋值", 'video_address': u"待赋值",'video_picture':u"待赋值", 'release_name':u"待赋值", 'praise_num':u"待赋值",
                             'collection_name':u"待赋值",'upload_name':u"待赋值",'download_name':u"待赋值"}
    videoarray =  VideoArray(video_name=u"待赋值", video_address=u"待赋值", video_picture=u"待赋值", release_name=u"待赋值", praise_num=u"待赋值",
                             collection_name=u"待赋值", upload_name=u"待赋值", download_name=u"待赋值")
    for s in video:
        videoarray.video_name = s['video_name']
        videoarray.video_address = s['video_address']
        videoarray.video_picture = s['video_picture']
        videoarray.release_name = s['release_name']
        videoarray.praise_num = s['praise_num']
        videoarray.collection_name = s['collection_name']
        videoarray.upload_name = s['upload_name']
        videoarray.download_name = s['download_name']

    form = VideoForm(obj=videoarray)
    videos.remove({'video_name': name})
    if form.validate_on_submit():
        edit_video['video_name'] = form.video_name.data
        edit_video['video_address'] = form.video_address.data
        edit_video['video_picture'] = form.video_picture.data
        edit_video['release_name'] = form.release_name.data
        edit_video['praise_num'] = form.praise_num.data
        edit_video['collection_name'] = form.collection_name.data
        edit_video['upload_name'] = form.upload_name.data
        edit_video['download_name'] = form.download_name.data
        videos.insert(edit_video)
        flash(u'您已经成功修改视频信息')

        # redirect to the dancegroups page
        return redirect(url_for('admin.list_videos'))
    for s in video:
       form.video_name.data = s['video_name']
       form.video_address.data = s['video_address']

    return render_template('admin/videos/video.html', action="Edit",
                           add_video=add_video, form=form,
                           video=video, title=u"修改舞群信息")

@admin.route('/videos/delete/<string:name>', methods=['GET', 'POST'])
@login_required
def delete_video(name):
    """
    Delete a video from the database
    """
    check_admin()
    videos = mongo.db.video
    videos.remove({'video_name': name})

    flash(u'您已成功删除该舞群信息')

    # redirect to the dancegroups page
    return redirect(url_for('admin.list_videos'))



# Goods Views

@admin.route('/goods', methods=['GET', 'POST'])
@login_required
def list_goods():
    """
    List all departments
    """
    check_admin()
    goods = mongo.db.goods.find({})
    return render_template('admin/goods/goods.html',
                           goods=goods, title=u"商品信息")

@admin.route('/goods/add', methods=['GET', 'POST'])
@login_required
def add_good():
    """
    Add a good to the database
    """
    check_admin()
    add_good = True
    form = GoodsForm()
    if form.validate_on_submit():
        goods = mongo.db.goods
        try:
            # add good to the database
            goods.insert({'shop_introduction': form.shop_introduction.data, 'goods_name': form.goods_name.data,'goods_weight':form.goods_weight.data, 'goods_price':
                form.goods_price.data, 'shop_tel':form.shop_tel.data,
                             'shop_address':form.shop_address.data,'goods_pciture':form.goods_pciture.data})
            flash(u'您已经成功添加一个商品信息')
        except:
            # in case goods name already exists
            flash(u'错误: 该商品信息已存在')

        # redirect to goods page
        return redirect(url_for('admin.list_goods'))

    # load goods template
    return render_template('admin/goods/good.html', action="Add",
                           add_good=add_good, form=form,
                           title=u"添加商品信息")

class GoodsArray(object):
    def __init__(self, shop_introduction, goods_name, goods_weight, goods_price,shop_tel, shop_address, goods_pciture):
        self.shop_introduction = shop_introduction
        self.goods_name = goods_name
        self.goods_weight = goods_weight
        self.goods_price = goods_price
        self.shop_tel = shop_tel
        self.shop_address = shop_address
        self.goods_pciture = goods_pciture


@admin.route('/goods/edit/<string:name>', methods=['GET', 'POST'])
@login_required
def edit_good(name):
    """
    Edit a good
    """
    check_admin()
    add_good = False
    good = mongo.db.goods.find({"goods_name":name})
    goods = mongo.db.goods
    edit_good = {'shop_introduction': u"待赋值", 'goods_name': u"待赋值",'goods_weight':u"待赋值", 'goods_price':u"待赋值", 'shop_tel':u"待赋值",
                             'shop_address':u"待赋值",'goods_pciture':u"待赋值"}
    goodarray =  GoodsArray(shop_introduction=u"待赋值", goods_name=u"待赋值", goods_weight=u"待赋值", goods_price=u"待赋值", shop_tel=u"待赋值",
                             shop_address=u"待赋值", goods_pciture=u"待赋值")
    for s in good:
        goodarray.shop_introduction = s['shop_introduction']
        goodarray.goods_name = s['goods_name']
        goodarray.goods_weight = s['goods_weight']
        goodarray.goods_price = s['goods_price']
        goodarray.shop_tel = s['shop_tel']
        goodarray.shop_address = s['shop_address']
        goodarray.goods_pciture = s['goods_pciture']

    form = GoodsForm(obj=goodarray)
    goods.remove({'goods_name': name})
    if form.validate_on_submit():
        edit_good['shop_introduction'] = form.shop_introduction.data
        edit_good['goods_name'] = form.goods_name.data
        edit_good['goods_weight'] = form.goods_weight.data
        edit_good['goods_price'] = form.goods_price.data
        edit_good['shop_tel'] = form.shop_tel.data
        edit_good['shop_address'] = form.shop_address.data
        edit_good['goods_pciture'] = form.goods_pciture.data
        goods.insert(edit_good)
        flash(u'您已经成功修改商品信息')

        # redirect to the goods page
        return redirect(url_for('admin.list_goods'))
    for s in good:
       form.shop_introduction.data = s['shop_introduction']
       form.goods_price.data = s['goods_price']

    return render_template('admin/goods/good.html', action="Edit",
                           add_good=add_good, form=form,
                           good=good, title=u"修改商品信息")

@admin.route('/goods/delete/<string:name>', methods=['GET', 'POST'])
@login_required
def delete_good(name):
    """
    Delete a good from the database
    """
    check_admin()
    goods = mongo.db.goods
    goods.remove({'goods_name': name})

    flash(u'您已成功删除该商品信息')

    # redirect to the goods page
    return redirect(url_for('admin.list_goods'))


# Comment Views

@admin.route('/comments', methods=['GET', 'POST'])
@login_required
def list_comments():
    """
    List all departments
    """
    check_admin()
    comments = mongo.db.comments.find({})
    return render_template('admin/comments/comments.html',
                           comments=comments, title=u"评论信息")

@admin.route('/comments/add', methods=['GET', 'POST'])
@login_required
def add_comment():
    """
    Add a comment to the database
    """
    check_admin()
    add_comment = True
    form = CommentForm()
    if form.validate_on_submit():
        comments = mongo.db.comments
        try:
            # add comment to the database
            comments.insert({'comment_videoname': form.comment_videoname.data, 'comment_nickname': form.comment_nickname.data,'reply_nickname':form.reply_nickname.data,
                             'comment_content':
                form.comment_content.data, 'comment_gender':form.comment_gender.data,
                             'comment_address':form.comment_address.data,'comment_time':form.comment_time.data})
            flash(u'您已经成功添加一个评论信息')
        except:
            # in case comment name already exists
            flash(u'错误: 该评论信息已存在')

        # redirect to comment page
        return redirect(url_for('admin.list_comments'))

    # load comment template
    return render_template('admin/comments/comment.html', action="Add",
                           add_comment=add_comment, form=form,
                           title=u"添加评论信息")

class CommentsArray(object):
    def __init__(self, comment_videoname, comment_nickname, reply_nickname, comment_content, comment_gender, comment_address,comment_time):
        self.comment_videoname = comment_videoname
        self.comment_nickname = comment_nickname
        self.reply_nickname = reply_nickname
        self.comment_content= comment_content
        self.comment_gender = comment_gender
        self.comment_address = comment_address
        self.comment_time = comment_time



@admin.route('/comments/edit/<string:name>', methods=['GET', 'POST'])
@login_required
def edit_comment(name):
    """
    Edit a comment
    """
    check_admin()
    add_comment = False
    comment = mongo.db.comments.find({"comment_nickname":name})
    comments = mongo.db.comments
    edit_comment = {'comment_videoname': u"待赋值", 'comment_nickname': u"待赋值",'reply_nickname':u"待赋值", 'comment_content':u"待赋值", 'comment_gender':u"待赋值",
                             'comment_address':u"待赋值",'comment_time':u"待赋值"}
    commentarray =  CommentsArray(comment_videoname=u"待赋值", comment_nickname=u"待赋值", reply_nickname=u"待赋值", comment_content=u"待赋值", comment_gender=u"待赋值",
                           comment_address=u"待赋值", comment_time=u"待赋值")
    for s in comment:
        commentarray.comment_videoname = s['comment_videoname']
        commentarray.comment_nickname = s['comment_nickname']
        commentarray.reply_nickname = s['reply_nickname']
        commentarray.comment_content = s['comment_content']
        commentarray.comment_gender = s['comment_gender']
        commentarray.comment_address = s['comment_address']
        commentarray.comment_time = s['comment_time']

    form = CommentForm(obj=commentarray)
    comments.remove({'comment_nickname': name})
    if form.validate_on_submit():
        edit_comment['comment_videoname'] = form.comment_videoname.data
        edit_comment['comment_nickname'] = form.comment_nickname.data
        edit_comment['reply_nickname'] = form.reply_nickname.data
        edit_comment['comment_content'] = form.comment_content.data
        edit_comment['comment_gender'] = form.comment_gender.data
        edit_comment['comment_address'] = form.comment_address.data
        edit_comment['comment_time'] = form.comment_time.data
        comments.insert(edit_comment)
        flash(u'您已经成功修改评论信息')

        # redirect to the comment page
        return redirect(url_for('admin.list_comments'))
    for s in comment:
       form.comment_videoname.data = s['comment_videoname']
       form.comment_nickname.data = s['comment_nickname']

    return render_template('admin/comments/comment.html', action="Edit",
                           add_comment=add_comment, form=form,
                           comment=comment, title=u"修改评论信息")


@admin.route('/comments/delete/<string:name>', methods=['GET', 'POST'])
@login_required
def delete_comment(name):
    """
    Delete a comment from the database
    """
    check_admin()
    comments = mongo.db.comments
    comments.remove({'comment_nickname': name})

    flash(u'您已成功删除该评论信息')

    # redirect to the comment page
    return redirect(url_for('admin.list_comments'))

# Department Views


@admin.route('/departments', methods=['GET', 'POST'])
@login_required
def list_departments():
    """
    List all departments
    """
    check_admin()

    departments = Department.query.all()

    return render_template('admin/departments/departments.html',
                           departments=departments, title=u"用户信息")


@admin.route('/departments/add', methods=['GET', 'POST'])
@login_required
def add_department():
    """
    Add a department to the database
    """
    check_admin()

    add_department = True

    form = DepartmentForm()
    if form.validate_on_submit():
        department = Department(name=form.name.data,
                                description=form.description.data)
        try:
            # add department to the database
            db.session.add(department)
            db.session.commit()
            flash('You have successfully added a new department.')
        except:
            # in case department name already exists
            flash('Error: department name already exists.')

        # redirect to departments page
        return redirect(url_for('admin.list_departments'))

    # load department template
    return render_template('admin/departments/department.html', action="Add",
                           add_department=add_department, form=form,
                           title="Add Department")


@admin.route('/departments/edit/<int:id>', methods=['GET', 'POST'])
@login_required
def edit_department(id):
    """
    Edit a department
    """
    check_admin()

    add_department = False

    department = Department.query.get_or_404(id)
    form = DepartmentForm(obj=department)
    if form.validate_on_submit():
        department.name = form.name.data
        department.description = form.description.data
        db.session.commit()
        flash('You have successfully edited the department.')

        # redirect to the departments page
        return redirect(url_for('admin.list_departments'))

    form.description.data = department.description
    form.name.data = department.name
    return render_template('admin/departments/department.html', action="Edit",
                           add_department=add_department, form=form,
                           department=department, title="Edit Department")


@admin.route('/departments/delete/<int:id>', methods=['GET', 'POST'])
@login_required
def delete_department(id):
    """
    Delete a department from the database
    """
    check_admin()

    department = Department.query.get_or_404(id)
    db.session.delete(department)
    db.session.commit()
    flash('You have successfully deleted the department.')

    # redirect to the departments page
    return redirect(url_for('admin.list_departments'))

    return render_template(title="Delete Department")


# Role Views


@admin.route('/roles')
@login_required
def list_roles():
    check_admin()
    """
    List all roles
    """
    roles = Role.query.all()
    return render_template('admin/roles/roles.html',
                           roles=roles, title='Roles')


@admin.route('/roles/add', methods=['GET', 'POST'])
@login_required
def add_role():
    """
    Add a role to the database
    """
    check_admin()

    add_role = True

    form = RoleForm()
    if form.validate_on_submit():
        role = Role(name=form.name.data,
                    description=form.description.data)

        try:
            # add role to the database
            db.session.add(role)
            db.session.commit()
            flash('You have successfully added a new role.')
        except:
            # in case role name already exists
            flash('Error: role name already exists.')

        # redirect to the roles page
        return redirect(url_for('admin.list_roles'))

    # load role template
    return render_template('admin/roles/role.html', add_role=add_role,
                           form=form, title='Add Role')


@admin.route('/roles/edit/<int:id>', methods=['GET', 'POST'])
@login_required
def edit_role(id):
    """
    Edit a role
    """
    check_admin()

    add_role = False

    role = Role.query.get_or_404(id)
    form = RoleForm(obj=role)
    if form.validate_on_submit():
        role.name = form.name.data
        role.description = form.description.data
        db.session.add(role)
        db.session.commit()
        flash('You have successfully edited the role.')

        # redirect to the roles page
        return redirect(url_for('admin.list_roles'))

    form.description.data = role.description
    form.name.data = role.name
    return render_template('admin/roles/role.html', add_role=add_role,
                           form=form, title="Edit Role")


@admin.route('/roles/delete/<int:id>', methods=['GET', 'POST'])
@login_required
def delete_role(id):
    """
    Delete a role from the database
    """
    check_admin()

    role = Role.query.get_or_404(id)
    db.session.delete(role)
    db.session.commit()
    flash('You have successfully deleted the role.')

    # redirect to the roles page
    return redirect(url_for('admin.list_roles'))

    return render_template(title="Delete Role")


# Employee Views

@admin.route('/employees')
@login_required
def list_employees():
    """
    List all employees
    """
    check_admin()

    employees = Employee.query.all()
    return render_template('admin/employees/employees.html',
                           employees=employees, title='Employees')


@admin.route('/employees/assign/<int:id>', methods=['GET', 'POST'])
@login_required
def assign_employee(id):
    """
    Assign a department and a role to an employee
    """
    check_admin()

    employee = Employee.query.get_or_404(id)

    # prevent admin from being assigned a department or role
    if employee.is_admin:
        abort(403)

    form = EmployeeAssignForm(obj=employee)
    if form.validate_on_submit():
        employee.department = form.department.data
        employee.role = form.role.data
        db.session.add(employee)
        db.session.commit()
        flash('You have successfully assigned a department and role.')

        # redirect to the roles page
        return redirect(url_for('admin.list_employees'))

    return render_template('admin/employees/employee.html',
                           employee=employee, form=form,
                           title='Assign Employee')