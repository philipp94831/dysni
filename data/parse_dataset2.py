import xml.etree.ElementTree as ET
import codecs
import os

fileName = "cddb_ID_nested_10000.xml"
targetDir = "../commons/src/main/resources/"
csvLines = []

indexLookup = {
	'did' : 0,
	'artist' : 1,
	'dtitle' : 2,
	'category' : 3,
	'genre' : 4,
	'year' : 5,
	'cdextra' : 6,
}

wrongKeys = [line.rstrip('\n') for line in open('wrong_keys.txt')]
xmlTree = ET.parse(fileName)
root = xmlTree.getroot()
for disc in root:
	values = ["","","","","","","",""]
	# values = []
	for child in disc:
		if child.tag == 'tracks':
			titles = []
			for title in child:
				titles.append(title.text)
			titleString = "|".join(titles)
			values[7] = titleString
		elif child.text is not None:
			values[indexLookup[child.tag]] += child.text
		
	if values[0] not in wrongKeys:
		csvLine = ",".join(map(lambda x: '"' + x.replace("\"", "\"\"") + '"', values))
		csvLines.append(csvLine)

headline = "id,artist,dtitle,category,genre,year,cdextra,tracks"

target = targetDir + "cd_dataset.csv"
os.makedirs(os.path.dirname(target), exist_ok=True)
with codecs.open(target, 'w', 'utf-8') as csvFile:
	csvFile.write(headline + "\n")
	for line in csvLines:
		csvFile.write(line + "\n")