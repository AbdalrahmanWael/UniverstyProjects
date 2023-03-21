import sys
import os
import keyboard
import vlc
#sys.argv[1]

video_path = sys.argv[1]  #R'assets/tArrival.mp4' 

media_player = vlc.MediaPlayer()

media = vlc.Media(video_path)

media_player.set_media(media)

media_player.play()

while True:  
        if keyboard.is_pressed('q'):  
            os.close()



