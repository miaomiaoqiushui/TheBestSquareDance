# -*- coding: utf-8 -*-
# app/auth/views.py

from flask import flash, redirect, render_template, url_for
from flask_login import login_required, login_user, logout_user

from . import auth
from forms import LoginForm, RegistrationForm
from .. import db, app,mongo
from ..models import Employee



@auth.route('/register', methods=['GET', 'POST'])
def register():
    form = RegistrationForm()
    if form.validate_on_submit():
        useradmin = mongo.db.userAdmin

        useradmin.insert({'email': form.email.data, 'username': form.username.data, 'user_tel': form.user_tel.data,
                         'user_address': form.user_address.data, 'password': form.password.data})
        flash(u'您已经成功注册，请使用新账号登陆')
        return redirect(url_for('auth.login'))
    # load registration template
    return render_template('auth/register.html', form=form, title='Register')

# Edit the login view to redirect to the admin dashboard if employee is an admin

class UserAdminArray(object):
    def __init__(self,id, email, username, user_tel, user_address, password, is_active,is_admin):
        self.id = str(id)
        self.email = email
        self.username = username
        self.user_tel = user_tel
        self.user_address = user_address
        self.password = password
        self.is_active = is_active
        self.is_admin = is_admin

    def get_id(self):
        return self.email

@auth.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        useradmin = mongo.db.userAdmin.find({"email":form.email.data})
        edit_admin = {'email': u"待赋值", 'password': u"待赋值"}
        useradminarray = UserAdminArray(id="111", email=u"待赋值", username=u"待赋值", user_tel=u"待赋值", user_address=u"待赋值", password=u"待赋值", is_active=True, is_admin=True)
        for s in useradmin:
            useradminarray.id = s['_id']
            useradminarray.email = s['email']
            useradminarray.username = s['username']
            useradminarray.user_tel = s['user_tel']
            useradminarray.user_address = s['user_address']
            useradminarray.password = s['password']
        for s in useradmin:
            edit_admin['email'] = s['email']
            edit_admin['password'] = s['password']
        if (useradminarray.email != u"待赋值") & (useradminarray.password == form.password.data):
            # log employee in
            login_user(useradminarray)
            return redirect(url_for('home.admin_dashboard'))
        # when login details are incorrect
        else:
            # login_user(useradminarray)
            flash(u'邮箱无效或密码错误')
            # return redirect(url_for('home.admin_dashboard'))
    # load login template
    return render_template('auth/login.html', form=form, title='Login')

@auth.route('/logout')
@login_required
def logout():
    """
    Handle requests to the /logout route
    Log an employee out through the logout link
    """
    logout_user()
    flash(u'您已成功退出登陆')

    # redirect to the login page
    return redirect(url_for('auth.login'))