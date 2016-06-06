import xml.etree.ElementTree as ET
import codecs
import os

fileName = "cddb_9763_dups.xml"
targetDir = "../commons/src/main/resources/"
csvLines = []

wrongKeys = [line.rstrip('\n') for line in open('wrong_keys.txt')]
xmlTree = ET.parse(fileName)
root = xmlTree.getroot()
for pair in root:
	values = []
	for disc in pair:
		for child in disc:
			if child.tag == "cid":
				text = child.text.replace(" ", "")
				values.append(text)
	if not [value for value in values if value in wrongKeys]:
		csvLines.append(values)

# for line in csvLines:
# 	print(line)

print(len(csvLines))


target = targetDir + 'cd_dataset_duplicates.csv'
os.makedirs(os.path.dirname(target), exist_ok=True)
with open(target, 'w') as csvFile:
	csvFile.write("firstId,secondId\n")
	for line in csvLines:
		csvFile.write(",".join(map(lambda x: '"' + x.replace("\"", "\"\"") + '"', line)) + "\n")