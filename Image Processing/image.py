import cv2
import numpy as np
import matplotlib.pyplot as plt
from tkinter import *
from tkinter import filedialog, messagebox
from tkinter import ttk
from PIL import Image, ImageTk
import matplotlib
matplotlib.use('TkAgg')
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

# Global variables
original_img = None
current_img = None
display_img = None
img_path = ""
photo_image = None
resize_pending = False

# Create the main GUI window
root = Tk()
root.title("Image Processing Project")
root.geometry("800x600")
root.resizable(True, True)

# Create widgets using grid layout
canvas = Canvas(root, borderwidth=0, highlightthickness=0)
canvas.grid(row=0, column=0, sticky=NSEW)

hist_frame = Frame(root)
hist_frame.grid(row=0, column=1, sticky=NSEW)

root.grid_rowconfigure(0, weight=1)
root.grid_columnconfigure(0, weight=2)
root.grid_columnconfigure(1, weight=1)

hscrollbar = Scrollbar(root, orient=HORIZONTAL, command=canvas.xview)
hscrollbar.grid(row=1, column=0, sticky=EW)
vscrollbar = Scrollbar(root, orient=VERTICAL, command=canvas.yview)
vscrollbar.grid(row=0, column=2, sticky=NS)

canvas.configure(xscrollcommand=hscrollbar.set, yscrollcommand=vscrollbar.set)

def open_image():
    global img_path, original_img, current_img, display_img, photo_image
    img_path = filedialog.askopenfilename()
    if img_path:
        original_img = cv2.imread(img_path)
        if original_img is not None:
            current_img = original_img.copy()
            display_img = cv2.cvtColor(current_img, cv2.COLOR_BGR2RGB)
            display_img = Image.fromarray(display_img)
            scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
            canvas.configure(scrollregion=canvas.bbox(ALL))
            show_histogram()
        else:
            messagebox.showerror("Error", "Failed to load the image.")
    else:
        messagebox.showinfo("Info", "No file selected.")

def scale_image(img, canvas_width, canvas_height, keep_aspect=True):
    global photo_image
    if keep_aspect:
        img_width, img_height = img.size
        aspect_ratio = img_width / img_height
        new_width = canvas_width
        new_height = int(new_width / aspect_ratio)
        if new_height > canvas_height:
            new_height = canvas_height
            new_width = int(new_height * aspect_ratio)
        img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)  # Use Image.Resampling.LANCZOS
    else:
        img = img.resize((canvas_width, canvas_height), Image.Resampling.LANCZOS)  # Use Image.Resampling.LANCZOS
    photo_image = ImageTk.PhotoImage(img)
    canvas.delete("all")
    canvas.create_image(0, 0, anchor=NW, image=photo_image)
    canvas.configure(scrollregion=canvas.bbox(ALL))

def on_canvas_resize(event):
    global resize_pending
    if not resize_pending:
        root.after(200, lambda: resize_image(event.width, event.height))
        resize_pending = True

def resize_image(width, height):
    global resize_pending
    resize_pending = False
    if current_img is not None and photo_image is not None:
        img = Image.fromarray(current_img)
        scale_image(img, width, height)
        canvas.configure(scrollregion=canvas.bbox(ALL))

def show_histogram():
    global current_img, ax
    if current_img is not None:
        ax.clear()
        if len(current_img.shape) == 3:
            for i, color in enumerate(['r','g','b']):
                hist = np.bincount(current_img[:,:,i].flatten(), minlength=256)
                ax.bar(range(256), hist, color=color, width=1, alpha=0.5)
        else:
            hist = np.bincount(current_img.flatten(), minlength=256)
            ax.bar(range(256), hist, width=1)
        ax.set_title('Histogram')
        ax.set_xlabel('Pixel Intensity')
        ax.set_ylabel('Frequency')
        canvas_histogram.draw()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def convert_to_gray_0():
    global current_img, display_img
    if original_img is not None:
        current_img = cv2.cvtColor(original_img, cv2.COLOR_BGR2GRAY)
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def convert_to_gray():
    global current_img, display_img
    if original_img is not None:
        img_rgb = cv2.cvtColor(original_img, cv2.COLOR_BGR2RGB)
        
        gray_values = (0.2989 * img_rgb[:, :, 0] + 
                       0.5870 * img_rgb[:, :, 1] + 
                       0.1140 * img_rgb[:, :, 2]).astype('uint8')
        
        gray_img = cv2.merge([gray_values, gray_values, gray_values])
        
        current_img = gray_img
        display_img = Image.fromarray(current_img)
        
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")


