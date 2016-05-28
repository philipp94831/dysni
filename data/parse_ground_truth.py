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
				text = child.text.replace(" ", "")
				values.append("\"" + text + "\"")
	csvLines.append(values)

# for line in csvLines:
# 	print(line)

print(len(csvLines))



with open('cd_dataset_duplicates.csv', 'w') as csvFile:
	csvFile.write("firstId,secondId\n")
	for line in csvLines:
		csvFile.write(",".join(line) + "\n")