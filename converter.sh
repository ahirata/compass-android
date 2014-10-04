#!/bin/bash

origin=$1
dest=$2

filename=$(basename $1)

mkdir -p $dest/drawable-xxhdpi/
mkdir -p $dest/drawable-xhdpi/
mkdir -p $dest/drawable-hdpi/
mkdir -p $dest/drawable-mdpi/
mkdir -p $dest/drawable-ldpi/

convert $origin -resize 75% -colors 256 $dest/drawable-xxhdpi/$filename
convert $origin -resize 50% -colors 256 $dest/drawable-xhdpi/$filename
convert $origin -resize 37.5% -colors 256 $dest/drawable-hdpi/$filename
convert $origin -resize 25% -colors 256 $dest/drawable-mdpi/$filename
convert $origin -resize 18.75% -colors 256  $dest/drawable-ldpi/$filename

