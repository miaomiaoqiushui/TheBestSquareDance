# -*- coding: utf-8 -*-
# app/auth/forms.py

from flask_wtf import FlaskForm
from wtforms import PasswordField, StringField, SubmitField, ValidationError
from wtforms.validators import DataRequired, Email, EqualTo

from ..models import Employee

class RegistrationForm(FlaskForm):
    """
    Form for users to create new account
    """
    email = StringField(u'邮箱', validators=[DataRequired(), Email()])
    username = StringField(u'用户名', validators=[DataRequired()])
    user_tel = StringField(u'联系电话', validators=[DataRequired()])
    user_address = StringField(u'联系地址', validators=[DataRequired()])
    password = PasswordField(u'密码', validators=[DataRequired(), EqualTo('confirm_password')])
    confirm_password = PasswordField(u'确认密码')
    submit = SubmitField(u'注册')

    # def validate_email(self, field):
    #     if Employee.query.filter_by(email=field.data).first():
    #         raise ValidationError(u'邮箱已被注册')
    #
    # def validate_username(self, field):
    #     if Employee.query.filter_by(username=field.data).first():
    #         raise ValidationError(u'该用户名已经被使用')

class LoginForm(FlaskForm):
    """
    Form for users to login
    """
    email = StringField(u'邮箱', validators=[DataRequired(), Email()])
    password = PasswordField(u'密码', validators=[DataRequired()])
    submit = SubmitField(u'登陆')