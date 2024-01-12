import os
import numpy as np
import cv2
import pandas as pd
import csv
import os
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.layers import *
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.utils import Sequence
import time

Latitude = 36.831232
longitude= 10.2301696

model = tf.keras.models.load_model('/home/wasp/FinalModel.h5')
def load_video(path, max_frames=30, resize=(224, 224)):
    cap = cv2.VideoCapture(path)
    frames = []

    try:
        for i in range(max_frames):
            ret, frame = cap.read()
            if not ret:
                break

            frame = cv2.resize(frame, resize)
            frames.append(frame/255)

    finally:
        cap.release()

    return frames



def save_video_segments(video_path, output_dir, frames_per_segment=30):
    # Open the video file
    cap = cv2.VideoCapture(video_path)

    # Get video properties
    fps = int(cap.get(cv2.CAP_PROP_FPS))
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

    # Create output directory if it doesn't exist
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Calculate the number of segments
    num_segments = total_frames // frames_per_segment

    frame_count = 0

    # Read and save video segments
    for i in range(num_segments):
        frames = []
        for j in range(frames_per_segment):
            ret, frame = cap.read()
            if not ret:
                break
            frames.append(frame)

        # Check if we have enough frames for a segment
        if len(frames) == frames_per_segment:
            # Create a VideoWriter object for the segment
            fourcc = cv2.VideoWriter_fourcc(*'mp4v')
            output_path = os.path.join(output_dir, f'segment_{i+1}.mp4')
            out = cv2.VideoWriter(output_path, fourcc, fps, (int(cap.get(3)), int(cap.get(4))))

            # Write each frame to the video file
            for frame in frames:
                out.write(frame)

            # Release the VideoWriter
            out.release()

    # Release the VideoCapture
    cap.release()

# Example usage
video_path = '/home/wasp/recorded-video.h264'
output_directory = '/home/wasp/segment'
save_video_segments(video_path, output_directory)


class VideoDataGenerator(Sequence):
    def __init__(self, video_paths, labels, batch_size, frame_shape=(224, 224), shuffle=True):
        self.video_paths = video_paths
        self.labels = labels
        self.batch_size = batch_size
        self.frame_shape = frame_shape
        self.shuffle = shuffle
        self.on_epoch_end()

    def __len__(self):
        return int(np.ceil(len(self.video_paths) / self.batch_size))

    def __getitem__(self, index):
        indices = self.indices[index * self.batch_size:(index + 1) * self.batch_size]
        batch_labels = []

        batch_data = []

        for i in indices:
            frames = self.load_and_preprocess_video(self.video_paths[i])
            if(len(frames) == 30):
                batch_data.append(frames)
                batch_labels.append(self.labels[i])

        return np.array(batch_data), np.array(batch_labels)

    def on_epoch_end(self):
        self.indices = np.arange(len(self.video_paths))
        if self.shuffle:
            np.random.shuffle(self.indices)

    def load_and_preprocess_video(self, video_path):
        cap = cv2.VideoCapture(video_path)
        frames = []
        while True:
            ret, frame = cap.read()
            if not ret:
                break
            frame = cv2.resize(frame, self.frame_shape)
            frames.append(frame/255)
        cap.release()
        return np.array(frames[:30])

video_paths = ['/home/wasp/segments/segment_{}.mp4'.format(i) for i in range(1, 17)]
video_labels = [1 for i in range(16)]
batch_size = 2

video_generator = VideoDataGenerator(video_paths, video_labels, batch_size)

prediction = model.predict(video_generator)
decision = sum(prediction)/len(prediction)
abnormalBehaviour = 0
if(decisoin[0]>0.5):
    abnormalBehaviour = 1

print(abnormalBehaviour)
print(time.time())
print(longitude)
print(Latitude)
