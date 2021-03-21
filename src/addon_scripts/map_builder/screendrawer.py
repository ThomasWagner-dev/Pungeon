try:
    import cv2 as cv
except:
    print("installing opencv, as it wasn't present before")
    os.system("python -m pip install opencv-python")
    import cv2 as cv
import numpy as np
    
baseimg = [[int("888888",16) if (i == 0 or i == 21 or j == 0 or j == 12) else str(int("523d22",16)) for i in range(22)] for j in range(13)]
img = np.array(baseimg, dtype = np.uint8)
for i in range(16):
    img = np.array(baseimg, dtype = np.uint8)
    j = i
    if (j % 2):
        img[5][-1] = str(int("523d22",16))
        img[6][-1] = str(int("523d22",16))
    j //= 2
    if (j%2):
        img[-1][10] = str(int("523d22",16))
        img[-1][11] = str(int("523d22",16))
    j //= 2
    if (j%2):
        img[5][0] = str(int("523d22",16))
        img[6][0] = str(int("523d22",16))
    j //= 2
    if (j%2):
        img[0][10] = str(int("523d22",16))
        img[0][11] = str(int("523d22",16))
    print(bin(i))
    cv.imwrite("screenImgs/"+str(bin(i))[2:]+".png", cv.UMat(img))
print("finished")
exit(0)