import pymssql
import os
import json
import flask
import decimal
from flask import Flask, flash, request, redirect, url_for
from werkzeug.utils import secure_filename
import insert as insert_helper
import time
import datetime
import jieba

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

jieba.lcut("a")
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

def getUserInfo(username, infoName):
    sqlstr = "select * from userInfoList where username='{}'".format(username)
    cursor.execute(sqlstr)
    data = cursor.fetchone()
    columns = [column[0] for column in cursor.description]
    datadict = dataToDict(columns, data)
    return datadict[infoName]

def setUserInfo(username, infoName, infoValue):
    sqlstr = "update userInfoList set {}='{}' where username='{}'".format(infoName, infoValue, username)
    cursor.execute(sqlstr)
    db.commit()

def addHistory(id, username):
    oldHistory = getUserInfo(username, 'historyid').strip()
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
    print(result)
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

UPLOAD_FOLDER = "G:\\Nice2020\\backend\\src\\static\\newpics"
ALLOWED_EXTENSIONS = {'pdf', 'png', 'jpg', 'jpeg', 'gif'}

def allowed_file(filename):
    return '.' in filename and \
        filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def newQuestion(QuestionID, SubjectName, TypeName, FilePath, ShiTiShow):
    print(QuestionID)
    sql_insert = insert_helper.insertQuestion(
        QuestionID,
        SubjectName,
        TypeName,
        FilePath,
        ShiTiShow
    )
    cursor.execute(sql_insert)
    db.commit()
    return sql_insert

@app.route('/handle_new_question', methods=['GET', 'POST'])
def handle_new_question():
    tmp = request.get_data().decode('utf-8','ignore')
    jsonObj = json.loads(tmp[tmp.rindex("{"):])
    print(jsonObj)
    timestamp = int(round(time.time() * 1000))
    file = request.files['media']
    filePath = UPLOAD_FOLDER
    print(filePath)
    if not os.path.exists(os.path.join(filePath, str(timestamp))):
        os.mkdir(os.path.join(filePath, str(timestamp)))
    file.save(os.path.join(filePath, str(timestamp), "show.jpg"))
    newQuestion(
        timestamp, 
        jsonObj['SubjectName'], 
        jsonObj['TypeName'],
        "newpics/",
        '''<img style="vertical-align: middle;" src="_questionImageIP_questionImagePath_questionImageID/show.jpg"><br>''' + 
        jsonObj['ShiTiShow']
        )
    #用户添加提问记录
    if(jsonObj['username'] != None):
        old = getUserInfo(jsonObj['username'], "myquestion")
        new = ""
        if old == None:
            new = str(timestamp)
        else:
            new = old.strip() + ";" + str(timestamp)
        setUserInfo(jsonObj['username'], "myquestion", new)
    return "success"

def updateQuestion(QuestionID, ShiTiAnalysis, ShiTiAnswer):
    print(QuestionID)
    sql_insert = insert_helper.updateQuestion(
        QuestionID,
        ShiTiAnalysis,
        ShiTiAnswer
    )
    cursor.execute(sql_insert)
    db.commit()
    return sql_insert

@app.route('/handle_new_answer', methods=['GET', 'POST'])
def handle_new_answer():
    tmp = request.get_data().decode('utf-8','ignore')
    jsonObj = json.loads(tmp[tmp.rindex("{"):])
    print(jsonObj)
    filePath = UPLOAD_FOLDER
    request.files['analysis'].save(os.path.join(filePath, jsonObj['QuestionID'], "analysis.jpg"))
    request.files['answer'].save(os.path.join(filePath, jsonObj['QuestionID'], "answer.jpg"))
    updateQuestion(
        jsonObj['QuestionID'],
        '''<img style="vertical-align: middle;" src="_questionImageIP_questionImagePath_questionImageID/analysis.jpg"><br>''',
        '''<img style="vertical-align: middle;" src="_questionImageIP_questionImagePath_questionImageID/answer.jpg"><br>'''
    )
    #用户添加作答记录
    if(jsonObj['username'] != None):
        old = getUserInfo(jsonObj['username'], "myanswer")
        new = ""
        if old == None:
            new = jsonObj['QuestionID']
        else:
            new = old.strip() + ";" + jsonObj['QuestionID']
        setUserInfo(jsonObj['username'], "myanswer", new)
    return "success"

@app.route('/unresovle', methods=['POST'])
def unresovle():
    jsonObj = json.loads(request.get_data())
    print(jsonObj)
    sql_serach = "select * from TK_QuestionInfo where SubjectName = '{}' and TypeName = '{}' and solved = 0".format(jsonObj['SubjectName'], jsonObj['TypeName'])
    cursor.execute(sql_serach)
    columns = [column[0] for column in cursor.description]
    res = []
    data = cursor.fetchone()
    while(data != None):
        data = dictToJson(dataToDict(columns, data))
        res.append(data)
        data = cursor.fetchone()
    print("***",{"data":res},"***")
    return {"data":res}

@app.route('/userquestion', methods=['POST'])
def userquestion():
    jsonObj = json.loads(request.get_data())
    return getUserInfo(jsonObj['username'], "myquestion")

@app.route('/useranswer', methods=['POST'])
def useranswer():
    jsonObj = json.loads(request.get_data())
    return getUserInfo(jsonObj['username'], "myanswer")

@app.route('/getQuestionUseId', methods=['POST'])
def getQuestionUseId():
    jsonObj = json.loads(request.get_data())
    command = "select * from TK_QuestionInfo where QuestionID like '%{}%'".format(jsonObj['QuestionID'])
    cursor.execute(command)
    columns = [column[0] for column in cursor.description]
    data = cursor.fetchone()
    if data == None:
        return None
    return dictToJson(dataToDict(columns, data))

@app.route('/test', methods=['POST'])
def test():
    print (json.loads(request.get_data()))
    jsonObj = json.loads(request.get_data())
    keywords = jieba.lcut(jsonObj['keyword'])

    sort_str = ""
    like_str = ""
    for keyword in keywords:
        if(len(keyword) < 2):
            continue
        if sort_str == "":
            sort_str = "(case when ShiTiShow like '%{}%' then 1 else 0 end)".format(keyword)
        else:
            sort_str = sort_str + " + (case when ShiTiShow like '%{}%' then 1 else 0 end)".format(keyword)
        if like_str == "":
            like_str = "ShiTiShow like '%{}%'".format(keyword)
        else:
            like_str = like_str + " or ShiTiShow like '%{}%'".format(keyword)

    sql_str = "select *,({}) as sort  from TK_QuestionInfo where {} order by sort desc".format(sort_str, like_str)
    print(sql_str)
    cursor.execute(sql_str)
    data=cursor.fetchone()
    columns = [column[0] for column in cursor.description]
    return dictToJson(dataToDict(columns, data))
    
@app.route("/getExp", methods=['POST'])
def getExp():
    jsonObj = json.loads(request.get_data())
    print({'exp': getUserInfo(jsonObj['username'], 'exp')})
    return {'exp': getUserInfo(jsonObj['username'], 'exp')}

@app.route("/setExp", methods=['POST'])
def setExp():
    jsonObj = json.loads(request.get_data())
    setUserInfo(jsonObj['username'], 'exp', jsonObj['exp'])
    return "success"

if __name__ == '__main__':
    app.run(debug=True)
    app.run(threaded=True)