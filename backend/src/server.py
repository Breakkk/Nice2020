from flask import Flask
from flask import request
import pymssql
import json

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

print('connected')

cursor = db.cursor()
# 执行sql语句

def searchDataBase(shitiShow):
    command = "select * from TK_QuestionInfo where ShiTiShow like '%" + shitiShow + "%'"
    cursor.execute(command)
    data = cursor.fetchone()
    return data

@app.route('/')
def hello_world():
    return 'hhh'

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
    str = searchDataBase(jsonObj['keyword'])
    print(str[72])
    return str[72] + '\n' + str[71] + '\n' + str[70]
    

if __name__ == '__main__':
    app.run(debug=True)