def convert_to_gray_1():
    global current_img, display_img
    if original_img is not None:
        img_rgb = cv2.cvtColor(original_img, cv2.COLOR_BGR2RGB)
        height, width, _ = img_rgb.shape
        
        for y in range(height):
            for x in range(width):
                r, g, b = img_rgb[y, x]
                gray = (r + g + b) // 3 
                img_rgb[y, x] = (gray, gray, gray) 
        
        current_img = img_rgb
        display_img = Image.fromarray(current_img)
        
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")


def show_color_channel(color_channel):
    global current_img, display_img
    if original_img is not None:
        img_rgb = cv2.cvtColor(original_img, cv2.COLOR_BGR2RGB)
        color_img = img_rgb.copy()
        if color_channel in ['red', 'green', 'blue']:
            channel_indices = {'red': (1, 2), 'green': (0, 2), 'blue': (0, 1)}
            indices_to_zero = channel_indices[color_channel]
            color_img[:, :, indices_to_zero[0]] = 0
            color_img[:, :, indices_to_zero[1]] = 0
            current_img = color_img
            display_img = Image.fromarray(current_img)
            scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
            show_histogram()
        else:
            messagebox.showwarning("Warning", "Invalid color channel.")
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def canny_edge_0():
    global current_img, display_img
    if original_img is not None:
        gray = cv2.cvtColor(original_img, cv2.COLOR_BGR2GRAY)
        edges = cv2.Canny(gray, 100, 200)
        current_img = edges
        display_img = Image.fromarray(edges)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def canny_edge():
    global current_img, display_img
    if original_img is not None:
        gray = cv2.cvtColor(original_img, cv2.COLOR_BGR2GRAY)
        gray = gray.astype(np.float32)

        # Step 1: Apply Sobel filters for gradient detection
        sobel_x = np.array([[-1, 0, 1], [-2, 0, 2], [-1, 0, 1]])
        sobel_y = np.array([[-1, -2, -1], [0, 0, 0], [1, 2, 1]])
        
        gradient_x = cv2.filter2D(gray, -1, sobel_x)
        gradient_y = cv2.filter2D(gray, -1, sobel_y)
        
        # Step 2: Compute gradient magnitude and direction
        magnitude = np.sqrt(gradient_x**2 + gradient_y**2)
        direction = np.arctan2(gradient_y, gradient_x)

        # Step 3: Non-maximum suppression
        suppressed = np.zeros_like(magnitude)
        for i in range(1, magnitude.shape[0] - 1):
            for j in range(1, magnitude.shape[1] - 1):
                angle = direction[i, j] * 180.0 / np.pi
                angle = (angle + 180) % 180  # Map to [0, 180)
                neighbor1, neighbor2 = 0, 0
                
                if (0 <= angle < 22.5) or (157.5 <= angle <= 180):
                    neighbor1 = magnitude[i, j - 1]
                    neighbor2 = magnitude[i, j + 1]
                elif 22.5 <= angle < 67.5:
                    neighbor1 = magnitude[i - 1, j + 1]
                    neighbor2 = magnitude[i + 1, j - 1]
                elif 67.5 <= angle < 112.5:
                    neighbor1 = magnitude[i - 1, j]
                    neighbor2 = magnitude[i + 1, j]
                elif 112.5 <= angle < 157.5:
                    neighbor1 = magnitude[i - 1, j - 1]
                    neighbor2 = magnitude[i + 1, j + 1]

                if magnitude[i, j] >= neighbor1 and magnitude[i, j] >= neighbor2:
                    suppressed[i, j] = magnitude[i, j]

        # Step 4: Thresholding
        low_thresh = 50
        high_thresh = 150
        edges = np.zeros_like(suppressed)
        strong = suppressed > high_thresh
        weak = (suppressed >= low_thresh) & (suppressed <= high_thresh)
        edges[strong] = 255

        # Edge tracking for weak edges
        for i in range(1, edges.shape[0] - 1):
            for j in range(1, edges.shape[1] - 1):
                if weak[i, j]:
                    if np.any(strong[i - 1:i + 2, j - 1:j + 2]):
                        edges[i, j] = 255

        current_img = edges.astype(np.uint8)
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def gaussian_blur_0():
    global current_img, display_img
    if original_img is not None:
        blurred = cv2.GaussianBlur(current_img, (15,15), 0)
        current_img = blurred
        display_img = Image.fromarray(blurred)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def gaussian_blur():
    global current_img, display_img
    if original_img is not None:
        kernel_size = 15
        sigma = 2.0
        
        # Create Gaussian kernel
        ax = np.linspace(-(kernel_size // 2), kernel_size // 2, kernel_size)
        gauss = np.exp(-0.5 * (ax / sigma)**2)
        kernel = np.outer(gauss, gauss)
        kernel /= np.sum(kernel)  # Normalize the kernel

        # Check if the image is color (RGB) or grayscale
        if len(current_img.shape) == 3:  # RGB image
            blurred = np.zeros_like(current_img, dtype=np.float32)
            for c in range(current_img.shape[2]):  # Apply Gaussian blur to each channel
                blurred[:, :, c] = cv2.filter2D(current_img[:, :, c], -1, kernel)
            
            blurred = np.clip(blurred, 0, 255).astype(np.uint8)
            # Convert from BGR to RGB for display
            blurred = cv2.cvtColor(blurred, cv2.COLOR_BGR2RGB)
        else:  # Grayscale image
            blurred = cv2.filter2D(current_img, -1, kernel)
            blurred = np.clip(blurred, 0, 255).astype(np.uint8)
        
        # Update the global image variables
        current_img = blurred
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")



def smoothing_0():
    global current_img, display_img
    if original_img is not None:
        avg_blur = cv2.blur(current_img, (5,5))
        current_img = avg_blur
        display_img = Image.fromarray(avg_blur)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def smoothing_1():
    global current_img, display_img
    if original_img is not None:
        kernel_size = 5

        # Create normalized kernel
        kernel = np.ones((kernel_size, kernel_size), dtype=np.float32) / (kernel_size**2)

        # Apply convolution
        smoothed = cv2.filter2D(current_img, -1, kernel)

        current_img = smoothed.astype(np.uint8)
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def smoothing():
    global current_img, display_img
    if original_img is not None:
        kernel_size = 5
        # Create normalized box kernel
        kernel = np.ones((kernel_size, kernel_size), dtype=np.float32) / (kernel_size**2)
        
        # Apply filter2D directly on the image
        if len(current_img.shape) == 3:  # Color image
            smoothed = cv2.filter2D(current_img, -1, kernel)
            # Convert from BGR to RGB for display
            smoothed = cv2.cvtColor(smoothed, cv2.COLOR_BGR2RGB)
        else:  # Grayscale image
            smoothed = cv2.filter2D(current_img, -1, kernel)
        
        smoothed = np.clip(smoothed, 0, 255).astype(np.uint8)
        
        # Update global variables
        current_img = smoothed
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def laplacian_filter_0():
    global current_img, display_img
    if original_img is not None:
        gray = cv2.cvtColor(current_img, cv2.COLOR_BGR2GRAY)
        lap = cv2.Laplacian(gray, cv2.CV_64F)
        lap = np.uint8(np.absolute(lap))
        current_img = lap
        display_img = Image.fromarray(lap)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def laplacian_filter():
    global current_img, display_img
    if original_img is not None:
        gray = cv2.cvtColor(current_img, cv2.COLOR_BGR2GRAY)

        # Laplacian kernel
        laplacian_kernel = np.array([[0, -1, 0],
                                     [-1, 4, -1],
                                     [0, -1, 0]])

        # Apply convolution
        laplacian = cv2.filter2D(gray, cv2.CV_64F, laplacian_kernel)
        laplacian = np.uint8(np.absolute(laplacian))

        current_img = laplacian
        display_img = Image.fromarray(current_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def equalize_histogram_0():
    global current_img, display_img
    if original_img is not None:
        if len(current_img.shape) == 3:
            img_gray = cv2.cvtColor(current_img, cv2.COLOR_RGB2GRAY)
        else:
            img_gray = current_img
        eq_img = cv2.equalizeHist(img_gray)
        current_img = eq_img
        display_img = Image.fromarray(eq_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def equalize_histogram_gray_scale():
    global current_img, display_img
    if original_img is not None:
        # Ensure the image is grayscale
        if len(current_img.shape) == 3:
            img_gray = cv2.cvtColor(current_img, cv2.COLOR_RGB2GRAY)
        else:
            img_gray = current_img
        
        # Calculate the histogram
        histogram = [0] * 256
        rows, cols = img_gray.shape
        for row in range(rows):
            for col in range(cols):
                intensity = img_gray[row, col]
                histogram[intensity] += 1

        # Calculate the CDF
        cdf = [0] * 256
        cdf[0] = histogram[0]
        for i in range(1, 256):
            cdf[i] = cdf[i - 1] + histogram[i]

        # Normalize the CDF
        cdf_min = min(cdf)
        total_pixels = rows * cols
        cdf_normalized = [(cdf[i] - cdf_min) * 255 // (total_pixels - cdf_min) for i in range(256)]

        # Map the original intensities to the equalized values
        equalized_img = np.zeros_like(img_gray)
        for row in range(rows):
            for col in range(cols):
                original_intensity = img_gray[row, col]
                equalized_img[row, col] = cdf_normalized[original_intensity]

        # Update globals with the processed image
        current_img = equalized_img
        display_img = Image.fromarray(equalized_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

def equalize_histogram():
    global current_img, display_img
    if original_img is not None:
        # Step 1: Split the image into R, G, B channels
        if len(current_img.shape) == 2:
            messagebox.showwarning("Warning", "Image is already grayscale. Use grayscale equalization.")
            return
        img_rgb = current_img.copy()
        rows, cols, _ = img_rgb.shape
        channels = cv2.split(img_rgb)  # Split into R, G, B

        equalized_channels = []
        for channel in channels:
            # Calculate the histogram for the channel
            histogram = [0] * 256
            for row in range(rows):
                for col in range(cols):
                    intensity = channel[row, col]
                    histogram[intensity] += 1

            # Calculate the CDF
            cdf = [0] * 256
            cdf[0] = histogram[0]
            for i in range(1, 256):
                cdf[i] = cdf[i - 1] + histogram[i]

            # Normalize the CDF
            cdf_min = min(cdf)
            total_pixels = rows * cols
            cdf_normalized = [(cdf[i] - cdf_min) * 255 // (total_pixels - cdf_min) for i in range(256)]

            # Map the original intensities to the equalized values
            equalized_channel = np.zeros_like(channel)
            for row in range(rows):
                for col in range(cols):
                    original_intensity = channel[row, col]
                    equalized_channel[row, col] = cdf_normalized[original_intensity]

            equalized_channels.append(equalized_channel)

        # Merge the equalized channels back into an RGB image
        equalized_img = cv2.merge(equalized_channels)

        # Update globals with the processed image
        current_img = equalized_img
        display_img = Image.fromarray(equalized_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")



def reset_image():
    global current_img, display_img
    if original_img is not None:
        current_img = original_img.copy()
        display_img = cv2.cvtColor(current_img, cv2.COLOR_BGR2RGB)
        display_img = Image.fromarray(display_img)
        scale_image(display_img, canvas.winfo_width(), canvas.winfo_height())
        show_histogram()
    else:
        messagebox.showwarning("Warning", "No image loaded.")

# Bind resize event to canvas
canvas.bind('<Configure>', on_canvas_resize)

# Create histogram plot
fig, ax = plt.subplots(figsize=(5,4), dpi=100)
canvas_histogram = FigureCanvasTkAgg(fig, master=hist_frame)
canvas_histogram.get_tk_widget().pack(fill=BOTH, expand=True)

# Create menu bar
menubar = Menu(root)
filemenu = Menu(menubar, tearoff=0)
filemenu.add_command(label="Open", command=open_image)
filemenu.add_command(label="Reset", command=lambda: reset_image())
menubar.add_cascade(label="File", menu=filemenu)

color_menu = Menu(menubar, tearoff=0)
color_menu.add_command(label="Gray", command=lambda: convert_to_gray())
color_menu.add_command(label="RGB", command=lambda: reset_image())
color_menu.add_command(label="Red", command=lambda: show_color_channel('red'))
color_menu.add_command(label="Green", command=lambda: show_color_channel('green'))
color_menu.add_command(label="Blue", command=lambda: show_color_channel('blue'))
menubar.add_cascade(label="Color", menu=color_menu)

filter_menu = Menu(menubar, tearoff=0)
filter_menu.add_command(label="Canny Edge", command=lambda: canny_edge())
filter_menu.add_command(label="Gaussian Blur", command=lambda: gaussian_blur())
filter_menu.add_command(label="Smoothing", command=lambda: smoothing())
filter_menu.add_command(label="Laplacian", command=lambda: laplacian_filter())
menubar.add_cascade(label="Filters", menu=filter_menu)

hist_menu = Menu(menubar, tearoff=0)
# hist_menu.add_command(label="Show Histogram", command=lambda: show_histogram())
hist_menu.add_command(label="Equalize Histogram", command=lambda: equalize_histogram())
hist_menu.add_command(label="Equalize Histogram GrayScale", command=lambda: equalize_histogram_gray_scale())
# hist_menu.add_command(label="Manual Histogram", command=lambda: manual_histogram())
menubar.add_cascade(label="Histogram", menu=hist_menu)

root.config(menu=menubar)

# Run the application
root.mainloop()