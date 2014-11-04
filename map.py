fileObj = open("output2", "r")
str_array = fileObj.read()
str_array = str_array.split("\n")
contents = []
for string in str_array:
	contents += [string.split(" ")]

PE = []
KE = []
for line in contents:
	if line[0] == "Potential":
		PE += [float(line[2])]
	if line[0] == "Kinetic":
		KE += [float(line[2])]
kinetic = open("KE.out", "w")
for elem in KE:
	kinetic.write(str(elem) + "\n")
potential = open("PE.out", "w")
for elem in PE:
	potential.write(str(elem) + "\n")
