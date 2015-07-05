from flask import Flask
app = Flask(__name__)
from flask import request
from flask import render_template
requests = {}

@app.route('/')
def hello_world():
    return "Hello World!"

@app.route('/requestshirts', methods=['POST'])
def request_list():
    if request.method == 'POST':
        requests[request.values['shirts']] = request.values['contact']
        print requests
        return "working"

@app.route('/checklist',methods=['GET', 'POST'])
def get_list():
    print "getting"
    data = request.values['shirt']
    index = data.index(';')
    teamOwned = data[:index]
    teamDesired = data[index+1:]
    for pair in requests:
        indexPair = pair.index(';')
        teamTheyOwn = pair[:indexPair]
        teamTheyDesire = pair[indexPair+1:]
        if teamTheyOwn == teamDesired and teamTheyDesire == teamOwned:
            return ""+requests[pair]
    return "not found"
   
if __name__ == '__main__':
    app.run()
