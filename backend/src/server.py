from flask import Flask, redirect
from flask import request
import pymssql
import os
import json
import flask
import decimal


class MyJSONEncoder(flask.json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, decimal.Decimal):
            # Convert decimal instances to strings.
            return str(obj)
        return super(MyJSONEncoder, self).default(obj)

app = Flask(__name__)

print('connecting')

# 连接mysql字符串
db=pymssql.connect(
    'localhost',
    'sa',
    '123456',
    'QuestionData',
    'utf8'
)
print ("path: ", os.path.abspath('.'))

print('connected!!')

def dictToJson(dict):
    return json.dumps(dict, cls=MyJSONEncoder)

cursor = db.cursor()
# 执行sql语句

def searchDataBase(shitiShow):
    command = "select * from TK_QuestionInfo where ShiTiShow like '%" + shitiShow + "%'"
    cursor.execute(command)
    columns = [column[0] for column in cursor.description]
    data = cursor.fetchone()
    return dict(zip(columns, data))

@app.route('/')
def hello_world():
    return "欢迎"

@app.route('/find/<content>')
def find(content):
    command = "select * from TK_QuestionInfo where ShiTiShow like '%" + content + "%'"
    print ("**********************************", command)
    cursor.execute(command)
    data = cursor.fetchone()
    print(data)
    return data[72] + data[71] + data[70]

@app.route('/search', methods=['POST'])
def search():
    print("******************************************************* recieve")
    if request.method != 'POST':
        return
    print (request.get_data())
    print (json.loads(request.get_data()))
    print (request.form)
    jsonObj = json.loads(request.get_data())
    result = searchDataBase(jsonObj['keyword'])
    print(result)
    return dictToJson(result)


@app.route('/signin', methods=['POST'])
def signIn():
    if request.method != 'POST':
            return
    jsonObj = json.loads(request.get_data())
    print (jsonObj)
    return "success"
    

if __name__ == '__main__':
    app.run(debug=True)