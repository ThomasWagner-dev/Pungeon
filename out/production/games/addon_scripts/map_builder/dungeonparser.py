import tkinter
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

import png_to_screen

allocations = png_to_screen.allocations

enallo = {
    "ffadad":"ghost_fire",
    "a7a6a9":"rat",
    "ffffff":"skeleton",
    "0a8fff":"slime_ice"
}

def crop(map, screenx, screeny):
    return [x[screenx*21 : screenx*21+22] for x in map[screeny*12:screeny*12+13]]

def getAdj(x, y, maxx, maxy, filename):
    l = {
        "left":"none",
        "right":"none",
        "up":"none",
        "down":"none"
    }
    if x != 0:
        l["left"]= str(x-1) + str(y) + filename
    if x != maxx:
        l["right"]=str(x+1) + str(y) + filename
    if y != 0:
        l["up"]=str(x) + str(y-1) + filename
    if y != maxy:
        l["down"]=str(x) + str(y+1) + filename
    return l

def dictToString(d):
    string = ""
    for k,v in d.items():
        #print(k,v)
        string += k+":"+v + "\n"
    return string[:-1]

def getEnMapString(hexmap):
    string = ""
    for yrow in range(len(hexmap)):
        for x in range(len(hexmap[0])):
            en = enallo.get(hexmap[yrow][x])
            hexval = hexmap[yrow][x]
            if (en != None):
                string += en + "=" + str(x) + "," + str(yrow) + "\n"
    return string

def wipe_mapinformation(pic):
    rgb_wall = np.array([136,136,136])
    rgb_tile = np.array([82,61,34])
    for i, y in enumerate(pic):
        for j, x in enumerate(y):
            if (not np.array_equal(x, rgb_wall)):
                pic[i][j] = rgb_tile
    return pic
    #return [[x if (np.array_equal(x, rgb_wall) or np.array_equal(x, rgb_tile)) else rgb_tile for x in y] for y in pic]

def file2screens(file):
    filename = file.split("/")[-1]
    pic = cv.imread(file)
    pic = cv.cvtColor(pic, cv.COLOR_BGR2RGB)
    dim = [len(pic[0]),len(pic)]
    mapdim = [dim[0]//21, dim[1]//12]
    for screenx in range(mapdim[0]):
        for screeny in range(mapdim[1]):
            map = crop(pic, screenx, screeny)
            hexmap = png_to_screen.pic2hex(map)
            screenmap = png_to_screen.pic2screenmap(hexmap)
            mapstring = "\n".join([",".join(x) for x in screenmap])
            screenname = "../../data/screens/" + str(screenx) + str(screeny) + ".".join(filename.split(".")[:-1]) + ".world"
            scrnfile = open(screenname, "w")
            scrnfile.write(mapstring + "\n###\n")
            scrnfile.write(dictToString(getAdj(screenx, screeny, dim[0], dim[1], ".".join(filename.split(".")[:-1]))))
            scrnfile.close()
            enfile = open(screenname.replace(".world", ".enemymap"), "w")
            enfile.write(getEnMapString(hexmap))
            enfile.close()
    wiped_map = wipe_mapinformation(pic)
    mapname = "../../images/map/maps/" + filename
    cv.imwrite(mapname, cv.cvtColor(wiped_map, cv.COLOR_RGB2BGR))
    print(f"wrote map to {mapname}")
    
if (__name__ == "__main__"):
    while (True):
        picname = tk.filedialog.askopenfilename(title="choose files", initialdir="screenImgs", filetypes=[("image files", ("*.png"," *.jpg", "*.gif", "*.svg"))])
        if (picname == ""):
            exit()
        file2screens(picname)