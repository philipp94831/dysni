import xml.etree.ElementTree as ET
import codecs

fileName = "cddb_9763_dups.xml"
csvLines = []

xmlTree = ET.parse(fileName)
root = xmlTree.getroot()
for pair in root:
	values = []
	for disc in pair:
		for child in disc:
			if child.tag == "cid":
				values.append("\"" + child.text + "\"")
	csvLines.append(values)

for line in csvLines:
	print(line)

print(len(csvLines))



with open('cd_dataset_duplicates.csv', 'w') as csvFile:
	for line in csvLines:
		csvFile.write(",".join(line) + "\n")