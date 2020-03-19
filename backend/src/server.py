import pymssql
import os
import json
import flask
import decimal
from flask import Flask, flash, request, redirect, url_for
from werkzeug.utils import secure_filename
import insert as insert_helper

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

print('connected!!')

def dictToJson(dict):
    return json.dumps(dict, cls=MyJSONEncoder)

cursor = db.cursor()

def dataToDict(columns, data):
    return dict(zip(columns, data))

def searchDataBase(shitiShow):
    command = "select * from TK_QuestionInfo where ShiTiShow like '%" + shitiShow + "%'"
    cursor.execute(command)
    columns = [column[0] for column in cursor.description]
    data = cursor.fetchone()
    if data == None:
        return None
    return dataToDict(columns, data)

@app.route('/')
def hello_world():
    return "欢迎"

@app.route('/find/<content>')
def find(content):
    command = "select * from TK_QuestionInfo where ShiTiShow like '%" + content + "%'"
    print ("**********************************", command)
    cursor.execute(command)
    columns = [column[0] for column in cursor.description]
    data = cursor.fetchone()
    print(data)
    # return dataToDict(columns, data)
    return data[72] + data[71] + data[70]

def getUserHistory(username):
    sqlstr = "select * from userInfoList where username='{}'".format(username)
    cursor.execute(sqlstr)
    data = cursor.fetchone()
    columns = [column[0] for column in cursor.description]
    datadict = dataToDict(columns, data)
    return datadict['historyid'].replace(" ", "")

def addHistory(id, username):
    oldHistory = getUserHistory(username)
    newHistory = oldHistory + ";" + id
    print(newHistory)
    if(oldHistory.find(id) >= 0):
        return
    sqlstr = "update userInfoList set historyid='{}' where username='{}'".format(newHistory, username)
    cursor.execute(sqlstr)
    db.commit()
    return

@app.route('/search', methods=['POST'])
def search():
    print("******************************************************* recieve")
    if request.method != 'POST':
        return
    print (json.loads(request.get_data()))
    jsonObj = json.loads(request.get_data())
    result = searchDataBase(jsonObj['keyword'])
    if result != None and jsonObj['signed']:
        #搜索到结果存储历史记录
        addHistory(result['QuestionID'], jsonObj['username'])
    return dictToJson(result)

@app.route("/getHistoryList", methods=['POST'])
def getHistoryList():
    if request.method != 'POST':
        return
    print (json.loads(request.get_data()))
    jsonObj = json.loads(request.get_data())
    return getUserHistory(jsonObj['username'])

@app.route('/signin', methods=['POST'])
def signIn():
    jsonObj = json.loads(request.get_data())
    print(jsonObj)
    sqlstr = "select * from userInfoList where username='{}' and pwd='{}'".format(jsonObj['username'], jsonObj['pwd'])
    cursor.execute(sqlstr)
    data = cursor.fetchone()
    if data != None:
        print("success")
        return "success"
    print("failed")
    return "failed"

@app.route('/signup', methods=['POST'])
def signUp():
    jsonObj = json.loads(request.get_data())
    print(jsonObj)
    sql_insert = "insert into userInfoList(username,pwd,historyid) values ('{}','{}','{}')".format(jsonObj['username'], jsonObj['pwd'], "")
    try:
        cursor.execute(sql_insert)
        db.commit()
    except:
        print("failed")
        return "failed"
    else:
        print("success")
        return "success"

UPLOAD_FOLDER = '/path/to/the/uploads'
ALLOWED_EXTENSIONS = {'pdf', 'png', 'jpg', 'jpeg', 'gif'}

def allowed_file(filename):
    return '.' in filename and \
        filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/uploadfile', methods=['GET', 'POST'])
def upload_file():
    file = request.files['media']
    print(file)
    file.save(os.path.join("G:\\Nice2020\\backend\\src", "test.jpg"))
    print(os.path.join("G:\\Nice2020\\backend\\src", "test.jpg"))
    return ""

@app.route('/uploadtext', methods=['GET', 'POST'])
def upload_text():
    jsonObj = json.loads(request.get_data())
    print(jsonObj)
    return "success"

@app.route('/newQuestion')
def newQuestion():
    sql_insert = insert_helper.insert(
        "testa",
        "test",
        "test"
    )
    cursor.execute(sql_insert)
    db.commit()
    return sql_insert

@app.route('/newAnswer')
def newAnswer():
    sql_str = insert_helper.update(
        "testhhh",
        "after",
        "after"
    )
    cursor.execute(sql_str)
    db.commit()
    return sql_str

if __name__ == '__main__':
    app.run(debug=True)