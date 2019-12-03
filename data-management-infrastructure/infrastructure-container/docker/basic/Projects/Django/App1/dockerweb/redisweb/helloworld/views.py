from django.shortcuts import render
from django.http import HttpResponse

import redis

# Create your views here.
def hello(request):
    str = redis.__file__
    str += "<br>"
    r = redis.Redis(host='db', port=6379, db=0)
    info = r.info()
    r.set('Hi', "Helloworld-APP1")
    for key in info:
        str += ("%s: %s <br>" % (key, info[key]))
    return HttpResponse(str)
