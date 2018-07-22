class Config(object):
    """
    Common configurations
    """
    CSRF_ENABLED = True
    SECRET_KEY = 'you-will-never-guess'
    MONGO_DBNAME = 'squaredance'
    MONGO_URI = 'mongodb://127.0.0.1:27017/squaredance'

    # Put any configurations here that are common across all environments

class DevelopmentConfig(Config):
    """
    Development configurations
    """


    DEBUG = True
    SQLALCHEMY_ECHO = True

class ProductionConfig(Config):
    """
    Production configurations
    """

    DEBUG = False

app_config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig
}
