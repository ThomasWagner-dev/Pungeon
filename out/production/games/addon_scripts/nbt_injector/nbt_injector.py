import os
import tkinter as tk
#import tkinter.filedialog
try:
    from nbt import *
except:
    print("installing nbt, as it wasn't present before")
    os.system("python -m pip install nbt")
    from nbt import *

data = nbt.NBTFile("../data.nbt", "rb")
def inject_weapons():
    print("start injecting weapons...")
    header = "../../data/weapons/"
    weapons_tag = data.get("weapons")
    if weapons_tag == None:
        weapons_tag = nbt.TAGCompound()
        weapons_tag.name = "weapons"
    data.tags.append(weapons_tag)
    
    #print(data.pretty_tree())
    for wpn in os.listdir(header):
        if (wpn.endswith(".template")): 
            continue
        print(f"injecting {wpn}...")
        #print(wpn.replace(".wpn",""))
        weapon = open(header + wpn, "r")
        wpn_tag = nbt.TAG_Compound(name = wpn.replace(".wpn", ""))
        wpn_tag.name = wpn.replace(".wpn", "")
        for line in weapon.read().split("\n"):
            #print(line)
            l = line.split("=")
            #print(l[1])
            value = l[1]
            try:
                value = int(value)
                line_tag = nbt.TAG_Int
            except:
                try:
                    value = float(value)
                    line_tag = nbt.TAG_Double
                except:
                    line_tag = nbt.TAG_String
            wpn_tag.tags.append(line_tag(name = l[0], value = value))
        
        data["weapons"].tags.append(wpn_tag)
        print("injected weapons")
        #print(wpn_tag.pretty_tree())
        
def inject_blocks():
    print("start injecting blocks...")
    header = "../../data/blocks/"
    blocks_tag = data.get("blocks")
    if blocks_tag == None:
        blocks_tag = nbt.TAGCompound()
        blocks_tag.name = "weapons"
    
    for blk in os.listdir(header):
        if blk.endswith(".template"): 
            continue
        print(f"injecting {blk}")
        block = open(header + blk, "r")
        blk_tag = nbt.TAG_Compound()
        blk_tag.name = blk.replace(".block", "")
        for line in block.read().split("\n"):
            l = line.split("=")
            value = l[1]
            try:
                value = int(value)
                line_tag = nbt.TAG_Int
            except:
                try:
                    value = float(value)
                    line_tag = nbt.TAG_Double
                except:
                    line_tag = nbt.TAG_String
            blk_tag.tags.append(line_tag(name=l[0], value = value))
        blocks_tag.tags.append(blk_tag)
    data.tags.append(blocks_tag)

def inject_maps():
    print("start injecting maps...")
    header = "../../data/screens/"
    screens_tag = data.get("screens")
    if screens_tag == None:
        screens_tag = nbt.TAG_Compound()
        screens_tag.name = "screens"
    
    for scr in os.listdir(header):
        if scr.endswith(".template") or scr.endswith(".enemymap"):
            continue
        print(f"injecting {scr}")
        screen = open(header + scr, "r")
        scr_tag = nbt.TAG_Compound()
        scr_tag.name = scr.replace(".world", "")
        map = ""
        lines = screen.read().split("\n")
        #print(lines)
        line = lines[0]
        i = 1
        while (line != "###"):
            map += line+"\n"
            #print(i)
            line = lines[i]
            i += 1
        while (i < len(lines)):
            line = lines[i].split(":")
            scr_tag.tags.append(nbt.TAG_String(name=line[0], value = line[1]))
            i += 1
        scr_tag.tags.append(nbt.TAG_String(name = "map", value = map))
        enemymap = open(header + scr.replace(".world", "") + ".enemymap")
        enm = nbt.TAG_Compound()
        enm.name = "enemymap"
        i = 0
        for line in enemymap.read().split("\n"):
            if (line == ""):
                continue
            line = line.split("=")
            ene = nbt.TAG_Compound()
            ene.name = str(i)
            print(line)
            ene.tags.append(nbt.TAG_String(name="type", value=line[0]))
            line = line[1].split(",")
            ene.tags.append(nbt.TAG_Int(name="x", value=int(line[0])))
            ene.tags.append(nbt.TAG_Int(name="y", value=int(line[1])))
            
            enm.tags.append(ene)
            i += 1
        scr_tag.tags.append(enm)
        screens_tag.tags.append(scr_tag)
    data.tags.append(screens_tag)
    
def inject_enemies():
    print("start injecting enemies...")
    header = "../../data/enemies/"
    enemies_tag = data.get("enemies")
    if enemies_tag == None:
        enemies_tag = nbt.TAG_Compound()
        enemies_tag.name = "enemies"
    
    for en in os.listdir(header):
        if not en.endswith(".enemy"):
            continue
        print(f"injecting {en}")
        enemy = open(header + en, "r")
        enemy_tag = nbt.TAG_Compound()
        enemy_tag.name = en.replace(".enemy", "")
        
        for line in enemy.read().split("\n"):
            l = line.split("=")
            #print(l[1])
            value = l[1]
            try:
                value = int(value)
                line_tag = nbt.TAG_Int
            except:
                try:
                    value = float(value)
                    line_tag = nbt.TAG_Double
                except:
                    line_tag = nbt.TAG_String
            enemy_tag.tags.append(line_tag(name = l[0], value = value))
        enemies_tag.tags.append(enemy_tag)
    data.tags.append(enemies_tag)

def inject_items():
    print("start injecting items...")
    header = "../../data/items/"
    items_tag = data.get("items")
    if items_tag == None:
        items_tag = nbt.TAG_Compound()
        items_tag.name = "items"
    
    for itm in os.listdir(header):
        if not itm.endswith(".itm"):
            continue
        print(f"injecting {itm}")
        item = open(header + itm, "r")
        item_tag = nbt.TAG_Compound()
        item_tag.name = itm.replace(".itm", "")
        
        for line in item.read().split("\n"):
            l = line.split("=")
            value = l[1]
            try:
                value = int(value)
                line_tag = nbt.TAG_Int
            except:
                try:
                    value = float(value)
                    line_tag = nbt.TAG_Double
                except:
                    line_tag = nbt.TAG_String
            item_tag.tags.append(line_tag(name = l[0], value = value))
        items_tag.tags.append(item_tag)
    data.tags.append(items_tag)
    
def inject_all():
    inject_weapons()
    inject_maps()
    inject_enemies()
    inject_blocks()
    inject_items()

if __name__ == "__main__":
    inject_all()
    print(data.pretty_tree())
    data.write_file("../../data.nbt")