import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = 'hide'

import subprocess
import numpy as np
import pygame
import tkinter as tk
from tkinter import filedialog
import sys
import sounddevice as sd
from scipy.signal import windows
from enum import Enum
import colorsys

# Configuration
CHUNK = 2048
RATE = 48000  # Changed from 44100 to 48000
CHANNELS = 2  # Explicitly define channels
VISUALIZATION_HEIGHT = 600
VISUALIZATION_WIDTH = 1000

class VisualizationMode(Enum):
    BARS = "bars"
    SPECTRUM = "spectrum"
    CIRCLES = "circles"

class ColorTheme(Enum):
    CLASSIC = "classic"
    NEON = "neon"
    RAINBOW = "rainbow"
    MONOCHROME = "monochrome"

class AudioVisualizer:
    def __init__(self):
        # Check FFmpeg availability with better error handling
        self.ffmpeg_available = self._check_ffmpeg()
        if not self.ffmpeg_available:
            print("FFmpeg not found. Please install FFmpeg and add it to your system PATH.")
            print("Download from: https://ffmpeg.org/download.html")
            sys.exit(1)
        
        # Device configuration with preference for WASAPI
        self.input_device, self.output_device = self._get_best_audio_devices()
        
        print(f"\nSelected devices:")
        print(f"Input: {sd.query_devices(self.input_device)['name']}")
        print(f"Output: {sd.query_devices(self.output_device)['name']}\n")
        
        # Initialize audio streams with better error handling
        try:
            self.input_stream = sd.InputStream(
                samplerate=RATE,
                blocksize=CHUNK,
                dtype=np.float32,
                channels=CHANNELS,
                device=self.input_device,
                latency='low'
            )
            self.output_stream = sd.OutputStream(
                samplerate=RATE,
                blocksize=CHUNK,
                dtype=np.float32,
                channels=CHANNELS,
                device=self.output_device,
                latency='low'
            )
            
            self.input_stream.start()
            self.output_stream.start()
        except sd.PortAudioError as e:
            print(f"Audio device error: {e}")
            print("\nTrying fallback configuration...")
            try:
                # Try default devices as fallback
                self.input_stream = sd.InputStream(
                    samplerate=RATE,
                    blocksize=CHUNK,
                    dtype=np.float32,
                    channels=CHANNELS,
                    latency='low'
                )
                self.output_stream = sd.OutputStream(
                    samplerate=RATE,
                    blocksize=CHUNK,
                    dtype=np.float32,
                    channels=CHANNELS,
                    latency='low'
                )
                self.input_stream.start()
                self.output_stream.start()
            except sd.PortAudioError as e:
                print(f"Fallback configuration failed: {e}")
                print("Please check your audio devices and drivers.")
                sys.exit(1)

        self.audio_source = "mic"
        self.audio_file = None
        self.ffmpeg_proc = None

        # Pygame setup
        pygame.init()
        self.screen = pygame.display.set_mode((VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT))
        pygame.display.set_caption("Audio Visualizer - Fixed Device Config")
        self.clock = pygame.time.Clock()

        # Frequency bands
        self.bands = [
            (20, 50), (50, 100), (100, 200), (200, 400), (400, 800),
            (800, 1600), (1600, 3200), (3200, 6400), (6400, 20000)
        ]
        self.prev_mags = np.zeros(len(self.bands))
        self.smoothing_factor = 0.7

        # Colors
        self.colors = [
            (255, 0, 0), (255, 165, 0), (255, 255, 0),
            (0, 255, 0), (0, 0, 255), (75, 0, 130),
            (238, 130, 238)
        ]
        
        # Additional visualization settings
        self.viz_mode = VisualizationMode.BARS
        self.color_theme = ColorTheme.CLASSIC
        self.show_volume_meter = True
        self.spectrum_scale = 1.0
        self.peak_hold = True
        self.peak_values = np.zeros(len(self.bands))
        self.peak_decay = 0.98
        self.volume_history = np.zeros(50)
        
        # Color themes
        self.color_themes = {
            ColorTheme.CLASSIC: [
                (255, 0, 0), (255, 165, 0), (255, 255, 0),
                (0, 255, 0), (0, 0, 255), (75, 0, 130),
                (238, 130, 238)
            ],
            ColorTheme.NEON: [
                (0, 255, 255), (255, 0, 255), (0, 255, 0),
                (255, 255, 0), (255, 0, 128), (0, 255, 128),
                (128, 0, 255)
            ],
            ColorTheme.MONOCHROME: [(255, 255, 255)] * 7
        }
        
        # Generate rainbow colors
        self.color_themes[ColorTheme.RAINBOW] = [
            tuple(int(c * 255) for c in colorsys.hsv_to_rgb(i / 7, 1.0, 1.0))
            for i in range(7)
        ]

    def _check_ffmpeg(self):
        try:
            subprocess.run(['ffmpeg', '-version'], capture_output=True)
            return True
        except FileNotFoundError:
            return False

    def file_dialog(self):
        if not self.ffmpeg_available:
            print("FFmpeg is required for file playback. Please install FFmpeg first.")
            return
            
        root = tk.Tk()
        root.withdraw()
        path = filedialog.askopenfilename(filetypes=[("Audio Files", "*.wav *.mp3 *.ogg *.flac")])
        root.destroy()
        if path:
            self.audio_file = path
            self.audio_source = "file"
            self._restart_ffmpeg()

    def _restart_ffmpeg(self):
        if self.ffmpeg_proc:
            try:
                self.ffmpeg_proc.terminate()
                self.ffmpeg_proc.wait(timeout=1)
            except:
                pass

        try:
            # First probe the file to get its details
            probe = subprocess.run([
                'ffmpeg', '-i', self.audio_file,
                '-hide_banner'
            ], capture_output=True, text=True)

            # Configure FFmpeg with better buffering and error handling
            self.ffmpeg_proc = subprocess.Popen([
                'ffmpeg',
                '-i', self.audio_file,
                '-f', 'f32le',
                '-acodec', 'pcm_f32le',
                '-ac', str(CHANNELS),
                '-ar', str(RATE),
                '-bufsize', '192k',
                '-thread_queue_size', '4096',
                '-'
            ], stdout=subprocess.PIPE, stderr=subprocess.PIPE, bufsize=CHUNK * 8)

            # Read any initial errors
            err = self.ffmpeg_proc.stderr.read1(1024)
            if err and b'Output file is empty' in err:
                print("Error: Could not read audio file. Check if the file is valid.")
                self.audio_source = "mic"
                return
            
        except Exception as e:
            print(f"FFmpeg error: {str(e)}")
            print("Check if the audio file format is supported.")
            self.audio_source = "mic"

    def get_audio_data(self):
        try:
            if self.audio_source == "mic":
                data, _ = self.input_stream.read(CHUNK)
                return np.mean(data, axis=1).astype(np.float32)
            else:
                if not self.ffmpeg_proc:
                    return np.zeros(CHUNK, dtype=np.float32)
                
                # Read raw audio data with proper size calculation
                bytes_to_read = CHUNK * CHANNELS * 4  # 4 bytes per float32
                raw = self.ffmpeg_proc.stdout.read(bytes_to_read)
                
                if not raw:
                    print("End of file or playback error")
                    self.audio_source = "mic"
                    return np.zeros(CHUNK, dtype=np.float32)
                
                # Convert to float32 directly since FFmpeg is outputting float32
                audio = np.frombuffer(raw, dtype=np.float32)
                
                if len(audio) < CHUNK * CHANNELS:
                    # Pad with zeros if we're at the end of the file
                    audio = np.pad(audio, (0, CHUNK * CHANNELS - len(audio)))
                
                # Reshape to stereo and mix down to mono for visualization
                audio = audio.reshape(-1, CHANNELS)
                mono = np.mean(audio, axis=1)
                
                # Play through output stream
                try:
                    self.output_stream.write(audio)
                except Exception as e:
                    print(f"Playback error: {e}")
                
                return mono
                
        except Exception as e:
            print(f"Audio processing error: {str(e)}")
            return np.zeros(CHUNK, dtype=np.float32)

    def process_audio(self):
        audio = self.get_audio_data()
        if np.all(audio == 0):
            return self.prev_mags

        # Apply window function
        window = windows.hann(len(audio))
        windowed = audio * window

        # Compute FFT
        fft = np.fft.rfft(windowed)
        magnitudes = np.abs(fft)
        
        # Normalize magnitudes based on window and signal length
        magnitudes = magnitudes / len(windowed)
        
        # Convert to dB with proper scaling
        db = 20 * np.log10(magnitudes + 1e-6)
        freqs = np.fft.rfftfreq(len(windowed), 1/RATE)

        # Calculate band magnitudes with improved scaling
        band_mags = []
        for low, high in self.bands:
            mask = (freqs >= low) & (freqs < high)
            if np.any(mask):
                band_db = np.max(db[mask])  # Using max instead of mean for better visualization
                # Normalize to a reasonable range
                band_db = np.clip(band_db, -60, 0)
            else:
                band_db = -60
            band_mags.append(band_db)

        # Smooth the values
        smoothed = (self.smoothing_factor * np.array(band_mags) +
                   (1 - self.smoothing_factor) * self.prev_mags)
        self.prev_mags = smoothed
        return smoothed

    def draw_visualization(self, mags):
        self.screen.fill((0, 0, 0))
        
        if self.viz_mode == VisualizationMode.BARS:
            self._draw_bars(mags)
        elif self.viz_mode == VisualizationMode.SPECTRUM:
            self._draw_spectrum(mags)
        elif self.viz_mode == VisualizationMode.CIRCLES:
            self._draw_circles(mags)
            
        if self.show_volume_meter:
            self._draw_volume_meter()
            
        self._draw_ui()

    def _draw_bars(self, mags):
        num_bands = len(mags)
        bar_width = VISUALIZATION_WIDTH // num_bands
        colors = self.color_themes[self.color_theme]
        
        for i, magnitude in enumerate(mags):
            height = np.interp(magnitude, [-60, 0], [0, VISUALIZATION_HEIGHT])
            height = max(10, height ** 1.3)
            
            # Update peak values
            if self.peak_hold:
                self.peak_values[i] = max(height, self.peak_values[i] * self.peak_decay)
            
            x = i * bar_width + 2
            color = colors[i % len(colors)]
            
            # Draw main bar with gradient
            for h in range(int(height)):
                alpha = 1.0 - (h / height) * 0.6
                bar_color = tuple(int(c * alpha) for c in color)
                pygame.draw.rect(
                    self.screen,
                    bar_color,
                    (x, VISUALIZATION_HEIGHT - h, bar_width - 4, 1)
                )
            
            # Draw peak marker
            if self.peak_hold:
                pygame.draw.rect(
                    self.screen,
                    (255, 255, 255),
                    (x, VISUALIZATION_HEIGHT - self.peak_values[i], bar_width - 4, 2)
                )

    def _draw_spectrum(self, mags):
        points = []
        width = VISUALIZATION_WIDTH / (len(mags) - 1)
        
        for i, mag in enumerate(mags):
            x = i * width
            y = VISUALIZATION_HEIGHT - np.interp(mag, [-60, 0], [0, VISUALIZATION_HEIGHT])
            points.append((x, y))
        
        if len(points) > 1:
            pygame.draw.lines(self.screen, (0, 255, 0), False, points, 2)
            # Fill area under curve
            points.append((VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT))
            points.append((0, VISUALIZATION_HEIGHT))
            pygame.draw.polygon(self.screen, (0, 50, 0), points)

    def _draw_circles(self, mags):
        center_x = VISUALIZATION_WIDTH // 2
        center_y = VISUALIZATION_HEIGHT // 2
        max_radius = min(VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT) // 3
        
        for i, mag in enumerate(mags):
            radius = np.interp(mag, [-60, 0], [0, max_radius])
            color = self.color_themes[self.color_theme][i % len(self.color_themes[self.color_theme])]
            pygame.draw.circle(self.screen, color, (center_x, center_y), 
                             int(radius * (i + 1) / len(mags)), 2)

    def _draw_volume_meter(self):
        # Calculate current volume level
        current_volume = np.max(np.abs(self.get_audio_data()))
        self.volume_history = np.roll(self.volume_history, -1)
        self.volume_history[-1] = current_volume
        
        # Draw volume history
        width = 20
        x = VISUALIZATION_WIDTH - width - 10
        for i, vol in enumerate(self.volume_history):
            height = int(vol * 100)
            color = (0, 255, 0) if vol < 0.8 else (255, 165, 0) if vol < 0.9 else (255, 0, 0)
            pygame.draw.rect(self.screen, color,
                           (x, VISUALIZATION_HEIGHT - i * 2, width, 2))

    def _draw_ui(self):
        font = pygame.font.SysFont('DejaVu Sans Mono', 18)
        texts = [
            f"Source: {self.audio_source.upper()}",
            f"File: {os.path.basename(self.audio_file) if self.audio_file else 'None'}",
            f"Mode: {self.viz_mode.value}",
            f"Theme: {self.color_theme.value}",
            "[F] File  [M] Mic  [V] Mode  [T] Theme  [P] Peaks  [ESC] Quit"
        ]
        
        y_offset = 10
        for text in texts:
            surface = font.render(text, True, (255, 255, 255))
            self.screen.blit(surface, (10, y_offset))
            y_offset += surface.get_height() + 5

    def run(self):
        running = True
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                elif event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_f:
                        self.file_dialog()
                    elif event.key == pygame.K_m:
                        self.audio_source = "mic"
                        if self.ffmpeg_proc:
                            self._restart_ffmpeg()
                    elif event.key == pygame.K_v:
                        # Cycle through visualization modes
                        modes = list(VisualizationMode)
                        current_index = modes.index(self.viz_mode)
                        self.viz_mode = modes[(current_index + 1) % len(modes)]
                    elif event.key == pygame.K_t:
                        # Cycle through color themes
                        themes = list(ColorTheme)
                        current_index = themes.index(self.color_theme)
                        self.color_theme = themes[(current_index + 1) % len(themes)]
                    elif event.key == pygame.K_p:
                        self.peak_hold = not self.peak_hold
                    elif event.key == pygame.K_ESCAPE:
                        running = False

            mags = self.process_audio()
            self.draw_visualization(mags)
            pygame.display.flip()
            self.clock.tick(30)

        # Cleanup
        self.input_stream.stop()
        self.output_stream.stop()
        if self.ffmpeg_proc:
            self.ffmpeg_proc.terminate()
        pygame.quit()
        sys.exit()

    def _get_best_audio_devices(self):
        """Select the best available audio devices, preferring WASAPI over others."""
        devices = sd.query_devices()
        
        # First try to find WASAPI devices
        wasapi_input = None
        wasapi_output = None
        
        for i, device in enumerate(devices):
            name = device['name'].lower()
            if 'wasapi' in name:
                if device['max_input_channels'] > 0 and wasapi_input is None:
                    wasapi_input = i
                if device['max_output_channels'] > 0 and wasapi_output is None:
                    wasapi_output = i
        
        # If WASAPI devices found, use them
        if wasapi_input is not None and wasapi_output is not None:
            return wasapi_input, wasapi_output
            
        # Otherwise find any working input/output devices
        default_input = None
        default_output = None
        
        for i, device in enumerate(devices):
            if device['max_input_channels'] > 0 and default_input is None:
                default_input = i
            if device['max_output_channels'] > 0 and default_output is None:
                default_output = i
                
        if default_input is None or default_output is None:
            print("Error: Could not find suitable audio devices.")
            print("Available devices:")
            print(sd.query_devices())
            sys.exit(1)
            
        return default_input, default_output

if __name__ == "__main__":
    print("Available audio devices:")
    print(sd.query_devices())
    AudioVisualizer().run()