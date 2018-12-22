import sys
import os, os.path

print ('Number of arguments:', len(sys.argv), 'arguments.')
list = []
for i in sys.argv[1:]:
	list.append(i)

if str.lower(list[0]) == "true":
	import cognitive_face as CF
	SUBSCRIPTION_KEY = '73a339d4f6484ff1aa6a2e9fc14d89a2'
	BASE_URL = 'https://eastasia.api.cognitive.microsoft.com/face/v1.0/'
	CF.BaseUrl.set(BASE_URL)
	CF.Key.set(SUBSCRIPTION_KEY)
	name = list[1]
	path = list[2]
	PERSON_GROUP_ID = list[4]
	if str.lower(list[3]) == "true":
		print("Creating new model")
		CF.person_group.create(PERSON_GROUP_ID, "new")
	else:
		print("Using existing model")
	user_data = str("Roll Number supposing")
	response = CF.person.create(PERSON_GROUP_ID, name, user_data)
	person_id = response['personId']
	valid_images = [".jpg",".png",".tga","jpeg"]
	for f in os.listdir(path):
		ext = os.path.splitext(f)[1]
		if ext.lower() in valid_images:
			CF.person.add_face(str(path+"/"+f), PERSON_GROUP_ID, person_id)
	
	CF.person_group.train(PERSON_GROUP_ID)
	response = CF.person_group.get_status(PERSON_GROUP_ID)
	status = response['status']
	print("Training done")

else:
	print("Now you can add new students\teachers to database")