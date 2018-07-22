# -*- coding: utf-8 -*-
from flask_wtf import FlaskForm
from wtforms import StringField, SubmitField
from wtforms.ext.sqlalchemy.fields import QuerySelectField
from wtforms.validators import DataRequired

from ..models import Department, Role


class DepartmentForm(FlaskForm):
    """
    Form for admin to add or edit a department
    """
    name = StringField('Name', validators=[DataRequired()])
    description = StringField('passward', validators=[DataRequired()])
    submit = SubmitField('Submit')


class RoleForm(FlaskForm):
    """
    Form for admin to add or edit a role
    """
    name = StringField('Name', validators=[DataRequired()])
    description = StringField('Description', validators=[DataRequired()])
    submit = SubmitField('Submit')


class EmployeeAssignForm(FlaskForm):
    """
    Form for admin to assign departments and roles to employees
    """
    department = QuerySelectField(query_factory=lambda: Department.query.all(),
                                  get_label="name")
    role = QuerySelectField(query_factory=lambda: Role.query.all(),
                            get_label="name")
    submit = SubmitField('Submit')

class UserInfoForm(FlaskForm):
    """
    Form for admin to add or edit a userInfo
    """
    name = StringField(u'用户名', validators=[DataRequired()])
    passward = StringField(u'密码', validators=[DataRequired()])
    nickname = StringField(u'昵称', validators=[DataRequired()])
    tel = StringField(u'手机号', validators=[DataRequired()])
    head_picture = StringField(u'头像', validators=[DataRequired()])
    gender = StringField(u'性别', validators=[DataRequired()])
    region = StringField(u'地区', validators=[DataRequired()])
    signature = StringField(u'个性签名', validators=[DataRequired()])
    submit = SubmitField(u'提交')

class DanceGroupForm(FlaskForm):
    """
    Form for admin to add or edit a danceGroup
    """
    dance_name = StringField(u'舞群名称', validators=[DataRequired()])
    dance_region = StringField(u'所在地区', validators=[DataRequired()])
    detailed_address = StringField(u'详细地址', validators=[DataRequired()])
    charge_name = StringField(u'负责人姓名', validators=[DataRequired()])
    charge_tel = StringField(u'负责人电话', validators=[DataRequired()])
    longitude = StringField(u'所在地经度', validators=[DataRequired()])
    latitude = StringField(u'所在地纬度', validators=[DataRequired()])
    submit = SubmitField(u'提交')

class VideoForm(FlaskForm):
    """
    Form for admin to add or edit a video
    """
    video_name = StringField(u'视频名称', validators=[DataRequired()])
    video_address = StringField(u'视频链接地址', validators=[DataRequired()])
    video_picture = StringField(u'视频首页图片', validators=[DataRequired()])
    release_name = StringField(u'发布人姓名', validators=[DataRequired()])
    praise_num = StringField(u'获点赞数量', validators=[DataRequired()])
    collection_name = StringField(u'收藏人用户名', validators=[DataRequired()])
    upload_name = StringField(u'上传人用户名', validators=[DataRequired()])
    download_name = StringField(u'下载人用户名', validators=[DataRequired()])
    submit = SubmitField(u'提交')

class GoodsForm(FlaskForm):
    """
    Form for admin to add or edit a goods
    """
    shop_introduction = StringField(u'店铺介绍', validators=[DataRequired()])
    goods_name = StringField(u'商品名称', validators=[DataRequired()])
    goods_weight = StringField(u'商品重量', validators=[DataRequired()])
    goods_price = StringField(u'商品价位', validators=[DataRequired()])
    shop_tel = StringField(u'商家联系电话', validators=[DataRequired()])
    shop_address = StringField(u'商家所在地', validators=[DataRequired()])
    goods_pciture = StringField(u'商品图片', validators=[DataRequired()])
    submit = SubmitField(u'提交')

class CommentForm(FlaskForm):
    """
    Form for admin to add or edit a comment
    """
    comment_videoname = StringField(u'评论视频名称', validators=[DataRequired()])
    comment_nickname = StringField(u'评论人昵称', validators=[DataRequired()])
    reply_nickname = StringField(u'回复人昵称', validators=[DataRequired()])
    comment_content = StringField(u'评论内容', validators=[DataRequired()])
    comment_gender = StringField(u'评论人性别', validators=[DataRequired()])
    comment_address = StringField(u'评论人所在地', validators=[DataRequired()])
    comment_time = StringField(u'评论发表时间', validators=[DataRequired()])
    submit = SubmitField(u'提交')