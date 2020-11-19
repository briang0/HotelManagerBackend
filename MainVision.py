import cv2
import numpy as np
import sys

path = "C:/Users/brian/Documents/Bandicam/camerafootage.mp4"

def main():
    global path
    path = sys.argv[0]
    video = cv2.VideoCapture(path)
    if not video.isOpened():
        print("Error reading video")
    standard = None
    prev = -1
    while True:
        ret, frame = video.read()
        if standard is None:
            standard = frame
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        gray2 = cv2.cvtColor(standard, cv2.COLOR_BGR2GRAY)
        dummy, bw = cv2.threshold(gray, 100, 255, cv2.THRESH_BINARY)
        dummy, bw2 = cv2.threshold(gray2, 100, 255, cv2.THRESH_BINARY)
        sub = bw2 - bw
        sub = cv2.erode(sub, np.ones((4, 4)))
        sub = cv2.dilate(sub, np.ones((75, 75)))
        sub = cv2.erode(sub, np.ones((50, 50)))

        comps, output, stats, centroids = cv2.connectedComponentsWithStats(sub, connectivity=8)
        people = 0
        for i in range(1, len(stats)):
            size = stats[i][4]
            x = stats[i][cv2.CC_STAT_LEFT]
            y = stats[i][cv2.CC_STAT_TOP]
            width = stats[i][cv2.CC_STAT_WIDTH]
            height = stats[i][cv2.CC_STAT_HEIGHT]
            if size >= 20000:
                people += 1
                cv2.rectangle(frame, (x, y), (x + width, y + height), (255, 0, 0), 3)
        if people != prev:
            print(people)
        prev = people
        cv2.imshow('frame', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    return video


main()