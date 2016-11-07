
import re
from operator import add

logFile = "home/shiyanlou/access.log"
logRDD = sc.textFile(logFile)

logPattern = "^(\\S+)(\\S+)(\\S+)\\[([\\w/]+)([\\w:/]+)\s([+\\-]\\d{4})\\]\"(\\S+)(\\S+)(\\S+)\"(\\d{3})(\\d+)"

def filterWithParse(s):
    m = re.match(logPattern, s)
    if m:
        return True
    return False

def parseLog(s):
    m = re.match(logPattern, s)
    clientIP = m.group(1)   
    requestData = m.group(4)
    requestURL = m.group(8)
    status = m.group(10)

    return (clientIP, requestData, requestURL, status)


logRDDv1 = logRDD.filter(filterWithParse).map(parseLog)

total_count = logRDDv1.count()

logRDDv2 = logRDDv1.map(lambda x: (x[1], 1)).reduceByKey(add)
logRDDv2.sortByKey().saveAsTextFile('/tmp/DPV')

logRDDv3 = logRDDv1.map(lambda x:(x[1], x[0]))
logRDDv4 = logRDDv3.distinct()

logRDDv5 = logRDDv4.map(lambda x:(x[0], 1)).reduceByKey(add)
DIP = logRDDv5.collect()

logRDDv6 = logRDDv1.map(lambda x:(x[3], 1)).reduceByKey(add)
StatusPV = logRDDv6.sortByKey().collect()

logRDDv7 = logRDDv1.map(lambda x:(x[0], 1)).reduceByKey(add)
IPPV = logRDDv7.sortBy(lambda x:x[1], ascending=False).collect()

logRDDv8 = logRDDv1.map(lambda x:(x[2], 1)).reduceByKey(add)
PagePV = logRDDv8.sortBy(lambda x:x[1], ascending=False).collect()

stopList = ['jpg', 'ico', 'png', 'gif', 'txt', 'asp']

def filterWithStop(s):
    for c in stopList:
        if s.endswith('.'+c):
            return False
    return True

logRDDv9 = logRDDv1.filter(lambda x:filterWithStop(x[2]))

logRDDv8 = logRDDv9.map(lambda x:(x[2], 1)).reduceByKey(add)
PagePV = logRDDv8.sortBy(lambda x:x[1], ascending=False).collect()
