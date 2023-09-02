import tkinter as tk
from tkinter import Canvas, Entry, Button, PhotoImage
import tkinter as tk
from tkinter import ttk
from tkinter import simpledialog
from tkinter import messagebox
import folium
import webview
from datetime import datetime

from ShortestRoute import *

class DeliverME:
    def __init__(self):
        self.window = tk.Tk()
        self.route_optimizer = RouteOptimizer()
        self.window.geometry("1250x750")
        self.window.configure(bg="#000000")

        self.canvas = Canvas(
            self.window,
            bg="#000000",
            height=750,
            width=1400,
            bd=0,
            highlightthickness=0,
            relief="ridge"
        )
        self.canvas.place(x=0, y=0)
        self.canvas.create_rectangle(0.0, 0.0, 1250.0, 750.0, fill="#000000", outline="")

        self.button_image_1 = PhotoImage(file=relative_to_assets("button_1.png"))
        self.add_package_button = Button(
            image=self.button_image_1,
            borderwidth=0,
            highlightthickness=0,
            command=self.add_package,
            relief="flat"
        )
        self.add_package_button.place(x=64.0, y=507.0, width=564.0, height=95.0)

        self.button_image_2 = PhotoImage(file=relative_to_assets("button_2.png"))
        self.find_optimal_route_button = Button(
            image=self.button_image_2,
            borderwidth=0,
            highlightthickness=0,
            command=self.optimize_route,
            relief="flat"
        )
        self.find_optimal_route_button.place(x=62.0, y=633.0, width=186.0, height=72.0)

        self.button_image_3 = PhotoImage(file=relative_to_assets("button_3.png"))
        self.show_map_button = Button(
            image=self.button_image_3,
            borderwidth=0,
            highlightthickness=0,
            command=self.show_map,
            relief="flat"
        )
        self.show_map_button.place(x=248.898681640625, y=633.0, width=186.101318359375, height=72.0)

        self.button_image_4 = PhotoImage(file=relative_to_assets("button_4.png"))
        self.remove_package_button = Button(
            image=self.button_image_4,
            borderwidth=0,
            highlightthickness=0,
            command=self.remove_package,
            relief="flat"
        )
        self.remove_package_button.place(x=434.0, y=627.0, width=186.0, height=72.0)

        self.canvas.create_text(
            68.0,
            357.0,
            anchor="nw",
            text="Pickup Location:",
            fill="#FFFFFF",
            font=("Inter Bold", 20 * -1)
        )

        self.canvas.create_text(
            62.0,
            445.0,
            anchor="nw",
            text="Delivery Location:",
            fill="#FFFFFF",
            font=("Inter Bold", 20 * -1)
        )

        self.entry_image_1 = PhotoImage(file=relative_to_assets("entry_1.png"))
        self.entry_bg_1 = self.canvas.create_image(432.0, 372.5, image=self.entry_image_1)
        self.entry_pickup = Entry(
            bd=0,
            bg="#D9D9D9",
            fg="#000716",
            highlightthickness=0
        )
        self.entry_pickup.place(x=276.0, y=345.0, width=312.0, height=53.0)

        self.entry_image_2 = PhotoImage(file=relative_to_assets("entry_2.png"))
        self.entry_bg_2 = self.canvas.create_image(432.0, 457.5, image=self.entry_image_2)
        self.entry_delivery = Entry(
            bd=0,
            bg="#D9D9D9",
            fg="#000716",
            highlightthickness=0
        )
        self.entry_delivery.place(x=276.0, y=430.0, width=312.0, height=53.0)

        self.image_image_1 = PhotoImage(file=relative_to_assets("image_1.png"))
        self.image_1 = self.canvas.create_image(315.0, 147.0, image=self.image_image_1)

        self.canvas.create_rectangle(160.5, 606.5221252441406, 512.0000610351562, 609.9778442382812, fill="#FFFFFF",
                                     outline="")
        

        ## Put on the right side of the application

        self.canvas.create_text(
            930.0,
            25,
            text="Packages",
            fill="#FFFFFF",
            font=("Bold", 20 * -1)
        )

        self.table_packages = ttk.Treeview()
        self.table_packages["columns"] = ("Pickup", "Delivery")
        self.table_packages.column("#0", width=0, stretch=tk.NO)
        self.table_packages.column("Pickup", width=250, anchor="w")
        self.table_packages.column("Delivery", width=250, anchor="w")

        self.table_packages.heading("#0", text="", anchor="w")
        self.table_packages.heading("Pickup", text="Pickup Location", anchor="w")
        self.table_packages.heading("Delivery", text="Delivery Location", anchor="w")

        self.table_packages.place(x=630, y=50, width=600, height=200)

        # self.label_route = ttk.Label(text="Optimized Route", font=("Helvetica", 16, "bold"))
        # self.label_route.place(x=850, y=270)
        self.canvas.create_text(
            930.0,
            270.0,
            text="Optimized Route",
            fill="#FFFFFF",
            font=("Bold", 20 * -1)
        )

        self.table_route = ttk.Treeview()
        self.table_route["columns"] = ("Distance", "Arrival Time")
        self.table_route.column("#0", width=300, minwidth=300, anchor="w")
        self.table_route.column("Distance", width=100, minwidth=100, anchor="center")
        self.table_route.column("Arrival Time", width=100, minwidth=100, anchor="center")
        self.table_route.heading("#0", text="Location")
        self.table_route.heading("Distance", text="Distance (km)")
        self.table_route.heading("Arrival Time", text="Estimated Arrival Time")
        self.table_route.place(x=630, y=300, width=600, height=400)

        
        self.window.resizable(False, False)

    def add_package(self):
        pickup_location = self.entry_pickup.get()
        delivery_location = self.entry_delivery.get()

        if pickup_location and delivery_location:
           
            package = Package(pickup_location, delivery_location)
            self.route_optimizer.add_package(package)
            self.table_packages.insert("", tk.END, text="", values=(package.pickup_location.name, package.delivery_location.name))
            self.entry_pickup.delete(0, tk.END)
            self.entry_delivery.delete(0, tk.END)
            self.entry_deadline.delete(0, tk.END)
        else:
            messagebox.showerror("Error", "Please enter all package details.")

    def add_package_manuel(self, package):
        pickup_location = package.pickup_location.name
        delivery_location = package.delivery_location.name
        deadline = datetime.now().strftime("%H:%M:%S")

        if pickup_location and delivery_location and deadline:
            # Validate deadline format
            try:
                datetime.strptime(deadline, "%H:%M:%S")
            except ValueError:
                messagebox.showerror("Error", "Invalid deadline format. Please enter time in HH:MM:SS format.")
                return

            self.route_optimizer.add_package(package)
            self.table_packages.insert("", tk.END, text="", values=(pickup_location, delivery_location))
            self.entry_pickup.delete(0, tk.END)
            self.entry_delivery.delete(0, tk.END)
        else:
            messagebox.showerror("Error", "Please enter all package details.")
    
    def read_addresses_from_file(self, file_path):
         with open(file_path, 'r', encoding='utf-8') as file:
            lines = file.readlines()
            total_lines = len(lines)

            for i in range(0, total_lines, 2):
                pickup_address = lines[i].strip()
                delivery_address = lines[i+1].strip()
                pkg = Package(pickup_address, delivery_address)
                self.add_package_manuel(pkg)

        

    def remove_package(self):
        selected_item = self.table_packages.focus()
        if selected_item:
            item_data = self.table_packages.item(selected_item)
            package_address = item_data['values'][0]  
            # Find the package in the RouteOptimizer instance and remove it
            package = next((pkg for pkg in self.route_optimizer.packages if
                            pkg.pickup_location_address == package_address or
                            pkg.delivery_location_address == package_address), None)
            if package:
                self.route_optimizer.remove_package(package)
                self.table_packages.delete(selected_item)
            else:
                messagebox.showerror("Error", "Package not found.")
        else:
            messagebox.showerror("Error", "Please select a package to remove.")

    def optimize_route(self):
        self.route_optimizer.optimize_route()
        optimized_route = self.route_optimizer.optimized_route
        self.table_route.delete(*self.table_route.get_children())
        for location in optimized_route:
            distance = self.route_optimizer.calculate_total_distance(optimized_route[:optimized_route.index(location) + 1])
            arrival_time = self.route_optimizer.calculate_estimated_arrival_time(optimized_route[:optimized_route.index(location) + 1])
            self.table_route.insert("", tk.END, text=location, values=(distance, arrival_time))

    def show_map(self):
        optimized_route = self.route_optimizer.optimized_route
        locations = self.route_optimizer.locations
        if len(optimized_route) > 1:
            center_lat = locations[optimized_route[0]].lat
            center_lon = locations[optimized_route[0]].lon
            m = folium.Map(location=[center_lat, center_lon], zoom_start=13)
            i = 0
            for location in optimized_route:
                lat = locations[location].lat
                lon = locations[location].lon
                folium.Marker([lat, lon], popup=location).add_to(m)

                if i < len(optimized_route) - 1:
                    folium.PolyLine(
                        [[locations[optimized_route[i]].lat, locations[optimized_route[i]].lon],
                         [locations[optimized_route[i + 1]].lat, locations[optimized_route[i + 1]].lon]],
                        color="blue",
                        weight=2.5,
                        opacity=1
                    ).add_to(m)
                    i += 1

            tmp = "map.html"
            m.save(tmp)
            # webbrowser.open_new_tab(tmp)
            # Create a GUI window to view the HTML content
            webview.create_window('Optimized Route', 'map.html')
            webview.start()
        else:
            messagebox.showerror("Error", "Insufficient locations to generate a map.")

    def run(self):
        self.window.mainloop()




def relative_to_assets(path):
    return 'assets\\' + path


if __name__ == "__main__":

    app = DeliverME()

    # TESTING
    # package1 = Package("Tower of London, London", "Kensington Palace, London" )
    # package2 = Package("Maadi, Cairo", "Buckingham Palace, London")
    # package3 = Package("حدائق المعادى", "منيا القمح, الشرقية")
    # app.add_package_manuel(package1)
    # app.add_package_manuel(package2)
    # app.add_package_manuel(package3)
    app.read_addresses_from_file('test.txt')
    app.run()
