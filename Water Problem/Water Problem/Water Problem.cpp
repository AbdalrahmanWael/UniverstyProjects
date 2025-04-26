#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

std::mutex mtx;
std::condition_variable cv;
int hydrogen_count = 0;
int oxygen_count = 0;
int water_molecules = 0;

void hydrogen_thread(int num_atoms) {
	for (int i = 0; i < num_atoms; i++) {
		std::unique_lock<std::mutex> lock(mtx);
		hydrogen_count++;
		cv.notify_all();
		cv.wait(lock, [] { return hydrogen_count < 2 || oxygen_count < 1; });
	}
}

void oxygen_thread(int num_atoms) {
	for (int i = 0; i < num_atoms; i++) {
		std::unique_lock<std::mutex> lock(mtx);
		oxygen_count++;
		cv.notify_all();
		cv.wait(lock, [] { return hydrogen_count < 2 || oxygen_count < 1; });
	}
}

void water_thread() {
	while (true) {
		std::unique_lock<std::mutex> lock(mtx);
		cv.wait(lock, [] { return hydrogen_count >= 2 && oxygen_count >= 1; });
		water_molecules++;
		std::cout << "Water molecule formed. Total: " << water_molecules << std::endl;
		hydrogen_count -= 2;
		oxygen_count -= 1;
		cv.notify_all();
	}
}

int main() {
	int hydrogen_atoms, oxygen_atoms;
	std::cout << "Enter the number of hydrogen atoms: ";
	std::cin >> hydrogen_atoms;
	std::cout << "Enter the number of oxygen atoms: ";
	std::cin >> oxygen_atoms;

	std::thread t1(hydrogen_thread, hydrogen_atoms);
	std::thread t2(oxygen_thread, oxygen_atoms);
	std::thread t3(water_thread);

	t1.join();
	t2.join();
	t3.join();

	return 0;
}