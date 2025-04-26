#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <queue>
#include <chrono>

std::mutex mtx;
std::condition_variable cv;
int carsOnBridge = 0;
int direction = 0; // 0: no direction, 1: north, 2: south
std::queue<int> waitingCarsNorth;
std::queue<int> waitingCarsSouth;

void carArrived(int id, int dir) {
	std::unique_lock<std::mutex> lock(mtx);
	if (dir == 1) { // north
		waitingCarsNorth.push(id);
	}
	else { // south
		waitingCarsSouth.push(id);
	}

	while (true) {
		if (carsOnBridge < 3 && (direction == 0 || direction == dir)) {
			break;
		}
		cv.wait(lock);
	}

	if (carsOnBridge == 0) {
		direction = dir;
	}

	if (dir == 1) { // north
		waitingCarsNorth.pop();
	}
	else { // south
		waitingCarsSouth.pop();
	}

	carsOnBridge++;
	std::cout << "Car " << id << " is on the bridge going " << (dir == 1 ? "north" : "south") << std::endl;
	lock.unlock();
	cv.notify_all();
}

void carLeftBridge(int id) {
	std::unique_lock<std::mutex> lock(mtx);
	carsOnBridge--;
	std::cout << "Car " << id << " left the bridge" << std::endl;

	if (carsOnBridge == 0) {
		direction = 0;
		if (!waitingCarsNorth.empty()) {
			direction = 1;
		}
		else if (!waitingCarsSouth.empty()) {
			direction = 2;
		}
	}

	lock.unlock();
	cv.notify_all();
}

void simulateCar(int id, int dir) {
	carArrived(id, dir);
	std::this_thread::sleep_for(std::chrono::milliseconds(1000)); // simulate time on bridge
	carLeftBridge(id);
}

int main() {
	std::thread t1(simulateCar, 1, 1);
	std::thread t2(simulateCar, 2, 1);
	std::thread t3(simulateCar, 3, 1);
	std::thread t4(simulateCar, 4, 2);
	std::thread t5(simulateCar, 5, 2);
	std::thread t6(simulateCar, 6, 1);
	std::thread t7(simulateCar, 7, 2);
	std::thread t8(simulateCar, 8, 1);

	t1.join();
	t2.join();
	t3.join();
	t4.join();
	t5.join();
	t6.join();
	t7.join();
	t8.join();

	return 0;
}