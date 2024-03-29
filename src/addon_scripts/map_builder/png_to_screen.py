import os
try:
    import cv2 as cv
except:
    print("installing opencv, as it wasn't present before")
    os.system("python -m pip install opencv-python")
    import cv2 as cv
import tkinter as tk
from tkinter import filedialog
import numpy as np
import json

allocations={
  "888888":"wall_brick",
  "777777":"wall_brick_crack",
  "666666":"wall_brick_hole",
  "008bff":"tile_ground_egg",
  "523d22":"tile_ground",
  "ff0000":"trap_spikes",
  "697c69":"tile_ground_moss",
  "000000":"wall_void",
  "343434":"wall_stone",
#  "de8e00":"chest_gold",
}

def rgb2hex(rgb:list) -> str:
    return "".join([str(hex(x)).removeprefix("0x").zfill(2) for x in rgb])

def pic2hex(pic):
    return [[rgb2hex(y) for y in x] for x in pic]

def pic2screenmap(pic):
    return [[allocations.get(y,"tile_ground") for y in x] for x in pic]

def file2screen(file):
    pic = cv.imread(file)
    pic = cv.cvtColor(pic, cv.COLOR_BGR2RGB)
    #print(pic)
    pic = pic2hex(pic)
    screen = pic2screenmap(pic)
    string = "\n".join([",".join(x) for x in screen])
    #print(string)
    return string
    


#with open('allocation.json') as json_file:
#    allocations = json.load(json_file)

#filenames = tk.filedialog.askopenfilenames(title="choose files", filetypes=[("image files", ("*.png"," *.jpg", "*.gif", "*.svg"))])
if (__name__ == "__main__"):
    while (True):
        picname = tk.filedialog.askopenfilename(title="choose files", initialdir="screenImgs", filetypes=[("image files", ("*.png"," *.jpg", "*.gif", "*.svg"))])
        if (picname == ""):
            exit()
        string = file2screen(picname)+"\n###\n"
        savefile = tk.filedialog.asksaveasfile(initialfile = picname.split("/")[-1].split(".")[0],title="save file", initialdir="../../data/screens", defaultextension=".world", filetype=[("screenfile", ("*.world"))])
        #print(type(savefile))
        savefile.write(string)
        savefile.close()
        #savefile.write(string)