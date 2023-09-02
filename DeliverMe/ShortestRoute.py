import tkinter as tk
from tkinter import ttk
from tkinter import simpledialog
from tkinter import messagebox
from datetime import datetime, timedelta
from itertools import permutations
from collections import defaultdict
import folium
import math
from geopy.geocoders import Nominatim
import webbrowser
from tkhtmlview import HTMLLabel
import webview
from collections import defaultdict
import heapq
from queue import PriorityQueue

class Location:
    def __init__(self, name, lat, lon):
        self.name = name
        self.lat = lat
        self.lon = lon

class Package:
    def __init__(self, pickup_location_address, delivery_location_address):
        self.pickup_location_address = pickup_location_address
        self.delivery_location_address = delivery_location_address

        self.pickup_location = self.coordinatesFromAddress(pickup_location_address)
        self.delivery_location = self.coordinatesFromAddress(delivery_location_address)


        self.pickup_location_address = self.pickup_location.name
        self.delivery_location_address = self.delivery_location.name
    
    def coordinatesFromAddress(self, address):
        geolocator = Nominatim(user_agent="CourierRouteSystem")
        location = geolocator.geocode(address)
        if location:
            name = location.address
            lat = location.latitude
            lon = location.longitude
            loc = Location(name, lat, lon)
            print(name)
            return loc
        else:
            print("Failed: ",address)
            return None

class RouteOptimizer:
    def __init__(self):
        self.locations = dict()
        self.packages = []
        self.optimized_route = []

    def add_package(self, package):
        self.packages.append(package)
        self.locations[package.pickup_location_address] = package.pickup_location
        self.locations[package.delivery_location_address] = package.delivery_location

    def remove_package(self, package):
        if package in self.packages:
            self.packages.remove(package)
            del self.locations[package.pickup_location_address]
            del self.locations[package.delivery_location_address]
        else:
            raise ValueError("Package not found.")
    
    def calculate_distance(self, loc1, loc2):
        if loc1 is None or loc2 is None:
            return float('inf')

        lat1 = math.radians(loc1.lat)
        lon1 = math.radians(loc1.lon)
        lat2 = math.radians(loc2.lat)
        lon2 = math.radians(loc2.lon)

        dlon = lon2 - lon1
        dlat = lat2 - lat1

        a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

        # Radius of the Earth in kilometers
        radius = 6371

        distance = radius * c
        return distance

    def generate_graph(self):
        graph = defaultdict(dict)
        locations = list(self.locations.keys())

        for i in range(len(locations)):
            for j in range(i + 1, len(locations)):
                loc1_name = locations[i]
                loc2_name = locations[j]
                loc1 = self.locations[loc1_name]
                loc2 = self.locations[loc2_name]
                distance = self.calculate_distance(loc1, loc2)
                graph[loc1_name][loc2_name] = distance
                graph[loc2_name][loc1_name] = distance
        #print("HERE\n",graph)
        return graph

    def isPickUp(self, loc_name):
        for package in self.packages:
            if package.pickup_location_address == loc_name:
                return True
        return False
    
    def getDelivery(self, loc_name):
        for package in self.packages:
            if package.pickup_location_address == loc_name:
                return  package.delivery_location.name
        return None

    def dijkstra(self, graph, start, end):
        distances = {node: float('inf') for node in graph}
        distances[start] = 0

        visited = {node: False for node in graph}
        previous = {node: None for node in graph}
        priority_queue = PriorityQueue()
        priority_queue.put((0, start))

        blocked = {node: False if self.isPickUp(node) else True for node in graph} # depending if it's a pickup or no we make it blocked so that when we
        # are visiting the nodes we don't visit the delivery node untill it's pickup node is picked first 
        # print("___________________")
        # print(blocked)

        while not priority_queue.empty():
            min_distance, min_node = priority_queue.get()

            if min_node == end:
                delname = self.getDelivery(min_node)
                blocked[delname] = False # since we are at pickup let's unblock it's delivery node
                # print("________________")
                # print(blocked)
                # print('________________')
                break

            if visited[min_node] or blocked[min_node]:
                continue

            visited[min_node] = True
            
            delname = self.getDelivery(min_node)
            blocked[delname] = False # since we are at pickup let's unblock it's delivery node


            # blocked['Kensington Palace, Palace Avenue, South Kensington, Royal Borough of Kensington and Chelsea, London, Greater London, England, W8 4PX, United Kingdom'] = False
            # print("________________")
            # print(blocked)
            # print('________________')


            for neighbor, weight in graph[min_node].items():
                if not visited[neighbor] and not blocked[neighbor]: # make sure it's not visited and it's not blocked
                    new_distance = distances[min_node] + weight
                    if new_distance < distances[neighbor]:
                        distances[neighbor] = new_distance
                        previous[neighbor] = min_node
                        # print(new_distance, " ", neighbor)
                        priority_queue.put((new_distance, neighbor))

        shortest_path = []
        current_node = end

        # Check if a valid path exists
        if previous[current_node] is None: 
            raise Exception("No valid path exists from the start to the end location.")

        while current_node != start:
            shortest_path.append(current_node)
            current_node = previous[current_node]

        shortest_path.append(start)
        shortest_path.reverse()

        shortest_distances = {node: distances[node] for node in graph}
        return shortest_path, shortest_distances

    def calculate_total_distance(self, route):
        total_distance = 0
        for i in range(len(route) - 1):
            loc1 = self.locations[route[i]]
            loc2 = self.locations[route[i + 1]]
            distance = self.calculate_distance(loc1, loc2)
            total_distance += distance
        return total_distance

    def calculate_estimated_arrival_time(self, route, speed=60):
        total_distance = self.calculate_total_distance(route)
        # Calculate time in hours
        estimated_time = total_distance / speed
        # Add 10 minutes buffer for each location
        buffer_time = len(route) * 0.17
        # Add buffer time to the estimated time
        estimated_time += buffer_time
        # Get the current time
        current_time = datetime.now()
        # Add the estimated time to the current time
        arrival_time = current_time + timedelta(hours=estimated_time)
        return arrival_time.strftime("%Y-%m-%d %H:%M:%S")

    def optimize_route(self):
        graph = self.generate_graph()
        start_location = list(self.locations.keys())[0]  # Use the first location as the source
        end_location = list(self.locations.keys())[-1]  # Use the last location as the destination
        shortest_path, distances = self.dijkstra(graph, start_location, end_location)

        sorted_distances = {k: v for k, v in sorted(distances.items(), key=lambda item: item[1])}
        self.optimized_route = list(sorted_distances.keys())
        print("ShortestPath",shortest_path)
        print("OptmizedRoute",self.optimized_route)
        


# Example usage:


# if __name__ == "__main__":
#     route_optimizer = RouteOptimizer()

#     package1 = Package("Tower of London, London", "Kensington Palace, London", deadline=datetime.now())
#     package2 = Package("Maadi, Cairo", "Buckingham Palace, London", deadline=datetime.now())
#     # gui.add_location_manuel("Tower of London, London")
#     #     gui.add_location_manuel("Kensington Palace, London")
#     route_optimizer.add_package(package1)
#     route_optimizer.add_package(package2)

#     route_optimizer.optimize_route()