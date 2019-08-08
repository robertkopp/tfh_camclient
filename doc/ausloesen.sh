#!/bin/bash
home="/home/pi/Desktop"
rm $home/cams 2>/dev/null
rm $home/img-raspi* 2>/dev/null
ls -1 /dev | grep video >> cams
hostname=`hostname`
param="-c brightness=0 -c contrast=32 -c saturation=64 -c hue=0 -c gamma=100 -c gain=0 -c power_line_frequency=1 -c white_balance_temperature_auto=0 -c sharpness=3 -c backlight_compensation=0 -c exposure_auto=3 -c exposure_auto_priority=0"
resolution="-s 1600x1200"
while read line
do
if [[ $line == *"parameter"* ]]
then
param=$(echo $line | cut -d ":" -f 2)
fi
if [[ $line == *"resolution"* ]]
then
resolution=$(echo $line | cut -d ":" -f 2)
fi
done < $home/config
while read cam
do
testfile="/dev/"
testfile=$testfile$cam
echo "$hostname-$cam "
v4l2-ctl -d /dev/$cam -c white_balance_temperature_auto=0 -c exposure_auto=1
v4l2-ctl -d /dev/$cam -c white_balance_temperature=4000 -c exposure_absolute=4570
v4l2-ctl -d /dev/$cam $param
if [ -c "$testfile" ]
then
#3264x2448 oder 320x640 oder 1600x1200
streamer -f jpeg -c /dev/$cam $resolution -o $home/img-$hostname-$cam.jpeg 
fi
done < $home/cams
rm $home/cams 2>/dev/null
