def parse_file(datafile):
    data = []
    with open(datafile, "r") as f:
    header = f.readline().split(",")
    for line in f:
        fields = line.split(",")
        entry = {}
        for i, value in enumerate(fields):
            entry[header[i].strip()] = value.strip()

        data.append(entry)
    return data

import csv

def parse_csv(datafile):
    data = []
    with open(datafile, "r") as f:
        r = csv.DictReader(f)
        for line in r:
            data.append(line)
    return data

import xlrd
def parse_xls(datafile):
    workbook = xlrd.open_workbook(datafile)
    sheet = workbook.sheet_by_index(0)

    data = [[sheet.cell_value(r, col) for col in range(sheet.ncols)] for r in range(1, sheet.nrows)]

    cv = sheet.col_values(1, start_rowx=1, end_rowx=None)

    maxval = max(cv)
    minval = min(cv)

    maxpos = cv.index(maxval) + 1
    minpos = cv.index(minval) + 1

    maxtime = sheet.cell_value(maxpos, 0)
    realtime = xlrd.xldate_as_tuple(maxtime, 0)
    mintime = sheet.cell_value(minpos, 0)
    realmintime = xlrd.xldata_as_tuple(mintime, 0)

    data = {
            'maxtime': realtime,
            'maxvalue': maxval,
            'mintime':realmintime,
            'minvalue': minval,
            'avgcoast': sum(cv) / float(len(cv))
    }

    return data

    data = parse_xls(datafile)
    import pprint
    pprint.pprint(data)
