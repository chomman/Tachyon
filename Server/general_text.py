#from PIL import Image
import sys
import pytesseract
import argparse
import cv2
import os
import re
import pprint
import shutil
import subprocess
import base64



def extract(filename):
	global got,attributes,phase,path
	#text = pytesseract.image_to_string(Image.open(filename))
	#print(text)
	img = cv2.imread(filename)

	text = pytesseract.image_to_data(img,output_type= 'dict')

	#print(pytesseract.image_to_data(Image.open(filename)))
	#print(text['text'])

	text = text['conf'],text['text']
	ttext = ' '
	
	for i in range(len(text[0])) :
		if(int(text[0][i])>55 ):
			ttext=ttext+text[1][i]+' '

	text=ttext

	dob_obj = re.compile(r'\d{2}[-/\\]\d{2}[-/\\]\d{4}|\d{4}[-/\\]\d{2}[-/\\]\d{2}')
	dob_ext = dob_obj.search(text)
	
	gender_obj = re.compile('Male|Female|MALE|FEMALE')
	gender_ext = gender_obj.search(text)
	
	######## Think of a better approach
	name_obj = re.compile(r'[A-Z_ ]*((?:[_	A-Z][a-z]+\s?)+)')
	name_ext = name_obj.search(text)
	########

	id_obj = re.compile(r' (\d{4} \d{4} \d{4})')
	id_ext = id_obj.search(text)
	
	address_obj = re.compile(r'Address(.*?(\d{6}|\d{3} \d{3}))|To(.*?(\d{6}|\d{3} \d{3}))')
	address_ext = address_obj.search(text)

	if(address_ext != None and got['address'] == False):
		attributes['Address'] = str(address_ext.group(0))
		got['address'] = True

	if(name_ext != None and got['name'] == False):
		attributes['Name'] = str(name_ext.group(1))
		got['name'] = True
		

	if(dob_ext != None and got['dob'] == False):
		attributes['DOB'] = str(dob_ext.group(0))
		got['dob'] = True

	if(gender_ext != None and got['gender'] == False):
		attributes['Gender'] = str(gender_ext.group(0))
		got['gender'] = True

	if(id_ext != None and got['id'] == False):
		attributes['ID'] = str(id_ext.group(1))
		got['id'] = True

	
	if(got['dob'] == True and got['gender'] == True and got['name'] == True and got['id'] == True and got['address'] == True):
		path = 'Extras/'
		shutil.rmtree(path) 
		for i in attributes:
			print(attributes[i])

		sys.exit()

def runner(filename):
	global phase,path
	path = 'Extras/'
	if not os.path.exists(path):
		os.makedirs(path)
	extract(filename)

	image = cv2.imread(filename)
	gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
	gray = cv2.threshold(gray, 0, 255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]
	filename = "{}.png".format(os.getpid())
	path = os.path.join(path , filename)
	cv2.imwrite(path, gray)	


	extract(path)

	path = 'Extras/'
	gray = cv2.medianBlur(gray, 3)
	filename = "{}.png".format(os.getpid())
	path = os.path.join(path , filename)

	cv2.imwrite(path, gray)	

	extract(path)

	path = 'Extras/'
	#if(phase == 1):
	for i in attributes:
		print(attributes[i])
	#elif(phase == 0):
	#	missing = ''
	#	for i in got:
	#		if not got[i]:
	#			missing += str(i)+" "
		#filename = input("Enter image with "+missing+"\n")
		#phase = 1
		#runner(filename)
	path = 'Extras/'
	if os.path.exists(path):
		shutil.rmtree(path) 

arguments = sys.argv[1:]


 
    

path = 'Extras/'

got={'dob':False,'gender':False,'name':False,'id':False,'address':False}

attributes={'DOB':'NA','Gender':'NA','Name':'NA','ID':'NA','Address':'NA'}
phase = 0


#filename = input("Enter File Name:\n")

runner(arguments[0])


